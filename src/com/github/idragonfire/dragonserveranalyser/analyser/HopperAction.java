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
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.plugin.Plugin;

public class HopperAction extends TeleportAnalyser implements Listener {
	private HashMap<Location, Counter> counterMap;
	private Plugin plugin;

	public HopperAction(Plugin plugin) {
		counterMap = new HashMap<Location, Counter>();
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
			plugin.getLogger().warning(
					"no InventoryMoveItemEvent Handler for" + sourceHolder);
			return;
		}
		// try catch to improve performance
		try {
			this.counterMap.get(loc).increase();
		} catch (NullPointerException e) {
			this.counterMap.put(loc, new Counter(loc));
		}
	}

	public void end(Player player) {
		HandlerList.unregisterAll(this);
		analyse(player);
	}

	public void start(Player player) {
		this.counterMap.clear();
		Bukkit.getPluginManager().registerEvents(this, this.plugin);
		player.sendMessage("Server tracks now Hopper actions");
	}

	public void analyse(Player player) {
		super.teleList.clear();
		List<Counter> list = new ArrayList<Counter>(this.counterMap.values());
		Collections.sort(list);
		Counter c = null;
		player.sendMessage("# Hopper activities:");
		int tpidx = 1;
		for (int i = 0; i < list.size(); i++) {
			c = list.get(i);
			if (connected(c.getLocation())) {
				continue;
			}
			super.teleList.add(c.getLocation());
			player.sendMessage(tpidx + ": " + c.getCount());
			tpidx++;
		}
		player.sendMessage("---------------------");
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
