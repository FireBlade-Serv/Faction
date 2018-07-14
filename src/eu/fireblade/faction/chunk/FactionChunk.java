package eu.fireblade.faction.chunk;

import org.bukkit.Chunk;
import org.bukkit.Location;

public class FactionChunk {

	private Location loc;
	
	public FactionChunk(Location loc){
		this.loc = loc;
	}
	
	public Chunk getChunk() {
		return this.loc.getChunk();
	}
}
