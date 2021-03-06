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

import de.dhbw.vierpunkt.logic.Game;
import de.dhbw.vierpunkt.tests.AI_Logic_Test.GameLogic;

import java.io.*;

public class FileInterface implements Runnable, Observer
{
	/**
	 * Initialisierung der Variablen
	 */
	public static String					serverString		= "";
	public static int						zug;
	public static int						move;
	public static int						stelle;
	public static char						charAtStelle;
	public static boolean					zugSchongespielt	= false;
	private static List<ZugListener>		listeners			= new ArrayList<ZugListener>();
	private static List<GewinnerListener>	gewinnerListeners	= new ArrayList<GewinnerListener>();
	private static boolean					fallbackActive		= true;
	private static GameLogic				fallbackGame		= new GameLogic();
	private static Game						game;
	private static int						depth				= 2;
	public static char						spielerKennung		= 'x';
	public static char						gegnerKennung		= 'o';
	public static String					kontaktPfad			= "C:\\FileInterface\\";
	public static int						zugZeit				= 1000;

	/**
	 * Getter und Setter f�r Kontaktpfad
	 * 
	 * @return kontaktPfad
	 */
	public static String getKontaktPfad()
	{
		return kontaktPfad;
	}

	public static void setKontaktPfad(String kontaktPfad)
	{
		FileInterface.kontaktPfad = kontaktPfad;
	}

	/**
	 * Der zum Status des Servers zu beobachtende Wert
	 * 
	 * @see ServerStatus
	 */
	private static ServerStatus	serverstatus	= new ServerStatus("unchanged");

	/**
	 * Timer, der die Beobachtung des Serverstatus unterstuetzt
	 */
	private static Timer		timer;

	/**
	 * Countdown, bei dessen Ablauf ein Beenden des Servers angenommen wird
	 */
	private static int			timervalue		= 30;
	private static int			incrementalVal	= 0;
	// bei der ersten Nachricht vom Server wird der Timer gestartet
	private static boolean		firstMessage	= true;
	private int					count;

	// Leerer Konstruktor: Standardwerte werden verwendet
	public FileInterface()
	{

	}

	// Dem Konstruktor werden die Spielerkennung und der Kontaktpfad uebergeben
	public FileInterface(char spielerKennung, String kontaktPfad, int zugZeit,
			Game game)
	{
		FileInterface.spielerKennung = spielerKennung;
		FileInterface.kontaktPfad = kontaktPfad;
		this.zugZeit = zugZeit;
		this.game = game;
		if (spielerKennung == 'x')
		{
			this.gegnerKennung = 'o';
		} else
		{
			this.gegnerKennung = 'x';
		}
	}

	@Override
	public void run()
	{
		// Das FileInterface beobachtet den ServerStatus
		serverstatus.addObserver(this);
		// Der Timer wird initialisiert, alle 1000 Millisekunden wird der Wert
		// des Timers um 1 verringert
		timer = new Timer(1000, new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				timervalue--;
				// Wenn der Timer nicht dur ServerEvents neu gestartet wird,
				// wird ein Absturz angenommen und folgender Code ausgefuehrt
				if (timervalue == 0)
				{
					System.err.println(
							"Server vermutlich abgestuerzt. Bitte Programm erneut starten.");
					fireServerConnectionErrorEvent();
				}
			}
		});

		// Testausgabe fuer Konstruktor
		System.out.println("Die eingestellte Zugzeit betraegt: " + zugZeit);
		System.out.println("Die eingestellte Kennung ist: " + spielerKennung);
		System.out.println("Der eingestellte Kontaktpfad ist: " + kontaktPfad);
		System.out.println(
				"Der neue Kontaktpfad ist: " + getNewPath(kontaktPfad));

		kontaktPfad = getNewPath(kontaktPfad);

		while (zugSchongespielt == false)
		{

			// Input aus ServerFile lesen
			try
			{
				serverString = zugEmpfangen();
			} catch (IOException e)
			{
				e.getMessage();
			}

			// Wenn die Datei vom Server ueberschrieben wurde:
			if (!serverString.isEmpty() && zugSchongespielt == false)
			{

				// Zug des Gegners wird erfasst
				stelle = ordinalIndexOf(serverString, ">", 6) + 1;
				charAtStelle = serverString.charAt(stelle);
				if (charAtStelle != '-')
				{
					zug = Integer.parseInt(String.valueOf(charAtStelle));

					// Zug des Gegners wird in eigenes Logikarray eingetragen
					if (fallbackActive)
					{
						if (fallbackGame.getCurrentPlayer() == 1)
						{ // ist dran

							System.out.print(" ist dran              " + "");
							System.out.println(
									"eval" + GameLogic.evaluate(fallbackGame));
							fallbackGame.playTurn(zug, 1);
							fallbackGame.setCurrentPlayer(2);
							System.out.println(" hat in Spalte gelegt " + zug);
							System.out.println("Bewertung: "
									+ GameLogic.evaluate(fallbackGame));

							// Sieg?
							if (GameLogic.evaluate(
									fallbackGame) == (int) Double.NEGATIVE_INFINITY)
							{
								fallbackGame.runInConsole();
								System.out.println(
										GameLogic.evaluate(fallbackGame));
								System.out.println(" hat gewonnen...");
							}
							;

							fallbackGame.runInConsole();

						}

					} else
					{
						game.getCurrentMatch().getCurrentTurn()
								.startOpponentTurn(zug);
					}

					// Zug des Gegners wird in GUI dargestellt
					System.out
							.println("Der Gegner spielt den Zug " + zug + ".");
					fireZugEvent(zug, gegnerKennung);

					// Aktion des Servers wird registriert
					incrementalVal++;
					if (firstMessage == true)
					{
						timer.start();
						firstMessage = false;
					}
					serverstatus.setValueToWatch("changed" + incrementalVal);

					// Es wird sichergestellt, dass die Daten nur einmal erhoben
					// werden
					zugSchongespielt = true;

					try
					{
						Thread.sleep(zugZeit);
					} catch (InterruptedException e)
					{
						e.printStackTrace();
					}

				} else
				{
					// TO-DO: Unterscheiden zwischen Spielanfang und Spielende
				}
			}

			// Wenn man am Zug ist: Zug spielen
			if (serverString.contains("true"))
			{

				// Eigener Zug wird durch Logik bestimmt
				if (fallbackActive)
				{

					System.out.println("Wir sind dran          " + "");
					if (count == 0)
					{
						move = 3;
						fallbackGame.playTurn(3, 2);
						count++;
					} else
					{
						System.out.println(
								"eval" + GameLogic.evaluate(fallbackGame));
						// x = (int) (Math.random()*6); // falls KI nicht
						// getestet werden soll, sondern nur Zufallszahl
						move = GameLogic.calcMove(fallbackGame, depth);
						fallbackGame.playTurn(move, 2);
						System.out
								.println("Wir haben in Spalte gelegt " + move);
						fallbackGame.setCurrentPlayer(1);
					}

					// Sieg?
					if (GameLogic.evaluate(
							fallbackGame) == (int) Double.POSITIVE_INFINITY)
					{
						fallbackGame.runInConsole();
						System.out.println(GameLogic.evaluate(fallbackGame));
						System.out.println(" Wir haben gewonnen!");
					}
					;

					fallbackGame.runInConsole();

					System.out.println("_____________________________________");
					System.out.println();

					System.out.println();

					// Eigener Zug wird dem Server uebergeben

					try
					{
						move = (int) (Math.random()*7);
						zugSpielen(move);
					} catch (IOException e1)
					{
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

					// Eigener Zug wird in GUI dargestellt
					fireZugEvent(move, spielerKennung);

					try
					{
						Thread.sleep(zugZeit);
						// Fuer diese Runde wurde ein Zug gespielt
						zugSchongespielt = false;

					} catch (InterruptedException e)
					{
						e.getMessage();
					}

				} else
				{
					if (zugZeit >= 8000)
					{
						// der Move wird von der Logik berechnet
						move = game.getCurrentMatch().getCurrentTurn()
								.startAgentTurn();
					} else
					{
						move = game.getCurrentMatch().getCurrentTurn()
								.setValidRandomTurn();

					}
				}

				// Wenn der serverString false enthaelt, ist das Match beendet
				// und der uebergebene Spieler gewinnt.
			} else if (serverString.contains("false")
					&& serverString.contains("Spieler X"))
			{
				System.err.println("******************** \n"
						+ "S P I E L   B E E N D E T\n"
						+ "********************");
				System.out.println("");
				System.out.println("Sieger des Spiels ist Spieler X!");
				fireGewinnerEvent('x');
				zugSchongespielt = true;

			} else if (serverString.contains("false")
					&& serverString.contains("Spieler O"))
			{
				System.err.println("******************** \n"
						+ "S P I E L   B E E N D E T\n"
						+ "********************");
				System.out.println("");
				System.out.println("Sieger des Spiels ist Spieler O!");
				fireGewinnerEvent('o');
				zugSchongespielt = true;
			}
		}
	}

	/**
	 * Die Methode uebergibt dem Server den Zug bzw. die Spalte in die der
	 * Spielstein gelegt werden soll als Textdatei.
	 * 
	 * @param spalte
	 * @throws IOException
	 */
	public static void zugSpielen(int spalte) throws IOException
	{
		FileWriter fileOut = new FileWriter(
				kontaktPfad + "spieler" + spielerKennung + "2server.txt");
		System.out.println("Der Zug " + spalte + " wird gespielt.");
		fileOut.write(String.valueOf(spalte));
		fileOut.flush();
		fileOut.close();

	}

	/**
	 * Im String @originalString wird das @n_tesVorkommen des @zuSuchenderString
	 * gesucht und als @position wiedergegeben
	 * 
	 * @param originalString
	 * @param zuSuchenderString
	 * @param n_tesVorkommen
	 * @return position
	 */
	public static int ordinalIndexOf(String originalString,
			String zuSuchenderString, int n_tesVorkommen)
	{
		int position = originalString.indexOf(zuSuchenderString, 0);
		while (n_tesVorkommen-- > 0 && position != -1)
			position = originalString.indexOf(zuSuchenderString, position + 1);
		return position;
	}

	/**
	 * Getter fuer Zugzeit
	 * 
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
	 * Die Methode liest den Inhalt der vom Server an den Spieler verschickte
	 * Datei
	 * 
	 * @return Den Inhalt der Server2Spieler.xml Datei
	 * @throws IOException
	 */
	public static String zugEmpfangen() throws IOException
	{
		String data = new String(Files.readAllBytes(Paths
				.get(kontaktPfad + "server2spieler" + spielerKennung + ".xml")),
				StandardCharsets.UTF_8);
		// System.out.print(data);
		return data;
	}

	/**
	 * Fuegt Listener fuer ZugeEvents hinzu
	 * 
	 * @param toAdd
	 */
	public void addListener(ZugListener toAdd)
	{
		listeners.add(toAdd);
	}

	/**
	 * Sendet ZugEvents an Listener
	 * 
	 * @param zug
	 * @param spieler
	 */
	public static void fireZugEvent(int zug, char spieler)
	{
		for (ZugListener zl : listeners)
		{
			zl.zugGespielt(zug, spieler);
		}
	}

	/**
	 * Triggert das ServerConnectionError Event und sendet es an alle Listener
	 */
	public static void fireServerConnectionErrorEvent()
	{
		for (ZugListener zl : listeners)
		{
			zl.onServerConnectionError();
		}
	}

	/**
	 * Fuegt GewinnerListener hinzu
	 * 
	 * @param toAdd
	 */
	public void addGewinnerListener(GewinnerListener toAdd)
	{
		gewinnerListeners.add(toAdd);
	}

	/**
	 * Sendet GewinnerEvents an GewinnerListener
	 * 
	 * @param sieger
	 */
	public static void fireGewinnerEvent(char sieger)
	{
		for (GewinnerListener gwl : gewinnerListeners)
		{
			gwl.siegerAnzeigen(sieger);
		}
	}

	/**
	 * Hier wird der uebergebene Kontaktpfad in ein von Java interpretierbares
	 * Format ueberfuehrt. Bsp: C:\FileInterface wird zu C:\\FileInterface\\
	 * 
	 * @param kontaktpfad
	 * @return kontaktpfad
	 */
	public static String getNewPath(String kontaktpfad)
	{

		int stelleSlash = 0;

		stelleSlash = ordinalIndexOf(kontaktpfad, "\\", 0);
		kontaktpfad = kontaktpfad.substring(0, stelleSlash) + "\\"
				+ kontaktpfad.substring(stelleSlash, kontaktpfad.length());
		kontaktpfad = kontaktpfad + "\\\\";

		return kontaktpfad;
	}

	/**
	 * Bei empfangen einer Nachricht vom Server wird der Timer zurueckgesetzt
	 */
	@Override
	public void update(Observable o, Object arg)
	{
		timervalue = 30;
		System.out.println(arg);
	}

}
