package de.dhbw.vierpunkt.logic;

public interface NameListener {

	void startGame(String name1, String name2);
	
	void startManGame(String name1, String name2);
	
	void setManTurn(String name, int x, int y);
	
}
