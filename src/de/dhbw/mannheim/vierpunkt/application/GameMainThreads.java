package de.dhbw.mannheim.vierpunkt.application;

import de.dhbw.mannheim.vierpunkt.interfaces.*;
import de.dhbw.mannheim.vierpunkt.logic.GameLogic;
import javafx.application.Application;

/**
 * Main-Methode zum Testen / Zum Reinkommen in die Spiellogik Autoren: Gruppe 4
 * (vier.) - Verantwortlich: Tobias Jung
 **/
public class GameMainThreads implements Runnable {

	@Override
	public void run() {
		Application.launch(MainApplication.class);
	}

}
