package com.github.idragonfire.dragonserveranalyser.command;

import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.github.idragonfire.dragonserveranalyser.analyser.CraftBookAnalyser;

public class CmdCraftbook extends PlayerCommand {
	private CraftBookAnalyser cb;

	public CmdCraftbook(Plugin plugin) {
		super(plugin);
		cb = new CraftBookAnalyser(plugin);
	}

	@Override
	public void onPlayerCommand(Player sender, Command command, String cmd,
			String label, String[] args) {
		if (args.length > 1) {
			try {
				int index = Integer.parseInt(args[1]) - 1;
				sender.teleport(cb.getLocation(index));
				sender.sendMessage("tp");
			} catch (Exception e) {
				sender.sendMessage("no valid location found");
			}
			return;
		}
		cb.analyseOnline(sender);
	}

}
