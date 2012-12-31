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
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class Listeners implements Listener {
	
	private final Main plugin;
	public HashMap<String, Long> times = new HashMap<String, Long>();
	
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
			
			if (line0.equalsIgnoreCase("[signtimer]") && !player.hasPermission("signtimer.removesigns")) {
				sign.update();
				this.plugin.Util.messagePlayer(playerName, "^redYou don't have permission to remove a sign timer");
				event.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void onSignChange(SignChangeEvent event) {
		Player player = event.getPlayer();
		String line0 = event.getLine(0);
		if (line0.equalsIgnoreCase("[signtimer]") && !player.hasPermission("signtimer.createsigns")) {
			player.sendMessage(this.plugin.Util.parseColors("^redYou do not have permission to create a sign timer."));
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void PlayerInteractEvent(PlayerInteractEvent event) {
		Long timeNow = this.plugin.Util.getNowAsMillis();
		if (event.isCancelled()) {
			return;
		}
		
		Player player = event.getPlayer();
		String playerName = player.getName();
		Block block = event.getClickedBlock();
		Action click = event.getAction();
		String message = "";
		if (click == Action.RIGHT_CLICK_BLOCK && (block.getType() == Material.WALL_SIGN || block.getType() == Material.SIGN_POST)) {
			
			BlockState blockState = block.getState();
			Sign sign = (Sign) blockState;
			String line0 = sign.getLine(0);
			String line1 = sign.getLine(1);
			if (line0.equalsIgnoreCase("[signtimer]")) {
				event.setCancelled(true);
				
				if (line1.equalsIgnoreCase("start")) {
					this.times.put(playerName, timeNow);
					message = this.plugin.Util.parseColors(this.plugin.prefix + "Timer Started.");
					player.sendMessage(message);
				}
				
				if (line1.equalsIgnoreCase("stop")) {
					Long startTime = this.times.get(playerName);
					if (startTime == null) {
						message = "^redCan't find start time.";
						this.plugin.Util.messagePlayer(playerName, message);
					}
					else {
						int elapseTime = (int) (timeNow - startTime);
						String time = this.plugin.Util.formatTime(elapseTime);
						
						if (this.plugin.broadcast_times) {
							message = this.plugin.broadcast_message.replace("%PLAYERNAME%", playerName);
							message = message.replace("%TIME%", time);
							this.plugin.Util.broadcast(message);
						}
						else {
							message = this.plugin.player_message;
							message = message.replace("%TIME%", time);
							this.plugin.Util.messagePlayer(playerName, message);
						}
						this.plugin.Times.addTime(playerName, elapseTime);
						this.times.remove(playerName);
					}
					
				}
			}
		}
	}
	
	@EventHandler
	public void PlayerQuitEvent(PlayerQuitEvent event) {
		String playerName = event.getPlayer().getName();
		if (this.times.containsKey(playerName)) {
			this.times.remove(playerName);
		}
	}
}
