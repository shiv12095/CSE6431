package org.restaurant.food;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.restaurant.util.Utilities;

public class Order {

	private Map<MenuItem, Integer> orderMap = new HashMap<MenuItem, Integer>();

	private Map<MenuItem, Integer> pendingItemsMap = new HashMap<MenuItem, Integer>();

	private int orderId;

	private int dinerId;

	public Order(int dinerId) {
		this.dinerId = dinerId;
		this.orderId = Utilities.getOrderId();
	}

	public int getDinerId() {
		return dinerId;
	}

	public int getOrderId() {
		return orderId;
	}

	public void add(MenuItem menuItem) {
		int quantity = orderMap.getOrDefault(menuItem, 0);
		orderMap.put(menuItem, quantity + 1);
		pendingItemsMap.put(menuItem, quantity + 1);
	}

	public void add(MenuItem menuItem, int quantity) {
		int originalQuantity = orderMap.getOrDefault(menuItem, 0);
		orderMap.put(menuItem, originalQuantity + quantity);
		pendingItemsMap.put(menuItem, originalQuantity + quantity);
	}

	public void preparedItem(MenuItem menuItem) {
		int quantity = pendingItemsMap.getOrDefault(menuItem, 0);
		if (quantity == 0) {
			return;
		}
		int newQuantity = quantity - 1;
		pendingItemsMap.put(menuItem, newQuantity);

	}

	public boolean isOrderCompleted() {
		int quantity = 0;
		Iterator<MenuItem> iterator = pendingItemsMap.keySet().iterator();
		while (iterator.hasNext()) {
			MenuItem menuItem = iterator.next();
			quantity += pendingItemsMap.getOrDefault(menuItem, 0);
		}
		return quantity == 0;
	}

	public MenuItem nextMenuItem() {
		Iterator<MenuItem> iterator = pendingItemsMap.keySet().iterator();
		while(iterator.hasNext()){
			MenuItem menuItem = iterator.next();
			if(pendingItemsMap.get(menuItem) != 0){
				return menuItem;
			}
		}
		return null;
	}
}
