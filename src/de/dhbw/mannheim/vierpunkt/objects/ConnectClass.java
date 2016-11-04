package de.dhbw.mannheim.vierpunkt.objects;

import de.dhbw.mannheim.vierpunkt.db.DBConnector;
import de.dhbw.mannheim.vierpunkt.gui.TestGui;
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
	 * Methode startet einen Satz (Match)
	 */
	public void startMatch() {
		int matchID = db.createMatch(game.getGameID());
		match = new Match(game.getGameID(), matchID);	
		
	}
	
	
	
	public int startTurn (int x, Player player) {
		Turn turn = new Turn();
		if (player.getIsServer()) {
			int y = turn.validPosition(x);
			turn.setX(x);;
			turn.setY(y);
			turn.setField(x, y, player);
			db.saveTurn(turn.getID(), match.getMatchID(), player.getName(), x, y);
			match.setTurnNumber();
			match.checkWinner();
			if (match.getEven() || match.getMatchWinner() != null) {
				System.out.println("Gewinner ist " + match.getMatchWinner().getName());
				if (match.getEven()) {
					System.out.println("UNENTSCHIEDEN!");
				}
			}
		}
		else {
			x = ki.calcMove(match);
			int y = turn.validPosition(x);
			turn.setX(x);
			turn.setY(y);
			db.saveTurn(turn.getID(), match.getMatchID(), player.getName(), x, y);
			match.setTurnNumber();
			match.checkWinner();
			if (match.getEven() || match.getMatchWinner() != null) {
				System.out.println("Gewinner ist " + match.getMatchWinner().getName());
				if (match.getEven()) {
					System.out.println("UNENTSCHIEDEN!");
				}
			}
		}
		return x;
		}

}
