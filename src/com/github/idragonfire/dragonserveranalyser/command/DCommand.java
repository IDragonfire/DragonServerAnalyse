package com.github.idragonfire.dragonserveranalyser.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

public abstract class DCommand {
	protected Plugin plugin;

	public DCommand(Plugin plugin) {
		this.plugin = plugin;
	}

	public abstract void onCommand(CommandSender sender, Command command,
			String cmdName, String label, String[] args);
}
