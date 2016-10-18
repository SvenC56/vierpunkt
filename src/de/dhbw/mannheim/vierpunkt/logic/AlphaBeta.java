package de.dhbw.mannheim.vierpunkt.logic;

public class AlphaBeta {
	
	private static final int POS_INFINITY = (int) Double.POSITIVE_INFINITY;
	private static final int NEG_INFINITY = (int) Double.NEGATIVE_INFINITY;
	static int depth = 6;

	public static int getMiniMaxValue(GameLogic game, int alpha, int beta) {
		
		//Declarations
		GameLogic game_tmp;
		int minimax_tmp;	// temporary minimax value
		int minimax_curr;	// current minimax value
		int player = game.getPlayer(); // Anmerkung: Wert muss aus GameLogic übergeben werden! (1 oder 2)
		
		// Initialization
		if (player == 1){ // true if our agent is playing
			minimax_curr = alpha; // maximize output for out agent
		}
		else { // true if opponent is playing
			minimax_curr = beta; // minimize output for opponent
		}
		
		if (depth == 0)
			return game.evaluate();
		else {
			// calculate all possible moves
			for (int i = 0; i <= game.getColumn(); i++) {
				game_tmp = game.copy(); // copy current game situation
				if (game_tmp.validPosition(i) != -1) {
						minimax_tmp = getMiniMaxValue(game_tmp, alpha, beta);
						if (player == 1) { // our agent is playing
							minimax_curr = java.lang.Math.max(minimax_tmp, minimax_curr);
							alpha = minimax_curr;
							if (alpha >= beta) {
								return beta;
							}
						}
						else { // opponent playing
							minimax_curr = java.lang.Math.min(minimax_tmp, minimax_curr);
							beta = minimax_curr;
							if (beta <= alpha)
								return alpha;
						}			
			}
		}				
		
		return minimax_curr;
		}
	}
		
	
	private static int calcMove(GameLogic game) { // method to be evoked by game main
		
		int [] values = new int[game.getColumn()+1];
		GameLogic game_tmp;
		
		for (int i = 0; i <= game.getColumn(); i++)
			if (game.validPosition(i) != -1) { //position is valid
				game_tmp = game.copy();
				game_tmp.setChip(i, 1);
				values[i] = getMiniMaxValue(game_tmp, depth, NEG_INFINITY, POS_INFINITY);
			}
				int move = -1;
				int max = NEG_INFINITY;
				for (int j = 0; j <= game.getColumn(); j++) {
					if ((values[j] >= max) && (game.validPosition(j) != -1)) {
						move = j; 
						max = values[j];
					}
				}
		return move;
			
	}
}
