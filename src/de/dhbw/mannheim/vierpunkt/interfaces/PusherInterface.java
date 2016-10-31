package de.dhbw.mannheim.vierpunkt.interfaces;

import javafx.concurrent.Task;

import java.util.ArrayList;
import java.util.List;

import com.pusher.client.AuthorizationFailureException;
import com.pusher.client.Authorizer;
import com.pusher.client.Pusher;
import com.pusher.client.PusherOptions;
import com.pusher.client.channel.Channel;
import com.pusher.client.channel.PrivateChannel;
import com.pusher.client.channel.PrivateChannelEventListener;
import com.pusher.client.connection.ConnectionEventListener;
import com.pusher.client.connection.ConnectionState;
import com.pusher.client.connection.ConnectionStateChange;

import de.dhbw.mannheim.vierpunkt.gui.TestGui;
import de.dhbw.mannheim.vierpunkt.logic.GameLogic;


public class PusherInterface implements Runnable
{
	/**
	 * App-ID der Pusher Instanz des Clients
	 */
	private static String MyAppID = "255967";
	
	/**
	 * App-Key der Pusher-Instanz des Clients
	 */
	private static String MyAppKey = "61783ef3dd40e1b399b2";
	
	/**
	 * App-Secret der Pusher-Instanz des Clients
	 */
	private static String MyAppSecret = "66b722950915220b298c";
	
	/**
	 * Channel-Name des Kommunikationskanals
	 */
	private static String ChannelName = "private-channel";
	
	/**
	 * Ein Array mit Listenern die auf das ZugEvent hoeren
	 */
	private static List<ZugListener> listeners = new ArrayList<ZugListener>();
	
	/**
	 * Die Zeit, die dem Agenten fuer seinen Zug bleibt
	 */
	public static int zugZeit = 1000;
	
	// Konstruktoren
	public PusherInterface(){
		
	}
	
	public PusherInterface(int zugZeit){
		this.zugZeit = zugZeit;
	}

	public void run(){
		
		
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
		
		GameLogic game = new GameLogic();
		
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
		        	
		        	
		        	
		        // Spielstein wird in der GUI eingeworfen
		        fireZugEvent(zug);

		        }
		        
		        if (data.contains("true")){
		        	// der Move wird von der Logik berechnet
		        	//int move = game.playTurn(-1, 2);
		        	
		        	int move = (int) (Math.random()*7);
		        	// der von der Logik berechnete Move wird an den Pusher uebertragen
		        	channel.trigger("client-event", "{\"move\": \"" + move + "\"}");
		        	// der Spielstein wird in der GUI eingeworfen
		        	fireZugEvent(move);
		        }
		        
		        if (data.contains("false")){
		        	System.err.println("******************** \n" + "S P I E L   B E E N D E T\n" + "********************");
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
	
	
	public static void fireZugEvent(int zug){
		for (ZugListener zl : listeners){
			zl.zugGespielt(zug);
		}
	}

}
