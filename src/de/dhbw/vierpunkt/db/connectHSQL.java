/* 
 * Author: Sven Cieslok
 */

package de.dhbw.vierpunkt.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class connectHSQL {
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
	public connectHSQL() {
		try {
			Class.forName("org.hsqldb.jdbcDriver");
		} catch (ClassNotFoundException e) {
			return;
		}
		con = null;

		try {
			con = DriverManager.getConnection("jdbc:hsqldb:file:.\\VierGewinntDB; shutdown=true", "root", "vierpunkt");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// /**
	// * Main Methode
	// **/
	// public static void main(String[] args) {
	// }

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
	 * Rueckgabe des hoechsten Indexwertes einer Tabelle, Die gewuenschte
	 * Tabelle muss uebergeben werden. In der Methode wird der String tableName
	 * aufgegliedert und der erste Buchstabe wird mit der Endung "_ID" ergaenzt.
	 * Anschlie�end wird ein Resultset erstellt, welches aus dem
	 * zusammengesetzten SQL Statement besteht. Daraufhin wird das Resultset
	 * ausgelesen und das Tabellenmaximum als Int Variable zurueckgegeben. Falls
	 * es im Statement ein SQL Error gibt, wird eine SQL Exception geworfen.
	 **/
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
	 * Ausgabe eines beliebigen ResultSets. Weiterfuehrung der Methode
	 * executeSQL(). Das zurueckgegebene Resultset Objekt wird komplett
	 * ausgegeben. Dazu werden ueber die Metadaten des ResultSets die Anzahl der
	 * Spalten in einer int Variable gespeichert. Die while Schleife prueft ob
	 * eine weitere Zeile vorhanden ist. Falls ja, wird eine for Schleife
	 * durchlaufen, welche jede Spalte ausgibt. Falls das SQL Statement
	 * fehlerhaft ist, wird eine SQL Exception zurueckgegeben.
	 **/
	public void printResult(ResultSet result) {
		try {
			while (result.next()) {
				int maxColumns = result.getMetaData().getColumnCount();
				String print = "";
				for (int i = 1; i <= maxColumns; i++) {
					print += " ";
					print += result.getMetaData().getColumnName(i);
					print += " = ";
					print += result.getString(i);
					if (i < maxColumns) {
						print += ",";
					} else {
						print += ";\n";
					}

				}
				System.out.println(print);
			}
			result.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

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
	 * Umwandlung eines bestimmten Arrays in SQL Statements. Falls das SQL
	 * Statement fehlerhaft ist, wird eine SQL Exception zurueckgegeben.
	 **/
	public void handOverArray(String[][] statements, String tableName) {
		String sqlValues = null;
		for (int zeile = 0; zeile < statements.length; zeile++) {
			sqlValues = "INSERT INTO " + "test" + " VALUES {";
			for (int spalte = 0; spalte < statements[zeile].length; spalte++) {
				if (spalte == statements[zeile].length - 1) {
					sqlValues += statements[zeile][spalte] + "};";
				} else {
					sqlValues += statements[zeile][spalte] + ", ";
				}
			}
		}
		executeSQL(sqlValues);
	}

	/**
	 * uebermittlung eines Matches in die DB
	 **/
	public void handOverGame(String Player1, String Player2, String WINNER, String POINTS) {
		System.out.println("INSERT INTO GAME (PLAYER1, PLAYER2, WINNER, POINTS) VALUES ('" + Player1 + "','" + Player2
				+ "','" + WINNER + "','" + POINTS + "');");
		executeSQL("INSERT INTO GAME (PLAYER1, PLAYER2, WINNER, POINTS) VALUES ('" + Player1 + "','" + Player2 + "','"
				+ WINNER + "','" + POINTS + "');");
	}

	/**
	 * uebermittlung eines Runde in die DB
	 **/
	public void handOverMatch(int M_ID, int G_ID) {
		System.out.println("INSERT INTO MATCH (M_ID,G_ID) VALUES(" + M_ID + "," + G_ID + ");");
		executeSQL("INSERT INTO MATCH (M_ID,G_ID) VALUES(" + M_ID + "," + G_ID + ");");
	}

	/**
	 * uebermittlung eines Zugs in die DB
	 **/
	public void handOverTurn(int M_ID, String PERSON, int POS_Y, int POS_X) {
		System.out.println("INSERT INTO TURN (M_ID, PERSON, POS_Y, POS_X) VALUES( " + M_ID + ", '" + PERSON + "', "
				+ POS_Y + ", " + POS_X + ");");
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