package de.dhbw.mannheim.vierpunkt.threads;

public class NebenlaeufigkeitTest {
	public static void main(String[] args) {
		Nebenlaeufigkeit counter = new Nebenlaeufigkeit();
		Thread secondThread = new Thread(counter, "second");
		secondThread.start();
		counter.run();
	}
}