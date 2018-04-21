package org.restaurant.diner;

import org.restaurant.food.Order;
import org.restaurant.util.Utilities;

public class Diner implements Comparable<Diner>{

	private int dinerId;
	
	private Order order;
	
	private int arrivalTime;
	
	public Diner(int arrivalTime){
		this.arrivalTime = arrivalTime;
		this.dinerId = Utilities.getDinerId();
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public int getDinerId() {
		return dinerId;
	}

	public Order getOrder() {
		return order;
	}

	public int getArrivalTime() {
		return arrivalTime;
	}

	@Override
	public int compareTo(Diner diner) {
		return this.dinerId - diner.getDinerId();
	}	
}
