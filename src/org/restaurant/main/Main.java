package org.restaurant.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;

import org.restaurant.diner.Diner;
import org.restaurant.resources.Resources;
import org.restaurant.util.Constants;
import org.restaurant.util.Utilities;

public class Main {

	public static void main(String[] args) throws FileNotFoundException, InterruptedException {
		String fileName = "test1.txt";
		File file = new File(fileName);

		Scanner scanner = new Scanner(file);
		Utilities.getNextNum(scanner);
		int tables = Utilities.getNextNum(scanner);
		int cooks = Utilities.getNextNum(scanner);
		Diner[] diners = Utilities.getDinersFromFile(scanner);

		Resources.init(tables, cooks);

		boolean shouldStartProgram = true;
		boolean isRestaurantOpen = true;
		int currTime = Utilities.getTime();

		int numThreads = cooks;
		if (diners[0] != null) {
			numThreads++;
		}

		Resources.READY_TO_START_COUNT_DOWN_LATCH = new CountDownLatch(numThreads);
		Resources.READY_TO_EXIT_COUNT_DOWN_LATCH = new CountDownLatch(numThreads);

		while (isRestaurantOpen) {
			if (currTime < Constants.RESTAURANT_CLOSE_TIME) {
				if (diners[currTime] != null) {
					Diner diner = diners[currTime];
					Utilities.initNewDiner(diner);
				}
			} else {
				if (Utilities.getDiners() == 0) {
					isRestaurantOpen = false;
					break;
				}
			}

			if (shouldStartProgram) {
				Resources.PROGRAM_START_LATCH.countDown();
				shouldStartProgram = false;
			}

			Resources.READY_TO_START_COUNT_DOWN_LATCH.await();
			Resources.READY_TO_EXIT_COUNT_DOWN_LATCH = new CountDownLatch(numThreads);
			Resources.EXIT_LATCH = new CountDownLatch(1);
			Resources.ENTRY_LATCH.countDown();

			Resources.READY_TO_EXIT_COUNT_DOWN_LATCH.await();
			Resources.ENTRY_LATCH = new CountDownLatch(1);

			Utilities.tick();

			currTime = Utilities.getTime();
			numThreads = Utilities.getDiners() + cooks;

			if (currTime < Constants.RESTAURANT_CLOSE_TIME) {
				if (diners[currTime] != null) {
					numThreads += 1;
				}
			}

			Resources.READY_TO_START_COUNT_DOWN_LATCH = new CountDownLatch(numThreads);
			Resources.EXIT_LATCH.countDown();
		}

		System.out.println("The restaurant is now closed at time " + Utilities.getTime());

	}
}
