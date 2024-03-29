package net.zeeraa.terrasmp.terrasmp.commands.map;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.PermissionDefault;

import net.zeeraa.novacore.spigot.command.NovaCommand;
import net.zeeraa.terrasmp.terrasmp.TerraSMP;

public class MapCommand extends NovaCommand {
	public MapCommand() {
		super("map", TerraSMP.getInstance());
		setPermission("terrasmp.command.map");
		setPermissionDefaultValue(PermissionDefault.TRUE);
	}
	
	@Override
	public boolean execute(CommandSender sender, String commandLabel, String[] args) {
		sender.sendMessage(ChatColor.GOLD + "Here is the map link: " + ChatColor.AQUA + "https://terrasmp-map.novauniverse.net/");
		return true;
	}
}