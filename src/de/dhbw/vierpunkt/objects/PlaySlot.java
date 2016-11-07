package de.dhbw.vierpunkt.objects;

public class PlaySlot {

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
