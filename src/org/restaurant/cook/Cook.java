package org.restaurant.cook;

import java.util.ArrayList;
import java.util.List;

import org.restaurant.food.Order;
import org.restaurant.util.Utilities;

public class Cook {
	
	private int cookId;
	
	private List<Order> completedOrders = new ArrayList<Order>();
	
	private Order currentOrder;
	
	private boolean isWorkingOnOrder;
	
	public Cook(){
		this.cookId = Utilities.getCookId();
		this.isWorkingOnOrder = false;
	}
	
	public void assignOrder(Order order){
		this.isWorkingOnOrder = true;
		this.currentOrder = order;
	}
	
	public void completeOrder(){
		this.completedOrders.add(currentOrder);
		this.isWorkingOnOrder = false;
		this.currentOrder = null;
	}
	
	public Order getCurrentOrder(){
		return currentOrder;
	}

	public int getCookId(){
		return this.cookId;
	}
	
	public boolean isWorkingOnOrder(){
		return this.isWorkingOnOrder;
	}
	
}
