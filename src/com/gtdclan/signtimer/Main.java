package com.gtdclan.signtimer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.gtdclan.signtimer.databases.DB_StartSign;
import com.gtdclan.signtimer.databases.DB_StopSign;
import com.gtdclan.signtimer.databases.DB_Timers;
import com.gtdclan.signtimer.databases.DB_Times;
import com.gtdclan.signtimer.utilities.Util_Database;
import com.gtdclan.signtimer.utilities.Util_Misc;
import com.gtdclan.signtimer.utilities.Util_Signs;
import com.gtdclan.signtimer.utilities.Util_Timers;
import com.gtdclan.signtimer.utilities.Util_Times;

public class Main extends JavaPlugin {
	
	private final Listeners Listener = new Listeners(this);
	public Boolean broadcast_times, include_days, include_milli, compact_time, auto_enable, stop_on_death, stop_on_quit;
	public Boolean upgrade = false;
	public Commands Commands = new Commands(this);
	public Configuration Config = new Configuration(this);
	public HashMap<Integer, HashMap<String, String>> rankList = new HashMap<Integer, HashMap<String, String>>();
	public Integer dbVersion, top_amount;
	public Logger log;
	public String prefix, broadcast_message, player_message, default_timer;
	public Util_Timers timers = new Util_Timers(this);
	public Util_Signs signs = new Util_Signs(this);
	public Util_Times times = new Util_Times(this);
	public Util_Misc util = new Util_Misc(this);
	public Util_Database database;
	
	private void initializeDatabase() {
		this.database = new Util_Database(this) {
			
			@Override
			protected java.util.List<Class<?>> getDatabaseClasses() {
				List<Class<?>> list = new ArrayList<Class<?>>();
				list.add(DB_StartSign.class);
				list.add(DB_StopSign.class);
				list.add(DB_Timers.class);
				list.add(DB_Times.class);
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
		
		if (this.getConfig().getInt("DB.version") == 0) {
			File original = new File(this.getDataFolder() + "/" + this.getName() + ".db");
			File backup = new File(this.getDataFolder() + "/" + this.getName() + "-BACKUP.db");
			try {
				this.util.copyFile(original, backup);
			}
			catch (IOException e) {
				e.printStackTrace();
			}
			this.Config.setProp("DB.version", 1);
			this.Config.setProp("DB.rebuild", true);
			this.Config.setProp("messages.broadcast_message", null);
			this.Config.setProp("messages.player_message", null);
			this.Config.LoadConfig();
			this.upgrade = true;
		}
		
		this.initializeDatabase();
		this.times.updateRanks(null, true);
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
