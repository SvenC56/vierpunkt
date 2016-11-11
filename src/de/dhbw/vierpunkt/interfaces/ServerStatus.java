package de.dhbw.vierpunkt.interfaces;

import java.util.Observable;

/**
 * In der Klasse ServerStatus wird ein zu beobachtender Wert festgelegt. Bei einer Aenderung werden die Beobachtenden Interfaces informiert.
 * Bei jedem Empfangen einer Nachricht durch den Server wird der zu beobachtende Wert geaendert, dabei wird ein Timer in der Interface-Klasse zurueckgesetzt.
 * Ein Ausbleiben einer Aenderung innerhalb des definierten Zeitraums wird mit einem unerwarteten Beenden des Servers gleichgesetzt.
 * 
 * @author Leon
 *
 */
public class ServerStatus extends Observable
{	
	/**
	 * Dummy Wert, soll zur Ueberwachung des Serverstatus dienen.
	 */
	private String valueToWatch;
	
	//Konstruktor
	public ServerStatus(String valueToWatch){
		this.valueToWatch = valueToWatch;
	}
	// Getter / Setter
	public String getValueToWatch()
	{return valueToWatch;
	}
	public void setValueToWatch(String valueToWatch)
	{
		if (!this.valueToWatch.equals(valueToWatch)){
		this.valueToWatch = valueToWatch;
		
		// Wert wird als geaendert markiert
		this.setChanged();
		// Beobachter werden benachrichtigt
		notifyObservers(valueToWatch);
		}
	}
}
