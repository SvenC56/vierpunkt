package de.dhbw.vierpunkt.interfaces;

/**
 * 
 * Klassen, die das ZugListener-Interface implementieren, koennen die alle Zuege empfangen, die waehrend des Spiels gesendet werden.
 * Es wird ein Integer-Wert fuer die gewaehlte Spalte versendet.
 *
 */
public interface ZugListener{
	
	void zugGespielt(int zug, char spieler);
	void onServerConnectionError();
}
