package com.gtdclan.signtimer;

import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.gtdclan.signtimer.databases.DB_StartSign;
import com.gtdclan.signtimer.databases.DB_StopSign;
import com.gtdclan.signtimer.databases.DB_Timers;
import com.gtdclan.signtimer.utilities.Temp_Times;

public class Listeners implements Listener {
	
	private final Main plugin;
	public HashMap<String, Temp_Times> times = new HashMap<String, Temp_Times>();
	
	public Listeners(Main instance) {
		this.plugin = instance;
	}
	
	@EventHandler
	public void BlockBreakEvent(BlockBreakEvent event) {
		if (event.isCancelled()) {
			return;
		}
		
		Player player = event.getPlayer();
		String playerName = player.getName();
		Block block = event.getBlock();
		
		if (block.getType() == Material.WALL_SIGN || block.getType() == Material.SIGN_POST) {
			
			BlockState blockState = block.getState();
			Sign sign = (Sign) blockState;
			String line0 = sign.getLine(0);
			String timerName = sign.getLine(1);
			String startStop = sign.getLine(2);
			
			if (line0.equalsIgnoreCase("[signtimer]")) {
				if (player.hasPermission("signtimer.removesigns")) {
					DB_Timers timer = this.plugin.timers.getTimerbyName(timerName);
					
					if (timer != null) {
						timer.setEnabled(false);
						this.plugin.database.getDatabase().save(timer);
						
						if (startStop.equalsIgnoreCase("Start")) {
							DB_StartSign startSign = this.plugin.signs.getStartSign(timer.getId());
							if (startSign != null) {
								this.plugin.database.getDatabase().delete(startSign);
							}
						}
						
						if (startStop.equalsIgnoreCase("Stop")) {
							DB_StopSign stopSign = this.plugin.signs.getStopSign(timer.getId());
							if (stopSign != null) {
								this.plugin.database.getDatabase().delete(stopSign);
							}
						}
						
						this.plugin.util.messagePlayer(playerName, "^redTimer " + timerName + " has been disabled.");
					}
				}
				else {
					
					sign.update();
					this.plugin.util.messagePlayer(playerName, "^redYou don't have permission to remove a sign timer");
					event.setCancelled(true);
				}
			}
		}
	}
	
	@EventHandler
	public void onSignChange(SignChangeEvent event) {
		Player player = event.getPlayer();
		String playerName = player.getName();
		String line0 = event.getLine(0);
		String timerName = event.getLine(1);
		String startStop = event.getLine(2);
		String message = "";
		Boolean autoEnable = this.plugin.auto_enable;
		Block signBlock = event.getBlock();
		
		if (line0.equalsIgnoreCase("[signtimer]")) {
			if (player.hasPermission("signtimer.createsigns")) {
				if (startStop.equalsIgnoreCase("Start") || startStop.equalsIgnoreCase("Stop")) {
					DB_Timers timer = this.plugin.timers.getTimerbyName(timerName);
					
					if (timer == null) {
						timer = new DB_Timers();
						timer.setTimername(timerName);
						timer.setEnabled(false);
						
						this.plugin.database.getDatabase().save(timer);
						timer = null;
						timer = this.plugin.timers.getTimerbyName(timerName);
						this.plugin.signs.createSign(signBlock, timer.getId(), startStop);
						
						message = "^greenTimer Created and sign added.";
					}
					else {
						if ((startStop.equalsIgnoreCase("Start") && this.plugin.signs.getStartSign(timer.getId()) != null) || (startStop.equalsIgnoreCase("Stop") && this.plugin.signs.getStopSign(timer.getId()) != null)) {
							message = "^redThe " + startStop + " sign for" + timer.getTimername() + " already exists. Please remove that sign first.";
							event.setCancelled(true);
						}
						else {
							this.plugin.signs.createSign(signBlock, timer.getId(), startStop);
							message = "^green " + startStop + " sign added to timer " + timerName + ".";
						}
					}
					
					if (autoEnable) {
						this.plugin.timers.toggleTimer(timer, playerName);
					}
					else {
						message += "^whiteUse /st enable " + timerName + " to enable it.";
					}
				}
				else {
					message = "^redLine 3 must be Start or Stop.";
					event.setCancelled(true);
				}
			}
			else {
				message = "^redYou do not have permission to create a sign timer.";
				event.setCancelled(true);
			}
			this.plugin.util.messagePlayer(playerName, message);
		}
	}
	
	@EventHandler
	public void PlayerDeathEvent(PlayerDeathEvent event) {
		if (this.plugin.stop_on_death) {
			String playerName = event.getEntity().getName();
			if (this.times.containsKey(playerName)) {
				this.times.remove(playerName);
			}
		}
	}
	
	@EventHandler
	public void PlayerInteractEvent(PlayerInteractEvent event) {
		Long timeNow = this.plugin.util.getNowAsMillis();
		if (event.isCancelled()) {
			return;
		}
		
		Player player = event.getPlayer();
		String playerName = player.getName();
		Block block = event.getClickedBlock();
		Action click = event.getAction();
		String message = "";
		Boolean oldSign = false;
		if (click == Action.RIGHT_CLICK_BLOCK && (block.getType() == Material.WALL_SIGN || block.getType() == Material.SIGN_POST)) {
			
			BlockState blockState = block.getState();
			Sign sign = (Sign) blockState;
			String line0 = sign.getLine(0);// [SignTimer]
			String line1 = sign.getLine(1);// Timer Name
			String line2 = sign.getLine(2);// Start or Stop
			if (line0.equalsIgnoreCase("[signtimer]")) {
				event.setCancelled(true);
				
				if (line1.equalsIgnoreCase("Start") || line1.equalsIgnoreCase("Stop")) {
					this.plugin.util.messagePlayer(playerName, "Converting old sign to new format.");
					line2 = line1;
					line1 = "default";
					
					sign.setLine(1, line1);
					sign.setLine(2, line2);
					sign.update();
					
					oldSign = true;
				}
				
				DB_Timers timer = this.plugin.timers.getTimerbyName(line1);
				if (timer != null) {
					if (oldSign) {
						this.plugin.signs.createSign(block, timer.getId(), line2);
						this.plugin.util.messagePlayer(playerName, "Trying to enable timer. If it fails try right clicking on the other sign.");
						
						this.plugin.timers.toggleTimer(timer, playerName);
					}
					if (timer.getEnabled()) {
						if (line2.equalsIgnoreCase("start")) {
							if (this.times.containsKey(playerName)) {
								this.plugin.util.messagePlayer(playerName, "Canceling old timer.");
							}
							Temp_Times time = new Temp_Times();
							time.setTime(timeNow);
							time.setTimerID(timer.getId());
							this.times.put(playerName, time);
							this.plugin.util.messagePlayer(playerName, "Timer Started for " + timer.getTimername() + ".");
						}
						
						if (line2.equalsIgnoreCase("stop")) {
							Temp_Times time = this.times.get(playerName);
							if (time == null) {
								message = "^redTimer not started.";
								this.plugin.util.messagePlayer(playerName, message);
								return;
							}
							
							if (timer.getId() != time.getTimerID()) {
								message = "^redYou started a different timer.";
								this.plugin.util.messagePlayer(playerName, message);
							}
							else {
								int elapseTime = (int) (timeNow - time.getTime());
								String st_time = this.plugin.util.formatTime(elapseTime);
								
								if (this.plugin.broadcast_times) {
									message = this.plugin.broadcast_message.replace("%PLAYERNAME%", playerName);
									message = message.replace("%TIME%", st_time);
									message = message.replace("%TIMERNAME%", timer.getTimername());
									this.plugin.util.broadcast(message);
								}
								else {
									message = this.plugin.player_message;
									message = message.replace("%TIME%", st_time);
									message = message.replace("%TIMERNAME%", timer.getTimername());
									this.plugin.util.messagePlayer(playerName, message);
								}
								this.plugin.times.addTime(playerName, elapseTime, timer);
								this.times.remove(playerName);
							}
						}
					}
					else {
						this.plugin.util.messagePlayer(playerName, "^redSorry this timer is disabled.");
					}
				}
				else {
					this.plugin.util.messagePlayer(playerName, "^redError finding timer.");
				}
			}
		}
	}
	
	@EventHandler
	public void PlayerQuitEvent(PlayerQuitEvent event) {
		if (this.plugin.stop_on_quit) {
			String playerName = event.getPlayer().getName();
			if (this.times.containsKey(playerName)) {
				this.times.remove(playerName);
			}
		}
	}
}
