package de.dhbw.mannheim.vierpunkt.logic;

import de.dhbw.mannheim.vierpunkt.db.connectHSQL;
import de.dhbw.mannheim.vierpunkt.db.sendGame;
import de.dhbw.mannheim.vierpunkt.db.sendMatch;
import de.dhbw.mannheim.vierpunkt.db.sendTurn;

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
	/******************* Variablen, Deklarationen *******************/
	/**************************************************************/

	// Spielfeld
	// MAXIMALE ANZAHL SPALTEN
	private int column = 6;
	// MAXIMALE ANZAHL ZEILEN
	private int row = 5;
	// Variable die Zuege mitzaehlt! //Move entspricht TURN
	private int move = 0; // --> maximale Anzahl Zuege 69!
	private int winnerIs = 0;
	/**
	 * Array fuer Spielfeld --> 0 enstpricht leere Position! 1 = SERVER! 2 =
	 * AGENT (SPIELER)
	 */
	private int[][] field = new int[row + 1][column + 1];
	connectHSQL db = new connectHSQL();
	AlphaBeta ki = new AlphaBeta();
	private int gameID = 0; // entspricht Spiel
	private int matchID= 0; // entspricht Runde
	private int turnId = 0; //DB Turn
	private String player1 = null;
	private String player2 = null;
	private int currentPlayer = 0; //Der aktuelle Spieler

	/**
	 * Methode zum Speichern des Spielstandes! Methode um Gewinn zu
	 * erkennen!(count == 4 BREAK)
	 * 
	 * eine funktion die das match, game saved!
	 * Wenn wir Daten vom Server bekommen (gegner)
	 */
	
	public void playTurn(int x, int player){
		setCurrentPlayer(player);
		//Maximierung, da eigener Spieler
		if (getCurrentPlayer() == 2) {
		x = ki.calcMove(this);
		}
		setChip(x);
	}
	
	
	/**
	 * Setzt den aktuellen Spieler
	 * @param value
	 */
	public void setCurrentPlayer(int value) {
		if (value == 1) {
			currentPlayer = 1;		
		}
		if (value == 2) {
			currentPlayer = 2;
		}
	}
	
	public int getCurrentPlayer() {
		return currentPlayer;
	}

	public int getColumn() {
		return column;
	}

	public int getRow() {
		return row;
	}

	private void setTurn() {
		this.move = move++;
	}
	
	public String getCurrentPlayerName() {
		if (getCurrentPlayer() == 1) {
			return player1;
		}
		else if (getCurrentPlayer() == 2) {
			return player2;
		}
		else{
			return null;
		}
	}

	// Allgemeine Information: x entspricht Spalte / y entspricht Zeile

	/**************************************************************/
	/******************* KONSTRUKTOR ********************************/
	/**************************************************************/

	public GameLogic() {
		// DB-Objekt anlegen
		this.db = new connectHSQL();
		// Array durchlaufen und mit Nullen fuellen + move auf false setzen, da
		// kein Spieler am Zug ist!
		move = 0;
		for (int y = 0; y <= row; y++) {
			for (int x = 0; x <= column; x++) {
				field[y][x] = 0;
			}
		}
		// System.err.println("Konstruktor durchlaufen.");
	}

	/**************************************************************/
	/******************* ZUGRIFFSMETHODEN ***************************/
	/**************************************************************/

	/**
	 * Methode gibt zurueck wo der Spieler gewonnen hat
	 */
	private void theWinner() {
		switch (winnerIs) {
		case 11:
			System.out.println("Server gewinnt in Spalte!");
		case 12:
			System.out.println("Spieler gewinnt in Spalte!");
		case 101:
			System.out.println("Server gewinnt in Reihe!");
		case 201:
			System.out.println("Spieler gewinnt in Reihe!");
		case 1001:
			System.out.println("Server gewinnt Diagonal!");
		case 1002:
			System.out.println("Spieler gewinnt Diagonal!");
		}
	}

	/**
	 * Getter fuer field. Erwartet x und y - Wert und liefert den Wert im Array
	 * zurueck!
	 **/

	public int arraylength() {
		return field.length;
	}

	public int getField(int x, int y) {
		return field[y][x];
	}

	// Setter fuer field
	private void setField(int x, int y) {
		field[y][x] = getCurrentPlayer();
		setTurn();
		saveTurn(x, y);
	}

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

	/**
	 * 
	 * Speichert den durchgefuehrten Zug
	 * 
	 * @param x
	 * @param y
	 */
	private void saveTurn(int x, int y) {
		sendTurn turnDBThread = new sendTurn(turnId,matchID,getCurrentPlayerName(), x, y);
		turnDBThread.run();
		turnId++;
	}

	/**
	 * Setzt den Chip eines Spielers
	 * 
	 * @param x
	 */
	public void setChip(int x) {
		int y = validPosition(x);
		setField(x, y);
		}


	/**************************************************************/
	/******************* TEMPORAER TEST-METHODS *********************/
	/**************************************************************/
	public void randomGame() {
		Random value = new Random();
		for (int y = 0; y <= row; y++) {
			for (int x = 0; x <= column; x++) {
				int zahl = value.nextInt(3);
				field[y][x] = zahl;
			}
		}
		// System.err.print("Array mit Zufallszahlen zwischen 0 und 2
		// gefuellt!");
	}

	/**************************************************************/
	/************************ LOGIK *********************************/
	/**************************************************************/

	/**
	 * Prueft, ob Chip eingeworfen werden kann gibt -1(keine valide Position)
	 * oder Zeile zurueck!
	 **/
	int validPosition(int x) {
		int temp = 0;
		// Spalte muss im richtigen Bereich > 0 & kleiner max. Anzahl SPALTEN
		if (x > -1 && x <= column) {
			for (int y = 0; y <= row; y++) {
				if (field[y][x] == 0) { // leere Position gefunden
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

	/** Gibt Anzahl der Chips des gleichen Spieler in Spalte zurueck **/ // Funktioniert!
	private int inColumn(int x, int y) {
		// System.err.println("Methode inColumn wurde aufgerufen!");
		int count = 0; // Zaehler der validen Chips des gleichen Spielers in
						// Spalte
		int temp = y;
		if (getField(x, y) == 0 || getField(x, y) == getCurrentPlayer()) {
			count++;
			y--;
		}
		for (; y > -1; y--) { // von unten nach oben!
			if (getField(x, y) == getCurrentPlayer()) {
				count++;
			} else
				break;
		}
		if (count < 4 && temp <= row) { // von oben nach unten! (nur, wenn
										// Counter 4 noch nicht erreicht, da
										// Spiel sonst gewonnen)
			y = temp + 1;
			for (; y <= row; y++) { // Limitiert durch Anzahl Zeilen!
				if (getField(x, y) == getCurrentPlayer()) {
					count++;
				} else
					break;
			}
		}
		if (count == 4) {
			winnerIs = getCurrentPlayer() + 10;
		}
		return count;
	}

	/** Gibt Anzahl der Chips des gleichen Spielers in der Diagonale zurueck **/
	private int inDiagonal(int x, int y) {
		// System.err.println("Methode inDiagonal wurde aufgerufen!");
		int count = 0;
		int startX = x;
		int startY = y;
		if (getField(x, y) == 0 || getField(x, y) == getCurrentPlayer()) {
			count++;
			x++;
			y--;
		}
		// Prueft oben - rechts
		for (; (x <= column && y > -1); x++, y--) {
			if (getField(x, y) == getCurrentPlayer()) {
				count++;
			} else
				break;
		}
		// Prueft oben - links
		if (count < 4 && (y > -1 && x > -1)) {
			x = startX - 1;
			y = startY - 1;
			for (; (x > -1 && y > -1); x--, y--) {
				if (getField(x, y) == getCurrentPlayer()) {
					count++;
				} else
					break;
			}
		}

		if (count < 4 && (y <= row && x > -1)) {
			x = startX - 1;
			y = startY + 1;
			// Prueft unten - links
			for (; (x > -1 && y <= row); x--, y++) {

				if (getField(x, y) == getCurrentPlayer()) {
					count++;
				} else
					break;
			}
		}
		if (count < 4 && (y <= row && x <= column)) {
			x = startX + 1;
			y = startY + 1;
			// Prueft unten - rechts
			for (; (x <= column && y <= row); x++, y++) {

				if (getField(x, y) == getCurrentPlayer()) {
					count++;
				} else
					break;
			}
		}
		if (count == 4) {
			winnerIs = getCurrentPlayer() + 1000;
		}

		return count;
	}

	/**
	 * Methode fuehrt Zug vom Spieler durch (KI oder Manuell) --->Noch ohne KI
	 
	public int playerTurn() {
		// Ermittelten die beste Spalte
		int x = bestPath(2);
		// Gibt naechste freie Position zurueck
		int y = validPosition(x);
		// Spielt den Zug
		setField(x, y, 2);
		System.err.println("Spieler spielte den " + getTurn() + ". Zug in Spalte " + x);
		return x;
	}
	*/

	/** Gibt Anzahl der Chips des gleichen Spieler in Reihe (Zeile) zurueck **/ // Funktioniert!
	private int inRow(int x, int y) {
		// System.err.println("Methode inRow wurde aufgerufen!");
		int count = 0;
		int temp = x;
		if (getField(x, y) == 0 || getField(x, y) == getCurrentPlayer()) {
			count++;
			x++;
		}
		for (; x <= column; x++) { // von links nach rechts! Limitiert durch
									// Anzahl Spalten!
			if (getField(x, y) == getCurrentPlayer()) {
				count++;
			} else
				break;
		}
		if (count < 4 && temp > 0) { // von rechts nach links (nur, wenn Counter
										// 4 noch nicht erreicht, da Spiel sonst
										// gewonnen)
			x = temp - 1;
			for (; x > -1; x--) {
				if (getField(x, y) == getCurrentPlayer()) {
					count++;
				} else
					break;

			}
		}
		if (count == 4) {
			winnerIs = getCurrentPlayer() + 100;
		}

		return count;
	}

	/**************************************************************/
	/************************ Datenbank *****************************/
	/**************************************************************/

	/**
	 * Ruft in der Datenbank die aktuelle GameID ab, welche einem Spiel
	 * entspricht. Gibt einen int-Wert zurueck
	 * 
	 * @return
	 */
	public int getNewGameID() {
		gameID = db.getMaxId("Game");
		gameID++; // + 1, da zuletzt belegte ID zurueck

		return gameID;
	}
	
	public int getGameID() {
		gameID = db.getMaxId("Game");

		return gameID;
	}

	/**
	 * Ruft in der Datenbank die aktuelle Match ab, welche einer Runde
	 * entspricht. Gibt einen int-Wert zurueck
	 * 
	 * @return
	 */
	public int getNewMatchID() {
		matchID = db.getMaxId("Match");
		matchID++; // + 1, da zuletzt belegte ID zurueck

		return matchID;
	}
	
	public int getMatchID() {
		matchID = db.getMaxId("Match");

		return matchID;
	}
	// JANAS TEIL!

	public GameLogic getDemoGame() {
		GameLogic game2 = new GameLogic();
		for (int i = 0; i <= column; i++) {
			for (int j = 0; j <= row; j++) {
				game2.setField(i, j);
			}
		}

		return game2;
	}

	public int getPlayer() { // liefert den aktuellen Spieler zurück; 1 für
								// unsren Agenten, 2 für den Gegner
		return 1;
	}
	/**
	 * Mthode startet / initialisiert das Spiel und auch die DB
	 */
	public void startGame() {
		gameID = getNewGameID();
		String player1= null;//Von Gui String mit Gegner und diesen Speichern
		String player2= null;
		String winner = null;
		int points = 0;
		sendGame dbGame = new sendGame(gameID, player1, player2, winner, points);
		dbGame.run();
	}
	
	public void startMatch() {
		matchID = getNewMatchID();
		sendMatch dbMatch = new sendMatch(matchID, getGameID());
		dbMatch.run();
	}

	public int evaluate() { // bewertet die gesamte Spielsituation
		int agentCount2 = 0;
		int agentCount3 = 0;
		int oppCount2 = 0;
		int oppCount3 = 0;
		for (int x = 0; x <= this.getColumn(); x++) {
			for (int y = 0; y <= this.getRow(); y++) {
				// inColumn
				if (inColumn(x, y, 1) == 4) { // unser Agent hat 4 in einer
												// Spalte --> wir haben gewonnen
					return (int) Double.POSITIVE_INFINITY;
				} else if (inColumn(x, y, 1) == 3) {
					agentCount3++;
				} else if (inColumn(x, y, 1) == 2) {
					agentCount2++;
				} else if (inColumn(x, y, 2) == 4) { // der Gegner hat 4 in
														// einer Spalte -->
														// Gegner hat gewonnen
					return (int) Double.NEGATIVE_INFINITY;
				} else if (inColumn(x, y, 2) == 3) {
					oppCount3++;
				} else if (inColumn(x, y, 2) == 2) {
					oppCount2++;
				}
				// inRow
				if (inRow(x, y, 1) == 4) { // unser Agent hat 4 in einer Zeile
											// --> wir haben gewonnen
					return (int) Double.POSITIVE_INFINITY;
				} else if (inRow(x, y, 1) == 3) {
					agentCount3++;
				} else if (inRow(x, y, 1) == 2) {
					agentCount2++;
				} else if (inRow(x, y, 2) == 4) { // der Gegner hat 4 in einer
													// Zeile --> Gegner hat
													// gewonnen
					return (int) Double.NEGATIVE_INFINITY;
				} else if (inRow(x, y, 2) == 3) {
					oppCount3++;
				} else if (inRow(x, y, 2) == 2) {
					oppCount2++;
				}
				// inDiagonal
				if (inDiagonal(x, y, 1) == 4) { // unser Agent hat 4 in der
												// Diagonale --> wir haben
												// gewonnen
					return (int) Double.POSITIVE_INFINITY;
				} else if (inDiagonal(x, y, 1) == 3) {
					agentCount3++;
				} else if (inDiagonal(x, y, 1) == 2) {
					agentCount2++;
				} else if (inDiagonal(x, y, 2) == 4) { // der Gegner hat 4 in
														// der Diagonale -->
														// Gegner hat gewonnen
					return (int) Double.NEGATIVE_INFINITY;
				} else if (inDiagonal(x, y, 2) == 3) {
					oppCount3++;
				} else if (inDiagonal(x, y, 2) == 2) {
					oppCount2++;
				}
			}
		}
		return agentCount2 + 2 * agentCount3 - oppCount2 - 4 * oppCount3;
	}

}
