package de.dhbw.mannheim.vierpunkt.logic;

import de.dhbw.mannheim.vierpunkt.db.*;
import de.dhbw.mannheim.vierpunkt.gui.*;

import java.util.Random; //Temporaer fuer Test

/**
 * Spiellogik / Zum Reinkommen in die Spiellogik Autoren: Gruppe 4 (vier.) -
 * Verantwortlich: Tobias Jung
 **/
public class GameLogic {

	// Allgemeine Regeln / Hinweise Lauterbach
	// Spalte: von links nach rechts (0,1,2,3,4,5,6) => 7 Maximale Breite
	// Zeile: von oben nach unten (2,3,4,5,6,7) => 6 Maximale Tiefe (+1 Kopf?)

	/**************************************************************/
	/******************* Attribute ********************************/
	/**************************************************************/
	private connectHSQL db = new connectHSQL();
	private AlphaBeta ki = new AlphaBeta();
	private TestGui gui = new TestGui();
	// Spielfeld
		// MAXIMALE ANZAHL SPALTEN
		private static final int COLUMN = 6;
		// MAXIMALE ANZAHL ZEILEN
		private static final int ROW = 5;
		// Variable die Zuege mitzaehlt! //Move entspricht TURN
		private int move = 0; // --> maximale Anzahl Zuege 69!
	/**
	 * Array fuer Spielfeld --> 0 enstpricht leere Position! 1 = SERVER! 2 =
	 * AGENT (SPIELER)
	 */
	private int[][] field = new int[ROW + 1][COLUMN + 1];
	private int gameID = 0; // entspricht Spiel
	private int matchID= 0; // entspricht Runde
	private int turnId = 0; // entspricht Zug
	private String player1 = null;
	private String player2 = null;
	private int currentPlayer = 0; //Der aktuelle Spieler
	
	// Allgemeine Information: x entspricht Spalte / y entspricht Zeile

	/**************************************************************/
	/******************* KONSTRUKTOR *******************************/
	/**************************************************************/

		public GameLogic() {
			// Array durchlaufen und mit Nullen fuellen + move auf false setzen, da
			// kein Spieler am Zug ist!
			this.move = 0;
			for (int y = 0; y <= ROW; y++) {
				for (int x = 0; x <= COLUMN; x++) {
					this.field[y][x] = 0;
				}
			}
		}
	
		/**
		 * Erstellt eine Kopie des derzeitigen Spiels zur Analyse in der KI 
		 * @return
		 */
		GameLogic getDemoGame() {
			GameLogic game2 = new GameLogic();
			for (int i = 0; i <= COLUMN; i++) {
				for (int j = 0; j <= ROW; j++) {
					game2.setDemoField(i, j, this.getField(i, j));
				}
			}
			return game2;
		}
	
		
		/**************************************************************/
		/****************** Getter / Setter ***************************/
		/**************************************************************/
		
	/**
	 * 
	 * 
	 * eine funktion die das match, game saved!
	 * Wenn wir Daten vom Server bekommen (gegner)
	 */
		
	int getCurrentPlayer() {
		return this.currentPlayer;
	}

	 int getColumn() {
		return COLUMN;
	}

	private int getRow() {
		return ROW;
	}
	/**
	 * Zaehlt den Zug um 1 hoch
	 */
	private void setTurn() {
		this.move = move++;
	}
	
	/**
	 * Setzt den aktuellen Spieler
	 * @param value
	 */
	private void setCurrentPlayer(int value) {
		if (value == 1) {
			this.currentPlayer = 1;		
		}
		if (value == 2) {
			this.currentPlayer = 2;
		}
	}
	
	/**
	 * Getter fuer field. Erwartet x und y - Wert und liefert den Wert im Array
	 * zurueck!
	 **/
	private int getField(int x, int y) {
		return this.field[y][x];
	}

	// Setter fuer field
	private void setField(int x, int y) {
		this.field[y][x] = this.getCurrentPlayer();
		this.setTurn();
		this.saveTurn(x, y);
	}
	
	/**************************************************************/
	/************************ LOGIK *********************************/
	/**************************************************************/

	/**
	 * Spielt den Zug --> Verbindung zum Interface, liefert Spalte zurueck
	 */
	public int playTurn(int x, int player){
		this.setCurrentPlayer(player);
		//Maximierung, da eigener Spieler
		if (getCurrentPlayer() == 2) {
		x = ki.calcMove(this);
		this.setChip(x);
		this.checkWinner();
		return x;
		}
		else {
		this.setChip(x);
		this.checkWinner();
		return -1;
		}
	}
	
	
	/**
	 * Setzt den Chip eines Spielers
	 * 
	 * @param x
	 */
	 void setChip(int x) {
		int y = this.validPosition(x);
		this.setField(x, y);
		}
	
	/**
	 * Prueft, ob ein Spieler gewonnen hat! Gibt einen int zurueck
	 * @return
	 * 1 --> Spieler 1 oder Server
	 * 2 --> Spieler 2 oder Agent
	 * 3 --> UNENTSCHIEDEN
	 * 0 --> noch kein Gewinner
	 */
	 
	private int checkWinner() {
		//pruefe nur, wenn move >= 4! Sonst ist kein Gewinn moeglich
		if (this.move >= 4) {
		//wenn negativ unendlich, dann hat der Gegner (Server) gewonnen
		if (this.evaluate() == (int)Double.NEGATIVE_INFINITY) {
			return 1;
		}
		//wenn positiv unendlich, dann hat der Agent (wir) gewonnen
		else if (this.evaluate() == (int)Double.POSITIVE_INFINITY) {
			return 2;
		}
		
		else {
			int counter=0;
			for (int x=0; x <= COLUMN; x++ ) {
				if (this.validPosition(x) == -1) {
				counter++;	
				}
			}
			//wenn Counter =7, dann steht es unentschieden
			if (counter == 7) {
				return 3;
			}
			//andernfalls kann kein Gewinner festgestellt werden
			return 0;
		}
		}
		return 0;
	}
	
	/**
	 * Prueft, ob Chip eingeworfen werden kann gibt -1(keine valide Position)
	 * oder Zeile zurueck!
	 **/
	int validPosition(int x) {
		int temp = 0;
		// Spalte muss im richtigen Bereich > 0 & kleiner max. Anzahl SPALTEN
		if (x > -1 && x <= COLUMN) {
			for (int y = 0; y <= ROW; y++) {
				if (this.field[y][x] == 0) { // leere Position gefunden
					return y; // gibt Zeile zurueck!
				} // kein leeres Feld
				else {
					temp = -1;

				}
			}
		} else {
			temp = -1; // Eingabe ausserhalb des Spielbereichs
			System.err.println("Eingabe ausserhalb Spielbereich!");

		}
		// temp nur zurueckgeben, wenn noch keine Zeile returned wurde, war eine
		// if, mal gucken, ob erforderlich!
		return temp;
	}
	
	private String getCurrentPlayerName() {
		if (this.getCurrentPlayer() == 1) {
			return this.player1;
		}
		else if (this.getCurrentPlayer() == 2) {
			return this.player2;
		}
		else{
			return null;
		}
	}
	
	/**************************************************************/
	/************************** KI ********************************/
	/**************************************************************/

	/**
	 * Setter fuer Demofeld der KI
	 * 
	 * @param x
	 * @param y
	 * @param value
	 */
	private void setDemoField(int x, int y, int value) {
		field[y][x] = value;
	}
	
	
	/**************************************************************/
	/************************ BEWERTUNG *****************************/
	/**************************************************************/

	/**
	 * Bewertungsfunktion - Bewertet den Pfad nach aktuellem Stand und liefert
	 * Zahlenwert!
	 *
	private int pathEval(int x, int y, int spieler) {
		int evaluation = 0;
		// Idee: Die Summe der count ist die Bewertung des Pfades!!
		// System.err.println("Methode pathEval wurde aufgerufen!");
		evaluation = inRow(x, y, spieler) + inColumn(x, y, spieler) + inDiagonal(x, y, spieler);
		// System.err.println("Bewertung des Pfades durchgefuehrt: " +
		// evaluation);
		return evaluation;
	}
	**/
	
	/**
	 * Bewertet die aktuelle Spielsituation und liefert die Spalte zurueck, in
	 * welche eingeworfen werden soll. Wenn -1 uebergeben wird, dann gibt es
	 * keinen validen Pfad! Bewertet die Situation des letzten Zuges und prueft
	 * somit, ob Gegner in besserer Gewinnsituation ist!
	 * 
	 * @param spieler
	 * @return
	 
	public int bestPath(int spieler) {
		int bestColumn = -1;
		int tmp = 0;
		int maxEval = 0;
		int oponent = 0;
		;
		if (spieler == 2 && move > 0) {
			oponent = pathEval(lastX, lastY, 1);
		}
		for (int x = 0; x <= row; x++) {
			int y = validPosition(x);
			if (y != -1) {
				tmp = pathEval(x, y, spieler);
				if (maxEval <= tmp) {
					maxEval = tmp;
					bestColumn = x;
				}
			}
		}
		if (maxEval < oponent) {
			bestColumn = lastX;
		}

		return bestColumn;
	}
*/

	/** Gibt Anzahl der Chips des gleichen Spieler in Spalte zurueck **/
	private int inColumn(int x, int y) {
		// System.err.println("Methode inColumn wurde aufgerufen!");
		int count = 0; // Zaehler der validen Chips des gleichen Spielers in
						// Spalte
		int temp = y;
		if (this.getField(x, y) == 0 || this.getField(x, y) == this.getCurrentPlayer()) {
			count++;
			y--;
		}
		for (; y > -1; y--) { // von unten nach oben!
			if (this.getField(x, y) == getCurrentPlayer()) {
				count++;
			} else
				break;
		}
		if (count < 4 && temp <= ROW) { // von oben nach unten! (nur, wenn
										// Counter 4 noch nicht erreicht, da
										// Spiel sonst gewonnen)
			y = temp + 1;
			for (; y <= ROW; y++) { // Limitiert durch Anzahl Zeilen!
				if (this.getField(x, y) == this.getCurrentPlayer()) {
					count++;
				} else
					break;
			}
		}
		return count;
	}

	/** Gibt Anzahl der Chips des gleichen Spielers in der Diagonale zurueck **/
	private int inDiagonal(int x, int y) {
		// System.err.println("Methode inDiagonal wurde aufgerufen!");
		int count = 0;
		int startX = x;
		int startY = y;
		if (this.getField(x, y) == 0 || this.getField(x, y) == this.getCurrentPlayer()) {
			count++;
			x++;
			y--;
		}
		// Prueft oben - rechts
		for (; (x <= COLUMN && y > -1); x++, y--) {
			if (this.getField(x, y) == this.getCurrentPlayer()) {
				count++;
			} else
				break;
		}
		// Prueft oben - links
		if (count < 4 && (y > -1 && x > -1)) {
			x = startX - 1;
			y = startY - 1;
			for (; (x > -1 && y > -1); x--, y--) {
				if (this.getField(x, y) == this.getCurrentPlayer()) {
					count++;
				} else
					break;
			}
		}

		if (count < 4 && (y <= ROW && x > -1)) {
			x = startX - 1;
			y = startY + 1;
			// Prueft unten - links
			for (; (x > -1 && y <= ROW); x--, y++) {

				if (this.getField(x, y) == this.getCurrentPlayer()) {
					count++;
				} else
					break;
			}
		}
		if (count < 4 && (y <= ROW && x <= COLUMN)) {
			x = startX + 1;
			y = startY + 1;
			// Prueft unten - rechts
			for (; (x <= COLUMN && y <= ROW); x++, y++) {

				if (this.getField(x, y) == this.getCurrentPlayer()) {
					count++;
				} else
					break;
			}
		}
		return count;
	}

	/** Gibt Anzahl der Chips des gleichen Spieler in Reihe (Zeile) zurueck **/
	private int inRow(int x, int y) {
		// System.err.println("Methode inRow wurde aufgerufen!");
		int count = 0;
		int temp = x;
		if (this.getField(x, y) == 0 || this.getField(x, y) == this.getCurrentPlayer()) {
			count++;
			x++;
		}
		for (; x <= COLUMN; x++) { // von links nach rechts! Limitiert durch
									// Anzahl Spalten!
			if (this.getField(x, y) == this.getCurrentPlayer()) {
				count++;
			} else
				break;
		}
		if (count < 4 && temp > 0) { // von rechts nach links (nur, wenn Counter
										// 4 noch nicht erreicht, da Spiel sonst
										// gewonnen)
			x = temp - 1;
			for (; x > -1; x--) {
				if (this.getField(x, y) == this.getCurrentPlayer()) {
					count++;
				} else
					break;

			}
		}
		return count;
	}

	/**************************************************************/
	/************************ Datenbank *****************************/
	/**************************************************************/

	/**
	 * Speichert den durchgefuehrten Zug
	 * @param x
	 * @param y
	 */
	private void saveTurn(int x, int y) {
		sendTurn turnDBThread = new sendTurn(turnId,matchID,getCurrentPlayerName(), x, y);
		turnDBThread.run();
		this.turnId++; //ist das Move?
	}

	
	/**
	 * Ruft in der Datenbank die aktuelle GameID ab, welche einem Spiel
	 * entspricht. Gibt einen int-Wert zurueck
	 * 
	 * @return
	 */
	private int getNewGameID() {
		this.gameID = db.getMaxId("Game");
		this.gameID++; // + 1, da zuletzt belegte ID zurueck

		return this.gameID;
	}
	
	private int getGameID() {
		this.gameID = db.getMaxId("Game");

		return this.gameID;
	}

	/**
	 * Ruft in der Datenbank die aktuelle Match ab, welche einer Runde
	 * entspricht. Gibt einen int-Wert zurueck
	 * 
	 * @return
	 */
	private int getNewMatchID() {
		this.matchID = db.getMaxId("Match");
		this.matchID++; // + 1, da zuletzt belegte ID zurueck

		return this.matchID;
	}
	
	private int getMatchID() {
		this.matchID = db.getMaxId("Match");

		return this.matchID;
	}
	
	/**
	 * Mthode startet / initialisiert das Spiel und auch die DB
	 */
	private void startGame() {
		sendGame dbGame = new sendGame(getNewGameID(), gui.getNames1(), gui.getNames2(), null, null);
		dbGame.run();
	}
	
	private void startMatch() {
		this.matchID = getNewMatchID();
		sendMatch dbMatch = new sendMatch(matchID, getGameID());
		dbMatch.run();
	}

	 int evaluate() { // bewertet die gesamte Spielsituation
		int agentCount2 = 0;
		int agentCount3 = 0;
		int oppCount2 = 0;
		int oppCount3 = 0;
		for (int x = 0; x <= this.getColumn(); x++) {
			for (int y = 0; y <= this.getRow(); y++) {
				
				if (this.getCurrentPlayer() == 2) { // unser Agent spielt
					// inColumn
					if (this.inColumn(x, y) == 4) { // unser Agent hat 4 in einer Spalte --> wir haben gewonnen
						return (int) Double.POSITIVE_INFINITY;
					} else if (this.inColumn(x, y) == 3) {
						agentCount3++;
					} else if (this.inColumn(x, y) == 2) {
						agentCount2++;	
	
					// inRow
					} else if (this.inRow(x, y) == 4) { // unser Agent hat 4 in einer Zeile --> wir haben gewonnen
						return (int) Double.POSITIVE_INFINITY;
					} else if (this.inRow(x, y) == 3) {
						agentCount3++;
					} else if (this.inRow(x, y) == 2) {
						agentCount2++;
					
					// inDiagonal
					} else if (this.inDiagonal(x, y) == 4) { // unser Agent hat 4 in der Diagonale --> wir haben gewonnen
						return (int) Double.POSITIVE_INFINITY;
					} else if (this.inDiagonal(x, y) == 3) {
						agentCount3++;
					} else if (this.inDiagonal(x, y) == 2) {
						agentCount2++;
					}
				}		
					
				if (this.getCurrentPlayer() == 1) { // Gegner spielt	
					// in column
					if (this.inColumn(x, y) == 4) { // der Gegner hat 4 in einer Spalte --> Gegner hat gewonnen
						return (int) Double.NEGATIVE_INFINITY;
					} else if (this.inColumn(x, y) == 3) {
						oppCount3++;
					} else if (this.inColumn(x, y) == 2) {
						oppCount2++;
						
					// in row
					} else if (this.inRow(x, y) == 4) { // der Gegner hat 4 in einer Zeile --> Gegner hat gewonnen
						return (int) Double.NEGATIVE_INFINITY;
					} else if (this.inRow(x, y) == 3) {
						oppCount3++;
					} else if (this.inRow(x, y) == 2) {
						oppCount2++;
					
					// in diagonal
					} else if (this.inDiagonal(x, y) == 4) { // der Gegner hat 4 in der Diagonale --> Gegner hat gewonnen
						return (int) Double.NEGATIVE_INFINITY;
					} else if (this.inDiagonal(x, y) == 3) {
						oppCount3++;
					} else if (this.inDiagonal(x, y) == 2) {
						oppCount2++;
					}
				}	
			}
		}
		return agentCount2 + 2 * agentCount3 - oppCount2 - 4 * oppCount3;
	}

}
