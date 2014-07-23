package com.github.idragonfire.dragonserveranalyser.analyser;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.bukkit.plugin.Plugin;

public abstract class Analyser {
	protected Plugin plugin;

	private FileWriter writer;
	private String name;

	public Analyser(Plugin plugin, String analyseName) {
		this.plugin = plugin;
		name = analyseName;
	}

	public void init() {
		try {
			writer = new FileWriter(plugin.getDataFolder() + File.separator
					+ name + ".log");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void write(String message) {
		try {
			writer.append(message + "\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void close() {
		try {
			this.writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
