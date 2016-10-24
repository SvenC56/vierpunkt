package de.dhbw.mannheim.vierpunkt.threadcommander;

import de.dhbw.mannheim.vierpunkt.application.GameMainThreads;
import de.dhbw.mannheim.vierpunkt.db.sendGame;
import javafx.application.Application;

public class commander {
	public static void main(String[] args) {
		GameMainThreads thread1 = new GameMainThreads();
		thread1.run();
	}
}
