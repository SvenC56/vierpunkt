package de.dhbw.vierpunkt.gui;

/**
 * Wenn die Verbindung mit dem Pusher Server nicht aufgebaut werden kann, wird die OnConnectionError Methode aufgerufen.
 * Standardfall: Pusher-Credentials falsch eingegeben
 *
 */
public interface ConnectionErrorListener
{
	public void onConnectionError();
}
