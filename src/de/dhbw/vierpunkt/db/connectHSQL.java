/**
 * Verbindung zur Datenbank
 *
 * @author Sven Cieslok
 * @version 1.0
 */

package de.dhbw.vierpunkt.db;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ConnectHSQL {
	/**
	 * Initialisierung des Connection Objekts. Initial wird dieses Objekt mit
	 * Null gefuellt.
	 **/
	Connection con = null;

	/**
	 * Konstruktor, welcher versucht ueber den angegebenen Pfad eine Verbindung
	 * mit der Datenbank aufzubauen. Falls das Programm keine Verbindung
	 * aufbauen kann wird ein leeres Connection Objekt zurueck gegeben. Falls es
	 * zu einem SQL Error kommt, wird eine SQL Exception ausgegeben.
	 **/
	public ConnectHSQL() {
		try {
			Class.forName("org.hsqldb.jdbcDriver");
		} catch (ClassNotFoundException e) {
			return;
		}
		con = null;

		try {
			con = DriverManager.getConnection("jdbc:hsqldb:file:" + "." + File.separatorChar + "database"
					+ File.separatorChar + "VierGewinntDB; shutdown=true", "root", "vierpunkt");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Speichern neuer Variablen in der Datenbank. Der Methode wird das SQL
	 * Statement uebergeben, welches dann in ein Resultset umgewandelt wird.
	 * Dieses Resultset wird anschlieï¿½end dann abgeschickt. Falls das SQL
	 * Statement fehlerhaft ist, wird eine SQL Exception ausgegeben.
	 **/
	public void saveStatement(String sql) {
		try {
			Statement stmt = con.createStatement();
			ResultSet res = stmt.executeQuery(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	
	/**
	 * Ausfuehrung eines beliebigen SQL Statements. Das Ergebnis wird in einem
	 * Resultstatement gespeichert und zurueckgegeben. Falls das SQL Statement
	 * fehlerhaft ist, wird eine SQL Exception ausgegeben.
	 **/
	public ResultSet executeSQL(String sql) {
		try {
			Statement stmt = con.createStatement();
			ResultSet res = stmt.executeQuery(sql);
			return res;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	

	/**
	 * Speichern eines beliebigen ResultSets in einem Array. Weiterfuehrung der
	 * Methode executeSQL(). Falls das SQL Statement fehlerhaft ist, wird eine
	 * SQL Exception zurueckgegeben.
	 **/
	public String[][] saveResult(ResultSet result) {
		try {
			int y = 0; // Zeilenwert
			String[][] returnStatements = new String[result.getRow() + 1][result.getMetaData().getColumnCount()];
			while (result.next()) {
				int maxColumns = result.getMetaData().getColumnCount();
				for (int i = 1; i <= maxColumns; i++) {
					returnStatements[y][(i - 1)] = result.getString(i);
				}
				y++; // hochzaehlen des Zeilenwerts
			}
			return returnStatements;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;

	}

	/**
	 * Speichern eines beliebigen ResultSets in einem Array. Weiterfuehrung der
	 * Methode executeSQL(). Falls das SQL Statement fehlerhaft ist, wird eine
	 * SQL Exception zurueckgegeben. Liefert 10 Zeilen zurück
	 **/
	public String[][] save10Result(ResultSet result) {
		try {
			int y = 0; // Zeilenwert
			String[][] returnStatements = new String[10][result.getMetaData().getColumnCount()];
			while (result.next()) {
				int maxColumns = result.getMetaData().getColumnCount();
				for (int i = 1; i <= maxColumns; i++) {
					returnStatements[y][(i - 1)] = result.getString(i);
				}
				y++; // hochzaehlen des Zeilenwerts
			}
			return returnStatements;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;

	}

	/**
	 * uebermittlung eines Spiels in die DB
	 **/
	public void setGameDb(String Player1, String Player2, String WINNER, String POINTS) {
		System.out.println("INSERT INTO GAME (PLAYER1, PLAYER2, WINNER, POINTS) VALUES ('" + Player1 + "','" + Player2
				+ "','" + WINNER + "','" + POINTS + "');");
		executeSQL("INSERT INTO GAME (PLAYER1, PLAYER2, WINNER, POINTS) VALUES ('" + Player1 + "','" + Player2 + "','"
				+ WINNER + "','" + POINTS + "');");
	}

	/**
	 * uebermittlung des aktuellen Punktestands
	 **/
	public void setScoreDb(int G_ID, String WINNER, String POINTS) {
		System.out.println(
				"UPDATE GAME SET WINNER=" + "'" + WINNER + "', POINTS=" + "'" + POINTS + "' WHERE G_ID=" + G_ID + ";");
		executeSQL(
				"UPDATE GAME SET WINNER=" + "'" + WINNER + "', POINTS=" + "'" + POINTS + "' WHERE G_ID=" + G_ID + ";");
	}

	/**
	 * uebermittlung eines Satzes in die DB
	 **/
	public void setMatchDb(int M_ID, int G_ID) {
		System.out.println("INSERT INTO MATCH (M_ID,G_ID) VALUES(" + M_ID + "," + G_ID + ");");
		executeSQL("INSERT INTO MATCH (M_ID,G_ID, MATCHWINNER) VALUES(" + M_ID + "," + G_ID + ");");
	}

	public void updateMatch(int M_ID, int G_ID, String MIDRESULT) {
		executeSQL("UPDATE MATCH SET MIDRESULT='" + MIDRESULT + "' WHERE G_ID=" + G_ID + " AND M_ID= " + M_ID + ";");
	}

	/**
	 * Rueckgabe der Z_ID zur erstellung eines Zugs
	 **/
	public int getZID(int G_ID, int M_ID) {
		String[][] temp = saveResult(
				executeSQL("SELECT Z_ID FROM MATCH WHERE G_ID = " + G_ID + " AND M_ID= " + M_ID + ";"));
		return Integer.parseInt(temp[0][0]);
	}

	/**
	 * uebermittlung eines Zugs in die DB
	 **/
	public void setTurnDb(int M_ID, String PERSON, int POS_Y, int POS_X, int Z_ID) {
		System.out.println("INSERT INTO TURN (M_ID, PERSON, POS_Y, POS_X) VALUES( " + M_ID + ", '" + PERSON + "', "
				+ POS_Y + ", " + POS_X + ");");
		executeSQL("INSERT INTO TURN (M_ID, PERSON, POS_Y, POS_X, Z_ID) VALUES( " + M_ID + ", '" + PERSON + "', "
				+ POS_Y + ", " + POS_X + Z_ID + ");");

	}

	/**
	 * uebermittlung des kompletten Highscores in das Game
	 */
	public String[][] getHighscoreFull() {
		String[][] highscore = null;
		highscore = saveResult(executeSQL("SELECT * FROM GAME NATURAL JOIN MATCH NATURAL JOIN TURN"));
		return highscore;
	}

	/**
	 * uebermittlung der letzten 10 Spiele aus Game
	 */
	public String[][] getLastTenGames() {
		String[][] highscore = null;
		highscore = save10Result(executeSQL("SELECT * FROM GAME ORDER BY G_ID DESC LIMIT 10;"));
		return highscore;
	}

	/**
	 * uebermittlung des Game Highscores in das Game
	 */
	public String[][] getHighscoreGame() {
		String[][] highscore = null;
		highscore = saveResult(executeSQL("SELECT * FROM GAME"));
		return highscore;
	}

	/**
	 * uebermittlung des Match Highscores in das Game
	 */
	public String[][] getHighscoreMatch(int G_ID) {
		String[][] highscore = null;
		highscore = saveResult(executeSQL("SELECT * FROM MATCH WHERE G_ID = " + G_ID));
		return highscore;
	}

	/**
	 * uebermittlung des Match Highscores in das Game
	 */
	public String[][] getHighscoreTurn(int G_ID, int M_ID) {
		String[][] highscore = null;
		highscore = saveResult(
				executeSQL("SELECT * FROM TURN NATURAL JOIN MATCH WHERE G_ID =" + G_ID + " and M_ID=" + M_ID + ";"));
		return highscore;
	}
	
	

}