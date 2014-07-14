package com.github.idragonfire.dragonserveranalyser;

import org.bukkit.plugin.Plugin;

import com.github.idragonfire.dragonserveranalyser.utils.Comment;
import com.github.idragonfire.dragonserveranalyser.utils.Config;

public class DAS_Config extends Config {

	public final String plugin_autoupdate_on = "on";
	public final String plugin_autoupdate_notify = "notify";
	public final String plugin_autoupdate_off = "off";

	// plugin section
	@Comment("options: " + plugin_autoupdate_on + ',' + plugin_autoupdate_off
			+ "," + plugin_autoupdate_notify)
	public String plugin_autoupdate = plugin_autoupdate_on;

	public DAS_Config(Plugin plugin) {
		setFile(plugin);
	}
}
