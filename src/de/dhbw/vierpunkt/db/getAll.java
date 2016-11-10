/**
 * @author Sven Cieslok
 * @version 1.0
 */
package de.dhbw.vierpunkt.db;

public class GetAll implements Runnable{

	@Override
	public void run() {
		ConnectHSQL getAllDb = new ConnectHSQL();
		//return getAllDb.getAll();
	}
}