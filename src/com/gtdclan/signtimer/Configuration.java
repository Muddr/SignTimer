package com.gtdclan.signtimer;

import java.util.logging.Level;

import org.bukkit.configuration.file.FileConfiguration;

public class Configuration {
	
	private final Main plugin;
	
	FileConfiguration PluginConfig;
	
	public Configuration(Main instance) {
		this.plugin = instance;
	}
	
	public Object getProp(String key) {
		return this.PluginConfig.get(key);
	}
	
	public void LoadConfig() {
		this.plugin.reloadConfig();
		this.plugin.getConfig().options().copyDefaults(true);
		this.plugin.saveConfig();
		this.PluginConfig = this.plugin.getConfig();
		
		this.plugin.broadcast_times = this.PluginConfig.getBoolean("settings.broadcast_times", false);
		this.plugin.include_days = this.PluginConfig.getBoolean("settings.include_days", false);
		this.plugin.include_milli = this.PluginConfig.getBoolean("settings.include_milli", true);
		this.plugin.compact_time = this.PluginConfig.getBoolean("settings.list_type", true);
		this.plugin.top_amount = this.PluginConfig.getInt("settings.top_amount", 10);
		
		this.plugin.compact_time = this.PluginConfig.getBoolean("timer.list_type", true);
		this.plugin.auto_enable = this.PluginConfig.getBoolean("timer.auto_enable", true);
		this.plugin.stop_on_death = this.PluginConfig.getBoolean("timer.stop_on_death", true);
		this.plugin.stop_on_quit = this.PluginConfig.getBoolean("timer.stop_on_quit", true);
		
		this.plugin.broadcast_message = this.PluginConfig.getString("messages.broadcast_message", "%PLAYERNAME% finished in %TIME%");
		this.plugin.player_message = this.PluginConfig.getString("messages.player_message", "Your time was %TIME%");
		this.plugin.default_timer = this.PluginConfig.getString("timer.default_timer", "main");
		
		this.plugin.util.console("Loaded plugin config file.", Level.INFO);
	}
	
	public void setProp(String key, Object value) {
		this.PluginConfig.set(key, value);
		this.plugin.saveConfig();
		
	}
}
