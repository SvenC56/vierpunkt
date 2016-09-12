

import java.net.*;
import java.io.*;

public class FileInterface {
public static final int serverPort = 7;
	
public static void main (String[] args) {

	// declarations
	String hostname = "localhost";
	PrintWriter networkOut = null;
	BufferedReader networkIn = null;
	Socket s = null;
	String data = null;
	String input = null;
	
	try {
		// set up connection
		s = new Socket(hostname, serverPort);
		// read incoming streams
		networkIn = new BufferedReader
				(new InputStreamReader(s.getInputStream()));
		// read outgoing streams
		networkOut = new PrintWriter
				(s.getOutputStream());
		
		System.out.println("Connected to server");
		
		while (true) {
			// send data to server
			networkOut.println(data);
			// flush outgoing stream
			networkOut.flush();
			// read incoming stream
			input = networkIn.readLine();
		}
		
		
	} catch (Exception e) {
		e.printStackTrace();
	}
	finally {
		try {
			// close both streams and server
			if (networkIn != null)
				networkIn.close();
			if (networkOut != null)
				networkOut.close();
			if (s != null)
				s.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
}
