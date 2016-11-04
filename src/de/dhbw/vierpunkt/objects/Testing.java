package de.dhbw.vierpunkt.objects;

public class Testing {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Player player = new Player("Tobi");
		Match match = new Match();
		match.setCurrentPlayer(player);
		match.setField(0, 5, player);
		System.out.println(match.inDiagonal(0, 5));
		
		

	}

}
