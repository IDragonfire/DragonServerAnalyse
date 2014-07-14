package com.github.idragonfire.dragonserveranalyser;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.idragonfire.dragonserveranalyser.analyser.EventAnalyse;
import com.github.idragonfire.dragonserveranalyser.analyser.HopperAction;
import com.github.idragonfire.dragonserveranalyser.analyser.ListenerAnalyse;
import com.github.idragonfire.dragonserveranalyser.analyser.PistonAction;
import com.github.idragonfire.dragonserveranalyser.analyser.RedStoneAction;
import com.github.idragonfire.dragonserveranalyser.command.CmdEvents;
import com.github.idragonfire.dragonserveranalyser.command.CommandHandler;

public class DAS_Plugin extends JavaPlugin {
	private CommandHandler cmds;

	@Override
	public void onEnable() {
		Bukkit.getPluginManager().registerEvents(new ListenerAnalyse(), this);
		Bukkit.getPluginManager()
				.registerEvents(new RedStoneAction(this), this);
		Bukkit.getPluginManager().registerEvents(new HopperAction(this), this);
		Bukkit.getPluginManager().registerEvents(new PistonAction(this), this);
		Bukkit.getPluginManager().registerEvents(new EventAnalyse(this), this);
		Bukkit.getPluginManager().registerEvents(new Listener() {
			@EventHandler
			public void event(BlockPistonExtendEvent event) {
				event.setCancelled(true);
			}
		}, this);
		this.initCommands();
	}

	private void initCommands() {
		cmds = new CommandHandler();
		cmds.add(new CmdEvents(this));
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		return cmds.onCommand(sender, command, label, args);
	}
}
