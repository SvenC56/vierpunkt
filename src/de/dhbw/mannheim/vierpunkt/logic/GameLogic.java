package de.dhbw.mannheim.vierpunkt.logic;

import de.dhbw.mannheim.vierpunkt.db.connectHSQL;
import java.util.Random; //Temporaer fuer Test

/**
 * Spiellogik / Zum Reinkommen in die Spiellogik
 * Autoren: Gruppe 4 (vier.) - Verantwortlich: Tobias Jung
 **/
public class GameLogic {
	
	//Allgemeine Regeln / Hinweise Lauterbach
	//Spalte: von links nach rechts (0,1,2,3,4,5,6) => 7 Maximale Breite
	//Zeile: von oben nach unten (2,3,4,5,6,7) => 6 Maximale Tiefe (+1 Kopf?)
		
	/**************************************************************/
	/*******************Variablen, Deklarationen*******************/
	/**************************************************************/
	
	//Spielfeld
	//MAXIMALE ANZAHL SPALTEN
	private int column = 6;
	//MAXIMALE ANZAHL ZEILEN
	private int row = 5;
	//Variable die Zuege mitzaehlt! //Move entspricht TURN
	private int move = 0; // --> maximale Anzahl Zuege 69!
	private int lastX = -1;
	private int lastY = -1;
	
	/**
	 * Array fuer Spielfeld --> 0 enstpricht leere Position! 1 = SERVER! 2 = AGENT (SPIELER)
	 */
	private int [][] field = new int[row+1][column+1];
	private int player;
	connectHSQL db = new connectHSQL();
	private int gameID; // entspricht Spiel
	private int matchID; //entspricht Runde

	/**
	 * Methode zum Speichern des Spielstandes!
	 * Methode um Gewinn zu erkennen!(count == 4 BREAK)
	 */
	
	public void setColumn(int column) {
		this.column=column;
	}
	
	public int getColumn() {
		return column;
	}
	
	public void setRow(int row) {
		this.row=row;
	}
	
	public void setMove(int move) {
		this.move=move;
	}
	
	public int getRow() {
		return row;
	}
	


	private int getTurn() {
		return move;
	}

	private void setTurn() {
		move++;
		this.move = move;
	}

	
	//Allgemeine Information: x entspricht Spalte / y entspricht Zeile


	/**************************************************************/
	/*******************KONSTRUKTOR********************************/
	/**************************************************************/
	
	public GameLogic () {
		//DB-Objekt anlegen
		this.db = new connectHSQL();
		//Array durchlaufen und mit Nullen fuellen + move auf false setzen, da kein Spieler am Zug ist!
		move = 0;
		for (int y = 0; y <= row ; y++) {
			for (int x = 0; x <= column; x++ ) {
				field [y][x] = 0;
			}
		}
		//System.err.println("Konstruktor durchlaufen.");
	}
	
	/**************************************************************/
	/*******************ZUGRIFFSMETHODEN***************************/
	/**************************************************************/
	
	/**Getter fuer field. Erwartet x und y - Wert und liefert den Wert im Array zurueck!**/
	
	public int arraylength() {
		return field.length;
	}
	public int getField (int x, int y) {
		return field[y][x];
	}
	
	//Setter fuer field
	private void setField (int x, int y, int value) {
		field[y][x] = value;
		setTurn();		//Zuege mitzaehlen!
		saveTurn(x, y);
	}
	
	/**
	 * 
	 * Speichert den durchgefuehrten Zug
	 * @param x
	 * @param y
	 */
	private void saveTurn(int x, int y) {
	lastX = x;
	lastY = y;
	}
	
	/**************************************************************/
	/*******************TEMPORAER TEST-METHODS*********************/
	/**************************************************************/
	public void randomGame() {
		Random value = new Random();
		for (int y = 0; y <= row ; y++) {
			for (int x = 0; x <= column; x++ ) {
				int zahl = value.nextInt(3);
				field [y][x] = zahl;
			}
		}
		//System.err.print("Array mit Zufallszahlen zwischen 0 und 2 gefuellt!");
	}
	
	/**************************************************************/
	/************************LOGIK*********************************/
	/**************************************************************/
	
	/**Prueft, ob Chip eingeworfen werden kann
	 * gibt -1(keine valide Position) oder Zeile zurueck!**/
	 int validPosition (int x) {
		int temp=0;
		//Spalte muss im richtigen Bereich > 0 & kleiner max. Anzahl SPALTEN
		if (x > -1 && x <= column) {
			for (int y = 0; y <= row; y++) {
				if (field[y][x]==0) { //leere Position gefunden
					return y; //gibt Zeile zurueck!
				} //kein leeres Feld
				else {
					temp = -1;
					System.err.println("NO VALID POSITION!");
					
				}
			}
		}
		else {
			temp = -1; //Eingabe ausserhalb des Spielbereichs
			System.err.println("Eingabe ausserhalb Spielbereich!");
			
		}
	  //temp nur zurueckgeben, wenn noch keine Zeile returned wurde, war eine if, mal gucken, ob erforderlich!
			return temp;
	}
	
	
	/**Chip einwerfen
	Ueberlegung: Informationen werden in Array geschrieben! Spieler 1 == 1 --> Spieler 2 == 2**/
	void setChip (int x) {
		int y = validPosition(x);
		if (y != -1 && player == 1) {
			setField(x, y, player);
			}
		if (y!= -1 && player == 2) {
			setField(x, y, player);
		}
	}
	
	
	/**************************************************************/
	/************************BEWERTUNG*****************************/
	/**************************************************************/
	
	/**Bewertungsfunktion - Bewertet den Pfad nach aktuellem Stand und liefert Zahlenwert!**/
	private int pathEval (int x, int y, int spieler) {
		int evaluation = 0;
		//Idee: Die Summe der count ist die Bewertung des Pfades!!
		//System.err.println("Methode pathEval wurde aufgerufen!");
		evaluation = inRow(x, y, spieler) + inColumn(x, y, spieler) + inDiagonal(x, y, spieler);
		//System.err.println("Bewertung des Pfades durchgefuehrt: " + evaluation);
		return evaluation;
	}
	
	/**
	 * Bewertet die aktuelle Spielsituation und liefert die Spalte zurueck, in welche eingeworfen werden soll.
	 * Wenn -1 uebergeben wird, dann gibt es keinen validen Pfad! Bewertet die Situation des letzten Zuges und prueft somit, ob Gegner in besserer Gewinnsituation ist!
	 * @param spieler
	 * @return
	 */
	public int bestPath(int spieler) {
		int bestColumn=-1;
		int tmp=0;
		int maxEval=0;
		int oponent=0;;
		if (spieler == 2 && move > 0){
		oponent = pathEval(lastX, lastY, 1);
			}
		for (int x = 0; x <= row; x++) {
			int y = validPosition(x);
			if (y != -1) { 
				tmp = pathEval(x, y, spieler);
				if (maxEval<=tmp){
					maxEval = tmp;
					bestColumn=x;
				}
			}
		}
		if (maxEval < oponent) {
			bestColumn = lastX;
		}
		
		return bestColumn;
	}
	
	/**Gibt Anzahl der Chips des gleichen Spieler in Spalte zurueck**/ //Funktioniert!
	private int inColumn(int x, int y, int spieler) {
		//System.err.println("Methode inColumn wurde aufgerufen!");
		int count=0; //Zaehler der validen Chips des gleichen Spielers in Spalte
		int temp = y;
		if (getField(x,y) == 0 || getField(x,y) == spieler) {
			count++;
			y--;
		}
		for (; y > -1; y--) { //von unten nach oben!
			if (getField(x, y) == spieler) {
				count++;
			}
			else break;
		}
		if (count < 4 && temp <= row) { //von oben nach unten! (nur, wenn Counter 4 noch nicht erreicht, da Spiel sonst gewonnen)
			y = temp+1;
			for (; y <= row; y++) { //Limitiert durch Anzahl Zeilen!
				if (getField(x, y) == spieler) {
					count++;
				}
				else break;
			}
		}
		return count;	
	}
	/**Gibt Anzahl der Chips des gleichen Spielers in der Diagonale zurueck **/
	private int inDiagonal(int x, int y, int spieler) {
		//System.err.println("Methode inDiagonal wurde aufgerufen!");
		int count=0;
		int startX = x;
		int startY = y;
		if (getField(x,y) == 0 || getField(x,y) == spieler) {
			count++;
			x++;
			y--;
		}
		//Prueft oben - rechts
		for (; (x <= column && y > -1); x++, y--) {
			if (getField(x,y) == spieler) {
				count++;
			}
			else break;
		}
		//Prueft oben - links
		if (count < 4 && (y > -1 && x > -1) ) {
			x = startX-1;
			y = startY-1;
				for (; (x > -1 && y > -1); x--, y--) {
					if (getField(x,y) == spieler) {
						count++;
					}
					else break;
				}
		}
		
		if (count < 4 && (y <= row && x > -1) ) {
			x = startX-1;
			y = startY+1;
		//Prueft unten - links
		for (; (x > -1 && y <= row); x--, y++) {
			
			if (getField(x,y) == spieler) {
				count++;
			}
			else break;
		}
		}
		if (count < 4 && (y <= row && x <= column) ) {
			x = startX+1;
			y = startY+1;
		//Prueft unten - rechts
		for (; (x <= column && y <= row); x++, y++) {
			
			if (getField(x,y) == spieler) {
				count++;
			}
			else break;
		}
		}
		return count;
	}
	/**
	 * Methode fuehrt Zug vom Server durch!
	 * @param x
	 */
	public void serverTurn(int x) {
		//Prueft naechste freie Stelle in der Spalte
		int y = validPosition(x);
		//Spielt den Zug
		setField(x, y, 1);
		System.err.println("Server spielte den " + getTurn() + ". Zug in Spalte " + x);
	}
	
	/**
	 * Methode fuehrt Zug vom Spieler durch (KI oder Manuell) 
	 * --->Noch ohne KI
	 */
	public int playerTurn() {
		//Ermittelten die beste Spalte
		int x = bestPath(2);
		//Gibt naechste freie Position zurueck
		int y = validPosition(x);
		//Spielt den Zug
		setField(x, y, 2);
		System.err.println("Spieler spielte den " + getTurn() + ". Zug in Spalte " + x);
		return x;
	}
	
	
	/**Gibt Anzahl der Chips des gleichen Spieler in Reihe (Zeile) zurueck**/ //Funktioniert!
	private int inRow(int x, int y, int spieler) {
		//System.err.println("Methode inRow wurde aufgerufen!");
		int count=0;
		int temp = x;
		if (getField(x,y) == 0 || getField(x,y) == spieler) {
			count++;
			x++;
		}
		for (; x <= column; x++) { //von links nach rechts! Limitiert durch Anzahl Spalten!
			if (getField(x,y) == spieler) {
				count++;
			}
			else break;
		}
		if (count < 4 && temp > 0) { //von rechts nach links (nur, wenn Counter 4 noch nicht erreicht, da Spiel sonst gewonnen)
			x = temp-1;
			for (; x > -1; x--) {
			if (getField(x,y) == spieler) {
				count++;
			}
			else break;
			
			}	
		}
		return count;	
	}

	public int getPlayer() {
		// TODO Auto-generated method stub
		return player;
	}


	public void setPlayer(int player) {
		this.player = player;
	}
	/**
	 * Ruft in der Datenbank die aktuelle GameID ab, welche einem Spiel entspricht. Gibt einen int-Wert zurueck
	 * @return
	 */
	public int getGameID () {
		gameID = db.getMaxId("Game");
		gameID++; // + 1, da zuletzt belegte ID zurueck
		
		return gameID;
	}
	
	/**
	 * Ruft in der Datenbank die aktuelle Match ab, welche einer Runde entspricht. Gibt einen int-Wert zurueck
	 * @return
	 */
	public int getMatchID () {
		matchID = db.getMaxId("Match");
		matchID++; // + 1, da zuletzt belegte ID zurueck
		
		return matchID;
	}
	
	/**
	 * Methoden zur uebergabe an die DB. 
	 * BETA VERSION
	 */
	public void setGameDB(int G_ID, String OPPONENT, String WINNER, int POINTS) {
		String[][] statements = null;
		statements[0][0] = Integer.toString(gameID);;
		statements[0][1] = OPPONENT;
		statements[0][2] = WINNER;
		statements[0][3] = Integer.toString(POINTS);
		db.handOverArray(statements, "GAME");
	}

	public void setMatchDB(int M_ID, int G_ID) {
		String[][] statements = null;
		statements[0][0] = Integer.toString(matchID);
		statements[0][1] = Integer.toString(gameID);
		db.handOverArray(statements, "MATCH");
	}

	public void setTurnDB(int T_ID, int M_ID, String PERSON, int POS_Y) {
		String[][] statements = null;
		statements[0][0] = Integer.toString(move);
		statements[0][1] = Integer.toString(matchID);
		statements[0][2] = PERSON;
		statements[0][3] = Integer.toString(POS_Y);
		db.handOverArray(statements, "TURN");
	}

	
	public void startGame() {
		
		
	}
	
	
	//JANAS TEIL!
	
	public GameLogic copy() {
		GameLogic game2 = new GameLogic();
		game2.setColumn(this.column);
		game2.setRow(this.row);
		game2.setMove(this.move);
		
		for (int i=0; i < column; i++) {
			for (int j=0; j < row; j++) {
				game2.setField(i,j, this.getField(i, j));
			}
		}
		
	return game2;
	}

	
	
	
}
