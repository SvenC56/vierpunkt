package de.dhbw.vierpunkt.logic;


public class AlphaBeta {
	

	public static int getAlphaBeta(Match match, int depth, int alpha, int beta) {
		
		//Declarations
		Match demoMatch;
		int minimax_tmp;	// temporary minimax value
		int minimax_curr;	// current minimax value
		
		// Initialization
		// System.out.println("Current Player? " + game.getCurrentPlayer());
		if (!match.getCurrentPlayer().getIsOpponent()){ // true if our agent is playing
			minimax_curr = alpha; // maximize output for our agent
		}
		else { // true if opponent is playing
			minimax_curr = beta; // minimize output for opponent
		}
		
		if (depth == 0)
			return match.evaluate(); // evaluate game situation by counting the coins in row/ in diagonal/ in column
		else {
			// calculate all possible moves
			for (int i = 0; i <= match.getColumn(); i++) { // try out all columns
				demoMatch = match.getDemoMatch(); // copy current game situation to simulate possible moves without actually changing the play field
				if (demoMatch.validPosition(i) != -1) { // position is valid
						minimax_tmp = getAlphaBeta(demoMatch, depth-1, alpha, beta);
						if (!match.getCurrentPlayer().getIsOpponent()) { // our agent is playing
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
		
	
	public static int calcMove(Match match, int depth) { // method to be evoked by game main to find the best possible move for our agent
		
		int [] values = new int[match.getColumn()+1];
		Match demoMatch;
		
		for (int i = 0; i <= match.getColumn(); i++) 
			if (match.validPosition(i) != -1) { //position is valid
				demoMatch = match.getDemoMatch(); // copy current game situation to simulate moves without actually changing the play field
				demoMatch.setChip(i); // simulate that our agent is placing coins in column i
				values[i] = getAlphaBeta(demoMatch, depth, (int) Double.NEGATIVE_INFINITY, (int) Double.POSITIVE_INFINITY);
			}
		
				int move = -1;
				int max = (int) Double.NEGATIVE_INFINITY;
				for (int j = 0; j <= match.getColumn(); j++) {
					if ((values[j] >= max) && (match.validPosition(j) != -1)) { // position is valid and current value is higher than old value
						move = j; // currently the best move
						max = values[j]; // currently the highest value
					}
				}
				//System.out.println("move: " + move);
				//System.out.println("max: " + max);
				return move; // best possible move after all columns have been evaluated
			
	}
 
 
}
