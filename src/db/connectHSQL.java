/* 
 * Author: Sven Cieslok
 */

package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class connectHSQL {
	Connection con = null;

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

	public static void main(String[] args) {
		connectHSQL connect = new connectHSQL();
		// Test ohne Logik
		String sql = "Select * from Kunde";
		connect.saveStatement(sql);
		// Test Zuende
	}

	//Ausführung eines beliebigen SQL Statements
	//Das Ergebnis wird in einem Resultstatement gespeichert
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

	//Speichern von neuen Variablen in der Datenbank
	public void saveStatement(String sql){
		try {
			Statement stmt = con.createStatement();
			ResultSet res = stmt.executeQuery(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	// Rückgabe des höchsten Indexwertes "G_Id"
	public int getGameId(connectHSQL connect) {
		try {
			ResultSet getId = connect.executeSQL("SELECT * FROM GAME;");
			int gameId = 0;
			while (getId.next()) {
				String print = getId.getString(1);
				gameId = Integer.parseInt(print);
			}
			getId.close();
			return gameId;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	// Rückgabe des höchsten Indexwertes "M_Id"
	public int getMatchId(connectHSQL connect) {
		try {
			ResultSet getId = connect.executeSQL("SELECT * FROM MATCH;");
			int turnId = 0;
			while (getId.next()) {
				String print = getId.getString(1);
				turnId = Integer.parseInt(print);
			}
			getId.close();
			return turnId;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	// Rückgabe des höchsten Indexwertes "T_Id"
	public int getTurnId(connectHSQL connect) {
		try {
			ResultSet getId = connect.executeSQL("SELECT * FROM TURN;");
			int turnId = 0;
			while (getId.next()) {
				String print = getId.getString(1);
				turnId = Integer.parseInt(print);
			}
			getId.close();
			return turnId;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	//Ausgabe eines beliebigen ResultSets
	public void printResult(ResultSet result) {
		try {
			while (result.next()) {
				int maxColumns = result.getMetaData().getColumnCount();
				String print = "";
				for (int i = 1; i < maxColumns; i++) {
					print += " ";
					print += result.getMetaData().getColumnName(i);
					print += " = ";
					print += result.getString(i);
					print += ",";
				}
				System.out.println(print);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
}