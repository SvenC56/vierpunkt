package de.dhbw.mannheim.vierpunkt.objects;

import de.dhbw.mannheim.vierpunkt.db.DBConnector;
import de.dhbw.mannheim.vierpunkt.gui.TestGui;
import de.dhbw.mannheim.vierpunkt.objects.AlphaBeta;

public class ConnectClass {
	
	/**************************************************************/
	/******************* Attribute ********************************/
	/**************************************************************/
	
	private AlphaBeta ki = new AlphaBeta();
	private TestGui gui = new TestGui();
	private DBConnector db = new DBConnector();
	private Game game;
	private Match match;
	
	
	
	public Game startGame() {
		String name1 = gui.getNames1();
		String name2 = gui.getNames2();
		int gameID = db.createGame(name1, name2);
		game = new Game(gameID, name1, name2);

		return game;
	}
	
	/**
	 * Methode startet einen Satz (Match)
	 */
	public void startMatch() {
		int matchID = db.createMatch(game.getGameID());
		match = new Match(game.getGameID(), matchID);	
		
	}
	
	
	
	public int startTurn (Match match, int x, Player player) {
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
