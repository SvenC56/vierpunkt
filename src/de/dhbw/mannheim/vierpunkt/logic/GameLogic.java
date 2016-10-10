package de.dhbw.mannheim.vierpunkt.logic;

import de.dhbw.mannheim.vierpunkt.db.connectHSQL;

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
	/**
	 * Array fuer Spielfeld --> 0 enstpricht leere Position! 1 = spieler 1! 2 = spieler 2
	 */
	private int [][] field = new int[column][row];
	private int player;
	connectHSQL db = new connectHSQL();
	private int gameID; // entspricht Spiel
	private int matchID; //entspricht Runde

	
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
	
	
	
	public void setField(int[][] field) {
		this.field = field;
	}

	public int getTurn() {
		return move;
	}

	public void setTurn(int move) {
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
		for (int x = 0; x < column ; x++) {
			for (int y = 0; y < row; y++ ) {
				field [x][y] = 0;
			}
		}
	}
	
	/**************************************************************/
	/*******************ZUGRIFFSMETHODEN***************************/
	/**************************************************************/
	
	/**Getter fuer field. Erwartet x und y - Wert und liefert den Wert im Array zurueck!**/
	private int getField (int x, int y) {
		return field[x][y];
	}
	
	//Setter fuer field
	private void setField (int x, int y, int value) {
		field[x][y] = value;
		move++; 		//Zuege mitzaehlen!
	}
	

	/**************************************************************/
	/************************LOGIK*********************************/
	/**************************************************************/
	
	/**Prueft, ob Chip eingeworfen werden kann
	 * gibt -1(keine valide Position) oder Zeile zurueck!**/
	 int validPosition (int x) {
		int temp=0;
		//Spalte muss im richtigen Bereich > 0 & kleiner max. Anzahl SPALTEN
		if (x >= 0 && x < column) {
			for (int y = 0; y < row; y++) {
				if (field[x][y]==0) { //leere Position gefunden
					return y; //gibt Zeile zurueck!
				} //kein leeres Feld
				else {
					temp = -1;
					
				}
			}
		}
		else {
			temp = -1; //Eingabe ausserhalb des Spielbereichs
			
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
		evaluation = inRow(x, y, spieler) + inColumn(x, y, spieler) + inDiagonal(x, y, spieler);
		return evaluation;
	}
	
	/**
	 * Bewertet die aktuelle Spielsituation und liefert die Spalte zurueck, in welche eingeworfen werden soll.
	 * Wenn -1 uebergeben wird, dann gibt es keinen validen Pfad!
	 * @param spieler
	 * @return
	 */
	public int bestPath(int spieler) {
		int bestColumn=-1;
		int tmp=0;
		int maxEval=0;
		
		for (int x = 0; x < column; x++) {
			int y = validPosition(x);
			if (y != -1) {
				tmp = pathEval(x, y, spieler);
				if (maxEval<=tmp){
					maxEval = tmp;
					bestColumn=x;
				}
			}
		}
		return bestColumn;
	}
	
	/**Gibt Anzahl der Chips des gleichen Spieler in Spalte zurueck**/
	private int inColumn(int x, int y, int spieler) {
		int count=0; //Zaehler der validen Chips des gleichen Spielers in Spalte
		int temp = y;
		for (; y <= 0 ; y--) { //von unten nach oben!
			if (getField(x, y) == spieler) {
				count++;
			}
			else break;
		}
		if (count < 4) { //von oben nach unten! (nur, wenn Counter 4 noch nicht erreicht, da Spiel sonst gewonnen)
			y = temp;
			for (; y < row; y++) { //Limitiert durch Anzahl Zeilen!
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
		int count=0;
		int startX = x;
		int startY = y;
		for (; x < column || y <= 0; x++, y--) { //Idee: Zwei Bedingungen in einer for-Schleife! PRUEFEN!
			if (getField(x,y) == spieler) {
				count++;
			}
			else break;
		}
		if (count < 4) {
			x = startX;
			y = startY;
		for (; x <= 0 || y < row; x--, y++) {
			
			if (getField(x,y) == spieler) {
				count++;
			}
			else break;
		}
		}
		return count;
	}
	
	
	/**Gibt Anzahl der Chips des gleichen Spieler in Reihe (Zeile) zurueck**/
	private int inRow(int x, int y, int spieler) {
		int count=0;
		int temp = x;
		for (; x<column; x++) { //von links nach rechts! Limitiert durch Anzahl Spalten!
			if (getField(x,y) == spieler) {
				count++;
			}
			else break;
		}
		if (count < 4) { //von rechts nach links (nur, wenn Counter 4 noch nicht erreicht, da Spiel sonst gewonnen)
			x = temp;
			for (; x <= 0; x--) {
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
