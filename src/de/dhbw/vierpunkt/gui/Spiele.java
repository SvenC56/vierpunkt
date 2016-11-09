package de.dhbw.vierpunkt.gui;

public class Spiele {
	private String gameID;
	private String player1;
	private String player2;
	private String winner;
	private String punkte;
	
	
	public Spiele(String gameID, String player1, String player2, String winner, String punkte) {
		super();
		this.gameID = gameID;
		this.player1 = player1;
		this.player2 = player2;
		this.winner = winner;
		this.punkte = punkte;
	}

	
	public String getGameID() {
		return gameID;
	}
	public void setGameID(String gameID) {
		this.gameID = gameID;
	}
	public String getPlayer1() {
		return player1;
	}
	public void setPlayer1(String player1) {
		this.player1 = player1;
	}
	public String getPlayer2() {
		return player2;
	}
	public void setPlayer2(String player2) {
		this.player2 = player2;
	}
	public String getWinner() {
		return winner;
	}
	public void setWinner(String winner) {
		this.winner = winner;
	}
	public String getPunkte() {
		return punkte;
	}
	public void setPunkte(String punkte) {
		this.punkte = punkte;
	}
	
	
}
