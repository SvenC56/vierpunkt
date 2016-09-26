

import com.pusher.client.connection.ConnectionEventListener;
import com.pusher.client.connection.ConnectionState;
import com.pusher.client.connection.ConnectionStateChange;
import com.pusher.client.AuthorizationFailureException;
import com.pusher.client.Authorizer;
import com.pusher.client.Pusher;
import com.pusher.client.PusherOptions;
import com.pusher.client.channel.Channel;
import com.pusher.client.channel.PrivateChannelEventListener;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Hex;


public class pushTest
{
	private static String MyAppKey = "6deb1d433f80e99f5864";
	private static String MyAppSecret = "2ce3f8c94dc009ca19c8";

	public static void main(String[] args) throws InterruptedException, InstantiationException, IllegalAccessException
	{
		// Das Pusher-Objekt wird mit dem App-Key des Testaccounts initialisiert
		PusherOptions options = new PusherOptions();
		options.setCluster("EU");
		options.setAuthorizer(new Authorizer() {
			
			public String authorize(String channel, String socketID)throws AuthorizationFailureException
			{
				String AuthString = socketID + ":" + channel;
				String hash = "";
				
				try	{
					hash = getHash(MyAppSecret, AuthString);
					} catch (InvalidKeyException | NoSuchAlgorithmException e){
							e.printStackTrace();
					}
				
				String signature = "{\"auth\":\"" + MyAppKey + ":" + hash + "\"}";
				return signature;
			}});
		/**
		 * to do: authorizer objekt den options übergeben
		 * authorize: hash wert aus app key, secrets und channel id bilden
		 * pusher objekt als client-pusher-instanz erzeugen mit dem com.pusher..... befehl+
		 * 
		 */

		com.pusher.client.Pusher pusher = new com.pusher.client.Pusher (MyAppKey, options);
		
		pusher.connect(new ConnectionEventListener() {
		    @Override
		    public void onConnectionStateChange(ConnectionStateChange change) {
		        System.out.println("Der Verbindungsstatus hat sich von " + change.getPreviousState() +
		                           " zu " + change.getCurrentState() + " geändert.");
		    }

		    @Override
		    public void onError(String message, String code, Exception e) {
		        System.out.println("Es gab ein Problem beim Verbindungsaufbau.");
		        System.out.println(message);
		    }
		}, ConnectionState.ALL);
	
		verbindungsaufbau_Kanal(pusher);
		while (true){}
	}
	
	public static void verbindungsaufbau_Kanal(Pusher pusher){
		// Der Pusher wartet auf dem vorgegebenen Channel
		Channel channel = pusher.subscribePrivate("private-channel");

		// Auf das "MoveToAgent"-Event wird reagiert, indem die empfangenen Daten in der Konsole ausgegeben werden
		channel.bind("MoveToAgent", new PrivateChannelEventListener() {
		    public void onEvent(String channel, String event, String data) {
		        System.out.println("Empfangene Daten: " + data);
		    }

			@Override
			public void onSubscriptionSucceeded(String arg0)
			{
				System.out.println("Client wurde im Channel private-channel angemeldet");
			}

			@Override
			public void onAuthenticationFailure(String arg0, Exception arg1)
			{
				System.out.println("Client konnte nicht im Channel private-channel angemeldet werden");
			}
		});
	}
	
	public static String getHash (String MyAppSecret, String AuthString) throws NoSuchAlgorithmException, InvalidKeyException{
		Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
		
		SecretKeySpec secretKey = new SecretKeySpec(MyAppSecret.getBytes(), "HmacSHA256");
		sha256_HMAC.init(secretKey);
		byte[] hash = sha256_HMAC.doFinal(AuthString.getBytes());
		// wenn es nicht funktioniert, Methode zur Konvertierung in lesbare Zeichen einfügen
		String check = Hex.encodeHexString(hash);
		
		return new String(check);
	}
}
