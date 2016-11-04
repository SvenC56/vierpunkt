package de.dhbw.vierpunkt.objects;

/**
 * Ein Match ist eine Runde im Spiel (Game)
 * @author tobias
 *
 */

public class Match {
	
	/**************************************************************/
	/******************* Attribute ********************************/
	/**************************************************************/
	private int matchID = 0;
	private int turnNumber = 0;
	private Player matchWinner;
	private Player currentPlayer;
	private boolean even = false;	//Unentschieden
	private AlphaBeta ki = new AlphaBeta();
	
	//Anzahl Spalten des Spielfeldes
	private static final int COLUMN = 6;
	
	//Anzahl Zeilen des Spielfeldes
	private static final int ROW = 5;
	
	//Maximal 69 Zuege pro Match moeglich
	private static final int TURNS = 68;
	
	//Das Spielfeld
	private PlaySlot[][] field = new PlaySlot[ROW + 1][COLUMN + 1]; //doing
	
	private Turn turn[] = new Turn[TURNS];
	

	
	
	/**************************************************************/
	/******************* KONSTRUKTOR *******************************/
	/**************************************************************/
	
	public Match() {
		this.matchID = matchID;
		this.matchID++;
		this.turnNumber=0;
		for (int y = 0; y <= ROW; y++) {
			for (int x = 0; x <= COLUMN; x++) {
				this.field[y][x] = null;
			}
		}
		for (int i = 0; i <= TURNS; i ++) {
			turn[i] = null;
		}
		
	}
	
	public Match(int matchID) {
		this.matchID = matchID;
		for (int y = 0; y <= ROW; y++) {
			for (int x = 0; x <= COLUMN; x++) {
				this.field[y][x] = null;
			}
		}
		for (int i = 0; i <= TURNS; i ++) {
			turn[i] = null;
		}
		
	}
	
	/**************************************************************/
	/****************** Getter / Setter ***************************/
	/**************************************************************/
	
	  int getColumn() {
			return COLUMN;
		}
		
	  int getRow() {
			return ROW;
		}
	 
	  boolean getEven() {
		 return this.even;
	 }
	 
	  int getTurnNumber() {
			return turnNumber;
		}
	 
	  int getMatchID() {
		 return this.matchID;
	 }

	 void setTurnNumber() {
		this.turnNumber++;
		}

	 Player getMatchWinner() {
		return matchWinner;
		}

	void setMatchWinner(Player winner) {
		this.matchWinner = winner;
		}

	 
	 
		
	 /**
	 * Getter fuer field. Erwartet x und y - Wert und liefert den Wert im Array
	 * zurueck!
	 **/
	 PlaySlot getField(int x, int y) {
		return this.field[y][x];
		}

	// Setter fuer field
	 void setField(int x, int y, Player player) {
		field[y][x].setOwnedBy(player);
		}
	 
		Turn[] getTurns() {
			return turn;
		}

		void setTurns(Turn turns[]) {
			this.turn = turns;
		}
		
		 Player getCurrentPlayer() {
			return currentPlayer;
		}

		 void setCurrentPlayer(Player currentPlayer) {
			this.currentPlayer = currentPlayer;
		}
		

			
	
	/**************************************************************/
	/******************* METHODEN *********************************/
	/**************************************************************/
		public Turn startTurn (Player player, int x) {
		if (!player.getIsServer()){
			x = ki.calcMove(this);
		}
		int y = this.validPosition(x);
		this.turn[turnNumber] = new Turn(turnNumber, player, x, y);
		return this.turn[turnNumber];
		}

		

	/**
	 * Erstellt eine Kopie des derzeitigen Spiels zur Analyse in der KI 
	 * @return
	 */
	 Match getDemoMatch() {
		Match match2 = new Match(this.matchID);
		for (int i = 0; i <= COLUMN; i++) {
			for (int j = 0; j <= ROW; j++) {
				match2.setField(i, j, this.getField(i, j).getOwnedBy());
			}
		}
		return match2;
	}
	
	
	 int validPosition(int x) {
		int temp = 0;
		// Spalte muss im richtigen Bereich > 0 & kleiner max. Anzahl SPALTEN
		if (x > -1 && x <= COLUMN) {
			for (int y = 0; y <= ROW; y++) {
				if (this.getField(x, y) == null  ) { // leere Position gefunden
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
	
	 Player winnerIs() {
		 if (this.matchWinner != null) {
			 return this.matchWinner;
		 }
		 else if (this.matchWinner != null && this.even) {
			 return null; 		//Kein Gewinner, unentschieden!
		 }
		 return null;
	 }

	 void checkWinner(Game game) {
			//pruefe nur, wenn move >= 4! Sonst ist kein Gewinn moeglich
			if (this.getTurnNumber() >= 4) {
				
				//wenn positiv unendlich, dann hat der Agent (wir) gewonnen
				if (this.evaluate() == (int)Double.POSITIVE_INFINITY && !currentPlayer.getIsServer()) {
				this.setMatchWinner(currentPlayer);
				currentPlayer.setWins();
				}
				//wenn negativ unendlich, dann hat der Gegner gewonnen
				else if (this.evaluate() == (int)Double.NEGATIVE_INFINITY) {
				if (game.getPlayer(0) == currentPlayer) {
					this.setMatchWinner(game.getPlayer(0));
					game.getPlayer(0).setWins();
					}
				else if (game.getPlayer(0) != currentPlayer){
					this.setMatchWinner(game.getPlayer(1));
					game.getPlayer(1).setWins();
				}
				else {
					int counter=0;
					for (int x=0; x <= COLUMN; x++ ) {
						if (this.validPosition(x) == -1) {
						counter++;	
						}			
				}
					if (counter == 7 && this.matchWinner== null) {
						this.even = true;
						}
					}}}
			}
	 
	
	/**************************************************************/
	/************* BEWERTUNGS-METHODEN ****************************/
	/**************************************************************/
	
	 int evaluate() { // bewertet die gesamte Spielsituation
			int agentCount2 = 0;
			int agentCount3 = 0;
			int oppCount2 = 0;
			int oppCount3 = 0;
			for (int x = 0; x <= this.getColumn(); x++) {
				for (int y = 0; y <= this.getRow(); y++) {
					
					if (!currentPlayer.getIsServer()) { // unser Agent spielt
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
						
					if (currentPlayer.getIsServer()) { // Gegner spielt	
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


	 
	 /** Gibt Anzahl der Chips des gleichen Spieler in Spalte zurueck **/
		 int inColumn(int x, int y) {
			// System.err.println("Methode inColumn wurde aufgerufen!");
			int count = 0; // Zaehler der validen Chips des gleichen Spielers in
							// Spalte
			int temp = y;
			if (this.getField(x, y).getOwnedBy() == null || this.getField(x, y).getOwnedBy() == this.getCurrentPlayer()) {
				count++;
				y--;
			}
			for (; y > -1; y--) { // von unten nach oben!
				if (this.getField(x, y).getOwnedBy() == getCurrentPlayer()) {
					count++;
				} else
					break;
			}
			if (count < 4 && temp <= ROW) { // von oben nach unten! (nur, wenn
											// Counter 4 noch nicht erreicht, da
											// Spiel sonst gewonnen)
				y = temp + 1;
				for (; y <= ROW; y++) { // Limitiert durch Anzahl Zeilen!
					if (this.getField(x, y).getOwnedBy() == this.getCurrentPlayer()) {
						count++;
					} else
						break;
				}
			}
			return count;
		}

		
		/** Gibt Anzahl der Chips des gleichen Spielers in der Diagonale zurueck **/
		 int inDiagonal(int x, int y) {
			// System.err.println("Methode inDiagonal wurde aufgerufen!");
			int count = 0;
			int startX = x;
			int startY = y;
			if (this.getField(x, y).getOwnedBy() == null || this.getField(x, y).getOwnedBy() == this.getCurrentPlayer()) {
				count++;
				x++;
				y--;
			}
			// Prueft oben - rechts
			for (; (x <= COLUMN && y > -1); x++, y--) {
				if (this.getField(x, y).getOwnedBy() == this.getCurrentPlayer()) {
					count++;
				} else
					break;
			}
			// Prueft oben - links
			if (count < 4 && (y > -1 && x > -1)) {
				x = startX - 1;
				y = startY - 1;
				for (; (x > -1 && y > -1); x--, y--) {
					if (this.getField(x, y).getOwnedBy() == this.getCurrentPlayer()) {
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

					if (this.getField(x, y).getOwnedBy() == this.getCurrentPlayer()) {
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

					if (this.getField(x, y).getOwnedBy() == this.getCurrentPlayer()) {
						count++;
					} else
						break;
				}
			}
			return count;
		}

		/** Gibt Anzahl der Chips des gleichen Spieler in Reihe (Zeile) zurueck **/
		 int inRow(int x, int y) {
			// System.err.println("Methode inRow wurde aufgerufen!");
			int count = 0;
			int temp = x;
			if (this.getField(x, y) == null || this.getField(x, y).getOwnedBy() == this.getCurrentPlayer()) {
				count++;
				x++;
			}
			for (; x <= COLUMN; x++) { // von links nach rechts! Limitiert durch
										// Anzahl Spalten!
				if (this.getField(x, y).getOwnedBy() == this.getCurrentPlayer()) {
					count++;
				} else
					break;
			}
			if (count < 4 && temp > 0) { // von rechts nach links (nur, wenn Counter
											// 4 noch nicht erreicht, da Spiel sonst
											// gewonnen)
				x = temp - 1;
				for (; x > -1; x--) {
					if (this.getField(x, y).getOwnedBy() == this.getCurrentPlayer()) {
						count++;
					} else
						break;

				}
			}
			return count;
		}

	

}
