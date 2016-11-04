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
	private Player player1;
	private Player player2;
	private Player winner = null;
	private Match[] match;
	//maximale Anzahl Matches = 3
	private static final int MATCHES = 2;
	
	/**************************************************************/
	/******************* KONSTRUKTOR *******************************/
	/**************************************************************/
	
	public Game(){
		
	}
	
	
	public Game(String playerName1, String playerName2) {
		this.winner = null;
		this.player1 =  new Player(playerName1);
		this.player2 = new Player (playerName2);
		for (int i = 0; i <= MATCHES; i++) {
			match[i] = null;
		}
	}
	

	
	/**************************************************************/
	/****************** Getter / Setter ***************************/
	/**************************************************************/

	 Player getPlayer1() {
		return player1;
	}



	 void setPlayer1(Player player1) {
		this.player1 = player1;
	}


	 Player getPlayer2() {
		return player2;
	}



	 void setPlayer2(Player player2) {
		this.player2 = player2;
	}



	 Player getWinner() {
		return winner;
	}



	 void setWinner(Player winner) {
		this.winner = winner;
	}



	
	
	/**************************************************************/
	/******************* METHODEN *********************************/
	/**************************************************************/
	
	
}
