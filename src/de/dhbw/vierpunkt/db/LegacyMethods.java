/**
 * @author Sven Cieslok
 * @version 1.0
 */
package de.dhbw.vierpunkt.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import de.dhbw.vierpunkt.db.ConnectHSQL;

public class LegacyMethods {
	/**
	 * ######OLD METHODS######
	 **/
	
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
}
