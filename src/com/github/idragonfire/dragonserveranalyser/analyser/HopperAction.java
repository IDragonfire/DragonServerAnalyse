package com.github.idragonfire.dragonserveranalyser.analyser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.BlockState;
import org.bukkit.block.DoubleChest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.plugin.Plugin;

public class HopperAction implements Listener {
	private HashMap<Location, Counter> counterMap;
	private List<Location> allClocks;
	private Plugin plugin;
	private int distance = 6;

	public HopperAction(Plugin plugin) {
		counterMap = new HashMap<Location, Counter>();
		allClocks = new ArrayList<Location>();
		this.plugin = plugin;
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void redstoneTick(InventoryMoveItemEvent event) {
		InventoryHolder sourceHolder = event.getSource().getHolder();

		Location loc = null;

		if (sourceHolder instanceof BlockState) {
			BlockState blockState = (BlockState) sourceHolder;
			loc = blockState.getLocation();
		} else if (sourceHolder instanceof DoubleChest) {
			DoubleChest chest = (DoubleChest) sourceHolder;
			loc = chest.getLocation();
		}

		if (loc == null) {
			Bukkit.broadcastMessage("no InventoryMoveItemEvent Handler for"
					+ sourceHolder);
			return;
		}
		// try catch to improve performance
		try {
			this.counterMap.get(loc).increase();
		} catch (NullPointerException e) {
			this.counterMap.put(loc, new Counter(loc));
		}
	}

	public void analyse() {
		allClocks.clear();
		HandlerList.unregisterAll(this);
		List<Counter> list = new ArrayList<Counter>(this.counterMap.values());
		Collections.sort(list);
		Counter c = null;
		for (int i = 0; i < list.size(); i++) {
			c = list.get(i);
			if (connected(c.getLocation())) {
				continue;
			}
			this.allClocks.add(c.getLocation());
			Bukkit.broadcastMessage(c.getCount() + ":"
					+ c.getLocation().toString());
		}
		counterMap.clear();
		Bukkit.getPluginManager().registerEvents(this, this.plugin);
		Player p = (Player) Bukkit.getOnlinePlayers().toArray()[0];
		p.teleport(this.allClocks.get(0));
	}

	public boolean connected(Location a) {
		for (Location b : allClocks) {
			if (Math.abs(a.distance(b)) < this.distance) {
				return true;
			}
		}
		return false;
	}

	public boolean connected(List<Counter> list, int position) {
		Location c = list.get(position).getLocation();
		for (int i = 0; i < position; i++) {
			if (list.get(i).getLocation().distance(c) < this.distance) {
				return true;
			}
		}
		return false;
	}

	@EventHandler
	public void command(PlayerCommandPreprocessEvent event) {
		if (event.getMessage().contains("das hopper")) {
			this.analyse();
		}
	}

	public class Counter implements Comparable<Counter> {
		private AtomicInteger count;
		private Location loc;

		public Counter(Location b) {
			this.loc = b;
			count = new AtomicInteger(1);
		}

		public void increase() {
			this.count.incrementAndGet();
		}

		public int getCount() {
			return this.count.get();
		}

		public Location getLocation() {
			return loc;
		}

		@Override
		public int compareTo(Counter o) {
			return o.getCount() - count.get();
		}
	}
}
