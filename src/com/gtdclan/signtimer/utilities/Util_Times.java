package com.gtdclan.signtimer.utilities;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.logging.Level;

import com.gtdclan.signtimer.Main;
import com.gtdclan.signtimer.databases.DB_Timers;
import com.gtdclan.signtimer.databases.DB_Times;

public class Util_Times {
	
	private final Main plugin;
	
	public Util_Times(Main instance) {
		this.plugin = instance;
	}
	
	public void addTime(String playerName, int time, DB_Timers timer) {
		String message, bmessage;
		Integer timerID = timer.getId();
		String st_time = this.plugin.util.formatTime(Integer.valueOf(time));
		DB_Times data = this.plugin.database.getDatabase().find(DB_Times.class).where().ieq("Playername", playerName).where().eq("TimerID", timerID).findUnique();
		if (data == null) {
			data = new DB_Times();
			data.setPlayername(playerName);
			data.setTime(time);
			data.setTimerID(timerID);
			message = "Your time has been saved.";
			bmessage = "It was their first time.";
			this.plugin.database.getDatabase().save(data);
		}
		else {
			Long savedTime = data.getTime();
			
			if (time < savedTime) {
				message = "New personal best. " + st_time;
				bmessage = "with a new personal best of " + st_time;
				data.setTime(time);
				this.plugin.database.getDatabase().save(data);
			}
			else if (time == savedTime) {
				message = "You matched your best time of " + st_time;
				bmessage = "They matched their best time of " + st_time;
			}
			else {
				Integer difference = (int) (time - savedTime);
				String fasterTime = this.plugin.util.formatTime(difference);
				message = "Your old time was " + fasterTime + " faster.";
				bmessage = "which was " + fasterTime + " slower.";
			}
		}
		if (this.plugin.broadcast_times) {
			this.plugin.util.broadcast(bmessage);
		}
		else {
			this.plugin.util.messagePlayer(playerName, message);
		}
		
		this.updateRanks(timer, false);
		
	}
	
	public Integer clearTimes(Integer timerID) {
		List<DB_Times> data = this.plugin.database.getDatabase().find(DB_Times.class).where().eq("TimerID", timerID).findList();
		Integer cleared = 0;
		if (data != null && data.size() > 0) {
			for (DB_Times time : data) {
				this.plugin.database.getDatabase().delete(time);
				cleared++;
			}
		}
		return cleared;
	}
	
	public void getTime(String playerName, DB_Timers timer, boolean allTimes) {
		if (allTimes) {
			for (Integer timerID : this.plugin.rankList.keySet()) {
				DB_Timers timerData = this.plugin.timers.getTimerbyID(timerID);
				HashMap<String, String> timerRanks = this.plugin.rankList.get(timerID);
				String timername = timerData.getTimername() + " | ";
				
				if (timerRanks != null) {
					String playerInfo = timerRanks.get(playerName);
					if (playerInfo != null) {
						String[] pInfo = playerInfo.split(",");
						String playerTime = this.plugin.util.formatTime(Integer.valueOf(pInfo[1]));
						
						this.plugin.util.messagePlayer(playerName, timername + " " + String.format("rank # %02d", Integer.valueOf(pInfo[0])) + " | " + playerTime);
					}
					else {
						this.plugin.util.messagePlayer(playerName, "No Time Found.");
					}
				}
			}
		}
		else {
			Integer timerID = timer.getId();
			HashMap<String, String> timerRanks = this.plugin.rankList.get(timerID);
			String timername = timer.getTimername() + " | ";
			
			if (timerRanks != null) {
				String playerInfo = timerRanks.get(playerName);
				if (playerInfo != null) {
					String[] pInfo = playerInfo.split(",");
					String playerTime = this.plugin.util.formatTime(Integer.valueOf(pInfo[1]));
					
					this.plugin.util.messagePlayer(playerName, timername + " " + String.format("rank # %02d", Integer.valueOf(pInfo[0])) + " | " + playerTime);
				}
				else {
					this.plugin.util.messagePlayer(playerName, "No Time Found.");
				}
			}
		}
		
	}
	
	public void rankList(String senderName, String rankPlayer, DB_Timers timer) {
		Integer timerID = timer.getId();
		HashMap<String, String> timerRanks = this.plugin.rankList.get(timerID);
		String message = timer.getTimername() + " | ";
		
		if (timerRanks != null) {
			String playerInfo = timerRanks.get(rankPlayer);
			if (playerInfo != null) {
				String[] pInfo = playerInfo.split(",");
				String playerTime = this.plugin.util.formatTime(Integer.valueOf(pInfo[1]));
				
				message += " " + String.format(rankPlayer + " is rank # %02d", Integer.valueOf(pInfo[0])) + " | " + playerTime;
			}
			else {
				message += "No Time Found for " + rankPlayer + ".";
			}
			this.plugin.util.messagePlayer(senderName, message);
		}
		else {
			this.plugin.util.messagePlayer(senderName, "^redError finding ranks for " + timer.getTimername());
		}
	}
	
	public void topList(String senderName, DB_Timers timer) {
		
		this.plugin.util.messagePlayer(senderName, "Top " + this.plugin.top_amount + " Times:");
		this.plugin.util.messagePlayer(senderName, "Timer: " + timer.getTimername());
		Integer timerID = timer.getId();
		HashMap<String, String> timerRanks = this.plugin.rankList.get(timerID);
		
		if (timerRanks != null && timerRanks.size() > 0) {
			int i = 0;
			for (String playerName : timerRanks.keySet()) {
				String[] playerInfo = timerRanks.get(playerName).split(",");
				String playerTime = this.plugin.util.formatTime(Integer.valueOf(playerInfo[1]));
				if (this.plugin.compact_time) {
					this.plugin.util.messagePlayer(senderName, "  " + String.format("%02d", Integer.valueOf(playerInfo[0])) + "  |  " + playerTime + "  |  " + playerName);
				}
				else {
					this.plugin.util.messagePlayer(senderName, "  " + String.format("%02d", Integer.valueOf(playerInfo[0])) + "  |  " + playerName + "  |  " + playerTime);
				}
				i++;
				if (i > (this.plugin.top_amount - 1)) {
					break;
				}
			}
		}
		else {
			this.plugin.util.messagePlayer(senderName, "No times found for " + timer.getTimername());
		}
	}
	
	public void updateRanks(DB_Timers timer, Boolean all) {
		if (all) {
			List<DB_Timers> timers = this.plugin.database.getDatabase().find(DB_Timers.class).findList();
			if (timers != null && timers.size() > 0) {
				for (DB_Timers timerdata : timers) {
					Integer TimerID = timerdata.getId();
					HashMap<String, String> timerRanks = new LinkedHashMap<String, String>();
					List<DB_Times> times = this.plugin.database.getDatabase().find(DB_Times.class).where().eq("TimerID", TimerID).order().asc("Time").findList();
					
					this.plugin.rankList.put(TimerID, timerRanks);
					
					if (times != null) {
						int i = 1;
						for (DB_Times time : times) {
							String playerName = time.getPlayername();
							Long playerTime = time.getTime();
							timerRanks.put(playerName, i + "," + playerTime);
							i++;
						}
						this.plugin.rankList.put(TimerID, timerRanks);
					}
				}
			}
			else {
				this.plugin.log.log(Level.INFO, "Error: could not find any enabled timers in DB to update ranks.");
			}
		}
		else {
			Integer TimerID = timer.getId();
			HashMap<String, String> timerRanks = new LinkedHashMap<String, String>();
			List<DB_Times> times = this.plugin.database.getDatabase().find(DB_Times.class).where().eq("TimerID", TimerID).order().asc("Time").findList();
			
			this.plugin.rankList.put(TimerID, timerRanks);
			
			if (times != null) {
				int i = 1;
				for (DB_Times time : times) {
					String playerName = time.getPlayername();
					Long playerTime = time.getTime();
					timerRanks.put(playerName, i + "," + playerTime);
					i++;
				}
				this.plugin.rankList.put(TimerID, timerRanks);
			}
		}
	}
}
