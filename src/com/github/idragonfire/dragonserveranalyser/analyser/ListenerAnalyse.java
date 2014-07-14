package com.github.idragonfire.dragonserveranalyser.analyser;

import java.lang.reflect.Field;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.EventExecutor;
import org.bukkit.plugin.RegisteredListener;

public class ListenerAnalyse implements Listener {
	@EventHandler
	public void command(PlayerCommandPreprocessEvent event) {
		String eventName = null;
		if (event.getMessage().contains("das listener")) {
			for (HandlerList handler : HandlerList.getHandlerLists()) {
				for (RegisteredListener rListener : handler
						.getRegisteredListeners()) {
					eventName = findEvent(rListener);
					Bukkit.broadcastMessage(rListener.getPlugin().getName()
							+ ":" + eventName + ":" + rListener.getPriority());
				}
			}
		}
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
			e.printStackTrace();
		}
		return "UnknownEvent";
	}
}
