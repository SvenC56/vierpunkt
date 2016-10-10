package de.dhbw.mannheim.vierpunkt.logic;

public class AlphaBeta {
	
	private static final int POS_INFINITY = (int) Double.POSITIVE_INFINITY;
	private static final int NEG_INFINITY = (int) Double.NEGATIVE_INFINITY;

	public static int getMiniMaxValue(GameLogic game, int depth, int alpha, int beta) {
		
		//Declarations
		GameLogic game_tmp;
		int minimax_tmp;	// temporary minimax value
		int minimax_curr;	// current minimax value
		int player; // values: player 1 = 1, player 2 = 2;
		
		// Initialization
		if (game.getPlayer() == 1) { // true if computer is playing
			player = 1; // computer playing (player 1)
		} else {
			player = 2; // manual playing (player 2)
		}
		if (player == 1)
			minimax_curr = alpha; // search for optimum alpha value
		else 
			minimax_curr = beta; // search for optimum beta value
		
		if (depth == 0);
/**		
 * 		Fehlt: Methode in GameLogic, die die gesamte Spielsituation auswertet
 * 		return evaluation(game); // if maximum depth is reached, game is evaluated 
 */
		else {
			// calculate all possible moves
			for (int i = 0; i < game.getColumn(); i++) {
				game_tmp = game.copy(); // copy current game situation
				if (game_tmp.validPosition(i) != -1) {
						minimax_tmp = getMiniMaxValue(game_tmp, depth-1, alpha, beta);
						if (player == 1) { // computer playing
							minimax_curr = java.lang.Math.max(minimax_tmp, minimax_curr);
							alpha = minimax_curr;
						}
							if (alpha >= beta) {
								return beta;
							}
						else { // manual player playing
							minimax_curr = java.lang.Math.min(minimax_tmp, minimax_curr);
							beta = minimax_curr;
							if (beta <= alpha)
								return alpha;
						}			
			}
		}				
		}
		return minimax_curr;
	
	}	
	
	private static int calcMove(GameLogic game, int depth) {
		
		int [] values = new int[game.getColumn()];
		GameLogic game_tmp;
		
		for (int i = 0; i < game.getColumn(); i++)
			if (game.validPosition(i) != -1) {
				game_tmp = game.copy();
				game_tmp.setChip(i);
				values[i] = getMiniMaxValue(game_tmp, depth, NEG_INFINITY, POS_INFINITY);
			}
				int move = -1;
				int max = NEG_INFINITY;
				for (int j = 0; j < game.getColumn(); j++) {
					if ((values[j] >= max) && (game.validPosition(j) != -1)) {
						move = j; 
						max = values[j];
					}
				}
		return move;
			
	}
}
