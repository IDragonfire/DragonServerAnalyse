package com.github.idragonfire.dragonserveranalyser;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.idragonfire.dragonserveranalyser.command.CmdEvents;
import com.github.idragonfire.dragonserveranalyser.command.CmdListeners;
import com.github.idragonfire.dragonserveranalyser.command.CmdRedstone;
import com.github.idragonfire.dragonserveranalyser.command.CommandHandler;

public class DAS_Plugin extends JavaPlugin {
	private CommandHandler cmds;

	@Override
	public void onEnable() {
		// Bukkit.getPluginManager().registerEvents(new HopperAction(this),
		// this);
		// Bukkit.getPluginManager().registerEvents(new PistonAction(this),
		// this);
		this.initCommands();
	}

	private void initCommands() {
		cmds = new CommandHandler();
		cmds.add(new CmdEvents(this));
		cmds.add(new CmdRedstone(this));
		cmds.add(new CmdListeners(this));
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		return cmds.onCommand(sender, command, label, args);
	}
}
