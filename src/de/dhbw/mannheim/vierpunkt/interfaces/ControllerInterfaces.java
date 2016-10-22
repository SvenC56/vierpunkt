package de.dhbw.mannheim.vierpunkt.interfaces;

import de.dhbw.mannheim.vierpunkt.gui.TestGui;

public class ControllerInterfaces
{

	public static void main(String[] args)
	{
		FileInterface filey = new FileInterface();
		
		Thread t1 = new Thread(){
			@Override
			public void run(){
				filey.run();	
			}
		};
		
		TestGui testey = new TestGui();
		
		Thread t2 = new Thread(){
			@Override
			public void run(){
				TestGui.main(args);
			}
		};
		
		//filey.addListener(testey);
		
		t1.start();
		t2.start();
		
		
		
	}

}
