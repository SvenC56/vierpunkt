package de.dhbw.vierpunkt.logic;

public class AlphaBeta {

/***********************************************************************************************************
******************************* Klasse wird momentan nicht mehr benutzt ************************************
******************************* später evtl. Kapselung von AI und Logik ************************************
************************************************************************************************************

	static int depth = 6;

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
					System.out.println("move: " + move);
					System.out.println("max: " + max);
					return move; // best possible move after all columns have been evaluated
				
		}
		 * 
 */
	 
}
