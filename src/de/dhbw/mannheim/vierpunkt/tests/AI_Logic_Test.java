package de.dhbw.mannheim.vierpunkt.tests;


public class AI_Logic_Test {


	// Allgemeine Regeln / Hinweise Lauterbach
	// Spalte: von links nach rechts (0,1,2,3,4,5,6) => 7 Maximale Breite
	// Zeile: von oben nach unten (2,3,4,5,6,7) => 6 Maximale Tiefe (+1 Kopf?)

	/**************************************************************/
	/******************* Attribute ********************************/
	/**************************************************************/
	// Spielfeld
		// MAXIMALE ANZAHL SPALTEN
		private static final int COLUMN = 6;
		// MAXIMALE ANZAHL ZEILEN
		private static final int ROW = 5;
		
	/**
	 * Array fuer Spielfeld --> 0 enstpricht leere Position! 1 = SERVER! 2 =
	 * AGENT (SPIELER)
	 */


	
	// Allgemeine Information: x entspricht Spalte / y entspricht Zeile

	/**************************************************************/
	/******************* KONSTRUKTOR *******************************/
	/**************************************************************/
	
	static class GameLogic {
		
		// Variable die Zuege mitzaehlt! //Move entspricht TURN
				private int move = 0; // --> maximale Anzahl Zuege 69!
				private int[][] field = new int[ROW + 1][COLUMN + 1];
				private String player1 = null;
				private String player2 = null;
				private int currentPlayer = 2; //Der aktuelle Spieler

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
			game2.setCurrentPlayer(this.getCurrentPlayer());
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
	}
	
	/**************************************************************/
	/************************ LOGIK *********************************/
	/**************************************************************/

	/**
	 * Spielt den Zug --> Verbindung zum Interface, liefert Spalte zurueck
	 */
	public void playTurn(int x, int player){
		//Maximierung, da eigener Spieler
		
		this.setChip(x);

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
	 
	private int checkWinner(GameLogic game) {
		//pruefe nur, wenn move >= 4! Sonst ist kein Gewinn moeglich
		if (this.move >= 4) {
		//wenn negativ unendlich, dann hat der Gegner (Server) gewonnen
		if (evaluate(game) == (int)Double.NEGATIVE_INFINITY) {
			return 1;
		}
		//wenn positiv unendlich, dann hat der Agent (wir) gewonnen
		else if (evaluate(game) == (int)Double.POSITIVE_INFINITY) {
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
		// System.out.println("getField in Column" + this.getField(x, y));
		if (this.getField(x, y) == 0 || this.getField(x, y) == this.getCurrentPlayer()) {
			count++;
			//System.out.println("count" + count);
			y--;
		}
		for (; y > -1; y--) { // von unten nach oben!
			if (this.getField(x, y) == getCurrentPlayer()) {
				count++;
				//System.out.println("count" + count);
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
					// System.out.println("count" + count);
				} else
					break;
			}
		}
		return count;
	}
	
	  public void print() {
		    System.out.println();
		    for (int y = ROW; y >= 0; y--) {
				for (int x = 0; x <= COLUMN; x++) 
		        if (this.field[y][x]==1) System.out.print(" X");
		        else if (this.field[y][x]==2) System.out.print(" O");
		        else System.out.print(" .");
		      System.out.println();
		    }
		    for (int x=0; x<7; x++) System.out.print("__");  System.out.println("_");
		    for (int x=0; x<7; x++) System.out.print(" " + x);  System.out.println();
		    System.out.println();
		    System.out.println((this.currentPlayer==1 ? "Server" : "Agent") + " ist am Zug.");
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



	 static int evaluate(GameLogic game) { // bewertet die gesamte Spielsituation
		// System.out.println("Ab jetzt sind wir in evaluate");
		int agentCount2 = 0;
		int agentCount3 = 0;
		int oppCount2 = 0;
		int oppCount3 = 0;
		for (int x = 0; x <= game.getColumn(); x++) {
			for (int y = 0; y <= game.getRow(); y++) {
				//System.out.println("Jetzt sind wir in den for-Schleifen");
				//System.out.println("Current Player? " + game.getCurrentPlayer());
				if (game.getCurrentPlayer() == 2) { // unser Agent spielt
					// inColumn
					//System.out.println("in Column: " + game.inColumn(x, y));
					if (game.inColumn(x, y) == 4) { // unser Agent hat 4 in einer Spalte --> wir haben gewonnen
						return (int) Double.POSITIVE_INFINITY;
					} else if (game.inColumn(x, y) == 3) {
						agentCount3++;
					} else if (game.inColumn(x, y) == 2) {
						agentCount2++;	
	
					// inRow
					} else if (game.inRow(x, y) == 4) { // unser Agent hat 4 in einer Zeile --> wir haben gewonnen
						return (int) Double.POSITIVE_INFINITY;
					} else if (game.inRow(x, y) == 3) {
						agentCount3++;
					} else if (game.inRow(x, y) == 2) {
						agentCount2++;
					
					// inDiagonal
					} else if (game.inDiagonal(x, y) == 4) { // unser Agent hat 4 in der Diagonale --> wir haben gewonnen
						return (int) Double.POSITIVE_INFINITY;
					} else if (game.inDiagonal(x, y) == 3) {
						agentCount3++;
					} else if (game.inDiagonal(x, y) == 2) {
						agentCount2++;
					}
				}		
					
				if (game.getCurrentPlayer() == 1) { // Gegner spielt	
					// in column
					//System.out.println("in Column: " + game.inColumn(x, y));
					if (game.inColumn(x, y) == 4) { // der Gegner hat 4 in einer Spalte --> Gegner hat gewonnen
						return (int) Double.NEGATIVE_INFINITY;
					} else if (game.inColumn(x, y) == 3) {
						oppCount3++;
					} else if (game.inColumn(x, y) == 2) {
						oppCount2++;
						
					// in row
					} else if (game.inRow(x, y) == 4) { // der Gegner hat 4 in einer Zeile --> Gegner hat gewonnen
						return (int) Double.NEGATIVE_INFINITY;
					} else if (game.inRow(x, y) == 3) {
						oppCount3++;
					} else if (game.inRow(x, y) == 2) {
						oppCount2++;
					
					// in diagonal
					} else if (game.inDiagonal(x, y) == 4) { // der Gegner hat 4 in der Diagonale --> Gegner hat gewonnen
						return (int) Double.NEGATIVE_INFINITY;
					} else if (game.inDiagonal(x, y) == 3) {
						oppCount3++;
					} else if (game.inDiagonal(x, y) == 2) {
						oppCount2++;
					}
				}	
			}
		}
		//System.out.println("agentCount2 " + agentCount2 + " agentCount3 " + agentCount3 + " oppCount2 " + oppCount2 + " oppCount3 " + oppCount3);
		return agentCount2 + 2 * agentCount3 - oppCount2 - 4 * oppCount3;
	}
			

			public static int getAlphaBeta(GameLogic game, int depth, int alpha, int beta) {
				
				//Declarations
				GameLogic game_tmp;
				int minimax_tmp;	// temporary minimax value
				int minimax_curr;	// current minimax value
				
				// Initialization
				// System.out.println("Current Player? " + game.getCurrentPlayer());
				if (game.getCurrentPlayer() == 2){ // true if our agent is playing
					minimax_curr = alpha; // maximize output for our agent
				}
				else { // true if opponent is playing
					minimax_curr = beta; // minimize output for opponent
				}
				
				if (depth <= 0)
					return evaluate(game); // evaluate game situation by counting the coins in row/ in diagonal/ in column
				else {
					// calculate all possible moves
					for (int i = 0; i <= game.getColumn(); i++) { // try out all columns
						game_tmp = game.getDemoGame(); // copy current game situation to simulate possible moves without actually changing the play field
						if (game_tmp.validPosition(i) != -1) { // position is valid
								minimax_tmp = getAlphaBeta(game_tmp, depth-1, alpha, beta);
								if (game.getCurrentPlayer() == 2) { // our agent is playing
									minimax_curr = Math.max(minimax_tmp, minimax_curr); // is the new value of minimax higher than the old one?
									alpha = minimax_curr; // highest value becomes alpha
									if (alpha >= beta) {
										//System.out.println("beta:" + beta);
										return beta; // beta equals lower boundary
									}
								}
								else { // opponent playing
									minimax_curr = Math.min(minimax_tmp, minimax_curr); // is the new value of minimax lower than the old one?
									beta = minimax_curr; // lowest value becomes beta
									if (beta <= alpha)
										//System.out.println("alpha" + alpha);
										return alpha; // alpha equals upper boundary
								}			
					}
				}				
				//System.out.println("minimax_curr" + minimax_curr);
				return minimax_curr;
				}
			}
				
			
			public static int calcMove(GameLogic game, int depth) { // method to be evoked by game main to find the best possible move for our agent
				
				int [] values = new int[game.getColumn()+1];
				GameLogic game_tmp;
				
				for (int i = 0; i <= game.getColumn(); i++) 
					if (game.validPosition(i) != -1) { //position is valid
						game_tmp = game.getDemoGame(); // copy current game situation to simulate moves without actually changing the play field
						game_tmp.setChip(i); // simulate that our agent is placing coins in column i
						values[i] = getAlphaBeta(game_tmp, depth, (int) Double.NEGATIVE_INFINITY, (int) Double.POSITIVE_INFINITY);
					}
				
						int move = (int) Double.NEGATIVE_INFINITY;
						int max = (int) Double.NEGATIVE_INFINITY;
						for (int j = 0; j <= game.getColumn(); j++) {
							if ((values[j] > max) && (game.validPosition(j) != -1)) { // position is valid and current value is higher than old value
								max = values[j]; // currently the highest value
								move = j; // currently the best move
							}
						}
						System.out.println("move:" + move);
						return move; // best possible move after all columns have been evaluated
					
			}

	 
		public static void main(String[] args) {
			// TODO Auto-generated method stub
			GameLogic game = new GameLogic();
			while (true) {
			
			int depth = 6;
			    System.out.println("depth: " + depth);

			    int x;
			    
			 

			    if (game.getCurrentPlayer() == 1) {
			    	
			      // Server ist dran
			   
			        System.out.print("Server ist dran");
			        x = (int) (Math.random()*5);
			      game.playTurn(x, 1);
			      game.setCurrentPlayer(2);
			      System.out.println("Server hat in Spalte gelegt " + x);
			      System.out.println("Bewertung: " + evaluate(game));
			      
			      // Sieg?
			      if (evaluate(game)==(int)Double.NEGATIVE_INFINITY) {
			        System.out.println("Server hat gewonnen!");
			        break;
			        
			      };
			      
			      game.print();
			    
			    }
			      
			      // wir sind dran
			     
			    if (game.getCurrentPlayer() == 2) {
			    	 System.out.println("Wir sind dran");
			      x = calcMove(game, depth);
			      game.playTurn(x, 2);
			      game.setCurrentPlayer(1);
			      System.out.println("WIr haben in Spalte gelegt " + x);
			      System.out.println("Bewertung: " + evaluate(game));
			      // Sieg?
			      if (evaluate(game)==(int)Double.POSITIVE_INFINITY) {
			        System.out.println(" Wir haben gewonnen!");
			        break;
			        
			      };
			      
			      game.print();
			      
			      System.out.println("_____________________________________");
			      System.out.println();
			    };
			    System.out.println();
			  }
		}
	}
}

