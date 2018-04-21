package org.restaurant.food;

import org.restaurant.util.Constants;

public enum MenuItem {

	BURGER(Constants.BURGER_TIME), FRIES(Constants.FRIES_TIME), COKE(
			Constants.COKE_TIME), SUNDAE(Constants.SUNDAE_TIME);

	private int cookTime;

	MenuItem(int cookTime) {
		this.cookTime = cookTime;
	}

	public int getCookTime() {
		return this.cookTime;
	}

}
