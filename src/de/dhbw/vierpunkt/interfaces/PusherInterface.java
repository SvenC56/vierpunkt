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



public class PusherInterface implements Runnable, Observer
{
	/**
	 * App-ID der Pusher Instanz des Clients
	 */
	private static String MyAppID = "";
	
	/**
	 * App-Key der Pusher-Instanz des Clients
	 */
	private static String MyAppKey = "";
	
	/**
	 * App-Secret der Pusher-Instanz des Clients
	 */
	private static String MyAppSecret = "";
	
	/**
	 * Channel-Name des Kommunikationskanals
	 */
	private static String ChannelName = "private-channel";
	
	/**
	 * Arrays mit Listenern die auf ZugEvents, ConnectionErrors und GewinnerEvents hoeren
	 */
	private static List<ZugListener> listeners = new ArrayList<ZugListener>();
	private static List<ConnectionErrorListener> errorListeners = new ArrayList<ConnectionErrorListener>();
	private static List<GewinnerListener> gewinnerListeners = new ArrayList<GewinnerListener>();
	
	/**
	 * Der zum Status des Servers zu beobachtende Wert
	 * @see ServerStatus
	 */
	private static ServerStatus serverstatus = new ServerStatus("unchanged");

	
	/**
	 * Timer, der die Beobachtung des Serverstatus unterstuetzt
	 */
	private static Timer serverTimer;
	
	
	/**
	 * Die Zeit, die dem Agenten fuer seinen Zug bleibt
	 */
	public static int zugZeit = 1000;
	
	
	/**
	 * Countdown, bei dessen Ablauf ein Beenden des Servers angenommen wird
	 */
	private static int serverTimerValue = 30;
	private static int incrementalVal = 0;
	

	
	/**
	 * Beim Start des Spiels wird festgelegt, ob der Spieler X oder O ist
	 */
	public static char spielerKennung = 'x';
	public static char gegnerKennung = 'o';
	private static Game game;
	
	// bei der ersten Nachricht vom Server wird der Timer gestartet
	private static boolean firstMessage = true;
	
	// Konstruktoren
	public PusherInterface(){
	}
	
	public PusherInterface(int zugZeit, String AppID, String AppKey, String AppSecret, char spielerKennung, Game game){
		this.zugZeit = zugZeit;
		this.MyAppID = AppID;
		this.MyAppKey = AppKey;
		this.MyAppSecret = AppSecret;
		this.spielerKennung = spielerKennung;
		this.game = game;
		
		if (spielerKennung == 'x'){
				this.gegnerKennung = 'o';
		} else {
			
			this.gegnerKennung = 'x';
		}
	}

	
	public void run(){
		
		// Die Pusher Klasse beobachtet den ServerStatus
		serverstatus.addObserver(this);
		// Der Timer wird initialisiert, alle 1000 Millisekunden wird der Wert des Timers um 1 verringert
		serverTimer = new Timer(1000, new ActionListener(){
			public void actionPerformed(ActionEvent e){
				serverTimerValue--;
				if (serverTimerValue == 0){
					// Wenn der Timer nicht dur ServerEvents neu gestartet wird, wird ein Absturz angenommen und folgender Code ausgefuehrt
					System.err.println("Server vermutlich abgestuerzt. Bitte Programm erneut starten.");
					fireServerConnectionErrorEvent();
					serverTimer.stop();
					firstMessage = true;
					}
				}
			});
		
		
		// Das Pusher-Objekt wird mit dem App-Key des Testaccounts initialisiert
		PusherOptions options = new PusherOptions();

		options.setAuthorizer(new Authorizer() {
			
			/**
			 * Nutzt die Client API von Pusher um eine Authentifizierung zu ermoeglichen.
			 * 
			 * Es wird ein Hash ueber die Socket-ID des Clients und den Namen des Channels gebildet,
			 * dieser wird zusammen mit dem gehashten App-Secret des Clients uebertragen.
			 * 
			 * Die Daten werden vom Server mit der Agents.json-Datei verglichen um die Authentifizierung zu ermoeglichen.
			 */
			public String authorize(String channel, String socketID)throws AuthorizationFailureException
			{
				com.pusher.rest.Pusher pusher = new com.pusher.rest.Pusher(MyAppID, MyAppKey, MyAppSecret);
				
				String signature = pusher.authenticate(socketID, channel);
				System.out.println(signature);
				return signature;
			}});

		com.pusher.client.Pusher pusher = new com.pusher.client.Pusher (MyAppKey, options);
		
		/**
		 * Das Pusher Client-Objekt wird mit dem Server verbunden.
		 * 
		 * Die Veraenderung des Verbindungsstatus wird in der Konsole ausgegeben.
		 */
		pusher.connect(new ConnectionEventListener() {
		    @Override
		    public void onConnectionStateChange(ConnectionStateChange change) {
		        System.out.println("Der Verbindungsstatus hat sich von " + change.getPreviousState() +
		                           " zu " + change.getCurrentState() + " geaendert.");
		    }

		    @Override
		    public void onError(String message, String code, Exception e) {
		        fireErrorEvent();
		        System.out.println("Es gab ein Problem beim Verbindungsaufbau.");
		        System.out.println(message);
		        System.out.println("Code:" + code);
		        System.out.println("Exception: " + e);
		        
		        
		    }
		}, ConnectionState.ALL);
		
		verbindungsaufbau_Kanal(pusher);


	}

	
	/**
	 * Das Pusher-Objekt baut eine Verbindung zu dem privaten Kommunikationskanal auf.
	 * 
	 * In dieser Methode wird sowohl der Name des Channels, als auch der Eventtyp benoetigt,
	 * auf den reagiert werden soll.
	 * @param pusher
	 */
	public static void verbindungsaufbau_Kanal(Pusher pusher){		
		
		// Der Pusher wartet auf dem vorgegebenen Channel
		PrivateChannel channel = pusher.subscribePrivate(ChannelName);
		
		
		// Auf das "MoveToAgent"-Event wird reagiert, indem die empfangenen Daten in der Konsole ausgegeben werden
		channel.bind("MoveToAgent", new PrivateChannelEventListener() {
		    public void onEvent(String channel1, String event, String data) {
		    	// Zug des Gegners aus Nachricht von Server erhalten
		        System.out.println("Empfangene Daten: " + data);

		        int zug = getGegnerzug(data);
		        System.err.println("Gegner spielte in Spalte: "+ zug);
		        
		        if (zug != -1){
		        // Zug des Gegners wird in Logik uebertragen
		         game.getCurrentMatch().getCurrentTurn().startOpponentTurn(zug);
		       	
		        	
		        	
		        // Spielstein wird in der GUI eingeworfen
		        fireZugEvent(zug, gegnerKennung);
		        System.out.println("In der GUI sollte angezeigt werden: Wurf in Spalte " + zug  + " von Spieler " + gegnerKennung);
		        
		        // Aktion des Servers wird registriert
		        incrementalVal++;
		        if (firstMessage == true){
		        	serverTimer.start();
		        	firstMessage = false;
		        }
		        serverstatus.setValueToWatch("changed" + incrementalVal);

		        }
		        
		        if (data.contains("true")){

		        	// der Move wird von der Logik berechnet
		        	int move = game.getCurrentMatch().getCurrentTurn().startAgentTurn();
		        	
		        	// der von der Logik berechnete Move wird an den Pusher uebertragen
		        	channel.trigger("client-event", "{\"move\": \"" + move + "\"}");
		        	System.out.println("Der von uns berechnete Zug: " + move + " wird ueber den Pusher verschickt.");
		        	
		        	// der Spielstein wird in der GUI eingeworfen
		        	fireZugEvent(move, spielerKennung);

		        }
		        
		        // Wird aufgerufen wenn Spieler X gewinnt
		        if (data.contains("false") && data.contains("Spieler X")){
		        	// Konsolenausgabe
		        	System.err.println("******************** \n" + "S P I E L   B E E N D E T\n" + "********************");
		        	System.out.println("");
		        	
		        	// Uebergabe des Gewinners an die Logik
		        	if (spielerKennung == 'x'){
		        		game.getCurrentMatch().setMatchWinner(game.getCurrentMatch().getCurrentPlayer());
		        	}
		        	
		        	// Vor Ausgabe der Nachricht wird gewartet, damit Spielstein eingeworfen werden kann
		        	try {
		        	Thread.sleep(1000);
		        	} catch (Exception e ){ e.getMessage();};
		        	
		        	fireGewinnerEvent('x');
		        	System.out.println("Sieger des Spiels ist Spieler X!");
		        	serverTimer.stop();
					firstMessage = true;
		        	
		        	// Wird aufgerufen wenn Spieler O gewinnt	
		        } else if (data.contains("false") && data.contains("Spieler O")) {
		        	// Konsolenausgabe
		        	System.err.println("******************** \n" + "S P I E L   B E E N D E T\n" + "********************");
		        	System.out.println("");
		        	
		        	// Vor Ausgabe der Nachricht wird gewartet, damit Spielstein eingeworfen werden kann
		        	try {
			        	Thread.sleep(1000);
			        	} catch (Exception e ){ e.getMessage();};
			        	
		        	fireGewinnerEvent('o');
		        	System.out.println("Sieger des Spiels ist Spieler O!");
		        	serverTimer.stop();
					firstMessage = true;
		        }
		       			        
		    }

			@Override
			public void onSubscriptionSucceeded(String channel)
			{
				System.out.println("Client wurde im Channel private-channel angemeldet");
			}
			
			@Override
			public void onAuthenticationFailure(String msg, Exception e)
			{
				System.out.println("Client konnte nicht im Channel private-channel angemeldet werden");
				System.out.println("Grund: "+ msg);
				System.out.println("Exception: " + e);
			}
		});
		
		
	}
	
	/**
	 * Aus dem von Pusher empfangenen Event-String wird der Zug des Gegners als Integer erfasst.
	 * @param data
	 * @return zug
	 */
	public static int getGegnerzug (String data){
		int zug;
		int stelle = ordinalIndexOf(data, "#", 1);
		
		try{
		zug = Integer.parseInt(String.valueOf(data.charAt(stelle+2)));
		} catch (Exception e) {
			// Wenn an dieser Stelle keine Zahl steht, ist entweder noch kein Stein gesetzt oder das Spiel vorbei
			zug = -1;
			}
		
		return zug;
		}
	
	
	/**
	 * Ein in str uebergebener String wird auf das n-te vorkommen vom String s ueberprueft, es wird die Position des Strings als Integer zurueckgegeben
	 * @param str
	 * @param s
	 * @param n
	 * @return pos
	 */
	public static int ordinalIndexOf(String str, String s, int n) {
	    int pos = str.indexOf(s, 0);
	    while (n-- > 0 && pos != -1)
	        pos = str.indexOf(s, pos+1);
	    return pos;
	}
	
	/**
	 * Ermoeglicht das hinzufuegen von Klassen als Listenern fuer ZugEvents, die das Zuglistener Interface implementieren
	 * @param toAdd
	 */
	public void addListener(ZugListener toAdd){
		listeners.add(toAdd);
	}
	
	/**
	 * Triggert das ZugEvent und sendet es an alle Listener
	 */
	public static void fireZugEvent(int zug, char spieler){
		for (ZugListener zl : listeners){
			zl.zugGespielt(zug, spieler);
		}
	}
	
	/**
	 * Triggert das ServerConnectionError Event und sendet es an alle Listener
	 */
	public static void fireServerConnectionErrorEvent(){
		for(ZugListener zl : listeners){
			zl.onServerConnectionError();
		}
	}

	/**
	 * Ermoeglicht das hinzufuegen von Klassen als Listenern fuer ConnectionErrorEvents, die das ConnectionErrorListener Interface implementieren
	 * @param toAdd
	 */
	public void addErrorListener(ConnectionErrorListener toAdd){
		errorListeners.add(toAdd);
	}
	
	
	/**
	 * Triggert das ConnectionErrorEvent und sendet es an alle Listener
	 */
	public static void fireErrorEvent(){
		for (ConnectionErrorListener cel : errorListeners){
			cel.onConnectionError();
		}
	}
	
	/**
	 * Ermoeglicht das hinzufuegen von Klassen als Listenern fuer GewinnerEvents, die das GewinnerListener Interface implementieren
	 * @param toAdd
	 */
	public void addGewinnerListener(GewinnerListener toAdd){
		gewinnerListeners.add(toAdd);
	}
	
	/**
	 * Triggert das GewinnerEvent und sendet es an alle Listener
	 */
	public static void fireGewinnerEvent(char sieger){
		for (GewinnerListener gwl : gewinnerListeners){
			gwl.siegerAnzeigen(sieger);
		}
	}

	/**
	 * Wenn der beobachtete Wert sich aendert (bei ServerEvents ueber den Pusher Channel) wird der Timer zurueckgesetzt
	 */
	@Override
	public void update(Observable serverstatus, Object arg)
	{
		serverTimerValue = 30;
		//System.out.println(arg);
	}
	
	

}
