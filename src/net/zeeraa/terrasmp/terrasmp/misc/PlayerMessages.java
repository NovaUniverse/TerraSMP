package net.zeeraa.terrasmp.terrasmp.misc;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class PlayerMessages {
	public static String getJoinMessage(Player player) {
		return ChatColor.DARK_GRAY + "[" + ChatColor.DARK_GREEN + "+" + ChatColor.DARK_GRAY + "] " + ChatColor.RESET + player.getDisplayName() + ChatColor.RESET + ChatColor.DARK_GRAY + " Joined";
	}

	public static String getLeaveMessage(Player player) {
		return ChatColor.DARK_GRAY + "[" + ChatColor.DARK_RED + "-" + ChatColor.DARK_GRAY + "] " + ChatColor.RESET + player.getDisplayName() + ChatColor.RESET + ChatColor.DARK_GRAY + " Left";
	}
}