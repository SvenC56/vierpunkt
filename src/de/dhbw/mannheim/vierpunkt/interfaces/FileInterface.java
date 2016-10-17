package de.dhbw.mannheim.vierpunkt.interfaces;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.*;

public class FileInterface {

	public static String serverFile ="";
	public static int zugZeit = 1000;
	
public static void main (String[] args) {

		
		
		while(true){

		// Input aus ServerFile lesen
		try {
		serverFile = zugEmpfangen();
		} catch (IOException e){e.getMessage();}
		
		
		// Wenn man am Zug ist: Zug spielen
		if (serverFile.contains("true")){
			try {
				zugSpielen(1);
				}catch (IOException e){e.getMessage();}
			}
		
			
		try {
		Thread.sleep(zugZeit);
			} catch (InterruptedException e){e.getMessage();}
		
		}
	}
	
	 /**
	  * Die Methode liest den Inhalt der vom Server an den Spieler verschickte Datei
	  * @return Den Inhalt der Server2Spieler.xml Datei
	  * @throws IOException
	  */
	public static String zugEmpfangen() throws IOException{
		String data = new String(Files.readAllBytes(Paths.get("C:\\FileInterface\\server2spielerx.xml")), StandardCharsets.UTF_8);
		System.out.print(data);
		return data;
	}
	/**
	 * Die Methode übergibt dem Server den Zug bzw. die Spalte in die der Spielstein gelegt werden soll als Textdatei.
	 * @param spalte
	 * @throws IOException
	 */
	public static void zugSpielen(int spalte) throws IOException{
		FileWriter fileOut = new FileWriter("C:\\FileInterface\\spielerx2server.txt");
		int move = (int) (Math.random()*6); // Zug wird zufällig festgelegt
		System.out.println("Der Zug " + move + " wird gespielt.");
		fileOut.write(String.valueOf(move)); // 
		fileOut.flush();
		fileOut.close();
	}
}

