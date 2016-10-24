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
		
		Thread t1 = new Thread(){
			@Override
			public void run(){
				filey.run();	
			}
		};
		filey.addListener(test);
		t1.start();

		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception
	{ 
		test.start(primaryStage);
	}

}
