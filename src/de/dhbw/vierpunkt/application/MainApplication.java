package de.dhbw.vierpunkt.application;

import de.dhbw.vierpunkt.interfaces.*;
import de.dhbw.vierpunkt.logic.Game;
import de.dhbw.vierpunkt.gui.TestGui;
import javafx.application.Application;
import javafx.stage.Stage;

public class MainApplication extends Application implements ParamListener{
	
	static PusherInterface pushy = new PusherInterface(); 
	static TestGui gui = new TestGui();
	static Game game = new Game();
	static MainApplication main = new MainApplication();
	/**
	 * In der Main-Methode der MainApplication Klasse werden die Empfaenger fuer die Start- und Logik-Events festgelegt.
	 * Danach wird die Startmethode aufgerufen, in der das GUI aufgebaut wird
	 */
	public static void main(String[] args) throws InterruptedException
	{
		gui.addParamListener(main);
		gui.addNameListener(game);
		
		pushy.addListener(gui);
		pushy.addErrorListener(gui);
		pushy.addGewinnerListener(gui);
		
		launch(args);
	}
	
	/**
	 * Die Start-Methode uebergibt dem GUI-Objekt die primary Stage und wird damit zum Application Thread.
	 * Veraenderungen am GUI duerfen nur hier, oder in der GUI-Klasse vorgenommen werden.
	 */
	@Override
	public void start(Stage primaryStage) throws Exception
	{ 		
		gui.start(primaryStage);
	}
	
	/**
	 * Diese Methode wird beim Klicken das Start-Buttons auf dem User Interface aufgerufen.
	 * es werden die Parameter fuer die gewaehlte Schnittstelle, und die in den Einstellungen
	 * festgelegten Werte fuer Zugzeit, Kontaktpfad und Spielerkennung uebergeben.
	 * @param Schnittstelle, Zugzeit, Kontaktpfad, spielerKennnung
	 */
	@Override
	public void startParameterAuswerten(int Zugzeit, String Schnittstelle,
			String Kontaktpfad, char spielerKennung, String AppID, String AppKey, String AppSecret)
	{
		// wenn Pusher als Schnittstelle ausgewaehlt wurde wird der Pusher Thread gestartet
		if(Schnittstelle.equals("pusher"))
		{
			PusherInterface pushy = new PusherInterface(Zugzeit, AppID, AppKey, AppSecret, spielerKennung, game);
			// das GUI Objekt wird zum Listener fuer Zug-Events der Schnittstelle
			
			
			Thread pusherThread = new Thread(){
				@Override
				public void run(){
					pushy.run();
				}
			};
		pusherThread.start();
		}
		
		// wenn Datei als Schnittstelle ausgewaehlt wurde wird der file Thread gestartet
		else {
				FileInterface filey = new FileInterface(spielerKennung, Kontaktpfad, Zugzeit);	
				// das GUI Objekt wird zum Listener fuer Zug-Events der Schnittstelle	
				filey.addListener(gui);
	
				Thread fileThread = new Thread(){
					@Override
					public void run(){
						filey.run();	
					}
				};
				fileThread.start();
		}
	}

}
