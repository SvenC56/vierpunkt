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
	public static GameLogic game = new GameLogic();
	public static int zug1;
	public static int zug2;
	public static int stelle;
	public static char charAtStelle;
	public static boolean zugSchongespielt = false;
	private static List<ZugListener> listeners = new ArrayList<ZugListener>();
	
	public static char spielerKennung = 'X';
	public static String kontaktPfad = "C:\\FileInterface\\";
	public static int zugZeit = 1000;
	
	// Leerer Konstruktor: Standardwerte werden verwendet
	public FileInterface()
	{
		
	}
	
	// Dem Konstruktor werden die Spielerkennung und der Kontaktpfad uebergeben
	public FileInterface(char spielerKennung, String kontaktPfad, int zugZeit){
		this.spielerKennung = spielerKennung;
		this.kontaktPfad = kontaktPfad;
		this.zugZeit = zugZeit;
	}
	


	@Override
	public void run()
	{	
		// Testausgabe fuer Konstruktor
		System.out.println("Die eingestellte Zugzeit betraegt: " + zugZeit);
		
		while (true)
		{
	
		// Input aus ServerFile lesen
		try {
		serverString = zugEmpfangen();
		} catch (IOException e){e.getMessage();}
		
		// Wenn die Datei vom Server ueberschrieben wurde:
		if(!serverString.isEmpty() && zugSchongespielt == false){	
			
			// Zug des Gegners wird erfasst
			stelle = ordinalIndexOf(serverString, ">", 6) + 1;
			charAtStelle = serverString.charAt(stelle);
			if (charAtStelle != '-'){
				zug1 = Integer.parseInt(String.valueOf(charAtStelle));
				
				// Zug des Gegners wird in eigenes Logikarray eingetragen
				//game.setChip(zug, 1);
				
				// Zug des Gegners wird in GUI dargestellt
				System.out.println("Der Gegner spielt den Zug " + zug1 + ".");
				fireZugEvent(zug1);
				
				// Es wird sichergestellt, dass die Daten nur einmal erhoben werden
				zugSchongespielt = true;
				
				try
				{
					Thread.sleep(zugZeit);
				} catch (InterruptedException e)
				{
					e.printStackTrace();
				}
				
			} else {
				// TO-DO: Unterscheiden zwischen Spielanfang und Spielende
			}
		}		
		
		// Wenn man am Zug ist: Zug spielen
		if (serverString.contains("true")){
			try {
				// Eigener Zug wird durch Logik bestimmt
				//zug = game.playerTurn();
				zug2 = (int)(Math.random()*7);
				
				// Eigener Zug wird dem Server uebergeben
				zugSpielen(zug2);
				
				// Eigener Zug wird in GUI dargestellt
				fireZugEvent(zug2);
				
				try {
								
						Thread.sleep(zugZeit);
						// Fuer diese Runde wurde ein Zug gespielt
						zugSchongespielt = false;	
						
						
			} catch (InterruptedException e){e.getMessage();}
			
				}catch (IOException e){e.getMessage();}
		}
	}
}
	
	
	/**
	 * Die Methode uebergibt dem Server den Zug bzw. die Spalte in die der Spielstein gelegt werden soll als Textdatei.
	 * @param spalte
	 * @throws IOException
	 */
	public static void zugSpielen(int spalte) throws IOException{
		FileWriter fileOut = new FileWriter( kontaktPfad + "spieler" + spielerKennung + "2server.txt");
		System.out.println("Der Zug " + spalte + " wird gespielt.");
		fileOut.write(String.valueOf(spalte)); // 
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
	 * Getter fuer Zugzeit
	 * @return Zugzeit
	 */
	public int getZugZeit()
	{
		return zugZeit;
	}

	/**
	 * Setter fuer Zugzeit
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
		String data = new String(Files.readAllBytes(Paths.get(kontaktPfad + "server2spieler" + spielerKennung +".xml")), StandardCharsets.UTF_8);
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


