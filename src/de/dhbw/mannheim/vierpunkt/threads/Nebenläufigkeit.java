package de.dhbw.mannheim.vierpunkt.threads;

public class Nebenläufigkeit implements Runnable {

	public void run() {
		Thread thread = Thread.currentThread();
		for (long count = 0; count <= 1000; ++count) {
			System.out.println(thread + "COUNT:" + count);
		}
	}
}

