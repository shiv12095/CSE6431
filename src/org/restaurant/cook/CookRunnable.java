package org.restaurant.cook;

import org.restaurant.food.MenuItem;
import org.restaurant.food.Order;
import org.restaurant.resources.Resources;
import org.restaurant.util.Constants;
import org.restaurant.util.Utilities;

public class CookRunnable implements Runnable {

	private Cook cook;

	public CookRunnable(Cook cook) {
		this.cook = cook;
	}

	private void awaitEntryLatch() {
		try {
			Resources.ENTRY_LATCH.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void awaitExitLatch() {
		try {
			Resources.EXIT_LATCH.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void startIteration() {
		Resources.READY_TO_START_COUNT_DOWN_LATCH.countDown();
		awaitEntryLatch();
	}

	private void endIteration() {
		Resources.READY_TO_EXIT_COUNT_DOWN_LATCH.countDown();
		awaitExitLatch();
	}

	@Override
	public void run() {
		try {
			Resources.PROGRAM_START_LATCH.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		int currTime = Utilities.getTime();

		while ((currTime < Constants.RESTAURANT_CLOSE_TIME) || Utilities.getDiners() > 0) {

			startIteration();

			Order order = Resources.getPendingOrders().poll();
			if (order == null) {
				endIteration();
			} else {
				System.out.println("Cook " + cook.getCookId() + " has been assigned order " + order.getOrderId()
						+ " at time " + currTime);
				Resources.getPendingOrders().remove(order);
				cook.assignOrder(order);
				endIteration();
				prepareOrder();
			}
			currTime = Utilities.getTime();
		}

		System.out.println("Cook " + cook.getCookId() + " is now leaving at time " + Utilities.getTime());
	}

	private void createDelay(int time) {
		int currTime = 0;
		while (currTime < time) {
			startIteration();
			currTime++;
			endIteration();
		}
	}

	private boolean getLockOnMaker(MenuItem menuItem, Order order) {
		if (menuItem == MenuItem.BURGER) {
			return Resources.lockBurgerMaker(order);
		} else if (menuItem == MenuItem.FRIES) {
			return Resources.lockFriesMaker(order);
		} else if (menuItem == MenuItem.COKE) {
			return Resources.lockCokeMaker(order);
		} else if (menuItem == MenuItem.SUNDAE) {
			return Resources.lockSundaeMaker(order);
		} else {
			return false;
		}
	}

	private void unLockMaker(MenuItem menuItem, Order order) {
		if (menuItem == MenuItem.BURGER) {
			Resources.unLockBurgerMaker(order);
		} else if (menuItem == MenuItem.FRIES) {
			Resources.unLockFriesMaker(order);
		} else if (menuItem == MenuItem.COKE) {
			Resources.unLockCokeMaker(order);
		} else if (menuItem == MenuItem.SUNDAE) {
			Resources.unLockSundaeMaker(order);
		}
	}

	private void prepareOrder() {
		Order order = cook.getCurrentOrder();

		if (order.isOrderCompleted()) {
			startIteration();
			endIteration();
			return;
		}

		while (!order.isOrderCompleted()) {
			startIteration();
			MenuItem menuItem = order.nextMenuItem();

			if (getLockOnMaker(menuItem, order)) {
				System.out.println("Cook " + cook.getCookId() + " got " + menuItem + " machine to prepare for order "
						+ order.getOrderId() + " at time " + Utilities.getTime());
				endIteration();

				createDelay(menuItem.getCookTime());

				startIteration();
				System.out.println("Cook " + cook.getCookId() + " prepared " + menuItem + " for order "
						+ order.getOrderId() + " at time " + Utilities.getTime());
				unLockMaker(menuItem, order);
				order.preparedItem(menuItem);
				endIteration();

			} else {
				endIteration();
			}
		}

		System.out.println("Cook " + cook.getCookId() + " has prepared order " + order.getOrderId() + " at time "
				+ Utilities.getTime());
		Resources.getCompletedOrders().add(order);

		endIteration();
	}

}
