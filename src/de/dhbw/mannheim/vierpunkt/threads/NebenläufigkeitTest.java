package de.dhbw.mannheim.vierpunkt.threads;

public class Nebenl�ufigkeitTest {
	public static void main(String[] args) {
		Nebenl�ufigkeit counter = new Nebenl�ufigkeit();
		Thread secondThread = new Thread(counter, "second");
		secondThread.start();
		counter.run();
	}
}