package org.restaurant.diner;

import org.restaurant.resources.Resources;
import org.restaurant.util.Constants;
import org.restaurant.util.Utilities;

public class DinerRunnable implements Runnable {

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

	private Diner diner;

	public DinerRunnable(Diner diner) {
		this.diner = diner;
	}

	public Diner getDiner() {
		return diner;
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

		int currTime = Utilities.getTime();

		boolean isWaitingToBeSeated = true;

		while (isWaitingToBeSeated) {
			startIteration();
			isWaitingToBeSeated = !(Resources.getTables().remainingCapacity() > 0
					&& Resources.getWaitingDiners().peek().getDinerId() == diner.getDinerId());
			if (!isWaitingToBeSeated) {
				System.out.println("Diner " + diner.getDinerId() + " has been seated and placed order "
						+ diner.getOrder().getOrderId() + " at time " + currTime);
				Utilities.seatDiner(diner);
			}
			endIteration();
			currTime = Utilities.getTime();
		}

		boolean isWaitingForFood = true;

		while (isWaitingForFood) {
			startIteration();
			isWaitingForFood = !Resources.getCompletedOrders().contains(diner.getOrder());
			if (!isWaitingForFood) {
				System.out.println("Diner " + diner.getDinerId() + " got food at time " + currTime);
			}
			endIteration();
			currTime = Utilities.getTime();
		}

		int eatingTime = 0;

		while (eatingTime < Constants.EATING_TIME) {
			startIteration();
			eatingTime++;
			endIteration();
			if (eatingTime == Constants.EATING_TIME) {
				System.out.println("Diner " + diner.getDinerId() + " finished food at time " + currTime);
			}
			currTime = Utilities.getTime();
		}

		startIteration();
		System.out.println("Diner " + diner.getDinerId() + " is leaving at time " + currTime);
		Utilities.removeDiner(diner);
		endIteration();
	}
}
