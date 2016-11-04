package de.dhbw.mannheim.vierpunkt.objects;

import de.dhbw.mannheim.vierpunkt.db.DBConnector;
import de.dhbw.mannheim.vierpunkt.objects.AlphaBeta;

public class ConnectClass implements NameListener {
	
	/**************************************************************/
	/******************* Attribute ********************************/
	/**************************************************************/
	
	private AlphaBeta ki = new AlphaBeta();
	private DBConnector db = new DBConnector();
	private Game game;

	
	
	
	public void startGame(String name1, String name2) {
		// WAS?!
		db.createGame(name1, name2);
		game = new Game(name1, name2);
	}
	
	/**
	 * Methode startet eine Runde (Match)
	 */
	public void startMatch() {
		int gameID = db.getGameID();
		Match match = game.getNewMatch();
		match = new Match();	
		db.createMatch(gameID, match.getMatchID());
	}
	
	
	
	public int startTurn (int x, Player player) {
		Match match = game.getCurrentMatch();
		Turn turn = match.setNewTurn();
		if (!player.getIsServer()) {
			x = ki.calcMove(match);
		}
		
		int y = match.validPosition(x);
		turn = new Turn(match.getTurnNumber(), player, x, y);
		db.saveTurn(turn.getTurnID(), match.getMatchID(), player.getName(), x, y);
		match.checkWinner(game);
		if (match.getEven() || match.getMatchWinner() != null) {
			System.out.println("Gewinner ist " + match.getMatchWinner().getName());
			if (match.getEven()) {
				System.out.println("UNENTSCHIEDEN!");
			}}
		return x;
	}

}