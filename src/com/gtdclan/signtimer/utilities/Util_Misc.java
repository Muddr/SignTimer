package com.gtdclan.signtimer.utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.Calendar;
import java.util.logging.Level;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.gtdclan.signtimer.Main;

public class Util_Misc {
	
	private final Main plugin;
	
	public Util_Misc(Main instance) {
		this.plugin = instance;
	}
	
	public void broadcast(String message) {
		this.plugin.getServer().broadcastMessage(this.parseColors(this.plugin.prefix + message));
	}
	
	public void console(String message, Level level) {
		this.plugin.log.log(level, "[" + this.plugin.getDescription().getName() + "] " + message);
	}
	
	public void console(String[] messages, Level level) {
		for (String Message : messages) {
			this.console(Message, level);
		}
	}
	
	public void copyFile(File sourceFile, File destFile) throws IOException {
		if (!destFile.exists()) {
			destFile.createNewFile();
		}
		
		FileChannel source = null;
		FileChannel destination = null;
		
		try {
			source = new FileInputStream(sourceFile).getChannel();
			destination = new FileOutputStream(destFile).getChannel();
			destination.transferFrom(source, 0, source.size());
		}
		finally {
			if (source != null) {
				source.close();
			}
			if (destination != null) {
				destination.close();
			}
		}
	}
	
	public String formatTime(Integer ms) {
		if (this.plugin.compact_time) {
			return this.getCompact(ms);
		}
		else {
			return this.getFriendly(ms);
		}
	}
	
	public String getCompact(Integer time) {
		String output = "";
		if (time > 0) {
			Integer second, minute, hour, day, ms;
			
			ms = time % 1000;
			time = time / 1000;
			hour = (int) Math.floor((time % 31536000) / 3600);
			minute = (int) Math.floor((((time % 31536000) % 86400) % 3600) / 60);
			second = (int) Math.floor(((time % 31536000) % 86400) % 3600 % 60);
			
			if (this.plugin.include_days) {
				day = (int) Math.floor((time % 31536000) / 86400);
				hour = (int) Math.floor(((time % 31536000) % 86400) / 3600);
				output = String.format("%02d", day) + "d^gold:^white";
			}
			
			String s_hour = String.format("%02d", hour) + "h^gold:^white";
			String s_minute = String.format("%02d", minute) + "m^gold:^white";
			String s_second = String.format("%02d", second) + "s";
			String s_ms = "^gold:^white" + String.format("%04d", ms) + "ms";
			
			output += s_hour + s_minute + s_second;
			if (this.plugin.include_milli) {
				output += s_ms;
			}
		}
		else {
			output = "Error: No Time.";
		}
		return output;
	}
	
	public String getFriendly(Integer time) {
		String output = "";
		if (time > 0) {
			Integer second, minute, hour, day;
			if (time < 1000) {
				return time + " ms";
			}
			
			day = 0;
			
			time = time / 1000;
			hour = (int) Math.floor((time % 31536000) / 3600);
			minute = (int) Math.floor((((time % 31536000) % 86400) % 3600) / 60);
			second = (int) Math.floor(((time % 31536000) % 86400) % 3600 % 60);
			
			if (this.plugin.include_days) {
				day = (int) Math.floor((time % 31536000) / 86400);
				hour = (int) Math.floor(((time % 31536000) % 86400) / 3600);
			}
			if (day > 0) {
				if (day == 1) {
					output += day + " day ";
				}
				else {
					output += day + " days ";
				}
			}
			if (hour > 0) {
				if (hour == 1) {
					output += hour + " hour ";
				}
				else {
					output += hour + " hours ";
				}
			}
			if (minute > 0) {
				if (minute == 1) {
					output += minute + " minute ";
				}
				else {
					output += minute + " minutes ";
				}
			}
			if (second > 0) {
				if (second == 1) {
					output += second + " second";
				}
				else {
					output += second + " seconds";
				}
			}
		}
		else {
			output = "0 seconds";
		}
		return output;
	}
	
	public Long getNowAsMillis() {
		Calendar calendar = Calendar.getInstance();
		return calendar.getTimeInMillis();
	}
	
	public long getSecondsAgo(Long time) {
		return (this.getNowAsMillis() - time) / 1000;
	}
	
	public String getSecondsAgoFriendly(Long time) {
		String timeStr = this.getFriendly((int) this.getSecondsAgo(time));
		if (timeStr == null) {
			return "just now";
		}
		return timeStr + " ago";
	}
	
	public void listCommands(Player player) {
		player.sendMessage("");
		player.sendMessage("");
		player.sendMessage(this.parseColors("^red^underlineCommands:"));
		player.sendMessage("");
		
		if (player.hasPermission("signtimer.cleartimes")) {
			player.sendMessage(this.parseColors("  ^gray/st clear <timerName>^white : Clears all times for <timerName>."));
		}
		
		if (player.hasPermission("signtimer.delete")) {
			player.sendMessage(this.parseColors("  ^gray/st delete <timerName>^white : Deletes <timerName> and removes all times/signs."));
		}
		
		if (player.hasPermission("signtimer.enabledisable")) {
			player.sendMessage(this.parseColors("  ^gray/st enable <timerName>^white : Enables <timername>."));
			player.sendMessage(this.parseColors("  ^gray/st disable <timerName>^white : Disables <timername>."));
		}
		
		if (player.hasPermission("signtimer.list")) {
			player.sendMessage(this.parseColors("  ^gray/st list^white : Lists all timers."));
		}
		
		player.sendMessage(this.parseColors("  ^gray/st mytime <timerName>^white : Lists all your times. <timerName> is optional."));
		player.sendMessage(this.parseColors("  ^gray/st rank <playerName> <timerName>^white : Displays the rank for <playerName> time for <timerName>."));
		
		if (player.hasPermission("signtimer.rename")) {
			player.sendMessage(this.parseColors("  ^gray/st rename <oldName> <newName>^white : Renames timer from <oldName> to <newName>"));
		}
		
		player.sendMessage(this.parseColors("  ^gray/st top <timerName>^white : Lists the top " + this.plugin.top_amount + " times for <timerName>"));
		player.sendMessage("");
		player.sendMessage(this.parseColors("^red^underlineNon Command Permissions:"));
		player.sendMessage("");
		if (player.hasPermission("signtimer.createsigns")) {
			player.sendMessage(this.parseColors("  ^grayYou can create timer signs."));
		}
		else {
			player.sendMessage(this.parseColors("  ^grayYou can not create timer signs."));
		}
		if (player.hasPermission("signtimer.removesigns")) {
			player.sendMessage(this.parseColors("  ^grayYou can remove timer signs."));
		}
		else {
			player.sendMessage(this.parseColors("  ^grayYou can not remove timer signs."));
		}
	}
	
	public void messagePlayer(String playerName, String message) {
		Player player = this.plugin.getServer().getPlayerExact(playerName);
		player.sendMessage(this.parseColors(this.plugin.prefix + message));
	}
	
	public void messagePlayer(String playerName, String[] messages) {
		for (String message : messages) {
			this.messagePlayer(playerName, message);
		}
		
	}
	
	// @formatter:off
	/**
	 * Parses chat colors and replaces it with the correct output value.<br/>
	 * <br/>
	 * Type a slash (\) in from of the caret (^) to display a literal caret (^).<br/>
	 * Color conversions are as follows:<br/>
	 * <table style="border: 2px solid black; background-color: #222; color: #fff; text-align:center;">
	 * <tr><th>Input</th><th>Final Color</th><th>Raw Code</th></tr>
	 * <tr><td>^black</td><td style="background-color: #000"></td><td>&0</td></tr>
	 * <tr><td>^darkblue</td><td style="background-color: #00006E"></td><td>&1</td></tr>
	 * <tr><td>^darkgreen</td><td style="background-color: #008000"></td><td>&2</td></tr>
	 * <tr><td>^darkaqua</td><td style="background-color: #43BFC7"></td><td>&3</td></tr>
	 * <tr><td>^darkred</td><td style="background-color: #9B0000"></td><td>&4</td></tr>
	 * <tr><td>^darkpurple</td><td style="background-color: #800080"></td><td>&5</td></tr>
	 * <tr><td>^gold</td><td style="background-color: #E09C14"></td><td>&6</td></tr>
	 * <tr><td>^gray</td><td style="background-color: #808080"></td><td>&7</td></tr>
	 * <tr><td>^darkgray</td><td style="background-color: #404040"></td><td>&8</td></tr>
	 * <tr><td>^blue</td><td style="background-color: #3F66FF"></td><td>&9</td></tr>
	 * <tr><td>^green</td><td style="background-color: #8AFF6D"></td><td>&a</td></tr>
	 * <tr><td>^aqua</td><td style="background-color: #9AFEFF"></td><td>&b</td></tr>
	 * <tr><td>^red</td><td style="background-color: #D1564E"></td><td>&c</td></tr>
	 * <tr><td>^lightpurple</td><td style="background-color: #DE6CFF"></td><td>&d</td></tr>
	 * <tr><td>^yellow</td><td style="background-color: #FFEF63"></td><td>&e</td></tr>
	 * <tr><td>^white</td><td style="background-color: #FFF; color: #000">default</td><td>&f</td></tr>
	 * </table>
	 * 
	 * @param message
	 *        the message
	 * @return The new chat/broadcast friendly colored message.
	 */
	// @formatter:on
	public String parseColors(String message) {
		for (ChatColor color : ChatColor.values()) {
			String colorName = color.name().toLowerCase().replace("_", "");
			if (colorName.equals("magic")) {
				continue;
			}
			message = message.replace("^" + colorName, color.toString()).replace("\\" + color.toString(),
			    "^" + colorName);
		}
		return message;
	}
	
	public String[] parseColors(String[] messages) {
		int amount = messages.length;
		String[] newmessages = new String[amount];
		for (int i = 0; i < amount; i++) {
			
			newmessages[i] = this.parseColors(messages[i]);
		}
		return newmessages;
	}
}
