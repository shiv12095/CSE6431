package org.restaurant.resources;

import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;

import org.restaurant.cook.Cook;
import org.restaurant.cook.CookRunnable;
import org.restaurant.diner.Diner;
import org.restaurant.food.Order;
import org.restaurant.util.Constants;

public class Resources {

	private static LinkedBlockingQueue<Order> PENDING_ORDERS = new LinkedBlockingQueue<Order>();

	private static LinkedBlockingQueue<Order> COMPLETED_ORDERS = new LinkedBlockingQueue<Order>();

	private static PriorityBlockingQueue<Diner> WAITING_DINERS = new PriorityBlockingQueue<>();

	private static ArrayBlockingQueue<Diner> TABLES;

	private static ArrayList<Order> BURGER_MAKER = new ArrayList<>();

	private static ArrayList<Order> COKE_MAKER = new ArrayList<>();

	private static ArrayList<Order> FRIES_MAKER = new ArrayList<>();

	private static ArrayList<Order> SUNDAE_MAKER = new ArrayList<>();
	
	public static CountDownLatch COUNT_DOWN_LATCH;
	
	public static CountDownLatch READY_TO_START_COUNT_DOWN_LATCH;
	
	public static CountDownLatch READY_TO_EXIT_COUNT_DOWN_LATCH;
	
	public static CountDownLatch ENTRY_LATCH = new CountDownLatch(1);
	
	public static CountDownLatch EXIT_LATCH = new CountDownLatch(1);
	
	public static CountDownLatch PROGRAM_START_LATCH = new CountDownLatch(1);

	public static void init(int tables, int cooks) {
		TABLES = new ArrayBlockingQueue<>(tables);
		for(int i = 0 ; i < cooks ; i++){
			Cook cook = new Cook();
			new Thread(new CookRunnable(cook)).start();
		}		
	}

	public static ArrayBlockingQueue<Diner> getTables() {
		return TABLES;
	}

	public static LinkedBlockingQueue<Order> getCompletedOrders() {
		return COMPLETED_ORDERS;
	}

	public static LinkedBlockingQueue<Order> getPendingOrders() {
		return PENDING_ORDERS;
	}

	public static PriorityBlockingQueue<Diner> getWaitingDiners() {
		return WAITING_DINERS;
	}

	public static synchronized boolean lockBurgerMaker(Order order) {
		if (BURGER_MAKER.size() < Constants.BURGER_MAKER_QUANTITY) {
			BURGER_MAKER.add(order);
			return true;
		} else {
			return false;
		}
	}

	public static synchronized void unLockBurgerMaker(Order order) {
		BURGER_MAKER.remove(order);
	}

	public static synchronized boolean lockCokeMaker(Order order) {
		if (COKE_MAKER.size() < Constants.COKE_MAKER_QUANTITY) {
			COKE_MAKER.add(order);
			return true;
		} else {
			return false;
		}
	}

	public static synchronized void unLockCokeMaker(Order order) {
		COKE_MAKER.remove(order);
	}

	public static synchronized boolean lockFriesMaker(Order order) {
		if (FRIES_MAKER.size() < Constants.FRIES_MAKER_QUANTITY) {
			FRIES_MAKER.add(order);
			return true;
		} else {
			return false;
		}
	}

	public static synchronized void unLockFriesMaker(Order order) {
		FRIES_MAKER.remove(order);
	}

	public static synchronized boolean lockSundaeMaker(Order order) {
		if (SUNDAE_MAKER.size() < Constants.SUNDAE_MAKER_QUANTITY) {
			SUNDAE_MAKER.add(order);
			return true;
		} else {
			return false;
		}
	}

	public static synchronized void unLockSundaeMaker(Order order) {
		SUNDAE_MAKER.remove(order);
	}
}
