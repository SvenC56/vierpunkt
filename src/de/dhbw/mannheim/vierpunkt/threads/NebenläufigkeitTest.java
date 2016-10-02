package de.dhbw.mannheim.vierpunkt.threads;

public class NebenläufigkeitTest {
	public static void main(String[] args) {
		Nebenläufigkeit counter = new Nebenläufigkeit();
		Thread secondThread = new Thread(counter, "second");
		secondThread.start();
		counter.run();
	}
}