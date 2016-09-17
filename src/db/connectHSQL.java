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
			con = DriverManager.getConnection(
					"jdbc:hsqldb:file:C:\\Users\\Sven\\workspace\\vierpunkt\\VierGewinntDB; shutdown=true", "root",
					"vierpunkt");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		connectHSQL connect = new connectHSQL();
		// connect.selectAll();
		ResultSet r = connect.executeSQL("Select * From Match");
		connect.printResult(r);
	}

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

	public void printResult(ResultSet r) {
		try {
			while (r.next()) {
				int maxColumns = r.getMetaData().getColumnCount();
				String print = "";
				for (int i = 1; i < maxColumns; i++) {
					print += " ";
					print += r.getMetaData().getColumnName(i);
					print += " = ";
					print += r.getString(i);
					print += ",";
				}
				System.out.println(print);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
}