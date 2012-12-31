package com.gtdclan.signtimer;

import java.util.List;
import java.util.logging.Level;

import com.avaje.ebean.Query;

public class Times {
	
	private final Main plugin;
	
	public Times(Main instance) {
		this.plugin = instance;
	}
	
	public void addTime(String playerName, int time) {
		String message, bmessage;
		DB data = this.plugin.database.getDatabase().find(DB.class).where().ieq("Playername", playerName).findUnique();
		if (data == null) {
			data = new DB();
			data.setPlayername(playerName);
			data.setTime(time);
			message = "Your time has been saved.";
			bmessage = "It was their first time.";
			this.plugin.database.getDatabase().save(data);
		}
		else {
			Long savedTime = data.getTime();
			
			String st_time = this.plugin.Util.formatTime(Integer.valueOf(time));
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
				String fasterTime = this.plugin.Util.formatTime(difference);
				message = "Your old time was " + fasterTime + " faster.";
				bmessage = "which was " + fasterTime + " slower.";
			}
		}
		if (this.plugin.broadcast_times) {
			this.plugin.Util.broadcast(bmessage);
		}
		else {
			this.plugin.Util.messagePlayer(playerName, message);
		}
		
		this.updateRanks();
		
	}
	
	public void clearTimes() {
		Query<DB> data = this.plugin.database.getDatabase().find(DB.class).where().gt("id", 0).orderBy("id");
		int timeCount = data.findRowCount();
		if (data != null && timeCount > 0) {
			List<DB> times = data.findList();
			for (DB time : times) {
				this.plugin.database.getDatabase().delete(time);
			}
		}
		else {
			this.plugin.log.log(Level.INFO, "Error: could not find any times in DB to remove.");
		}
		this.plugin.rankList.clear();
	}
	
	public void getTime(String playerName) {
		String message;
		String playerInfo = this.plugin.rankList.get(playerName);
		if (playerInfo != null) {
			String[] pInfo = playerInfo.split(",");
			String playerTime = this.plugin.Util.formatTime(Integer.valueOf(pInfo[1]));
			
			message = " " + String.format("At rank # %02d", Integer.valueOf(pInfo[0])) + ", your best time is " + playerTime;
		}
		else {
			message = "Error: could not find any times for you.";
		}
		
		this.plugin.Util.messagePlayer(playerName, message);
	}
	
	public void rankList(String senderName) {
		this.rankList(senderName, "", true);
	}
	
	public void rankList(String senderName, String rankPlayer, Boolean top) {
		if (this.plugin.rankList.size() > 0) {
			if (top) {
				
				// this.plugin.Util.messagePlayer(senderName, "^gold^underline  #                     Time                        Name         ");
				this.plugin.Util.messagePlayer(senderName, "Top 10 Times:");
				
				int i = 0;
				for (String playerName : this.plugin.rankList.keySet()) {
					String[] playerInfo = this.plugin.rankList.get(playerName).split(",");
					String playerTime = this.plugin.Util.formatTime(Integer.valueOf(playerInfo[1]));
					if (this.plugin.compact_time) {
						this.plugin.Util.messagePlayer(senderName, "  " + String.format("%02d", Integer.valueOf(playerInfo[0])) + "  |  " + playerTime + "  |  " + playerName);
					}
					else {
						this.plugin.Util.messagePlayer(senderName, "  " + String.format("%02d", Integer.valueOf(playerInfo[0])) + "  |  " + playerName + "  |  " + playerTime);
					}
					i++;
					if (i > 9) {
						break;
					}
				}
			}
			else {
				String playerInfo = this.plugin.rankList.get(rankPlayer);
				if (playerInfo != null) {
					String[] pInfo = playerInfo.split(",");
					String playerTime = this.plugin.Util.formatTime(Integer.valueOf(pInfo[1]));
					
					this.plugin.Util.messagePlayer(senderName, " " + String.format(rankPlayer + " is rank # %02d", Integer.valueOf(pInfo[0])) + ", with a time of " + playerTime);
				}
				else {
					this.plugin.Util.messagePlayer(senderName, "Error: could not find any times for " + rankPlayer + ".");
				}
			}
		}
		else {
			this.plugin.Util.messagePlayer(senderName, "Error: could not find any times.");
		}
	}
	
	public void updateRanks() {
		Query<DB> data = this.plugin.database.getDatabase().find(DB.class).where().gt("id", 0).order().asc("Time");
		
		int timeCount = data.findRowCount();
		if (data != null && timeCount > 0) {
			List<DB> timeList = data.findList();
			int i = 1;
			this.plugin.rankList.clear();
			for (DB time : timeList) {
				String playerName = time.getPlayername();
				Long playerTime = time.getTime();
				this.plugin.rankList.put(playerName, i + "," + playerTime);
				i++;
			}
		}
		else {
			this.plugin.log.log(Level.INFO, "Error: could not find any times in DB.");
		}
	}
}
