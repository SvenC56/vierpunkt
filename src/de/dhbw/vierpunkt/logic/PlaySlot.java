package de.dhbw.vierpunkt.logic;
/**
 * Die Klasse PlaySlot stellt eine Position im Spielfeld dar
 * @author tobias
 *
 */
public class PlaySlot {
/**
 * Die Position im Spielfeld wird von einem Spieler belegt
 */
	private Player ownedBy = null;
	
	public PlaySlot(Player player) {
		this.ownedBy = player;
	}

	public Player getOwnedBy() {

		return ownedBy;
	}

	public void setOwnedBy(Player ownedBy) {
		this.ownedBy = ownedBy;
	}
}
