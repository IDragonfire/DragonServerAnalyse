package com.github.idragonfire.dragonserveranalyser.command;

import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.github.idragonfire.dragonserveranalyser.analyser.PistonAction;

public class CmdPiston extends PlayerCommand {
	private PistonAction listener;
	private boolean active;

	public CmdPiston(Plugin plugin) {
		super(plugin);
		this.listener = new PistonAction(plugin);
		this.active = false;
	}

	@Override
	public void onPlayerCommand(Player sender, Command command, String cmd,
			String label, String[] args) {
		if (args.length > 1) {
			try {
				int index = Integer.parseInt(args[1]) - 1;
				sender.teleport(this.listener.getLocation(index));
				sender.sendMessage("tp");
			} catch (Exception e) {
				sender.sendMessage("no valid location found");
			}
			return;
		}
		if (!active) {
			this.listener.start(sender);
		} else {
			this.listener.end(sender);
		}
		active = !active;
	}
}