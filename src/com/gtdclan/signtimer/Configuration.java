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
		this.plugin.Util.console("Loaded plugin config file.", Level.INFO);
	}
	
	public void setProp(String key, Object value) {
		this.PluginConfig.set(key, value);
		this.plugin.saveConfig();
		
	}
}
