package de.dhbw.vierpunkt.interfaces;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.Timer;

import com.pusher.client.AuthorizationFailureException;
import com.pusher.client.Authorizer;
import com.pusher.client.Pusher;
import com.pusher.client.PusherOptions;
import com.pusher.client.channel.PrivateChannel;
import com.pusher.client.channel.PrivateChannelEventListener;
import com.pusher.client.connection.ConnectionEventListener;
import com.pusher.client.connection.ConnectionState;
import com.pusher.client.connection.ConnectionStateChange;

import de.dhbw.vierpunkt.gui.ConnectionErrorListener;
import de.dhbw.vierpunkt.logic.Game;
import de.dhbw.vierpunkt.tests.AI_Logic_Test.GameLogic;

public class PusherInterface implements Runnable, Observer
{
	private static int								count				= 0;
	private static boolean							fallbackActive		= true;
	private static GameLogic						fallbackGame		= new GameLogic();
	private static int								depth				= 2;

	/**
	 * App-ID der Pusher Instanz des Clients
	 */
	private static String							MyAppID				= "";

	/**
	 * App-Key der Pusher-Instanz des Clients
	 */
	private static String							MyAppKey			= "";

	/**
	 * App-Secret der Pusher-Instanz des Clients
	 */
	private static String							MyAppSecret			= "";

	/**
	 * Channel-Name des Kommunikationskanals
	 */
	private static String							ChannelName			= "private-channel";

	/**
	 * Arrays mit Listenern die auf ZugEvents, ConnectionErrors und
	 * GewinnerEvents hoeren
	 */
	private static List<ZugListener>				listeners			= new ArrayList<ZugListener>();
	private static List<ConnectionErrorListener>	errorListeners		= new ArrayList<ConnectionErrorListener>();
	private static List<GewinnerListener>			gewinnerListeners	= new ArrayList<GewinnerListener>();

	/**
	 * Der zum Status des Servers zu beobachtende Wert
	 * 
	 * @see ServerStatus
	 */
	private static ServerStatus						serverstatus		= new ServerStatus(
			"unchanged");

	/**
	 * Timer, der die Beobachtung des Serverstatus unterstuetzt
	 */
	private static Timer							serverTimer;

	/**
	 * Die Zeit, die dem Agenten fuer seinen Zug bleibt
	 */
	public static int								zugZeit				= 1000;

	/**
	 * Countdown, bei dessen Ablauf ein Beenden des Servers angenommen wird
	 */
	private static int								serverTimerValue	= 30;
	private static int								incrementalVal		= 0;
	private static int								move;

	/**
	 * Beim Start des Spiels wird festgelegt, ob der Spieler X oder O ist
	 */
	public static char								spielerKennung		= 'x';
	public static char								gegnerKennung		= 'o';
	private static Game								game;

	// bei der ersten Nachricht vom Server wird der Timer gestartet
	private static boolean							firstMessage		= true;

	// Konstruktoren
	public PusherInterface()
	{
	}

	public PusherInterface(int zugZeit, String AppID, String AppKey,
			String AppSecret, char spielerKennung, Game game)
	{
		PusherInterface.zugZeit = zugZeit;
		PusherInterface.MyAppID = AppID;
		PusherInterface.MyAppKey = AppKey;
		PusherInterface.MyAppSecret = AppSecret;
		PusherInterface.spielerKennung = spielerKennung;
		PusherInterface.game = game;

		if (spielerKennung == 'x')
		{
			PusherInterface.gegnerKennung = 'o';
		} else
		{

			PusherInterface.gegnerKennung = 'x';
		}
	}

	public void run()
	{

		// Die Pusher Klasse beobachtet den ServerStatus
		serverstatus.addObserver(this);
		// Der Timer wird initialisiert, alle 1000 Millisekunden wird der Wert
		// des Timers um 1 verringert
		serverTimer = new Timer(1000, new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				serverTimerValue--;
				if (serverTimerValue == 0)
				{
					// Wenn der Timer nicht dur ServerEvents neu gestartet wird,
					// wird ein Absturz angenommen und folgender Code
					// ausgefuehrt
					System.err.println(
							"Server vermutlich abgestuerzt. Bitte Programm erneut starten.");
					fireServerConnectionErrorEvent();
					serverTimer.stop();
					firstMessage = true;
				}
			}
		});

		// Das Pusher-Objekt wird mit dem App-Key des Testaccounts initialisiert
		PusherOptions options = new PusherOptions();

		options.setAuthorizer(new Authorizer()
		{

			/**
			 * Nutzt die Client API von Pusher um eine Authentifizierung zu
			 * ermoeglichen.
			 * 
			 * Es wird ein Hash ueber die Socket-ID des Clients und den Namen
			 * des Channels gebildet, dieser wird zusammen mit dem gehashten
			 * App-Secret des Clients uebertragen.
			 * 
			 * Die Daten werden vom Server mit der Agents.json-Datei verglichen
			 * um die Authentifizierung zu ermoeglichen.
			 */
			public String authorize(String channel, String socketID)
					throws AuthorizationFailureException
			{
				com.pusher.rest.Pusher pusher = new com.pusher.rest.Pusher(
						MyAppID, MyAppKey, MyAppSecret);

				String signature = pusher.authenticate(socketID, channel);
				System.out.println(signature);
				return signature;
			}
		});

		com.pusher.client.Pusher pusher = new com.pusher.client.Pusher(MyAppKey,
				options);

		/**
		 * Das Pusher Client-Objekt wird mit dem Server verbunden.
		 * 
		 * Die Veraenderung des Verbindungsstatus wird in der Konsole
		 * ausgegeben.
		 */
		pusher.connect(new ConnectionEventListener()
		{
			@Override
			public void onConnectionStateChange(ConnectionStateChange change)
			{
				System.out.println("Der Verbindungsstatus hat sich von "
						+ change.getPreviousState() + " zu "
						+ change.getCurrentState() + " geaendert.");
			}

			@Override
			public void onError(String message, String code, Exception e)
			{
				fireErrorEvent();
				System.out
						.println("Es gab ein Problem beim Verbindungsaufbau.");
				System.out.println(message);
				System.out.println("Code:" + code);
				System.out.println("Exception: " + e);

			}
		}, ConnectionState.ALL);

		verbindungsaufbau_Kanal(pusher);

	}

	/**
	 * Das Pusher-Objekt baut eine Verbindung zu dem privaten
	 * Kommunikationskanal auf.
	 * 
	 * In dieser Methode wird sowohl der Name des Channels, als auch der
	 * Eventtyp benoetigt, auf den reagiert werden soll.
	 * 
	 * @param pusher
	 */
	public static void verbindungsaufbau_Kanal(Pusher pusher)
	{

		// Der Pusher wartet auf dem vorgegebenen Channel
		PrivateChannel channel = pusher.subscribePrivate(ChannelName);

		// Auf das "MoveToAgent"-Event wird reagiert, indem die empfangenen
		// Daten in der Konsole ausgegeben werden
		channel.bind("MoveToAgent", new PrivateChannelEventListener()
		{
			public void onEvent(String channel1, String event, String data)
			{
				// Zug des Gegners aus Nachricht von Server erhalten
				System.out.println("Empfangene Daten: " + data);

				int zug = getGegnerzug(data);
				System.err.println("Gegner spielte in Spalte: " + zug);

				if (zug != -1)
				{
					// Zug des Gegners wird in Logik uebertragen
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

					// Spielstein wird in der GUI eingeworfen
					fireZugEvent(zug, gegnerKennung);
					System.out.println(
							"In der GUI sollte angezeigt werden: Wurf in Spalte "
									+ zug + " von Spieler " + gegnerKennung);

					// Aktion des Servers wird registriert
					incrementalVal++;
					if (firstMessage == true)
					{
						serverTimer.start();
						firstMessage = false;
					}
					serverstatus.setValueToWatch("changed" + incrementalVal);

				}

				if (data.contains("true"))
				{
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
							System.out.println(
									"Wir haben in Spalte gelegt " + move);
							fallbackGame.setCurrentPlayer(1);
						}

						// Sieg?
						if (GameLogic.evaluate(
								fallbackGame) == (int) Double.POSITIVE_INFINITY)
						{
							fallbackGame.runInConsole();
							System.out
									.println(GameLogic.evaluate(fallbackGame));
							System.out.println(" Wir haben gewonnen!");
						}
						;

						fallbackGame.runInConsole();

						System.out.println(
								"_____________________________________");
						System.out.println();

						System.out.println();

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
							// (int) Math.random()*7;
							// int move = (int) (Math.random()*7);
						}
					}
					// der von der Logik berechnete Move wird an den Pusher
					// uebertragen
					channel.trigger("client-event",
							"{\"move\": \"" + move + "\"}");
					System.out.println("Der von uns berechnete Zug: " + move
							+ " wird ueber den Pusher verschickt.");
					/**
					 * Speichern in die Datenbank
					 */
					int y = fallbackGame.validPosition(move);
					int yO = fallbackGame.validPosition(zug);
					game.getDb().saveTurn(game.getMatchID(),
							game.getPlayer(1).getName(), zug, yO - 1);
					game.getDb().saveTurn(game.getMatchID(),
							game.getPlayer(0).getName(), move, y - 1);

					// der Spielstein wird in der GUI eingeworfen
					fireZugEvent(move, spielerKennung);

				}

				// Wird aufgerufen wenn Spieler X gewinnt
				if (data.contains("false") && data.contains("Spieler X"))
				{
					if (fallbackActive)
						fallbackGame.emptyField();
					// Konsolenausgabe
					System.err.println("******************** \n"
							+ "S P I E L   B E E N D E T\n"
							+ "********************");
					System.out.println("");

					// Uebergabe des Gewinners an die Logik
					if (data.contains("-1"))
					{
						game.getCurrentMatch()
								.setMatchWinner(game.getPlayer(0));
					} else
					{
						game.getCurrentMatch()
								.setMatchWinner(game.getPlayer(1));
						int yO = fallbackGame.validPosition(zug);
						game.getDb().saveTurn(game.getMatchID(),
								game.getPlayer(1).getName(), zug, yO - 1);
					}
					System.out.println("MatchID bevor Save:" + game.getDb().getMatchID());
					game.getDb().saveMatchScore(game.getDb().getMatchID(), game.getCurrentMatch().getScore());
			
					;

					// Vor Ausgabe der Nachricht wird gewartet, damit Spielstein
					// eingeworfen werden kann
					// try {
					// Thread.sleep(1000);
					// } catch (Exception e ){ e.getMessage();};

					fireGewinnerEvent('x', game.getCurrentMatch().getScore());
					System.out.println("Sieger des Spiels ist Spieler X!");
					serverTimer.stop();
					firstMessage = true;

					// Wird aufgerufen wenn Spieler O gewinnt
				} else if (data.contains("false") && data.contains("Spieler O"))
				{
					if (fallbackActive)
						fallbackGame.emptyField();
					// Konsolenausgabe
					System.err.println("******************** \n"
							+ "S P I E L   B E E N D E T\n"
							+ "********************");
					System.out.println("");

					// Uebergabe des Gewinners an die Logik
					if (data.contains("-1"))
					{
						game.getCurrentMatch()
								.setMatchWinner(game.getPlayer(0));
					} else
					{
						game.getCurrentMatch()
								.setMatchWinner(game.getPlayer(1));
						int yO = fallbackGame.validPosition(zug);
						game.getDb().saveTurn(game.getMatchID(),
								game.getPlayer(1).getName(), zug, yO - 1);
					}
					System.out.println("MatchID bevor Save:" + game.getDb().getMatchID());
					game.getDb().saveMatchScore(game.getDb().getMatchID(),
							game.getCurrentMatch().getScore());
					;

					// Vor Ausgabe der Nachricht wird gewartet, damit Spielstein
					// eingeworfen werden kann
					// try {
					// Thread.sleep(1000);
					// } catch (Exception e ){ e.getMessage();};
					//
					fireGewinnerEvent('o', game.getCurrentMatch().getScore());
					System.out.println("Sieger des Spiels ist Spieler O!");
					serverTimer.stop();
					firstMessage = true;
				}

			}

			@Override
			public void onSubscriptionSucceeded(String channel)
			{
				System.out.println(
						"Client wurde im Channel private-channel angemeldet");
			}

			@Override
			public void onAuthenticationFailure(String msg, Exception e)
			{
				System.out.println(
						"Client konnte nicht im Channel private-channel angemeldet werden");
				System.out.println("Grund: " + msg);
				System.out.println("Exception: " + e);
			}
		});

	}

	/**
	 * Aus dem von Pusher empfangenen Event-String wird der Zug des Gegners als
	 * Integer erfasst.
	 * 
	 * @param data
	 * @return zug
	 */
	public static int getGegnerzug(String data)
	{
		int zug;
		int stelle = ordinalIndexOf(data, "#", 1);

		try
		{
			zug = Integer.parseInt(String.valueOf(data.charAt(stelle + 2)));
		} catch (Exception e)
		{
			// Wenn an dieser Stelle keine Zahl steht, ist entweder noch kein
			// Stein gesetzt oder das Spiel vorbei
			zug = -1;
		}

		return zug;
	}

	/**
	 * Ein in str uebergebener String wird auf das n-te vorkommen vom String s
	 * ueberprueft, es wird die Position des Strings als Integer zurueckgegeben
	 * 
	 * @param str
	 * @param s
	 * @param n
	 * @return pos
	 */
	public static int ordinalIndexOf(String str, String s, int n)
	{
		int pos = str.indexOf(s, 0);
		while (n-- > 0 && pos != -1)
			pos = str.indexOf(s, pos + 1);
		return pos;
	}

	/**
	 * Ermoeglicht das hinzufuegen von Klassen als Listenern fuer ZugEvents, die
	 * das Zuglistener Interface implementieren
	 * 
	 * @param toAdd
	 */
	public void addListener(ZugListener toAdd)
	{
		listeners.add(toAdd);
	}

	/**
	 * Triggert das ZugEvent und sendet es an alle Listener
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
	 * Ermoeglicht das hinzufuegen von Klassen als Listenern fuer
	 * ConnectionErrorEvents, die das ConnectionErrorListener Interface
	 * implementieren
	 * 
	 * @param toAdd
	 */
	public void addErrorListener(ConnectionErrorListener toAdd)
	{
		errorListeners.add(toAdd);
	}

	/**
	 * Triggert das ConnectionErrorEvent und sendet es an alle Listener
	 */
	public static void fireErrorEvent()
	{
		for (ConnectionErrorListener cel : errorListeners)
		{
			cel.onConnectionError();
		}
	}

	/**
	 * Ermoeglicht das hinzufuegen von Klassen als Listenern fuer
	 * GewinnerEvents, die das GewinnerListener Interface implementieren
	 * 
	 * @param toAdd
	 */
	public void addGewinnerListener(GewinnerListener toAdd)
	{
		gewinnerListeners.add(toAdd);
	}

	/**
	 * Triggert das GewinnerEvent und sendet es an alle Listener
	 */
	public static void fireGewinnerEvent(char sieger, String Spielstand)
	{
		for (GewinnerListener gwl : gewinnerListeners)
		{
			gwl.siegerAnzeigen(sieger);
			gwl.setSpielstand(Spielstand);
		}
	}

	/**
	 * Wenn der beobachtete Wert sich aendert (bei ServerEvents ueber den Pusher
	 * Channel) wird der Timer zurueckgesetzt
	 */
	@Override
	public void update(Observable serverstatus, Object arg)
	{
		serverTimerValue = 30;
		// System.out.println(arg);
	}

}
