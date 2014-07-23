package com.github.idragonfire.dragonserveranalyser.analyser;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class CraftBookAnalyser extends TeleportAnalyser {

	public CraftBookAnalyser(Plugin plugin) {
		super(plugin, "craftbook");
	}

	public void analyseOnline(Player player) {
		super.init();
		player.sendMessage("Analyse all loaded Chunks");
		int tpidx = 1;
		for (World world : Bukkit.getWorlds()) {
			Chunk[] chunks = world.getLoadedChunks();
			for (int i = 0; i < chunks.length; i++) {
				BlockState[] states = chunks[i].getTileEntities();
				for (int j = 0; j < states.length; j++) {
					if (!(states[j] instanceof Sign)) {
						continue;
					}
					Sign sign = (Sign) states[j];
					String[] lines = sign.getLines();
					if (lines.length < 2) {
						continue;
					}
					if (!lines[1].contains("[MC")) {
						continue;
					}
					super.teleList.add(sign.getLocation());
					// add IC arguments
					String args = "";
					if (lines.length > 2) {
						for (int k = 2; k < lines.length; k++) {
							if (lines[k].equals("")) {
								continue;
							}
							if (!args.equals("")) {
								args += ";";
							}
							args += lines[k];
						}
					}
					if (!args.equals("")) {
						args = "(" + args + ")";
					}
					String msg = "#" + tpidx + " " + lines[1] + " " + args;
					player.sendMessage(msg);
					super.write(msg);
					tpidx++;
				}
			}
		}
		player.sendMessage("... analyse finished");
		super.close();
	}
}
