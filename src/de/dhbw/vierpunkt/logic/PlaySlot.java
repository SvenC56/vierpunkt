package de.dhbw.vierpunkt.logic;
/**
 * Die Klasse PlaySlot stellt eine Position im Spielfeld dar
 * @author tobias jung
 *
 */
public class PlaySlot {
/**
 * Die Position im Spielfeld wird von einem Spieler belegt
 */
	private Player ownedBy = null;
	
	 PlaySlot(Player player) {
		this.ownedBy = player;
	}

	Player getOwnedBy() {

		return ownedBy;
	}

	void setOwnedBy(Player ownedBy) {
		this.ownedBy = ownedBy;
	}
}
