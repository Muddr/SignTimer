package com.gtdclan.signtimer.utilities;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import com.avaje.ebean.Query;
import com.gtdclan.signtimer.Main;
import com.gtdclan.signtimer.databases.DB_StartSign;
import com.gtdclan.signtimer.databases.DB_StopSign;
import com.gtdclan.signtimer.databases.DB_Timers;

public class Util_Timers {
	
	private final Main plugin;
	
	public Util_Timers(Main instance) {
		this.plugin = instance;
	}
	
	public void deleteTimer(DB_Timers timer, String playerName) {
		int timerID = timer.getId();
		String timerName = timer.getTimername();
		
		Integer timesCleared = this.plugin.times.clearTimes(timerID);
		Integer signsCleared = this.plugin.signs.clearSigns(timerID);
		this.plugin.database.getDatabase().delete(timer);
		
		this.plugin.util.messagePlayer(playerName, timerName + " should have been deleted.");
		this.plugin.util.messagePlayer(playerName, "Times Cleared: " + timesCleared);
		this.plugin.util.messagePlayer(playerName, "Signs Cleared: " + signsCleared);
		
	}
	
	public DB_Timers getTimerbyID(Integer timerID) {
		return this.plugin.database.getDatabase().find(DB_Timers.class).where().eq("id", timerID).findUnique();
	}
	
	public DB_Timers getTimerbyName(String timerName) {
		return this.plugin.database.getDatabase().find(DB_Timers.class).where().ieq("Timername", timerName).findUnique();
	}
	
	public HashMap<String, Boolean> getTimerList() {
		HashMap<String, Boolean> tempMap = new LinkedHashMap<String, Boolean>();
		Query<DB_Timers> data = this.plugin.database.getDatabase().find(DB_Timers.class).where().gt("id", 0).order().asc("Timername");
		
		int timerCount = data.findRowCount();
		if (data != null && timerCount > 0) {
			List<DB_Timers> timerList = data.findList();
			for (DB_Timers timer : timerList) {
				String timerName = timer.getTimername();
				Boolean enabled = timer.getEnabled();
				tempMap.put(timerName, enabled);
			}
		}
		return tempMap;
	}
	
	public void toggleTimer(DB_Timers timer, String playerName) {
		int timerID = timer.getId();
		boolean isEnabled = timer.getEnabled();
		boolean setEnabled = false;
		
		DB_StartSign startSign = this.plugin.signs.getStartSign(timerID);
		DB_StopSign stopSign = this.plugin.signs.getStopSign(timerID);
		String message = "Timer Disabled";
		
		if (!isEnabled) {
			if (startSign == null) {
				this.plugin.util.messagePlayer(playerName, "Error: Can't find Start sign.");
			}
			else if (stopSign == null) {
				this.plugin.util.messagePlayer(playerName, "Error: Can't find Stop sign.");
			}
			else {
				message = "Timer Enabled.";
				setEnabled = true;
			}
		}
		timer.setEnabled(setEnabled);
		this.plugin.signs.toggleSign(timer, setEnabled);
		this.plugin.util.messagePlayer(playerName, message);
		this.plugin.database.getDatabase().save(timer);
	}
}
