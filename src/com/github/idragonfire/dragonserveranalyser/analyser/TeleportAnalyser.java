package com.github.idragonfire.dragonserveranalyser.analyser;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;

public class TeleportAnalyser {
	protected List<Location> teleList = new ArrayList<Location>();

	public Location getLocation(int tpidx) {
		if (tpidx > teleList.size()) {
			return null;
		}
		return teleList.get(tpidx);
	}
}
