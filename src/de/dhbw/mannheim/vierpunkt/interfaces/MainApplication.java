package de.dhbw.mannheim.vierpunkt.interfaces;

import de.dhbw.mannheim.vierpunkt.gui.TestGui;
import javafx.application.Application;
import javafx.stage.Stage;

public class MainApplication extends Application
{
	static TestGui test = new TestGui();
	public static void main(String[] args)
	{
		FileInterface filey = new FileInterface();
		
		Thread fileThread = new Thread(){
			@Override
			public void run(){
				filey.run();	
			}
		};
		filey.addListener(test);
		fileThread.start();
		
		PusherInterface_Object pushy = new PusherInterface_Object();
		
		Thread pusherThread = new Thread(){
			@Override
			public void run(){
				pushy.run();
			}
		};
		pushy.addListener(test);
		pusherThread.start();
		
		
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception
	{ 
		test.start(primaryStage);
		
	}

}
