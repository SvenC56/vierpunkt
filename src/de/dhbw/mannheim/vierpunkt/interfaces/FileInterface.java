package de.dhbw.mannheim.vierpunkt.interfaces;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import de.dhbw.mannheim.vierpunkt.logic.GameLogic;

import java.io.*;

public class FileInterface implements Runnable{

	public String serverFile ="";
	public int zugZeit = 1000;
	
	@Override
	public void run()
	{

		GameLogic game = new GameLogic();
		int zug;
		int stelle;
		char charAtStelle;
		boolean onetime = false;
		
		while(true){

		// Input aus ServerFile lesen
		try {
		serverFile = zugEmpfangen();
		} catch (IOException e){e.getMessage();}
		
		// Wenn die Datei vom Server überschrieben wurde:
		if(!serverFile.isEmpty() && onetime == false){		
			// Zug des Gegners wird erfasst
			stelle = ordinalIndexOf(serverFile, ">", 6) + 1;
			charAtStelle = serverFile.charAt(stelle);
			if (charAtStelle != '-'){
				zug = Integer.parseInt(String.valueOf(charAtStelle));
				// Zug des Gegners wird in eigenes Logikarray eingetragen
				game.setChip(zug, 1);
				// Es wird sichergestellt, dass die Daten nur einmal erhoben werden
				onetime = true;
			} else {
				// TO-DO: Unterscheiden zwischen Spielanfang und Spielende
			}
		}		
		
		// Wenn man am Zug ist: Zug spielen
		if (serverFile.contains("true")){
			try {
				zug = game.playerTurn();
				zugSpielen(zug);
				}catch (IOException e){e.getMessage();}
		}
		
		try {
		Thread.sleep(zugZeit);
			} catch (InterruptedException e){e.getMessage();}
		}
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
	
	/**
	 * Im String @originalString wird das @n_tesVorkommen des @zuSuchenderString gesucht und als @position wiedergegeben 
	 * @param originalString
	 * @param zuSuchenderString
	 * @param n_tesVorkommen
	 * @return position
	 */
	public static int ordinalIndexOf(String originalString, String zuSuchenderString, int n_tesVorkommen) {
	    int position = originalString.indexOf(zuSuchenderString, 0);
	    while (n_tesVorkommen-- > 0 && position != -1)
	        position = originalString.indexOf(zuSuchenderString, position+1);
	    return position;
	}

	/**
	 * Getter für Zugzeit
	 * @return Zugzeit
	 */
	public int getZugZeit()
	{
		return zugZeit;
	}

	/**
	 * Setter für Zugzeit
	 */
	public void setZugZeit(int zugZeit)
	{
		this.zugZeit = zugZeit;
	}

	
	 /**
	  * Die Methode liest den Inhalt der vom Server an den Spieler verschickte Datei
	  * @return Den Inhalt der Server2Spieler.xml Datei
	  * @throws IOException
	  */
	public String zugEmpfangen() throws IOException{
		String data = new String(Files.readAllBytes(Paths.get("C:\\FileInterface\\server2spielerx.xml")), StandardCharsets.UTF_8);
		//System.out.print(data);
		return data;
	}
}


