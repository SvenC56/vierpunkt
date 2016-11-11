package de.dhbw.vierpunkt.interfaces;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.Timer;

import java.io.*;

public class FileInterface implements Runnable, Observer {

	public static String serverString ="";
	public static int zug;
	public static int move;
	public static int stelle;
	public static char charAtStelle;
	public static boolean zugSchongespielt = false;
	private static List<ZugListener> listeners = new ArrayList<ZugListener>();
	private static List<GewinnerListener> gewinnerListeners = new ArrayList<GewinnerListener>();
	
	public static char spielerKennung = 'x';
	public static char gegnerKennung = 'o';
	public static String kontaktPfad = "C:\\FileInterface\\";
	public static int zugZeit = 1000;
	
	
	public static String getKontaktPfad()	{
		return kontaktPfad;
	}

	public static void setKontaktPfad(String kontaktPfad)	{
		FileInterface.kontaktPfad = kontaktPfad;
	}
	/**
	 * Der zum Status des Servers zu beobachtende Wert
	 * @see ServerStatus
	 */
	private static ServerStatus serverstatus = new ServerStatus("unchanged");
	
	/**
	 * Timer, der die Beobachtung des Serverstatus unterstuetzt
	 */
	private static Timer timer;
	
	/**
	 * Countdown, bei dessen Ablauf ein Beenden des Servers angenommen wird
	 */
	private static int timervalue = 30;
	private static int incrementalVal = 0;
	// bei der ersten Nachricht vom Server wird der Timer gestartet
	private static boolean firstMessage = true;
	

	// Leerer Konstruktor: Standardwerte werden verwendet
	public FileInterface()
	{
		
	}
	
	// Dem Konstruktor werden die Spielerkennung und der Kontaktpfad uebergeben
	public FileInterface(char spielerKennung, String kontaktPfad, int zugZeit){
		FileInterface.spielerKennung = spielerKennung;
		FileInterface.kontaktPfad = kontaktPfad;
		this.zugZeit = zugZeit;
		if (spielerKennung == 'x'){
			this.gegnerKennung = 'o';
			} else {
				this.gegnerKennung = 'x';
			}
	}
	


	@Override
	public void run()
	{
		serverstatus.addObserver(this);
		timer = new Timer(1000, new ActionListener(){
			public void actionPerformed(ActionEvent e){
				timervalue--;
				if (timervalue == 0){
					System.err.println("Server vermutlich abgestuerzt. Bitte Programm erneut starten.");
					}
				}
			});
		
		// Testausgabe fuer Konstruktor
		System.out.println("Die eingestellte Zugzeit betraegt: " + zugZeit);
		System.out.println("Die eingestellte Kennung ist: " + spielerKennung);
		System.out.println("Der eingestellte Kontaktpfad ist: " + kontaktPfad);
		System.out.println("Der neue Kontaktpfad ist: " + getNewPath(kontaktPfad));

		kontaktPfad = getNewPath(kontaktPfad);		
		
		while (zugSchongespielt == false)
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
				zug = Integer.parseInt(String.valueOf(charAtStelle));
				
				// Zug des Gegners wird in eigenes Logikarray eingetragen
				//game.setChip(zug, 1);
				
				// Zug des Gegners wird in GUI dargestellt
				System.out.println("Der Gegner spielt den Zug " + zug + ".");
				fireZugEvent(zug, gegnerKennung);
				

		        // Aktion des Servers wird registriert
		        incrementalVal++;
		        if (firstMessage == true){
		        	timer.start();
		        	firstMessage = false;
		        }
		        serverstatus.setValueToWatch("changed" + incrementalVal);
				
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
				move = (int)(Math.random()*7);
				
				// Eigener Zug wird dem Server uebergeben
				zugSpielen(move);
				
				// Eigener Zug wird in GUI dargestellt
				fireZugEvent(move, spielerKennung);
				
				try {
						Thread.sleep(zugZeit);
						// Fuer diese Runde wurde ein Zug gespielt
						zugSchongespielt = false;	
						
					} catch (InterruptedException e){e.getMessage();}
						}catch (IOException e){e.getMessage();}
			
		} else if  (serverString.contains("false") && serverString.contains("Spieler X")){
			System.err.println("******************** \n" + "S P I E L   B E E N D E T\n" + "********************");
        	System.out.println("");
        	System.out.println("Sieger des Spiels ist Spieler X!");
        	fireGewinnerEvent('x');
        	zugSchongespielt = true;
        	
		} else if  (serverString.contains("false") && serverString.contains("Spieler O")){
			System.err.println("******************** \n" + "S P I E L   B E E N D E T\n" + "********************");
        	System.out.println("");
        	System.out.println("Sieger des Spiels ist Spieler O!");
        	fireGewinnerEvent('o');
        	zugSchongespielt = true;
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
		fileOut.write(String.valueOf(spalte)); 
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
	
//	public static void fireZugEvent(int zug){
//		for (ZugListener zl : listeners){
//			zl.zugGespielt(zug);
//		}
//	}
	
	public static void fireZugEvent(int zug, char spieler){
		for (ZugListener zl : listeners){
			zl.zugGespielt(zug, spieler);
		}
	}
	
	public static void fireZugEvent(char spieler){
		for (ZugListener zl : listeners){
			zl.zugGespielt(spieler);
		}
	}
	
	public void addGewinnerListener(GewinnerListener toAdd){
		gewinnerListeners.add(toAdd);
	}
	
	public static void fireGewinnerEvent(char sieger){
		for (GewinnerListener gwl : gewinnerListeners){
			gwl.siegerAnzeigen(sieger);
		}
	}
	
	public static String getNewPath(String kontaktpfad){
		
		int stelleSlash = 0;
		
	
		stelleSlash = ordinalIndexOf(kontaktpfad, "\\", 0);
		kontaktpfad = kontaktpfad.substring(0, stelleSlash) + "\\" + kontaktpfad.substring(stelleSlash, kontaktpfad.length());
		kontaktpfad = kontaktpfad + "\\\\";
		
		return kontaktpfad;
	}
	
	@Override
	public void update(Observable o, Object arg)
	{
		timervalue = 30;
		System.out.println(arg);
	}

}


