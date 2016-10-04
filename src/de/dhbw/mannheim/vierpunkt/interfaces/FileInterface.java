package de.dhbw.mannheim.vierpunkt.interfaces;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.*;

public class FileInterface {

	
public static void main (String[] args) {
	
	try {
		
		while(true){


		// Input aus ServerFile lesen
		String serverFile = zugEmpfangen();
		
		// Wenn man am Zug ist: Zug spielen
		if (serverFile.contains("true")){
					zugSpielen(1);
			}
		Thread.sleep(10000);
		}
		
		}catch (Exception e){}
	}
	 /**
	  * Die Methode liest den Inhalt der vom Server an den Spieler verschickte Datei
	  * @return Den Inhalt der Server2Spieler.xml Datei
	  * @throws IOException
	  */
	public static String zugEmpfangen() throws IOException{
		String data = new String(Files.readAllBytes(Paths.get("C:\\FileInterface\\server2spielero.xml")), StandardCharsets.UTF_8);
		System.out.print(data);
		return data;
	}
	/**
	 * Die Methode übergibt dem Server den Zug bzw. die Spalte in die der Spielstein gelegt werden soll als Textdatei.
	 * @param spalte
	 * @throws IOException
	 */
	public static void zugSpielen(int spalte) throws IOException{
		FileWriter fileOut = new FileWriter("C:\\FileInterface\\spielero2server.txt");
		fileOut.write("1"); // 1 als dummy, später dann int spalte
		fileOut.flush();
		fileOut.close();
	}
}
