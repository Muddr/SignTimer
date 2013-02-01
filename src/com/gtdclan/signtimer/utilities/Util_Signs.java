package com.gtdclan.signtimer.utilities;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;

import com.gtdclan.signtimer.Main;
import com.gtdclan.signtimer.databases.DB_StartSign;
import com.gtdclan.signtimer.databases.DB_StopSign;
import com.gtdclan.signtimer.databases.DB_Timers;

public class Util_Signs {
	
	private final Main plugin;
	
	public Util_Signs(Main instance) {
		this.plugin = instance;
	}
	
	public Integer clearSigns(Integer timerID) {
		Integer changed = 0;
		DB_StartSign startSign = this.getStartSign(timerID);
		DB_StopSign stopSign = this.getStopSign(timerID);
		if (startSign != null) {
			Sign sign = this.getSign_Start(startSign);
			if (sign != null) {
				sign.setLine(0, "");
				sign.setLine(1, "");
				sign.setLine(2, "");
				sign.setLine(3, "");
				sign.update();
				changed++;
			}
			this.plugin.database.getDatabase().delete(startSign);
		}
		if (stopSign != null) {
			Sign sign = this.getSign_Stop(stopSign);
			if (sign != null) {
				sign.setLine(0, "");
				sign.setLine(1, "");
				sign.setLine(2, "");
				sign.setLine(3, "");
				sign.update();
				changed++;
			}
			this.plugin.database.getDatabase().delete(stopSign);
		}
		return changed;
	}
	
	public void createSign(Block sign, int timerID, String text) {
		int x = sign.getX();
		int y = sign.getY();
		int z = sign.getZ();
		String world = sign.getWorld().getName();
		
		if (text.equalsIgnoreCase("start")) {
			DB_StartSign signInfo = new DB_StartSign();
			signInfo.setSign_x(x);
			signInfo.setSign_y(y);
			signInfo.setSign_z(z);
			signInfo.setSign_world(world);
			signInfo.setTimerID(timerID);
			
			this.plugin.database.getDatabase().save(signInfo);
		}
		else {
			DB_StopSign signInfo = new DB_StopSign();
			signInfo.setSign_x(x);
			signInfo.setSign_y(y);
			signInfo.setSign_z(z);
			signInfo.setSign_world(world);
			signInfo.setTimerID(timerID);
			
			this.plugin.database.getDatabase().save(signInfo);
		}
		
	}
	
	public Sign getSign_Start(DB_StartSign signInfo) {
		String world = signInfo.getSign_world();
		Integer x = signInfo.getSign_x();
		Integer y = signInfo.getSign_y();
		Integer z = signInfo.getSign_z();
		Block signBlock = this.plugin.getServer().getWorld(world).getBlockAt(x, y, z);
		
		if (signBlock.getType() == Material.WALL_SIGN || signBlock.getType() == Material.SIGN_POST) {
			BlockState blockState = signBlock.getState();
			return (Sign) blockState;
		}
		return null;
	}
	
	public Sign getSign_Stop(DB_StopSign signInfo) {
		String world = signInfo.getSign_world();
		Integer x = signInfo.getSign_x();
		Integer y = signInfo.getSign_y();
		Integer z = signInfo.getSign_z();
		Block signBlock = this.plugin.getServer().getWorld(world).getBlockAt(x, y, z);
		
		if (signBlock.getType() == Material.WALL_SIGN || signBlock.getType() == Material.SIGN_POST) {
			BlockState blockState = signBlock.getState();
			return (Sign) blockState;
		}
		return null;
	}
	
	public DB_StartSign getStartSign(Integer timerID) {
		return this.plugin.database.getDatabase().find(DB_StartSign.class).where().eq("TimerID", timerID).findUnique();
	}
	
	public DB_StopSign getStopSign(Integer timerID) {
		return this.plugin.database.getDatabase().find(DB_StopSign.class).where().eq("TimerID", timerID).findUnique();
	}
	
	public Integer renameSigns(String newName, Integer timerID) {
		Integer changed = 0;
		DB_StartSign startSign = this.getStartSign(timerID);
		DB_StopSign stopSign = this.getStopSign(timerID);
		if (startSign != null) {
			Sign sign = this.getSign_Start(startSign);
			if (sign != null) {
				sign.setLine(1, newName);
				sign.update();
				changed++;
			}
		}
		if (stopSign != null) {
			Sign sign = this.getSign_Stop(stopSign);
			if (sign != null) {
				sign.setLine(1, newName);
				sign.update();
				changed++;
			}
		}
		return changed;
	}
	
	public void toggleSign(DB_Timers timer, Boolean enabled) {
		String text = (enabled ? "" : "<DISABLED>");
		Integer timerID = timer.getId();
		DB_StartSign startSign = this.getStartSign(timerID);
		DB_StopSign stopSign = this.getStopSign(timerID);
		if (startSign != null) {
			Sign sign = this.getSign_Start(startSign);
			if (sign != null) {
				sign.setLine(3, text);
				sign.update();
			}
		}
		if (stopSign != null) {
			Sign sign = this.getSign_Stop(stopSign);
			if (sign != null) {
				sign.setLine(3, text);
				sign.update();
			}
		}
	}
}
