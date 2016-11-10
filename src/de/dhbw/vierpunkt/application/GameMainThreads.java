package de.dhbw.vierpunkt.application;

import de.dhbw.vierpunkt.interfaces.*;
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
