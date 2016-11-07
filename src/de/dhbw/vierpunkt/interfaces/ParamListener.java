package de.dhbw.vierpunkt.interfaces;

/**
 * Klassen, die das ParamListener-Interface implementieren, koennen die in den Einstellungen eingebenen Parameter
 * @param Schnittstelle, Zugzeit, Kontaktpfad, spielerKennung
 * empangen, wenn sie die Methode startParameterAuswerten implementieren.
 * 
 */
public interface ParamListener
{
	void startParameterAuswerten(int Zugzeit, String Schnittstelle, String Kontaktpfad, char spielerKennung,
								 String AppID, String AppKey, String AppSecret);
}
