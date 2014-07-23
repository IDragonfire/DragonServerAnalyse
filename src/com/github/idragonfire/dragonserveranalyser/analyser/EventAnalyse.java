package com.github.idragonfire.dragonserveranalyser.analyser;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.EventExecutor;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredListener;

public class EventAnalyse extends Analyser {

	private Set<Class<? extends Event>> activeEvents = new HashSet<Class<? extends Event>>();
	private Listener injectionListener;

	private Map<String, AtomicInteger> counts = new HashMap<String, AtomicInteger>();
	private Map<String, AtomicInteger> cancelled = new HashMap<String, AtomicInteger>();

	public EventAnalyse(Plugin plugin) {
		super(plugin, "events");
		this.injectionListener = new EventListener(this);
	}

	public void start(CommandSender sender) {
		Class<? extends Event> tmpClass = null;
		for (HandlerList handler : HandlerList.getHandlerLists()) {
			for (RegisteredListener rListener : handler
					.getRegisteredListeners()) {
				tmpClass = findEvent(rListener);
				if (tmpClass == null) {
					continue;
				}
				activeEvents.add(tmpClass);
			}
		}
		counts.clear();
		cancelled.clear();
		super.init();
		for (Class<? extends Event> event_class : activeEvents) {
			String msg = "register: " + event_class.getSimpleName();
			sender.sendMessage(msg);
			write(msg);
			Bukkit.getPluginManager().registerEvent(event_class,
					injectionListener, EventPriority.MONITOR,
					new EventExecutor() {

						@Override
						public void execute(Listener listener, Event event)
								throws EventException {
							if (!(listener instanceof EventListener)) {
								return;
							}
							((EventListener) listener).getMaster().globalEvent(
									event);
						}
					}, this.plugin, false);
		}
	}

	public void end(CommandSender sender) {
		sender.sendMessage("Deregister events. Results:");
		HandlerList.unregisterAll(this.injectionListener);
		List<Container> list = new ArrayList<Container>();
		for (String eventName : this.counts.keySet()) {
			list.add(new Container(eventName, this.counts.get(eventName)));
		}
		Collections.sort(list);
		for (int i = 0; i < list.size(); i++) {
			String msg = list.get(i).getKey() + ":"
					+ list.get(i).getValue().get() + " (cancelled: "
					+ this.cancelled.get(list.get(i).getKey()) + ")";
			sender.sendMessage(msg);
			super.write(msg);
		}
		sender.sendMessage("---------------------");
		super.close();
	}

	public void globalEvent(Event event) {
		try {
			counts.get(event.getEventName()).incrementAndGet();
		} catch (Exception e) {
			counts.put(event.getEventName(), new AtomicInteger(1));
		}
		try {
			if (isCancelled(event)) {
				cancelled.get(event.getEventName()).incrementAndGet();
			}
		} catch (Exception e) {
			cancelled.put(event.getEventName(), new AtomicInteger(1));
		}
	}

	public boolean isCancelled(Event event) {
		try {
			Method method_isCancelled = event.getClass().getMethod(
					"isCancelled");
			method_isCancelled.setAccessible(true);
			return (boolean) method_isCancelled.invoke(event);

		} catch (Exception e) {
			// cannot be cancelled
		}
		return false;
	}

	public Class<? extends Event> findEvent(RegisteredListener rlistener) {
		try {
			Field field_executor = rlistener.getClass().getDeclaredField(
					"executor");
			field_executor.setAccessible(true);
			EventExecutor eventExecutor = (EventExecutor) field_executor
					.get(rlistener);
			Field field_event = eventExecutor.getClass().getDeclaredField(
					"val$eventClass");
			field_event.setAccessible(true);
			@SuppressWarnings("unchecked")
			Class<? extends Event> event_class = (Class<? extends Event>) field_event
					.get(eventExecutor);
			return event_class;
		} catch (Exception e) {
			// nothing
		}
		return null;
	}

	public class Container implements Comparable<Container> {
		private String key;
		private AtomicInteger value;

		public Container(String key, AtomicInteger value) {
			super();
			this.key = key;
			this.value = value;
		}

		public String getKey() {
			return key;
		}

		public AtomicInteger getValue() {
			return value;
		}

		@Override
		public int compareTo(Container o) {
			int compare = o.getValue().get() - value.get();
			if (compare == 0)
				return key.compareTo(o.getKey());
			return compare;
		}
	}

	public class EventListener implements Listener {
		public EventAnalyse master;

		public EventListener(EventAnalyse master) {
			super();
			this.master = master;
		}

		public EventAnalyse getMaster() {
			return this.master;
		}
	}
}
