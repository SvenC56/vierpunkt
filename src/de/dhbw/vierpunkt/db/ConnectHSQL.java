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
	 * Dieses Resultset wird anschlie�end dann abgeschickt. Falls das SQL
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
	 * SQL Exception zurueckgegeben. Liefert 10 Zeilen zur�ck
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
	public void setGameDb(String Player1, String Player2) {
		System.out.println("INSERT INTO GAME (PLAYER1, PLAYER2) VALUES ('" + Player1 + "','" + Player2 + "');");
		executeSQL("INSERT INTO GAME (PLAYER1, PLAYER2) VALUES ('" + Player1 + "','" + Player2 + "');");
	}

	/**
	 * uebermittlung des aktuellen Punktestands
	 **/
	public void setScoreDb(int G_ID, String WINNER) {
		System.out.println("UPDATE GAME SET WINNER=" + "'" + WINNER + "' WHERE G_ID=" + G_ID + ";");
		executeSQL("UPDATE GAME SET WINNER=" + "'" + WINNER + "' WHERE G_ID=" + G_ID + ";");
	}

	/**
	 * uebermittlung eines Satzes in die DB
	 **/
	public void setMatchDb(int G_ID, int MATCHNUMBER) {
		System.out.println(
				"INSERT INTO MATCH (G_ID, MATCHNUMBER) VALUES(" + G_ID + "," + MATCHNUMBER + ");");
		executeSQL("INSERT INTO MATCH (G_ID, MATCHNUMBER) VALUES(" + G_ID + "," + MATCHNUMBER + ");");
	}

	public void updateMatch(int M_ID, int G_ID, int MATCHNUMBER, String SCORE) {
		System.out.println("UPDATE MATCH SET SCORE='" + SCORE + "' WHERE G_ID=" + G_ID + " AND M_ID= " + M_ID
				+ " AND MATCHNUMBER= " + MATCHNUMBER + ";");
		executeSQL("UPDATE MATCH SET SCORE='" + SCORE + "' WHERE G_ID=" + G_ID + " AND M_ID= " + M_ID
				+ " AND MATCHNUMBER= " + MATCHNUMBER + ";");
	}

	/**
	 * uebermittlung eines Zugs in die DB
	 **/
	public void setTurnDb(int M_ID, String PERSON, int POS_Y, int POS_X) {
		System.out.println("INSERT INTO TURN (M_ID, PERSON, POS_Y, POS_X) VALUES( " + M_ID + ", '" + PERSON + "', " + POS_Y
				+ ", " + POS_X + ");");
		executeSQL("INSERT INTO TURN (M_ID, PERSON, POS_Y, POS_X) VALUES( " + M_ID + ", '" + PERSON + "', " + POS_Y
				+ ", " + POS_X + ");");

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

	/**
	 * R�ckgabe der IDs
	 */
	public int getMaxId(String tableName) {
		try {
			String firstLetter = String.valueOf(tableName.charAt(0));
			ResultSet getId = executeSQL("SELECT MAX(" + firstLetter + "_ID) FROM " + tableName + ";");
			int tableMaxId = 0;
			while (getId.next()) {
				String print = getId.getString(1);
				if (print == null) {
					return 0;
				}
				tableMaxId = Integer.parseInt(print);
			}
			getId.close();
			return tableMaxId;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	public int getMaxMatchId(int G_ID) {
		try {
			ResultSet getId = executeSQL("SELECT MAX(M_ID) FROM MATCH WHERE G_ID=" + G_ID + ";");
			int tableMaxId = 0;
			while (getId.next()) {
				String print = getId.getString(1);
				if (print == null) {
					return 0;
				}
				tableMaxId = Integer.parseInt(print);
			}
			getId.close();
			return tableMaxId;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}
}