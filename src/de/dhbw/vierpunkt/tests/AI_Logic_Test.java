package de.dhbw.vierpunkt.tests;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class AI_Logic_Test {
	
	/*****************************************************************************************************/
	/*************** Testklasse zum Testen des Zusammenspiels von KI und Spiellogik **********************/
	/*************** unabhÃ¤ngig von GUI/ DB etc., lÃ¤uft in Konsole ***************************************/
	/*****************************************************************************************************/
	
	

	/**************************************************************/
	/******************* Attribute ********************************/
	/**************************************************************/
	// Spielfeld
		// MAXIMALE ANZAHL SPALTEN
		private static final int COLUMN = 6;
		// MAXIMALE ANZAHL ZEILEN
		private static final int ROW = 5;

	// Allgemeine Information: x entspricht Spalte / y entspricht Zeile

	/**************************************************************/
	/******************* KONSTRUKTOR *******************************/
	/**************************************************************/
	
	static class GameLogic {
		
		// Variable die Zuege mitzaehlt! //Move entspricht TURN
				private int move = 0; // --> maximale Anzahl Zuege 69!
				private int[][] field = new int[ROW + 1][COLUMN + 1];
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
	
/** 
 * nur zum testen
 */
	  public void runInConsole() {
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

	 static int evaluate(GameLogic game) { // bewertet die Spielsituation
		// System.out.println("Ab jetzt sind wir in evaluate");
		int agentCount2 = 0;
		int agentCount3 = 0;
		int oppCount2 = 0;
		int oppCount3 = 0;
		
		for (int x = 0; x <= game.getColumn(); x++) {
			for (int y = 0; y <= game.getRow(); y++) {
				//System.out.println("Jetzt sind wir in den for-Schleifen");
				//System.out.println("Current Player? " + game.getCurrentPlayer());
				
				// new try
				// in column?
				if (game.getRow()-y>=4) {
				//if (game.getRow()-y>=4) {
					if (isRow(game.field, 2, x, y, 0, 1) == 4) {
						return (int) Double.POSITIVE_INFINITY;
					}	else if (isRow(game.field, 1, x, y, 0, 1) == 4) {
						return (int) Double.NEGATIVE_INFINITY;
					}	else if (isRow(game.field, 2, x, y, 0, 1) == 3) {
						agentCount3++;
					}	else if (isRow(game.field, 1, x, y, 0, 1) == 3) {
						oppCount3++;
					}	else if (isRow(game.field, 2, x, y, 0, 1) == 2) {
						agentCount2++;
					}	else if (isRow(game.field, 1, x, y, 0, 1) == 2) {
						oppCount2++;
					}
				}
				
				// in row?
		        if (game.getColumn()-x>=2) {
		          if (isRow(game.field,2,x,y,1,0)==4) 
		            return (int) Double.POSITIVE_INFINITY;  // gewonnen
		          else if (isRow(game.field,1,x,y,1,0)==4)
		            return (int) Double.NEGATIVE_INFINITY;  // verloren
		          // 3 gleiche Chips uebereinander?
		          else if (isRow(game.field,2,x,y,1,0)==3) 
		            agentCount3++; 
		          else if (isRow(game.field,1,x,y,1,0)==3)
		            oppCount3++;
		          // 2 gleiche Chips uebereinander?
		          else if (isRow(game.field,2,x,y,1,0)==2) 
		            agentCount2++; 
		          else if (isRow(game.field,1,x,y,1,0)==2)
		            oppCount2++;
		        }
				
				// in diagonal right?
				if ((game.getRow()-y>=1) && (game.getColumn()-x>=2)) {
					if (isRow(game.field,2,x,y,1,1) == 4) {
			            return (int) Double.POSITIVE_INFINITY; 
					}	else if (isRow(game.field,1,x,y,1,1) == 4) {
			            return (int) Double.NEGATIVE_INFINITY;
					}  else if (isRow(game.field,2,x,y,1,1) == 3) {
			            agentCount3++; 
					}	else if (isRow(game.field,1,x,y,1,1) == 3) {
			            oppCount3++;
					}	else if (isRow(game.field,2,x,y,1,1) == 2) {
			            agentCount2++; 
					}	else if (isRow(game.field,2,x,y,1,1) == 2) {
			            oppCount2++;
			          }
			        } 
				
		        // in diagonal left?
		        if ((game.getColumn()-x>=2) && (y>=3)) {
		           if (isRow(game.field,2,x,y,1,-1)==4) 
		            return (int) Double.POSITIVE_INFINITY;  // gewonnen
		          else if (isRow(game.field,1,x,y,1,-1)==4)
		            return (int) Double.NEGATIVE_INFINITY;  // verloren
		          // 3 gleiche Chips uebereinander?
		          else if (isRow(game.field,2,x,y,1,-1)==3) 
		            agentCount3++; 
		          else if (isRow(game.field,1,x,y,1,-1)==3)
		            oppCount3++;
		          // 2 gleiche Chips uebereinander?
		          else if (isRow(game.field,2,x,y,1,-1)==2) 
		            agentCount2++; 
		          else if (isRow(game.field,1,x,y,1,-1)==2)
		            oppCount2++;
		        } 
			}
			}
			
			//System.out.println("agentCount2 " + agentCount2 + " agentCount3 " + agentCount3 + " oppCount2 " + oppCount2 + " oppCount3 " + oppCount3);
			return agentCount2 + 100 * agentCount3 - oppCount2 - 500 * oppCount3;
			
		}
										
			
	 private static int isRow(int[][] field, int player, int x, int y, int dx, int dy) {
		 int cnt = 0;
		 
//		 if (field[y][x] == player) { // current field ist von uns belegt
//			 cnt++; // Count ist 1
//			 if (	(0<=(y+1*dy)) && ((y+1*dy)<=ROW) && (0<=(x+1*dx)) && ((x+1*dx)<=COLUMN)	) {
//				 if (field[y+1*dy][x+1*dx] == player) { // nächstes auch 
//					 cnt++; // Count ist 2
//					 if (	(0<=(y+2*dy)) && ((y+2*dy)<=ROW) && (0<=(x+2*dx)) && ((x+2*dx)<=COLUMN)	) {
//						 if (field[y+2*dy][x+2*dx] == player) { // übernächstes auch
//							 cnt++; // Count ist 3
//							 if (	(0<=(y+3*dy)) && ((y+3*dy)<=ROW) && (0<=(x+3*dx)) && ((x+3*dx)<=COLUMN)	) {
//								 if (field[y+3*dy][x+3*dx] == player) { // 3.nächstes auch
//									 cnt++; // Count ist 4
//								 }
//							 }	 
//						 }
//					 }
//				 }
//			 }

		 if (	(0<=(y+3*dy)) && ((y+3*dy)<=ROW) && (0<=(x+3*dx)) && ((x+3*dx)<=COLUMN)
				&& ((field[y][x] == player) || (field[y][x] == 0)) 
				&& ((field[y+1*dy][x+1*dx] == player) || (field[y+1*dy][x+1*dx] == 0))
				&& ((field[y+2*dy][x+2*dx] == player) || (field[y+2*dy][x+2*dx] == 0)) 
				&& ((field[y+3*dy][x+3*dx] == player) || (field[y+3*dy][x+3*dx] == 0))) {
				
			 for (int i = 0; i < 4; i++) {
				 if (field[y+i*dy][x+i*dx] == player) {
					 cnt++;
				 }
			 }
			} 
		 return cnt;
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
				
				if (depth == 0)
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
				
						// int move = (int) Double.NEGATIVE_INFINITY;
						int move = -1;
						int max = (int) Double.NEGATIVE_INFINITY;
						for (int j = 0; j <= game.getColumn(); j++) {
							if ((values[j] >= max) && (game.validPosition(j) != -1)) { // position is valid and current value is higher than old value
								move = j; // currently the best move
								max = values[j]; // currently the highest value
							}
						}
						System.out.println("move: " + move);
						System.out.println("max: " + max);
						return move; // best possible move after all columns have been evaluated
					
			}

	 
	public static void main(String[] args) throws IOException {
			// TODO Auto-generated method stub
		
/*** 
 * Achtung: Nur zum Testen! While Schleife bricht nur ab, wenn Gegner gewinnt -> Also im Optimalfall nie, wenn wir anfangen zu spielen! => Endlosschleife!
 */
			int test = 0;

			//BufferedReader br = new BufferedReader(new InputStreamReader(System.in)); // für manuellen Spielmodus
	      
			
	while(test == 0) { 
	
			GameLogic game = new GameLogic();
			int count = 0;
			while (true) {
			
			int depth = 6;
			    System.out.println("depth: " + depth);

			    int x;
			    
			    if (game.getCurrentPlayer() == 1) {      // Server ist dran
		   
			      System.out.print("Server ist dran              "
			      		+ "");
			      System.out.println("eval" + evaluate(game));
			      //System.out.print("Enter column");
			       //String s = br.readLine();
			      //int input = Integer.parseInt(s);
			     // x = input;
			     x = (int) (Math.random()*7);
			      game.playTurn(x, 1);
			      game.setCurrentPlayer(2);
			      System.out.println("Server hat in Spalte gelegt " + x);
			      System.out.println("Bewertung: " + evaluate(game));
			      
		      // Sieg?
			      if (evaluate(game)==(int)Double.NEGATIVE_INFINITY) {
			    	   test++;
			    	  game.runInConsole();
			    	  System.out.println(evaluate(game));
			        System.out.println("Server hat gewonnen...");
			        break;
			      };
			      
			      game.runInConsole();
			    
			    }
			      
			      // wir sind dran
			     
			    if (game.getCurrentPlayer() == 2) {
			    	 System.out.println("Wir sind dran          "
			    	 		+ "");
			    	 if (count == 0) {
			    		 game.playTurn(3, 2);
			    		 count++;
			    	 } else {
			    	System.out.println("eval" +evaluate(game));
			      // x = (int) (Math.random()*6); // falls KI nicht getestet werden soll, sondern nur Zufallszahl
			    	x = calcMove(game, depth);
			      game.playTurn(x, 2);
			      System.out.println("Wir haben in Spalte gelegt " + x);
			    	 }
			      game.setCurrentPlayer(1);
			      

			      // Sieg?
			      if (evaluate(game)==(int)Double.POSITIVE_INFINITY) {
			    	  game.runInConsole();
			    	  System.out.println(evaluate(game));
			        System.out.println(" Wir haben gewonnen!");
			        break;
			      };
			      
			      game.runInConsole();
			      
			      System.out.println("_____________________________________");
			      System.out.println();
			    };
			    System.out.println();
			  }
		 } // End of while(true) loop
		}
	}
}


