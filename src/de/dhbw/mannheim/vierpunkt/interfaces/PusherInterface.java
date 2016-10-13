package de.dhbw.mannheim.vierpunkt.interfaces;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Hex;

import com.google.gson.Gson;
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

import de.dhbw.mannheim.vierpunkt.logic.*;

public class PusherInterface
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

	public static void main(String[] args) throws InterruptedException, InstantiationException, IllegalAccessException
	{
		// Das Pusher-Objekt wird mit dem App-Key des Testaccounts initialisiert
		PusherOptions options = new PusherOptions();
		//options.setCluster("EU");
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
	
		// Der Pusher wartet auf dem vorgegebenen Channel
				PrivateChannel channel = pusher.subscribePrivate(ChannelName);

				// Auf das "MoveToAgent"-Event wird reagiert, indem die empfangenen Daten in der Konsole ausgegeben werden
				channel.bind("MoveToAgent", new PrivateChannelEventListener() {
				    public void onEvent(String channel1, String event, String data) {
				        System.out.println("Empfangene Daten: " + data);
				        int zug = getGegnerzug(data);
				        GameLogic game = new GameLogic();
				        game.serverTurn(zug);
				        
				        
				        if (data.contains("true")){
				        	// der Move ist im Moment noch eine Zufallszahl zwischen 1 und 6
				        	int move = (int) Math.random() * 6 + 1;
				        	channel.trigger("client-event", "{\"move\": \"" + move + "\"}");
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
		while (true){}
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
		Channel channel = pusher.subscribePrivate(ChannelName);

		// Auf das "MoveToAgent"-Event wird reagiert, indem die empfangenen Daten in der Konsole ausgegeben werden
		channel.bind("MoveToAgent", new PrivateChannelEventListener() {
		    public void onEvent(String channel, String event, String data) {
		        System.out.println("Empfangene Daten: " + data);
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
				System.out.println("Exception " + e);
			}
		});
		
	}
	
	/**
	 * Die zur Authentifizierung noetigen Daten werden in dem von Pusher benoetigten Format aufbereitet.
	 * 
	 * Hierzu werden das App-Secret, sowie der Channel und die Socket-ID in einem definierten Vorgang gehasht.
	 * @param MyAppSecret
	 * @param AuthString
	 * @return Key
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeyException
	 */
	public static String getHash (String MyAppSecret, String AuthString) throws NoSuchAlgorithmException, InvalidKeyException{
		Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
		
		SecretKeySpec secretKey = new SecretKeySpec(MyAppSecret.getBytes(), "HmacSHA256");
		sha256_HMAC.init(secretKey);
		byte[] hash = sha256_HMAC.doFinal(AuthString.getBytes());
		// wenn es nicht funktioniert, Methode zur Konvertierung in lesbare Zeichen einfuegen
		String check = Hex.encodeHexString(hash);
		
		return new String(check);
	}
	
	public static int getGegnerzug (String data){

		int posZug = ordinalIndexOf(data, "#", 1);
		int zug = Integer.parseInt(String.valueOf(data.charAt(posZug + 2)));
		return zug;
	}
	
	public static int ordinalIndexOf(String str, String s, int n) {
	    int pos = str.indexOf(s, 0);
	    while (n-- > 0 && pos != -1)
	        pos = str.indexOf(s, pos+1);
	    return pos;
	}
	
	public static ServerEvent entpacken(String data){
		Gson gson = new Gson();
		ServerEvent se = gson.fromJson(data, ServerEvent.class);
		return se;
	}
	
}

class ServerEvent {
	public static boolean freigabe = true;
	public static String status = "Satz spielen";
	public static int spalte = 0;
	public static String sieger = null;
	
	public static int getGegnerzug(){
		return spalte;
	}

}


