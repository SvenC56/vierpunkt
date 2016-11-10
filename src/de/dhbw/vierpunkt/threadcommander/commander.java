package de.dhbw.vierpunkt.threadcommander;

import de.dhbw.vierpunkt.application.GameMainThreads;
import javafx.application.Application;

public class commander {
	public static void main(String[] args) {
		GameMainThreads thread1 = new GameMainThreads();
		thread1.run();
	}
}
