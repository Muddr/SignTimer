package com.gtdclan.signtimer;

import java.util.logging.Level;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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
					
					String[] messages = new String[] {
					    "^redCommands:",
					    "  ^gray/st mytime^white : Displays your best time.",
					    "  ^gray/st rank <name>^white : Displays the rank for this player.",
					    "  ^gray/st top10^white : Displays the top 10 quickest times."
					};
					this.plugin.Util.messagePlayer(senderName, messages);
					return true;
				}
				if (args[0].equalsIgnoreCase("mytime")) {
					this.plugin.Times.getTime(senderName);
				}
				if (args[0].equalsIgnoreCase("rank")) {
					if (args.length == 1) {
						this.plugin.Util.messagePlayer(senderName, "^redYou must include a players name.");
					}
					else {
						String rankPlayer = args[1];
						this.plugin.Times.rankList(senderName, rankPlayer, false);
					}
				}
				if (args[0].equalsIgnoreCase("top10")) {
					this.plugin.Times.rankList(senderName);
				}
			}
			else {
				this.plugin.log.log(Level.INFO, "Commands must be done in game.");
			}
			return true;
		}
		return false;
	}
}
