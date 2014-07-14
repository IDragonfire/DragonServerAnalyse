package com.github.idragonfire.dragonserveranalyser.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

import com.github.idragonfire.dragonserveranalyser.analyser.EventAnalyse;

public class CmdEvents extends DCommand {
	private EventAnalyse listener;
	private boolean active;

	public CmdEvents(Plugin plugin) {
		super(plugin);
		this.listener = new EventAnalyse(plugin);
		this.active = false;
	}

	@Override
	synchronized public void onCommand(CommandSender sender, Command command,
			String cmdName, String label, String[] args) {
		if (active) {
			this.listener.end(sender);
		} else {
			this.listener.start(sender);
		}
		active = !active;
	}
}
