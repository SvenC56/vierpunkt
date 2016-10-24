package de.dhbw.mannheim.vierpunkt.interfaces;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import de.dhbw.mannheim.vierpunkt.logic.GameLogic;

import java.io.*;

public class FileInterface implements Runnable {

	public static String serverString ="";
	public static int zugZeit = 1000;
	public static GameLogic game = new GameLogic();
	public static int zug;
	public static int stelle;
	public static char charAtStelle;
	public static boolean zugSchongespielt = false;
	private static List<ZugListener> listeners = new ArrayList<ZugListener>();
	
	@Override
	public void run()
	{	

		while (true)
		{
	
		// Input aus ServerFile lesen
		try {
		serverString = zugEmpfangen();
		} catch (IOException e){e.getMessage();}
		
		// Wenn die Datei vom Server überschrieben wurde:
		if(!serverString.isEmpty() && zugSchongespielt == false){	
			
			// Zug des Gegners wird erfasst
			stelle = ordinalIndexOf(serverString, ">", 6) + 1;
			charAtStelle = serverString.charAt(stelle);
			if (charAtStelle != '-'){
				zug = Integer.parseInt(String.valueOf(charAtStelle));
				
				// Zug des Gegners wird in eigenes Logikarray eingetragen
				//game.setChip(zug, 1);
				
				// Zug des Gegners wird in GUI dargestellt
				fireZugEvent(zug);
				
				// Es wird sichergestellt, dass die Daten nur einmal erhoben werden
				zugSchongespielt = true;
				
			} else {
				// TO-DO: Unterscheiden zwischen Spielanfang und Spielende
			}
		}		
		
		// Wenn man am Zug ist: Zug spielen
		if (serverString.contains("true")){
			try {
				// Eigener Zug wird durch Logik bestimmt
				//zug = game.playerTurn();
				zug = (int)(Math.random()*7);
				
				// Eigener Zug wird dem Server übergeben
				zugSpielen(zug);
				
				// Eigener Zug wird in GUI dargestellt
				fireZugEvent(zug);
				
				try {
						zugSchongespielt = false;			
						Thread.sleep(zugZeit);
						
						// Für diese Runde wurde ein Zug gespielt
						
						
			} catch (InterruptedException e){e.getMessage();}
			
				}catch (IOException e){e.getMessage();}
		}
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
		FileInterface.zugZeit = zugZeit;
	}

	
	 /**
	  * Die Methode liest den Inhalt der vom Server an den Spieler verschickte Datei
	  * @return Den Inhalt der Server2Spieler.xml Datei
	  * @throws IOException
	  */
	public static String zugEmpfangen() throws IOException{
		String data = new String(Files.readAllBytes(Paths.get("C:\\FileInterface\\server2spielerx.xml")), StandardCharsets.UTF_8);
		//System.out.print(data);
		return data;
	}
	
	public void addListener(ZugListener toAdd){
		listeners.add(toAdd);
	}
	
	public static void fireZugEvent(int zug){
		for (ZugListener zl : listeners){
			zl.zugGespielt(zug);
		}
	}

}


