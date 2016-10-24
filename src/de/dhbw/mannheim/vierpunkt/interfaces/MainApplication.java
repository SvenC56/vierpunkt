package de.dhbw.mannheim.vierpunkt.interfaces;

import de.dhbw.mannheim.vierpunkt.gui.TestGui;
import javafx.application.Application;
import javafx.stage.Stage;

public class MainApplication extends Application
{
	static TestGui gui = new TestGui();
	static FileInterface filey = new FileInterface();
	static PusherInterface pushy = new PusherInterface();
	
	public static void main(String[] args)
	{
		// Zwei Süße Interfaces senden Events an die GUI
		filey.addListener(gui);
		pushy.addListener(gui);
		
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception
	{ 		
		// Thread fuer das User Interface
		Thread GuiThread = new Thread(){
			@Override
			public void run(){
				gui.start(primaryStage);
			}
		};
		GuiThread.start();
		
		// wenn pusher als Schnittstelle ausgewaehlt wurde wird der Pusher Thread gestartet
		if(gui.getSchnittstelle().equals("pusher"))
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
					filey.run();	
				}
			};
			fileThread.start();
		
		}
		
		
	}

}
