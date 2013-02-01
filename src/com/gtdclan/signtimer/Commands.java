package com.gtdclan.signtimer;

import java.util.HashMap;
import java.util.logging.Level;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.gtdclan.signtimer.databases.DB_Timers;

public class Commands implements CommandExecutor {
	
	private final Main plugin;
	
	public Commands(Main instance) {
		this.plugin = instance;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command senderCommand, String label, String[] args) {
		Player player = null;
		String senderName = "Console";
		if (sender instanceof Player) {
			player = (Player) sender;
			senderName = player.getName();
		}
		
		String command = senderCommand.getName().toLowerCase();
		
		if (command.equals("st")) {
			if (player != null) {
				if (args.length == 0) {
					this.plugin.util.listCommands(player);
					return true;
				}
				
				//
				// st clear <timername>
				//
				if (args[0].equalsIgnoreCase("clear")) {
					if (player.hasPermission("signtimer.cleartimes")) {
						if (args.length == 1) {
							this.plugin.util.messagePlayer(senderName, "^redYou must include a timer name.");
							this.plugin.util.messagePlayer(senderName, "^redTry using /st list to see timer names.");
						}
						else {
							String timername = args[1];
							DB_Timers timer = this.plugin.timers.getTimerbyName(timername);
							if (timer != null) {
								Integer timesCleared = this.plugin.times.clearTimes(timer.getId());
								this.plugin.util.messagePlayer(senderName, "^yellow" + timesCleared + " times cleared for " + timername);
								this.plugin.times.updateRanks(timer, false);
							}
							else {
								this.plugin.util.messagePlayer(senderName, "^redError: Can't find a timer with the name: " + timername);
								this.plugin.util.messagePlayer(senderName, "^redTry using /st list to see timer names.");
							}
						}
					}
					else {
						this.plugin.util.messagePlayer(senderName, "^redYou do not have permission to clear times.");
					}
					return true;
				}
				
				//
				// st delete <timername>
				//
				if (args[0].equalsIgnoreCase("delete")) {
					if (player.hasPermission("signtimer.delete")) {
						if (args.length == 1) {
							this.plugin.util.messagePlayer(senderName, "^redYou must include a timer name.");
							this.plugin.util.messagePlayer(senderName, "^redTry using /st list to see timer names.");
						}
						else {
							String timername = args[1];
							DB_Timers timer = this.plugin.timers.getTimerbyName(timername);
							if (timer != null) {
								this.plugin.timers.deleteTimer(timer, senderName);
							}
							else {
								this.plugin.util.messagePlayer(senderName, "^redError: Can't find a timer with the name: " + timername);
								this.plugin.util.messagePlayer(senderName, "^redTry using /st list to see timer names.");
							}
						}
					}
					else {
						this.plugin.util.messagePlayer(senderName, "^redYou do not have permission to clear times.");
					}
					return true;
				}
				
				//
				// st enable <timername>
				// st disable <timername>
				//
				if (args[0].equalsIgnoreCase("enable") || args[0].equalsIgnoreCase("disable")) {
					if (player.hasPermission("signtimer.enabledisable")) {
						if (args.length == 1) {
							this.plugin.util.messagePlayer(senderName, "^redYou must include a timer name.");
							this.plugin.util.messagePlayer(senderName, "^redTry using /st list to see timer names.");
						}
						else {
							String timername = args[1];
							DB_Timers timer = this.plugin.timers.getTimerbyName(timername);
							if (timer != null) {
								if (args[0].equalsIgnoreCase("enable") && timer.getEnabled()) {
									this.plugin.util.messagePlayer(senderName, "^yellow" + timername + " is already enabled");
									return true;
								}
								
								if (args[0].equalsIgnoreCase("disable") && !timer.getEnabled()) {
									this.plugin.util.messagePlayer(senderName, "^yellow" + timername + " is already disabled");
									return true;
								}
								
								this.plugin.timers.toggleTimer(timer, senderName);
							}
							else {
								this.plugin.util.messagePlayer(senderName, "^redError: Can't find a timer with the name: " + timername);
								this.plugin.util.messagePlayer(senderName, "^redTry using /st list to see timer names.");
							}
						}
					}
					else {
						this.plugin.util.messagePlayer(senderName, "^redYou do not have permission to enable/disable timers.");
					}
					return true;
				}
				
				//
				// st list
				//
				if (args[0].equalsIgnoreCase("list")) {
					if (player.hasPermission("signtimer.list")) {
						HashMap<String, Boolean> timerMap = this.plugin.timers.getTimerList();
						
						if (timerMap.size() > 0) {
							this.plugin.util.messagePlayer(senderName, "^yellow^underlineTimers: red=disabled");
							this.plugin.util.messagePlayer(senderName, "");
							for (String timerName : timerMap.keySet()) {
								String color = "^red";
								Boolean isEnabled = timerMap.get(timerName);
								if (isEnabled) {
									color = "^green";
								}
								this.plugin.util.messagePlayer(senderName, color + timerName);
							}
						}
						else {
							this.plugin.util.messagePlayer(senderName, "^yellowNo timers found.");
						}
					}
					else {
						this.plugin.util.messagePlayer(senderName, "^redYou do not have permission to list timers.");
					}
					return true;
				}
				
				//
				// st mytime
				// st mytime <timername>
				//
				if (args[0].equalsIgnoreCase("mytime")) {
					if (args.length == 1) {
						this.plugin.times.getTime(senderName, null, true);
						this.plugin.util.messagePlayer(senderName, "^yellowUse /st mytime <timerName> to show 1 time.");
					}
					else {
						String timername = args[1];
						DB_Timers timer = this.plugin.timers.getTimerbyName(timername);
						if (timer != null) {
							this.plugin.times.getTime(senderName, timer, false);
						}
						else {
							this.plugin.util.messagePlayer(senderName, "^redError: Can't find a timer with the name: " + timername);
							this.plugin.util.messagePlayer(senderName, "^redTry using /st list to see timer names.");
						}
					}
					
					return true;
				}
				
				//
				// st rank <playername> <timername>
				//
				if (args[0].equalsIgnoreCase("rank")) {
					if (args.length < 3) {
						this.plugin.util.messagePlayer(senderName, "^redYou must include the player's name and timer's name.");
						this.plugin.util.messagePlayer(senderName, "^red/st rank <playername> <timername>");
					}
					else {
						String timername = args[2];
						DB_Timers timer = this.plugin.timers.getTimerbyName(timername);
						if (timer != null) {
							String rankPlayer = args[1];
							this.plugin.times.rankList(senderName, rankPlayer, timer);
						}
						else {
							this.plugin.util.messagePlayer(senderName, "^redError: Can't find a timer with the name: " + timername);
							this.plugin.util.messagePlayer(senderName, "^redTry using /st list to see timer names.");
						}
						
					}
					return true;
				}
				
				//
				// st rename <oldName> <newName>
				//
				if (args[0].equalsIgnoreCase("rename")) {
					if (player.hasPermission("signtimer.rename")) {
						if (args.length < 3) {
							this.plugin.util.messagePlayer(senderName, "^redYou must include the old and new timer names.");
							this.plugin.util.messagePlayer(senderName, "^red/st rename <oldName> <newName>");
						}
						else {
							String oldName = args[1];
							String newName = args[2];
							DB_Timers timer = this.plugin.timers.getTimerbyName(oldName);
							if (timer != null) {
								timer.setTimername(newName);
								this.plugin.database.getDatabase().save(timer);
								Integer signsChanged = this.plugin.signs.renameSigns(newName, timer.getId());
								
								this.plugin.util.messagePlayer(senderName, "^yellowTimer should be renamed.");
								this.plugin.util.messagePlayer(senderName, "^yellowUpdated " + signsChanged + " sign(s).");
							}
							else {
								this.plugin.util.messagePlayer(senderName, "^redError: Can't find a timer with the name: " + oldName);
								this.plugin.util.messagePlayer(senderName, "^redTry using /st list to see timer names.");
							}
						}
					}
					else {
						this.plugin.util.messagePlayer(senderName, "^redYou do not have permission to rename timers.");
					}
					return true;
				}
				
				//
				// st top <timername>
				//
				if (args[0].equalsIgnoreCase("top")) {
					if (args.length == 1) {
						this.plugin.util.messagePlayer(senderName, "^redYou must include a timer name.");
						this.plugin.util.messagePlayer(senderName, "^redTry using /st list to see timer names.");
					}
					else {
						String timerName = args[1];
						DB_Timers timer = this.plugin.timers.getTimerbyName(timerName);
						if (timer != null) {
							this.plugin.times.topList(senderName, timer);
						}
						else {
							this.plugin.util.messagePlayer(senderName, "^redError: Can't find a timer with the name: " + timerName);
							this.plugin.util.messagePlayer(senderName, "^redTry using /st list to see timer names.");
						}
					}
					return true;
				}
				// END OF INSTANCEOF PLAYER
			}
			else {
				this.plugin.log.log(Level.INFO, "Commands must be done in game.");
				return true;
			}
			
		}
		return false;
	}
}
