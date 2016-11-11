package de.dhbw.vierpunkt.gui;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Spiele {
	
	private final SimpleStringProperty gameID;
    private final SimpleStringProperty player1;
    private final SimpleStringProperty player2;
    private final SimpleStringProperty winner;
    
  

    

    
    
	/*private String gameID;
	private String player1;
	private String player2;
	private String winner;*/
    
    
    public Spiele(String gameID, String player1, String player2, String winner) {
        this.gameID = new SimpleStringProperty(gameID);
        this.player1 = new SimpleStringProperty(player1);
        this.player2 = new SimpleStringProperty(player2);
        this.winner = new SimpleStringProperty(winner);
    }

    

    public String getGameID() {
        return gameID.get();
    }
    public void setGameID(String g_id) {
        gameID.set(g_id);
    }
    
    public StringProperty gameIDProperty() {
        return gameID;
    }
     
    
    public String getPlayer1() {
        return player1.get();
    }
    public void setPlayer1(String pl1) {
        player1.set(pl1);
    }
    public StringProperty player1Property() {
        return player1;
    }

    public String getPlayer2() {
        return player2.get();
    }
    public void setPlayer2(String pl2) {
        player2.set(pl2);
    }
    public StringProperty player2Property() {
        return player2;
    }

    public String getWinner() {
        return winner.get();
    }
    public void setWinner(String win) {
        winner.set(win);
    }
    public StringProperty winnerProperty() {
        return winner;
    }
	


	
	
	/*public Spiele(String gameID, String player1, String player2, String winner) {
		super();
		this.gameID = gameID;
		this.player1 = player1;
		this.player2 = player2;
		this.winner = winner;
	}*/
	
	
 
   

	
	/*public String getGameID() {
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
	}*/
	
}
