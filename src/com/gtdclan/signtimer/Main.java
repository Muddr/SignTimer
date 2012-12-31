package com.gtdclan.signtimer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
	
	private final Listeners Listener = new Listeners(this);
	public Boolean broadcast_times, include_days, include_milli, compact_time;
	public Commands Commands = new Commands(this);
	public Configuration Config = new Configuration(this);
	public HashMap<String, String> rankList = new LinkedHashMap<String, String>();
	public Logger log;
	public String prefix, broadcast_message, player_message;
	public Times Times = new Times(this);
	public Util Util = new Util(this);
	public UtilDatabase database;
	
	private void initializeDatabase() {
		this.database = new UtilDatabase(this) {
			
			@Override
			protected java.util.List<Class<?>> getDatabaseClasses() {
				List<Class<?>> list = new ArrayList<Class<?>>();
				list.add(DB.class);
				return list;
			};
		};
		this.database.initializeDatabase(
		    (String) this.Config.getProp("DB.driver"),
		    (String) this.Config.getProp("DB.url"),
		    (String) this.Config.getProp("DB.username"),
		    (String) this.Config.getProp("DB.password"),
		    (String) this.Config.getProp("DB.isolation"),
		    (Boolean) this.Config.getProp("DB.logging"),
		    (Boolean) this.Config.getProp("DB.rebuild"));
		
		this.Config.setProp("DB.rebuild", false);
	}
	
	@Override
	public void onDisable() {
		
		this.log.log(Level.INFO, this.getDescription().getName() + " has been disabled.");
	}
	
	@Override
	public void onEnable() {
		this.log = this.getLogger();
		this.prefix = "^gold[^white" + this.getDescription().getName() + "^gold] ^white";
		
		PluginManager PluginManager = this.getServer().getPluginManager();
		PluginManager.registerEvents(this.Listener, this);
		
		this.getCommand("st").setExecutor(this.Commands);
		
		this.Config.LoadConfig();
		this.broadcast_times = this.getConfig().getBoolean("settings.broadcast_times", false);
		this.include_days = this.getConfig().getBoolean("settings.include_days", false);
		this.include_milli = this.getConfig().getBoolean("settings.include_milli", true);
		this.compact_time = this.getConfig().getBoolean("settings.list_type", true);
		this.broadcast_message = this.getConfig().getString("messages.broadcast_message", "%PLAYERNAME% finished in %TIME%");
		this.player_message = this.getConfig().getString("messages.player_message", "Your time was %TIME%");
		
		this.initializeDatabase();
		this.Times.updateRanks();
		try {
			Metrics metrics = new Metrics(this);
			metrics.start();
		}
		catch (IOException e) {
			// Failed to submit the stats :-(
		}
		this.log.log(Level.INFO, this.getDescription().getName() + " has been enabled.");
	}
}
