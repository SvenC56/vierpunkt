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
	/**
	 * Initialisierung des Connection Objekts. Initial wird dieses Objekt mit
	 * Null gefüllt.
	 **/
	Connection con = null;

	/**
	 * Konstruktor, welcher versucht über den angegebenen Pfad eine Verbindung
	 * mit der Datenbank aufzubauen. Falls das Programm keine Verbindung
	 * aufbauen kann wird ein leeres Connection Objekt zurück gegeben. Falls es
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

	/**
	 * Main Methode
	 **/
	public static void main(String[] args) {
		connectHSQL connect = new connectHSQL();
		int id = connect.getMaxId(connect, "Game");
		System.out.println(id);
	}

	/**
	 * Speichern neuer Variablen in der Datenbank. Der Methode wird das SQL
	 * Statement übergeben, welches dann in ein Resultset umgewandelt wird.
	 * Dieses Resultset wird anschließend dann abgeschickt. Falls das SQL
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
	 * Rueckgabe des hoechsten Indexwertes einer Tabelle, Die Connection und die
	 * gewuenschte Tabelle muss uebergeben werden. In der Methode wird der
	 * String tableName aufgegliedert und der erste Buchstabe wird mit der
	 * Endung "_ID" ergänzt. Anschließend wird ein Resultset erstellt, welches
	 * aus dem zusammengesetzten SQL Statement besteht. Daraufhin wird das
	 * Resultset ausgelesen und das Tabellenmaximum als Int Variable
	 * zurückgegeben. Falls es im Statement ein SQL Error gibt, wird eine SQL
	 * Exception geworfen.
	 **/
	public int getMaxId(connectHSQL connect, String tableName) {
		try {
			String firstLetter = String.valueOf(tableName.charAt(0));
			ResultSet getId = connect.executeSQL("SELECT MAX(" + firstLetter + "_ID) FROM " + tableName + ";");
			int tableMaxId = 0;
			while (getId.next()) {
				String print = getId.getString(1);
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
	 * Resultstatement gespeichert und zurückgegeben. Falls das SQL Statement
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
	 * Ausgabe eines beliebigen ResultSets. Weiterführung der Methode
	 * executeSQL(). Das zurückgegebene Resultset Objekt wird komplett
	 * ausgegeben. Dazu werden über die Metadaten des ResultSets die Anzahl der
	 * Spalten in einer int Variable gespeichert. Die while Schleife prüft ob
	 * eine weitere Zeile vorhanden ist. Falls ja, wird eine for Schleife
	 * durchlaufen, welche jede Spalte ausgibt. Falls das SQL Statement
	 * fehlerhaft ist, wird eine SQL Exception zurückgegeben.
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
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
}