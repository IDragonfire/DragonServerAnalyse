package com.github.idragonfire.dragonserveranalyser;

import java.io.IOException;
import java.util.logging.Level;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.idragonfire.dragonserveranalyser.command.CmdEvents;
import com.github.idragonfire.dragonserveranalyser.command.CmdHopper;
import com.github.idragonfire.dragonserveranalyser.command.CmdListeners;
import com.github.idragonfire.dragonserveranalyser.command.CmdPiston;
import com.github.idragonfire.dragonserveranalyser.command.CmdRedstone;
import com.github.idragonfire.dragonserveranalyser.command.CommandHandler;
import com.github.idragonfire.dragonserveranalyser.utils.Metrics;
import com.github.idragonfire.dragonserveranalyser.utils.Updater;

public class DAS_Plugin extends JavaPlugin {
	private CommandHandler cmds;
	private DAS_Config config;

	@Override
	public void onEnable() {
		loadConfig();
		this.initCommands();
		enableAutoUpdate();
		enableMetrics();
	}

	private void initCommands() {
		cmds = new CommandHandler();
		cmds.add(new CmdEvents(this));
		cmds.add(new CmdRedstone(this));
		cmds.add(new CmdListeners(this));
		cmds.add(new CmdHopper(this));
		cmds.add(new CmdPiston(this));
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		return cmds.onCommand(sender, command, label, args);
	}

	@Override
	public void saveConfig() {
		config.save();
	}

	public void loadConfig() {
		config = new DAS_Config(this);
		config.load();
		config.save();
	}

	protected void enableAutoUpdate() {
		try {
			String updateMode = this.config.plugin_autoupdate;
			if (updateMode.equals(this.config.plugin_autoupdate_off)) {
				return;
			}
			Updater.UpdateType updateType = Updater.UpdateType.NO_DOWNLOAD;
			if (updateMode.equals(this.config.plugin_autoupdate_on)) {
				updateType = Updater.UpdateType.DEFAULT;
			}
			Updater updater = new Updater(this, 82654, this.getFile(),
					updateType, false);
			Updater.UpdateResult result = updater.getResult();
			switch (result) {
			case UPDATE_AVAILABLE:
				getLogger().log(Level.INFO, "#########################");
				getLogger().log(Level.INFO, "New version available: ");
				getLogger().log(Level.INFO, updater.getLatestName());
				getLogger().log(Level.INFO,
						"for: " + updater.getLatestGameVersion());
				getLogger().log(Level.INFO,
						"Your version: " + getDescription().getVersion());
				getLogger().log(Level.INFO, "#########################");
				break;
			case SUCCESS:
				getLogger().log(
						Level.INFO,
						"downloaded successfull " + updater.getLatestName()
								+ ". Updating plugin at next server restart!");
				break;
			default:
				getLogger().log(Level.WARNING, " Updater has problems");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void enableMetrics() {
		try {
			Metrics metrics = new Metrics(this);
			metrics.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
