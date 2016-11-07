package de.dhbw.vierpunkt.application;

import java.nio.file.Files;

import de.dhbw.vierpunkt.interfaces.*;
import de.dhbw.vierpunkt.gui.TestGui;
import de.dhbw.vierpunkt.objects.Game;
import javafx.application.Application;
import javafx.stage.Stage;

public class MainApplication extends Application implements ParamListener {
	static TestGui gui = new TestGui();

	static PusherInterface pushy = new PusherInterface();
	static Game game = new Game();
	
	public static void main(String[] args) throws InterruptedException
	{
		// Drei suesse Interfaces senden Events an die GUI
		MainApplication main = new MainApplication();
		
		pushy.addListener(gui);
		gui.addNameListener(game);
		gui.addParamListener(main);
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception
	{ 		
		gui.start(primaryStage);

	}

	@Override
	public void startParameterAuswerten(int Zugzeit, String Schnittstelle,
			String Kontaktpfad, char spielerKennnung)
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
			FileInterface filey = new FileInterface(spielerKennnung, Kontaktpfad, Zugzeit);			
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
