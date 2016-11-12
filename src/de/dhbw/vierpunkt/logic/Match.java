package de.dhbw.vierpunkt.logic;



/**
 * Ein Match ist eine Runde im Spiel (Game)
 * @author tobias
 *
 */

public class Match {
	
	/**************************************************************/
	/******************* Attribute ********************************/
	/**************************************************************/
	/**
	 * MatchNumber, diese kann 0, 1 oder 2 sein
	 */
	private int matchNumber = 0;
	/**
	 * die aktuelle TurnNumber im Match
	 */
	private int matchTurnNumber;
	/**
	 * Hinterlegt den Gewinner des Match
	 */
	private Player matchWinner;
	/**
	 * hinterlegt den Spieler, der gerade am Zug ist
	 */
	private Player currentPlayer;
	/**
	 * Eine Boolean-Variable die gesetzt wird, sobald das Match unentschieden ausgegangen ist
	 */
	private boolean even = false;
	/**
	 * Eine Boolean-Variable die gesetzt wird, sobald ein Match aktiv ist
	 * Wird benoetigt, um nach einem Match, das naechste Match zu starten
	 * Hintergrund 3 Matches = 1 Game
	 */
	private boolean matchActive=false;
	/**
	 * Eine Boolean-Variable die gesetzt wird, sobald ein Turn aktiv ist
	 * Wird benoetigt, um nach letztem Zug den naechsten Zug zu starten
	 */
	private boolean turnActive = false;
	/**
	 * Das Attribut game, welches im Konstruktor uebergeben wird
	 */
	private Game game;
	
	/**Anzahl Spalten des Spielfeldes
	 * 
	 */
	private static final int COLUMN = 6;
	
	/**Anzahl Zeilen des Spielfeldes
	 * 
	 */
	private static final int ROW = 5;
	
	
	/**Maximal 69 Zuege pro Match moeglich
	 * 
	 */
	private static final int TURNS = 68;
	
	/**
	 * Das Spielfeld, in welchem die gespielten Steine positioniert werden (fuer KI)
	 */
	private PlaySlot[][] field = new PlaySlot[ROW + 1][COLUMN + 1]; //doing
	
	/**
	 * Das Turn-Array, in welchem saemtliche Zuege abgelegt werden
	 */
	private Turn turn[] = new Turn[TURNS+1];
	

	
	
	/**************************************************************/
	/******************* KONSTRUKTOR *******************************/
	/**************************************************************/
	/**
	 * Konstruktor zum Anlegen eines neuen Matches
	 * @param game
	 * @param matchNumber
	 */
	public Match(Game game, int matchNumber) {
		this.game = game;
		this.matchTurnNumber = 0;
		this.matchNumber = matchNumber;
		for (int x = 0; x <= COLUMN; x++ ) {
			for (int y = 0; y <= ROW; y++) {
				this.field[y][x] = new PlaySlot();
			}
		}
	}
	/**
	 * Konstruktor fuer die KI
	 * @param matchNumber
	 */
	public Match(int matchNumber) {
		this.matchNumber = matchNumber;
		for (int x = 0; x <= COLUMN; x++ ) {
			for (int y = 0; y <= ROW; y++) {
				this.field[y][x] = new PlaySlot();
			}
		}
	}
	
	/**************************************************************/
	/****************** Getter / Setter ***************************/
	/**************************************************************/
	//Bei getter- / und setter-Methoden wird auf die Kommentare verzichtet
	
	
	boolean getMatchActive() {
		return this.matchActive;
	}
	
	void setMatchActive(boolean matchActive) {
		this.matchActive = matchActive;
	}
	
	  int getColumn() {
			return COLUMN;
		}
		
	  int getRow() {
			return ROW;
		}
	 
	  boolean getEven() {
		 return this.even;
	 }
	 
	  int getMatchNumber() {
		 return this.matchNumber;
	 }

	 Player getMatchWinner() {
		return matchWinner;
		}

	public void setMatchWinner(Player winner) {
		this.matchWinner = winner;
		}

	 void setField(int x, int y, Player player) {
		field[y][x].setOwnedBy(player);
		}
	 
		Turn[] getTurns() {
			return turn;
		}

		void setTurns(Turn turns[]) {
			this.turn = turns;
		}
		
		public Player getCurrentPlayer() {
			return this.currentPlayer;
		}

		 void setCurrentPlayer(Player currentPlayer) {
			this.currentPlayer = currentPlayer;
		}
		
		  boolean isTurnActive() {
				return turnActive;
			}

			void setTurnActive(boolean turnActive) {
				this.turnActive = turnActive;
			}

			Game getGame() {
				return game;
			}

			void setGame(Game game) {
				this.game = game;
			}
			
			int getMatchTurnNumber() {
				return matchTurnNumber;
			}

			void setMatchTurnNumber(int matchTurnNumber) {
				this.matchTurnNumber = matchTurnNumber;
			}
			
			
	/**************************************************************/
	/******************* METHODEN *********************************/
	/**************************************************************/
//			
//		public void startTurn() {
//			while (!this.turnActive){
//				setNewTurn();
//				while (this.turnActive) {
//			
//				}
//				
//			}
//				 
//			}	
		/**
		 * Liefert den aktuellen Zug zurueck (fuer Interfaces)
		 * @return
		 */
		public Turn getCurrentTurn() {
			return turn[this.getMatchTurnNumber()];
		}
		 
		 /**
		 * Unsere moeglichen Zuege werden in einem Array gespeichert. Diese Methode erstellt, wenn moeglich einen neuen Turn 
		 * @return
		 */
		  Turn setNewTurn() {
				for (int i = 0; i <= TURNS; i++) {
					if (turn[i] == null) { //Sucht nach der naechsten freien Position im turn-Array
						turn[i] = new Turn (i, this.currentPlayer, this); //legt neuen Turn an
						System.out.println("TURN " + i +" GELADEN!"); //Kontrollausgabe
						this.setTurnActive(true);
						this.setMatchTurnNumber(i);	//setzt die aktuelle MatchTurnNumber (fuer getCurrentTurn())
						return this.turn[i];
					}
		}
				return null;
}
		
		/**
		 * Uebergibt den Player, welcher im PlayerSlot liegt (Spielfeld)
		 * @param x
		 * @param y
		 * @return
		 */
		Player getFieldPlayer(int x, int y) {
				return this.field[y][x].getOwnedBy();
				}
	


	/**
	 * Erstellt eine Kopie des derzeitigen Spiels zur Analyse in der KI 
	 * @return
	 */
	 Match getDemoMatch() {
		Match match2 = this;
//		match2.setCurrentPlayer(this.getCurrentPlayer());
		return match2;
	}
	
	/**
	 * Prueft ob in der angegebenen Spalte x eine moegliche Position ist und liefert dann die Zeile zurueck
	 * @param x
	 * @return
	 */
	 int validPosition(int x) {
		int temp = 0;
		// Spalte muss im richtigen Bereich > 0 & kleiner max. Anzahl SPALTEN
		if (x > -1 && x <= COLUMN) {
			for (int y = 0; y <= ROW; y++) {
				if (this.getFieldPlayer(x, y) == null  ) { // leere Position gefunden
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
	 
	 /**
		 * Setzt den Chip eines Spielers fuer die KI, ohne Pruefung
		 * 
		 * @param x
		 */
		 void setChip(int x) {
			int y = this.validPosition(x);
			this.setField(x, y, this.currentPlayer);
			}
		 
		 
	/**
	 * Liefert zurueck wer der Gewinner des Matches ist und prueft ob unentschieden gespielt wurde
	 * @return
	 */
	 Player winnerIs() {
		 if (this.matchWinner != null) {
			 return this.matchWinner;
		 }
		 else if (this.matchWinner != null && this.even) {
			 return null; 		//Kein Gewinner, unentschieden!
		 }
		 return null;
	 }
	 
	 
	 /**
	  * Ueberprueft ob ein Match-Winner existiert (Spiel gewonnen)
	  */
	 Player checkWinner() {
			//pruefe nur, wenn move >= 4! Sonst ist kein Gewinn moeglich
			if (this.getMatchTurnNumber() >= 7) {
				
				//wenn positiv unendlich, dann hat der Agent (wir) gewonnen
				if (this.evaluate() == (int)Double.POSITIVE_INFINITY) {
				this.setMatchWinner(currentPlayer);
				currentPlayer.setWins();
				this.matchActive = false;
				}
				//wenn negativ unendlich, dann hat der Gegner gewonnen
				else if (this.evaluate() == (int)Double.NEGATIVE_INFINITY) {
					this.setMatchWinner(currentPlayer);
					if (currentPlayer != this.getGame().getPlayer(0)){
						this.getGame().getPlayer(0).setWins();
						this.setMatchWinner(this.getGame().getPlayer(0));
					}
					else {
						this.getGame().getPlayer(1).setWins();
						this.setMatchWinner(this.getGame().getPlayer(1));
					}
					this.matchActive = false;
					}
				
					int counter=0;
					for (int x=0; x <= COLUMN; x++ ) {
						if (this.validPosition(x) == -1) {
						counter++;	
						}			
				}
				if (counter == 7 && this.matchWinner== null) {
						this.even = true;
						this.matchActive = false;
						return null;
						}
					}
			return this.matchWinner;
			}
	 
	
	/**************************************************************/
	/************* BEWERTUNGS-METHODEN ****************************/
	/**************************************************************/
	/**
	 * Bewertet die derzeitige Spielsituation und liefert eine Bewertung
	 * @return
	 */
	 int evaluate() { // bewertet die Spielsituation
			// System.out.println("Ab jetzt sind wir in evaluate");
			int agentCount2 = 0;
			int agentCount3 = 0;
			int oppCount2 = 0;
			int oppCount3 = 0;
			
			for (int x = 0; x <= this.getColumn(); x++) {
				for (int y = 0; y <= this.getRow(); y++) {
					//System.out.println("Jetzt sind wir in den for-Schleifen");
					//System.out.println("Current Player? " + game.getCurrentPlayer());
					
					// new try
					// in column?
					if (this.getRow()-y>=4) {
						if (isRow(this.field, 2 , x, y, 0, 1) == 4) {
							return (int) Double.POSITIVE_INFINITY;
						}	else if (isRow(this.field, 1, x, y, 0, 1) == 4) {
							return (int) Double.NEGATIVE_INFINITY;
						}	else if (isRow(this.field, 2, x, y, 0, 1) == 3) {
							agentCount3++;
						}	else if (isRow(this.field, 1, x, y, 0, 1) == 3) {
							oppCount3++;
						}	else if (isRow(this.field, 2, x, y, 0, 1) == 2) {
							agentCount2++;
						}	else if (isRow(this.field, 1, x, y, 0, 1) == 2) {
							oppCount2++;
						}
					}
					
					// in row?
			        if (this.getColumn()-x>=4) {
			          if (isRow(this.field,2,x,y,1,0)==4) 
			            return (int) Double.POSITIVE_INFINITY;  // gewonnen
			          else if (isRow(this.field,1,x,y,1,0)==4)
			            return (int) Double.NEGATIVE_INFINITY;  // verloren
			          // 3 gleiche Chips uebereinander?
			          else if (isRow(this.field,2,x,y,1,0)==3) 
			            agentCount3++; 
			          else if (isRow(this.field,1,x,y,1,0)==3)
			            oppCount3++;
			          // 2 gleiche Chips uebereinander?
			          else if (isRow(this.field,2,x,y,1,0)==2) 
			            agentCount2++; 
			          else if (isRow(this.field,1,x,y,1,0)==2)
			            oppCount2++;
			        }
					
					// in diagonal right?
					if ((this.getRow()-y>=4) && (this.getColumn()-x>=4)) {
						if (isRow(this.field,2,x,y,1,1) == 4) {
				            return (int) Double.POSITIVE_INFINITY; 
						}	else if (isRow(this.field,1,x,y,1,1) == 4) {
				            return (int) Double.NEGATIVE_INFINITY;
						}  else if (isRow(this.field,2,x,y,1,1) == 3) {
				            agentCount3++; 
						}	else if (isRow(this.field,1,x,y,1,1) == 3) {
				            oppCount3++;
						}	else if (isRow(this.field,2,x,y,1,1) == 2) {
				            agentCount2++; 
						}	else if (isRow(this.field,2,x,y,1,1) == 2) {
				            oppCount2++;
				          }
				        } 
					
			        // in diagonal left?
			        if ((this.getColumn()-x>=4) && (y>=3)) {
			           if (isRow(this.field,2,x,y,1,-1)==4) 
			            return (int) Double.POSITIVE_INFINITY;  // gewonnen
			          else if (isRow(this.field,1,x,y,1,-1)==4)
			            return (int) Double.NEGATIVE_INFINITY;  // verloren
			          // 3 gleiche Chips uebereinander?
			          else if (isRow(this.field,2,x,y,1,-1)==3) 
			            agentCount3++; 
			          else if (isRow(this.field,1,x,y,1,-1)==3)
			            oppCount3++;
			          // 2 gleiche Chips uebereinander?
			          else if (isRow(this.field,2,x,y,1,-1)==2) 
			            agentCount2++; 
			          else if (isRow(this.field,1,x,y,1,-1)==2)
			            oppCount2++;
			        } 
				}
				}
				
				//System.out.println("agentCount2 " + agentCount2 + " agentCount3 " + agentCount3 + " oppCount2 " + oppCount2 + " oppCount3 " + oppCount3);
				return agentCount2 + 100 * agentCount3 - oppCount2 - 500 * oppCount3;
				
			}
	 
	 
	 String getScore() {
		 String score = this.getGame().getPlayer(0).getWins() + " : " + this.getGame().getPlayer(1).getWins();
		 return score;
	 }
	 
	 /**
	  * Diese Klasse bewertet die Spielsteine des aktuellen Spielers in Reihe und liefert die Anzahl der Steine in Reihe zurueck
	  * @param field
	  * @param player
	  * @param x
	  * @param y
	  * @param dx
	  * @param dy
	  * @return
	  */
	 private int isRow(PlaySlot[][] field, int player, int x, int y, int dx, int dy) {
		 int cnt = 0;
			if (player == 2) {
			 if (	(0<=(y+3*dy)) && ((y+3*dy)<=ROW) && (0<=(x+3*dx)) && ((x+3*dx)<=COLUMN)
					 && ((field[y][x].getOwnedBy() == null) || (field[y][x].getOwnedBy().getIsOpponent() == true)) 
					 && ((field[y+1*dy][x+1*dx].getOwnedBy() == null) || (field[y+1*dy][x+1*dx].getOwnedBy().getIsOpponent() == true))
					 && ((field[y+2*dy][x+2*dx].getOwnedBy() == null) || (field[y+2*dy][x+2*dx].getOwnedBy().getIsOpponent() == true)) 
					 && ((field[y+3*dy][x+3*dx].getOwnedBy() == null) || (field[y+3*dy][x+3*dx].getOwnedBy().getIsOpponent() == true))) {
					
				 for (int i = 0; i < 4; i++) {
					 if (field[y+i*dy][x+i*dx].getOwnedBy() != null) {
					 if (field[y+i*dy][x+i*dx].getOwnedBy().getIsOpponent() == true) {
						 cnt++;
					 }
					 }
				 }
				}
			} else if (player == 1) {
				 if (		(0<=(y+3*dy)) && ((y+3*dy)<=ROW) && (0<=(x+3*dx)) && ((x+3*dx)<=COLUMN)
						 	&& ((field[y][x].getOwnedBy() == null) || (field[y][x].getOwnedBy().getIsOpponent() == false)) 
						 	&& ((field[y+1*dy][x+1*dx].getOwnedBy() == null) || (field[y+1*dy][x+1*dx].getOwnedBy().getIsOpponent() == false))
							&& ((field[y+2*dy][x+2*dx].getOwnedBy() == null) || (field[y+2*dy][x+2*dx].getOwnedBy().getIsOpponent() == false)) 
							&& ((field[y+3*dy][x+3*dx].getOwnedBy() == null) || (field[y+3*dy][x+3*dx].getOwnedBy().getIsOpponent() == false))) {
								
							 for (int i = 0; i < 4; i++) {
								 if (field[y+i*dy][x+i*dx].getOwnedBy() != null) {
								 if (field[y+i*dy][x+i*dx].getOwnedBy().getIsOpponent() == false) {
									 cnt++;
								 }
								 }
							 }
							}
			}		 

		 return cnt;
	 	} 

	 
//	 /** Gibt Anzahl der Chips des gleichen Spieler in Spalte zurueck **/
//		 int inColumn(int x, int y) {
//			// System.err.println("Methode inColumn wurde aufgerufen!");
//			int count = 0; // Zaehler der validen Chips des gleichen Spielers in
//							// Spalte
//			int temp = y;
//			if ( this.getFieldPlayer(x, y) == this.getCurrentPlayer() || this.getFieldPlayer(x, y) == null) {
//				count++;
//				y--;
//			}
//			for (; y > -1; y--) { // von unten nach oben!
//				if (this.getFieldPlayer(x,y) == this.getCurrentPlayer()) {
//					count++;
//				} else
//					break;
//			}
//			if (count < 4 && temp <= ROW) { // von oben nach unten! (nur, wenn
//											// Counter 4 noch nicht erreicht, da
//											// Spiel sonst gewonnen)
//				y = temp + 1;
//				for (; y <= ROW; y++) { // Limitiert durch Anzahl Zeilen!
//					if (this.getFieldPlayer(x, y) == this.getCurrentPlayer()) {
//						count++;
//					} else
//						break;
//				}
//			}
//			return count;
//		}
//
//		
//		/** Gibt Anzahl der Chips des gleichen Spielers in der Diagonale zurueck **/
//		 int inDiagonal(int x, int y) {
//			// System.err.println("Methode inDiagonal wurde aufgerufen!");
//			int count = 0;
//			int startX = x;
//			int startY = y;
//			if (this.getFieldPlayer(x, y) == null || this.getFieldPlayer(x, y) == this.getCurrentPlayer()) {
//				count++;
//				x++;
//				y--;
//			}
//			// Prueft oben - rechts
//			for (; (x <= COLUMN && y > -1); x++, y--) {
//				if (this.getFieldPlayer(x, y) == this.getCurrentPlayer()) {
//					count++;
//				} else
//					break;
//			}
//			// Prueft oben - links
//			if (count < 4 && (y > -1 && x > -1)) {
//				x = startX - 1;
//				y = startY - 1;
//				for (; (x > -1 && y > -1); x--, y--) {
//					if (this.getFieldPlayer(x, y) == this.getCurrentPlayer()) {
//						count++;
//					} else
//						break;
//				}
//			}
//
//			if (count < 4 && (y <= ROW && x > -1)) {
//				x = startX - 1;
//				y = startY + 1;
//				// Prueft unten - links
//				for (; (x > -1 && y <= ROW); x--, y++) {
//
//					if (this.getFieldPlayer(x, y) == this.getCurrentPlayer()) {
//						count++;
//					} else
//						break;
//				}
//			}
//			if (count < 4 && (y <= ROW && x <= COLUMN)) {
//				x = startX + 1;
//				y = startY + 1;
//				// Prueft unten - rechts
//				for (; (x <= COLUMN && y <= ROW); x++, y++) {
//
//					if (this.getFieldPlayer(x, y) == this.getCurrentPlayer()) {
//						count++;
//					} else
//						break;
//				}
//			}
//			return count;
//		}

	
		 
//		/** Gibt Anzahl der Chips des gleichen Spieler in Reihe (Zeile) zurueck **/
//		 int inRow(int x, int y) {
//			// System.err.println("Methode inRow wurde aufgerufen!");
//			int count = 0;
//			int temp = x;
//			if (this.getFieldPlayer(x, y) == null || this.getFieldPlayer(x, y) == this.getCurrentPlayer()) {
//				count++;
//				x++;
//			}
//			for (; x <= COLUMN; x++) { // von links nach rechts! Limitiert durch
//										// Anzahl Spalten!
//				if (this.getFieldPlayer(x, y) == this.getCurrentPlayer()) {
//					count++;
//				} else
//					break;
//			}
//			if (count < 4 && temp > 0) { // von rechts nach links (nur, wenn Counter
//											// 4 noch nicht erreicht, da Spiel sonst
//											// gewonnen)
//				x = temp - 1;
//				for (; x > -1; x--) {
//					if (this.getFieldPlayer(x, y) == this.getCurrentPlayer()) {
//						count++;
//					} else
//						break;
//
//				}
//			}
//			return count;
//		}
}
