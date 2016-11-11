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
	private static Timer timer;
	
	/**
	 * Countdown, bei dessen Ablauf ein Beenden des Servers angenommen wird
	 */
	private static int timervalue = 30;
	
	private static int incrementalVal = 0;
	
	
	/**
	 * Die Zeit, die dem Agenten fuer seinen Zug bleibt
	 */
	public static int zugZeit = 1000;
	
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
		
		serverstatus.addObserver(this);
		timer = new Timer(1000, new ActionListener(){
			public void actionPerformed(ActionEvent e){
				timervalue--;
				if (timervalue == 0){
					System.err.println("Server vermutlich abgestuerzt. Bitte Programm erneut starten.");
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
		        // game.playTurn(zug, 1);
		         game.getCurrentMatch().getCurrentTurn().startOpponentTurn(zug);
		       	
		        	
		        	
		        // Spielstein wird in der GUI eingeworfen
		        fireZugEvent(zug, gegnerKennung);
		        
		        // Aktion des Servers wird registriert
		        incrementalVal++;
		        if (firstMessage == true){
		        	timer.start();
		        	firstMessage = false;
		        }
		        serverstatus.setValueToWatch("changed" + incrementalVal);

		        }
		        
		        if (data.contains("true")){
		        	// der Move wird von der Logik berechnet
		        	 //int move = (int) (Math.random()*7);
		        	// int move = game.playTurn(-1, 2);
		        	int move = game.getCurrentMatch().getCurrentTurn().startAgentTurn();
		        	// der von der Logik berechnete Move wird an den Pusher uebertragen
		        	channel.trigger("client-event", "{\"move\": \"" + move + "\"}");
		        	// der Spielstein wird in der GUI eingeworfen
		        	fireZugEvent(move, spielerKennung);
		        }
		        
		        // Wird aufgerufen wenn Spieler X gewinnt
		        if (data.contains("false") && data.contains("Spieler X")){
		        	System.err.println("******************** \n" + "S P I E L   B E E N D E T\n" + "********************");
		        	System.out.println("");
		        	if (spielerKennung == 'x'){
		        		game.getCurrentMatch().setMatchWinner(game.getCurrentMatch().getCurrentPlayer());
		        	}
		        	fireGewinnerEvent('x');
		        	System.out.println("Sieger des Spiels ist Spieler X!");
		        	
		        	
		        	// Wird aufgerufen wenn Spieler O gewinnt	
		        } else if (data.contains("false") && data.contains("Spieler O")) {
		        	System.err.println("******************** \n" + "S P I E L   B E E N D E T\n" + "********************");
		        	System.out.println("");
		        	fireGewinnerEvent('o');
		        	System.out.println("Sieger des Spiels ist Spieler O!");
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
	 * @return
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
	
	
	public static int ordinalIndexOf(String str, String s, int n) {
	    int pos = str.indexOf(s, 0);
	    while (n-- > 0 && pos != -1)
	        pos = str.indexOf(s, pos+1);
	    return pos;
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
	
	public void addErrorListener(ConnectionErrorListener toAdd){
		errorListeners.add(toAdd);
	}
	
	public static void fireErrorEvent(){
		for (ConnectionErrorListener cel : errorListeners){
			cel.onConnectionError();
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


	@Override
	public void update(Observable o, Object arg)
	{
		timervalue = 30;
		System.out.println(arg);
	}

}
