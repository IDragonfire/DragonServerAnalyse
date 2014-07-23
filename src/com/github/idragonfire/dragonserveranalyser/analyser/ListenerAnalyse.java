package com.github.idragonfire.dragonserveranalyser.analyser;

import java.lang.reflect.Field;

import org.bukkit.command.CommandSender;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.EventExecutor;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredListener;

public class ListenerAnalyse extends Analyser {
	public ListenerAnalyse(Plugin plugin) {
		super(plugin, "listeners");
	}

	public void command(CommandSender sender) {
		super.init();
		sender.sendMessage("Analyse Listeners: ");
		String eventName = null;
		for (HandlerList handler : HandlerList.getHandlerLists()) {
			for (RegisteredListener rListener : handler
					.getRegisteredListeners()) {
				eventName = findEvent(rListener);
				String msg = rListener.getPlugin().getName() + ":" + eventName
						+ ":" + rListener.getPriority();
				sender.sendMessage(msg);
				super.write(msg);
			}
		}
		sender.sendMessage("---------------------");
		super.close();
	}

	public String findEvent(RegisteredListener rlistener) {
		try {
			Field field_executor = rlistener.getClass().getDeclaredField(
					"executor");
			field_executor.setAccessible(true);
			EventExecutor eventExecutor = (EventExecutor) field_executor
					.get(rlistener);
			Field field_event = eventExecutor.getClass().getDeclaredField(
					"val$eventClass");
			field_event.setAccessible(true);
			Class<?> event_class = (Class<?>) field_event.get(eventExecutor);
			return event_class.getSimpleName();
		} catch (Exception e) {
			// nothing
		}
		return "UnknownEvent";
	}
}
