
import com.pusher.client.connection.ConnectionEventListener;
import com.pusher.client.connection.ConnectionState;
import com.pusher.client.connection.ConnectionStateChange;
import com.pusher.client.util.HttpAuthorizer;
import com.pusher.client.Pusher;
import com.pusher.client.PusherOptions;
import com.pusher.client.channel.Channel;
import com.pusher.client.channel.SubscriptionEventListener;


public class pushTest
{

	public static void main(String[] args) throws InterruptedException
	{
		// Das Pusher-Objekt wird mit dem App-Key des Testaccounts initialisiert
		PusherOptions options = new PusherOptions();
		options.setCluster("eu");
		Pusher pusher = new Pusher("6deb1d433f80e99f5864", options);
	
		pusher.connect(new ConnectionEventListener() {
		    @Override
		    public void onConnectionStateChange(ConnectionStateChange change) {
		        System.out.println("Der Status hat sich von " + change.getPreviousState() +
		                           " zu " + change.getCurrentState() + " ge�ndert.");
		    }

		    @Override
		    public void onError(String message, String code, Exception e) {
		        System.out.println("Es gab ein Problem beim Verbindungsaufbau.");
		        System.out.println(message);
		    }
		}, ConnectionState.ALL);
	
		verbindungsaufbau_Kanal(pusher);
		
		Thread.sleep(100000);
	}
	
	public static void verbindungsaufbau_Kanal(Pusher pusher){
		// Der Pusher wartet auf dem vorgegebenen Test-Channel
		Channel channel = pusher.subscribe("test-channel");

		// Auf das "my-event" wird reagiert, indem die empfangenen Daten in der Konsole ausgegeben werden
		channel.bind("my-event", new SubscriptionEventListener() {
		    public void onEvent(String channel, String event, String data) {
		        System.out.println("Empfangene Daten: " + data);		    }
		});
	}
}
