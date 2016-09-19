/**
 * DRAFTLogik / Zum Reinkommen in die Spiellogik, Versuch: Alpha-Beta
 * Autoren: Gruppe 4 (vier.) - Verantwortlich: Tobias Jung
 **/
public class DraftLogik {
	
	//Allgemeine Regeln / Hinweise Lauterbach
	//Spalte: von links nach rechts (0,1,2,3,4,5,6) => 7 Maximale Breite
	//Zeile: von oben nach unten (2,3,4,5,6,7) => 6 Maximale Tiefe (+1 Kopf?)
		
	/**************************************************************/
	/*******************Variablen, Deklarationen*******************/
	/**************************************************************/
	
	//Spielfeld
	//MAXIMALE ANZAHL SPALTEN
	private int column = 7;
	//Idee: 6 tief (+ Kopfzeile (? Umsetzung mit oder ohne Kopfzeile?))
	//MAXIMALE ANZAHL ZEILEN
	private int row = 6;	
	//Array fuer Feld
	private int [][] field = new int[column][row];
	//Allgemeine Information: x entspricht Spalte / y entspricht Zeile

	/**************************************************************/
	/*******************KONSTRUKTOR********************************/
	/**************************************************************/
	
	public DraftLogik () {
		//Array durchlaufen und mit Nullen fuellen + move auf false setzen, da kein Spieler am Zug ist!
		for (int x = 0; x < column ; x++) {
			for (int y = 0; y < row; y++ ) {
				field [x][y] = 0;
			}
		}
	}
	
	/**************************************************************/
	/*******************ZUGRIFFSMETHODEN***************************/
	/**************************************************************/
	
	//Getter fuer field
	public int [][] getField () {
		return field;
	}
	
	//Setter fuer field
	public void setField (int x, int y, int value) {
		field[x][y] = value;
	}
	
	//Setter fuer validMove
	public void setValidMove (boolean move) {
		validMove = move;
	}

	/**************************************************************/
	/************************LOGIK*********************************/
	/**************************************************************/
	
	/**Prueft, ob Chip eingeworfen werden kann
	 * gibt -1(keine valide Position) oder Zeile zurueck!**/
	private int validPosition (int x) {
		int temp=0;
		//Spalte muss im richtigen Bereich > 0 & kleiner max. Anzahl SPALTEN
		if (x >= 0 && x < column) {
			for (int y = 0; y < row; y++) {
				if (field[x][y]==0) { //leere Position gefunden
					return y; //gibt Zeile zurueck!
				} //kein leeres Feld
				else temp = -1;
			}
		else temp = -1; //Eingabe ausserhalb des Spielbereichs
		}
		if (temp == -1) { //temp nur zurueckgeben, wenn noch keine Zeile returned wurde
			return temp;
		}
	}
	
	
	/**Chip einwerfen
	Ueberlegung: Informationen werden in Array geschrieben! Spieler 1 = 1 --> Spieler 2 = 2**/
	private void setChip (int x, int spieler) {
		int y = validPosition(x);
		if (y != -1 && spieler == 1) {
			field[x][y]=1;
			}
		if (y!= -1 && spieler == 2) {
			field[x][y]=2;
		}
	}
	
	
	/**************************************************************/
	/************************BEWERTUNG*****************************/
	/**************************************************************/
	/**Gibt Anzahl der Chips des gleichen spieler in Reihe zurueck**/
	private int inRow(int x, int spieler) {
		int count=0; //Zaehler der validen Chips && Chips des gleichen Spielers
		if (validPosition(x) == spieler) { 
			int y= validPosition(x); //merken der Zeile, da alle Chips in gleicher Zeile sein muessen, damit diese in Reihe sind
			count++;
			if (validPosition(x+1) == spieler && validPosition(x)==y) {
				count++;
				if (validPosition(x+2) == spieler && validPosition(x)==y) {
					count++
					if (validPosition(x+3) == spieler && validPosition(x)==y) {
						count++;
				}
				
				}
			}
		}
		return count; 
		
	}
	
	
	
}
