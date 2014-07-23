package com.github.idragonfire.dragonserveranalyser.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

import com.github.idragonfire.dragonserveranalyser.analyser.ListenerAnalyse;

public class CmdListeners extends DCommand {
	public ListenerAnalyse analyser;

	public CmdListeners(Plugin plugin) {
		super(plugin);
		this.analyser = new ListenerAnalyse(plugin);
	}

	@Override
	public void onCommand(CommandSender sender, Command command,
			String cmdName, String label, String[] args) {
		analyser.command(sender);
	}

}
