package com.github.idragonfire.dragonserveranalyser.analyser;

import java.lang.reflect.Field;

import org.bukkit.command.CommandSender;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.EventExecutor;
import org.bukkit.plugin.RegisteredListener;

public class ListenerAnalyse {
	public void command(CommandSender sender) {
		sender.sendMessage("Analyse Listeners: ");
		String eventName = null;
		for (HandlerList handler : HandlerList.getHandlerLists()) {
			for (RegisteredListener rListener : handler
					.getRegisteredListeners()) {
				eventName = findEvent(rListener);
				sender.sendMessage(rListener.getPlugin().getName() + ":"
						+ eventName + ":" + rListener.getPriority());
			}
		}
		sender.sendMessage("---------------------");
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
