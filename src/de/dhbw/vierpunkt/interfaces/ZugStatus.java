package de.dhbw.vierpunkt.interfaces;

import java.util.Observable;

/**
 * In der Klasse SatzStatus wird ein zu beobachtender Wert festgelegt. Bei einer Aenderung werden die Beobachtenden Interfaces informiert.
 * Bei jedem Errechnen eines Zugs Server wird der zu beobachtende Wert geaendert, dabei wird ein Timer in der Interface-Klasse zurueckgesetzt.
 * Ein Ausbleiben einer Aenderung innerhalb des definierten Zeitraums wird mit einem unerwarteten Beenden der KI gleichgesetzt.
 * 
 * @author Leon
 *
 */
public class ZugStatus extends Observable
{	
	/**
	 * Dummy Wert, soll zur Ueberwachung des Satzstatus dienen.
	 */
	private int ZugZeit;
	
	//Konstruktor
	public ZugStatus(int Zugzeit){
		this.ZugZeit = Zugzeit;
	}
	// Getter / Setter
	public int getValueToWatch()
	{return ZugZeit;
	}
	
	public void setValueToWatch(int ZugZeit)
	{
		if (this.ZugZeit != ZugZeit){
		this.ZugZeit = ZugZeit;
		
		// Wert wird als geaendert markiert
		this.setChanged();
		// Beobachter werden benachrichtigt
		notifyObservers(ZugZeit);
		}
	}
}
