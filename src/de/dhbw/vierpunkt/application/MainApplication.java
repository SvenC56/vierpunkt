package de.dhbw.vierpunkt.application;

import java.nio.file.Files;

import de.dhbw.vierpunkt.interfaces.*;
import de.dhbw.vierpunkt.gui.TestGui;
import de.dhbw.vierpunkt.objects.Game;
import javafx.application.Application;
import javafx.stage.Stage;

public class MainApplication extends Application implements ParamListener {
	static TestGui gui = new TestGui();
	static FileInterface filey = new FileInterface();
	static PusherInterface pushy = new PusherInterface();
	static Game game = new Game();
	
	public static void main(String[] args) throws InterruptedException
	{
		// Drei suesse Interfaces senden Events an die GUI
		MainApplication main = new MainApplication();
		filey.addListener(gui);
		pushy.addListener(gui);
		gui.addNameListener(game);
		gui.addParamListener(main);
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception
	{ 		
		
		
		System.out.println("gewaehlte Schnittstelle: " + gui.getSchnittstelle());
	
		
		// wenn pusher als Schnittstelle ausgewaehlt wurde wird der Pusher Thread gestartet
		
		
		gui.start(primaryStage);
		
		
	}

	@Override
	public void startParameterAuswerten(int Zugzeit, String Schnittstelle,
			String Kontaktpfad)
	{
		System.out.println("**** Startevent fired ****");
		
		if(Schnittstelle.equals("pusher"))
		{
			Thread pusherThread = new Thread(){
				@Override
				public void run(){
					pushy.run();
				}
			};
		pusherThread.start();
		}
		
		// wenn datei als Schnittstelle ausgewaehlt wurde wird der file Thread gestartet
		else {

			Thread fileThread = new Thread(){
				@Override
				public void run(){
					filey.setZugZeit(Zugzeit);
					filey.setKontaktPfad(Kontaktpfad);
					filey.run();	
				}
			};
			fileThread.start();
		
		}
	}

}
