package de.dhbw.vierpunkt.objects;

public class Testing {

	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
		Game game = new Game();
		game.startGame("Tobi", "Sven");
		game.startMatch();
		game.playTurn(2);
		Thread.sleep(500);
		

	}

}
