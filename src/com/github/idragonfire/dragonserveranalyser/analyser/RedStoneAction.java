package com.github.idragonfire.dragonserveranalyser.analyser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.plugin.Plugin;

public class RedStoneAction extends TeleportAnalyser implements Listener {
	private HashMap<Block, Counter> counterMap;

	public RedStoneAction(Plugin plugin) {
		super(plugin, "redstone");
		counterMap = new HashMap<Block, Counter>();
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void redstoneTick(BlockRedstoneEvent event) {
		Block b = event.getBlock();
		// try catch to improve performance
		try {
			this.counterMap.get(b).increase();
		} catch (NullPointerException e) {
			this.counterMap.put(b, new Counter(b));
		}
	}

	public void end(Player player) {
		HandlerList.unregisterAll(this);
		analyse(player);
	}

	public void start(Player player) {
		this.counterMap.clear();
		Bukkit.getPluginManager().registerEvents(this, this.plugin);
		player.sendMessage("Server tracks now Redstone actions");
	}

	public void analyse(Player player) {
		super.init();
		List<Counter> list = new ArrayList<Counter>(this.counterMap.values());
		Collections.sort(list);
		Counter c = null;
		player.sendMessage("# Redstone activities:");
		int tpidx = 1;
		for (int i = 0; i < list.size(); i++) {
			c = list.get(i);
			if (connected(c.getBlock().getLocation())) {
				continue;
			}
			super.teleList.add(c.getBlock().getLocation());
			String msg = tpidx + ": " + c.getCount();
			player.sendMessage(msg);
			super.write(msg);
			tpidx++;
		}
		player.sendMessage("---------------------");
		super.close();
	}

	public class Counter implements Comparable<Counter> {
		private AtomicInteger count;
		private Block b;

		public Counter(Block b) {
			this.b = b;
			count = new AtomicInteger(1);
		}

		public void increase() {
			this.count.incrementAndGet();
		}

		public int getCount() {
			return this.count.get();
		}

		public Block getBlock() {
			return b;
		}

		@Override
		public int compareTo(Counter o) {
			return o.getCount() - count.get();
		}
	}
}
