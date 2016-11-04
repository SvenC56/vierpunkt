package de.dhbw.mannheim.vierpunkt.objects;
/**
 * Drei Runden (Matches) ergeben ein Spiel (Game)
 * @author tobias
 *
 */
public class Game {
	
	/**************************************************************/
	/******************* Attribute ********************************/
	/**************************************************************/
	private Player player[] = new Player[1];
	private Player winner = null;
	private Match[] match;
	private Match currentMatch;
	//maximale Anzahl Matches = 3
	private static final int MATCHES = 2;
	
	/**************************************************************/
	/******************* KONSTRUKTOR *******************************/
	/**************************************************************/
	
	
	public Game(String playerName1, String playerName2) {
			this.player[0] = new Player(playerName1);
			this.player[1] = new Player(playerName2);
		this.winner = null;
		for (int i = 0; i <= MATCHES; i++) {
			match[i] = null;
		}
	}
	

	
	/**************************************************************/
	/****************** Getter / Setter ***************************/
	/**************************************************************/


	 Player getWinner() {
		return winner;
	}



	 void setWinner(Player winner) {
		this.winner = winner;
	}
	 
	 Match getMatch(int i) {
		 return this.match[i];
	 }
	 
	 void setMatch(int i, Match match) {
		 this.match[i] = match;
	 }

	  Match getCurrentMatch() {
			return currentMatch;
		}


		 void setCurrentMatch(Match currentMatch) {
			this.currentMatch = currentMatch;
		}
		
		void setPlayer (int i, Player player) {
			this.player[i] = player;
		}
		
		Player getPlayer(int i) {
			return this.player[i];
		}

	
	
	/**************************************************************/
	/******************* METHODEN *********************************/
	/**************************************************************/
	 Match getNewMatch() {
		 for (int i = 0; i <= MATCHES; i++) {
			 if (this.match[i] == null) {
				 this.setCurrentMatch(match[i]);
				 return this.match[i];
			 }
		 }
		 
		 return null;
	 }





	
}
