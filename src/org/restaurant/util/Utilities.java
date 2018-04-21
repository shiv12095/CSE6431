package org.restaurant.util;

import java.util.Scanner;

import org.restaurant.diner.Diner;
import org.restaurant.diner.DinerRunnable;
import org.restaurant.food.Order;
import org.restaurant.resources.Resources;

public class Utilities {

	private static int DINER_ID = 0;

	private static int ORDER_ID = 0;

	private static int COOK_ID = 0;

	private static int TIME = 0;

	private static int DINERS = 0;
	
	private synchronized static void increaseDiners() {
		DINERS++;
	}
	
	private synchronized static void decreaseDiners() {
		DINERS--;
	}
	
	public synchronized static int getDiners() {
		return DINERS;
	}
		
	public static void initNewDiner(Diner diner){
		increaseDiners();
		Resources.getWaitingDiners().add(diner);
		new Thread(new DinerRunnable(diner)).start();
	}
	
	public static void removeDiner(Diner diner){
		decreaseDiners();
		Resources.getTables().remove(diner);
//		Resources.getWaitingDiners().add(diner);
	}
	
	public static void seatDiner(Diner diner){
		Resources.getTables().add(diner);
		Resources.getPendingOrders().add(diner.getOrder());
		Resources.getWaitingDiners().remove(diner);
	}
	
	public synchronized static void tick() {
		TIME++;
	}

	public synchronized static int getTime() {
		return TIME;
	}

	public static void sleep(int millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static synchronized int getDinerId() {
		DINER_ID++;
		int newId = DINER_ID;
		return newId;
	}

	public static synchronized int getOrderId() {
		ORDER_ID++;
		int newId = ORDER_ID;
		return newId;
	}

	public static synchronized int getCookId() {
		COOK_ID++;
		int newId = COOK_ID;
		return newId;
	}

	public static int getNextNum(Scanner scanner) {
		String line = scanner.nextLine().trim();
		return Integer.parseInt(line);
	}

	public static Diner[] getDinersFromFile(Scanner scanner) {
		Diner[] diners = new Diner[Constants.RESTAURANT_CLOSE_TIME];
		while (scanner.hasNext()) {
			String line = scanner.nextLine();
			Diner diner = createDiner(line);
			if(diner.getArrivalTime() < Constants.RESTAURANT_CLOSE_TIME){
				diners[diner.getArrivalTime()] = diner;				
			}

		}
		return diners;
	}

	private static Diner createDiner(String line) {
		String arr[] = line.split("\\s+");
		int arrivalTime = Integer.parseInt(arr[0]);
		Diner diner = new Diner(arrivalTime);
		Order order = new Order(diner.getDinerId());
		for (int i = 1; i < arr.length; i++) {
			int quantity = Integer.parseInt(arr[i]);
			int menuIndex = i - 1;
			order.add(Constants.ORDERED_MENU[menuIndex], quantity);
		}
		diner.setOrder(order);
		return diner;
	}

}
