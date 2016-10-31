package de.dhbw.mannheim.vierpunkt.objects;

/**
 * Ein Match ist eine Runde im Spiel (Game)
 * @author tobias
 *
 */

public class Match extends Game{
	
	/**************************************************************/
	/******************* Attribute ********************************/
	/**************************************************************/
	private int matchID = 0;
	private int turnNumber = 0;
	private Player matchWinner;
	private boolean even = false;	//Unentschieden
	private Player currentPlayer;
	
	//Anzahl Spalten des Spielfeldes
	private static final int COLUMN = 6;
	
	//Anzahl Zeilen des Spielfeldes
	private static final int ROW = 5;
	
	//Das Spielfeld
	private Player[][] field = new Player[ROW + 1][COLUMN + 1];
	

	
	
	/**************************************************************/
	/******************* KONSTRUKTOR *******************************/
	/**************************************************************/
	
	public Match() {
		
	}
	
	public Match(int gameID, int matchID) {
		super();
		this.matchID = matchID;
		for (int y = 0; y <= ROW; y++) {
			for (int x = 0; x <= COLUMN; x++) {
				this.field[y][x] = null;
			}
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
	 
	 Player getCurrentPlayer() {
		 return this.currentPlayer;
	 }
	 
	  void setCurrentPlayer(Player player) {
		  this.currentPlayer=player;
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
	 Player getField(int x, int y) {
		return this.field[y][x];
		}

	// Setter fuer field
	 void setField(int x, int y, Player player) {
		this.field[y][x] = player;
		}
	
	/**************************************************************/
	/******************* METHODEN *********************************/
	/**************************************************************/
	
	/**
	 * Erstellt eine Kopie des derzeitigen Spiels zur Analyse in der KI 
	 * @return
	 */
	 Match getDemoMatch() {
		Match match2 = new Match(this.getGameID(), this.matchID);
		for (int i = 0; i <= COLUMN; i++) {
			for (int j = 0; j <= ROW; j++) {
				match2.setField(i, j, this.getField(i, j));
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
	

	 void checkWinner() {
			//pruefe nur, wenn move >= 4! Sonst ist kein Gewinn moeglich
			if (this.getTurnNumber() >= 4) {
				
				//wenn positiv unendlich, dann hat der Agent (wir) gewonnen
				if (this.evaluate() == (int)Double.POSITIVE_INFINITY && !this.currentPlayer.getIsServer()) {
				this.setMatchWinner(this.currentPlayer);
				this.currentPlayer.setWins();
				}
				//wenn negativ unendlich, dann hat der Gegner gewonnen
				else if (this.evaluate() == (int)Double.NEGATIVE_INFINITY) {
				if (this.getPlayer1() == this.currentPlayer) {
					this.setMatchWinner(this.getPlayer1());
					this.getPlayer1().setWins();
					}
				else if (this.getPlayer1() != this.currentPlayer){
					this.setMatchWinner(this.getPlayer2());
					this.getPlayer2().setWins();
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
						
					if (this.getCurrentPlayer().getIsServer()) { // Gegner spielt	
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
			if (this.getField(x, y) == null || this.getField(x, y) == this.getCurrentPlayer()) {
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
		 int inDiagonal(int x, int y) {
			// System.err.println("Methode inDiagonal wurde aufgerufen!");
			int count = 0;
			int startX = x;
			int startY = y;
			if (this.getField(x, y) == null || this.getField(x, y) == this.getCurrentPlayer()) {
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
		 int inRow(int x, int y) {
			// System.err.println("Methode inRow wurde aufgerufen!");
			int count = 0;
			int temp = x;
			if (this.getField(x, y) == null || this.getField(x, y) == this.getCurrentPlayer()) {
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
			
		

}