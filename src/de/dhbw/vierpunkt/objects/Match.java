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
	static int depth = 6;
	private boolean matchActive=false;
	private Game game;
	
	//Anzahl Spalten des Spielfeldes
	private static final int COLUMN = 6;
	
	//Anzahl Zeilen des Spielfeldes
	private static final int ROW = 5;
	
	//Maximal 69 Zuege pro Match moeglich
	private static final int TURNS = 68;
	
	//Das Spielfeld
	private PlaySlot[][] field = new PlaySlot[ROW + 1][COLUMN + 1]; //doing
	
	private Turn turn[] = new Turn[TURNS+1];
	

	
	
	/**************************************************************/
	/******************* KONSTRUKTOR *******************************/
	/**************************************************************/
	
	public Match(Game game) {
		this.game = game;
		this.matchID++;
		this.turnNumber=0;
		for (int y = 0; y <= ROW; y++) {
			for (int x = 0; x <= COLUMN; x++) {
				this.field[y][x] = new PlaySlot(null);
			}
		}
		for (int i = 0; i <= TURNS; i++) {
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
	 
	  int getTurnNumber() {
			return turnNumber;
		}
	 
	  int getMatchID() {
		 return this.matchID;
	 }

	 void setTurnNumber(int turnNumber) {
		this.turnNumber = turnNumber;
		}

	 Player getMatchWinner() {
		return matchWinner;
		}

	void setMatchWinner(Player winner) {
		this.matchWinner = winner;
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
		 
		Player getFieldPlayer(int x, int y) {
				return this.field[y][x].getOwnedBy();
				}
		
		
		Turn createTurn(Player player, int x, int y) {
			for (int i = 0; i <= TURNS; i++) {
				if (turn[i] == null) {
					turn[i] = new Turn(i, player, x, y);
					turn[i].setTurnActive(true);
					setTurnNumber(i);
					return turn[i];
				}
			}
			return null;
		}
		
		
		public Turn startTurn (Player player, int x) {
			if (!player.getIsOpponent()){
			if (this.getTurnNumber()>0){
			x = ki.calcMove(this, this.depth);
			}
			else {
				x=3;
			}
			
		}
		int y = this.validPosition(x);
		Turn turn = createTurn(player, x, y);
		return turn;
		}


	/**
	 * Erstellt eine Kopie des derzeitigen Spiels zur Analyse in der KI 
	 * @return
	 */
	 Match getDemoMatch() {
		Match match2 = new Match(this.matchID);
		for (int i = 0; i <= COLUMN; i++) {
			for (int j = 0; j <= ROW; j++) {
				match2.setField(i, j, this.getFieldPlayer(i, j));
			}
		}
		return match2;
	}
	
	
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
	
	 Player winnerIs() {
		 if (this.matchWinner != null) {
			 return this.matchWinner;
		 }
		 else if (this.matchWinner != null && this.even) {
			 return null; 		//Kein Gewinner, unentschieden!
		 }
		 return null;
	 }

	 void checkWinner() {
			//pruefe nur, wenn move >= 4! Sonst ist kein Gewinn moeglich
			if (this.getTurnNumber() >= 4) {
				
				//wenn positiv unendlich, dann hat der Agent (wir) gewonnen
				if (this.evaluate() == (int)Double.POSITIVE_INFINITY) {
				this.setMatchWinner(currentPlayer);
				currentPlayer.setWins();
				this.matchActive = false;
				}
				//wenn negativ unendlich, dann hat der Gegner gewonnen
				else if (this.evaluate() == (int)Double.NEGATIVE_INFINITY) {
					this.setMatchWinner(currentPlayer);
					currentPlayer.setWins();
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
						}
					}
			}
	 
	
	/**************************************************************/
	/************* BEWERTUNGS-METHODEN ****************************/
	/**************************************************************/
	
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
					if (this.getRow()-y>=1) {
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
			        if (this.getColumn()-x>=2) {
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
					if ((this.getRow()-y>=1) && (this.getColumn()-x>=2)) {
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
			        if ((this.getColumn()-x>=2) && (y>=3)) {
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
	 
	 private int isRow(PlaySlot[][] field, int player, int x, int y, int dx, int dy) {
		 int cnt = 0;
		 if (y<=2 && x <= 3) {
			if (player == 1) {
			 if (	((field[y][x].getOwnedBy().getIsOpponent() == true) || (field[y][x].getOwnedBy() == null)) && ((field[y+1*dy][x+1*dx].getOwnedBy().getIsOpponent() == true) || (field[y+1*dy][x+1*dx].getOwnedBy() == null))
				&& ((field[y+2*dy][x+2*dx].getOwnedBy().getIsOpponent() == true) || (field[y+2*dy][x+2*dx].getOwnedBy() == null)) && ((field[y+3*dy][x+3*dx].getOwnedBy().getIsOpponent() == true) || (field[y+3*dy][x+3*dx].getOwnedBy() == null))) {
					
				 for (int i = 0; i < 4; i++) {
					 if (field[y+i*dy][x+i*dx].getOwnedBy().getIsOpponent() == true) {
						 cnt++;
					 }
				 }
				}
			} else if (player == 2) {
				 if (	((field[y][x].getOwnedBy().getIsOpponent() == false) || (field[y][x].getOwnedBy() == null)) && ((field[y+1*dy][x+1*dx].getOwnedBy().getIsOpponent() == false) || (field[y+1*dy][x+1*dx].getOwnedBy() == null))
							&& ((field[y+2*dy][x+2*dx].getOwnedBy().getIsOpponent() == false) || (field[y+2*dy][x+2*dx].getOwnedBy() == null)) && ((field[y+3*dy][x+3*dx].getOwnedBy().getIsOpponent() == false) || (field[y+3*dy][x+3*dx].getOwnedBy() == null))) {
								
							 for (int i = 0; i < 4; i++) {
								 if (field[y+i*dy][x+i*dx].getOwnedBy().getIsOpponent() == false) {
									 cnt++;
								 }
							 }
							}
			}		 
			 
		 }
		 return cnt;
	 	} 

	 
	 /** Gibt Anzahl der Chips des gleichen Spieler in Spalte zurueck **/
		 int inColumn(int x, int y) {
			// System.err.println("Methode inColumn wurde aufgerufen!");
			int count = 0; // Zaehler der validen Chips des gleichen Spielers in
							// Spalte
			int temp = y;
			if ( this.getFieldPlayer(x, y) == this.getCurrentPlayer() || this.getFieldPlayer(x, y) == null) {
				count++;
				y--;
			}
			for (; y > -1; y--) { // von unten nach oben!
				if (this.getFieldPlayer(x,y) == this.getCurrentPlayer()) {
					count++;
				} else
					break;
			}
			if (count < 4 && temp <= ROW) { // von oben nach unten! (nur, wenn
											// Counter 4 noch nicht erreicht, da
											// Spiel sonst gewonnen)
				y = temp + 1;
				for (; y <= ROW; y++) { // Limitiert durch Anzahl Zeilen!
					if (this.getFieldPlayer(x, y) == this.getCurrentPlayer()) {
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
			if (this.getFieldPlayer(x, y) == null || this.getFieldPlayer(x, y) == this.getCurrentPlayer()) {
				count++;
				x++;
				y--;
			}
			// Prueft oben - rechts
			for (; (x <= COLUMN && y > -1); x++, y--) {
				if (this.getFieldPlayer(x, y) == this.getCurrentPlayer()) {
					count++;
				} else
					break;
			}
			// Prueft oben - links
			if (count < 4 && (y > -1 && x > -1)) {
				x = startX - 1;
				y = startY - 1;
				for (; (x > -1 && y > -1); x--, y--) {
					if (this.getFieldPlayer(x, y) == this.getCurrentPlayer()) {
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

					if (this.getFieldPlayer(x, y) == this.getCurrentPlayer()) {
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

					if (this.getFieldPlayer(x, y) == this.getCurrentPlayer()) {
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
			if (this.getFieldPlayer(x, y) == null || this.getFieldPlayer(x, y) == this.getCurrentPlayer()) {
				count++;
				x++;
			}
			for (; x <= COLUMN; x++) { // von links nach rechts! Limitiert durch
										// Anzahl Spalten!
				if (this.getFieldPlayer(x, y) == this.getCurrentPlayer()) {
					count++;
				} else
					break;
			}
			if (count < 4 && temp > 0) { // von rechts nach links (nur, wenn Counter
											// 4 noch nicht erreicht, da Spiel sonst
											// gewonnen)
				x = temp - 1;
				for (; x > -1; x--) {
					if (this.getFieldPlayer(x, y) == this.getCurrentPlayer()) {
						count++;
					} else
						break;

				}
			}
			return count;
		}
}
