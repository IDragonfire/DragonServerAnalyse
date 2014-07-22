package com.github.idragonfire.dragonserveranalyser.analyser;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;

public class TeleportAnalyser {
	protected List<Location> teleList = new ArrayList<Location>();
	protected int distance = 6;

	public Location getLocation(int tpidx) {
		if (tpidx > teleList.size()) {
			return null;
		}
		return teleList.get(tpidx);
	}

	protected boolean connected(Location a) {
		for (Location b : teleList) {
			// bukkit cannot calculate distance between different world
			// locations
			if (a.getWorld() != b.getWorld()) {
				continue;
			}
			if (Math.abs(a.distance(b)) < this.distance) {
				return true;
			}
		}
		return false;
	}
}
