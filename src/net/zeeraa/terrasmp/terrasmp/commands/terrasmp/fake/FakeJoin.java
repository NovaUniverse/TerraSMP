package net.zeeraa.terrasmp.terrasmp.commands.terrasmp.fake;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionDefault;

import net.zeeraa.novacore.spigot.command.AllowedSenders;
import net.zeeraa.novacore.spigot.command.NovaSubCommand;
import net.zeeraa.terrasmp.terrasmp.misc.PlayerMessages;

public class FakeJoin extends NovaSubCommand {
	public FakeJoin() {
		super("fakejoin");

		setPermission("terrasmp.command.terrasmp.fakejoin");
		setPermissionDefaultValue(PermissionDefault.OP);
		setDescription("Fake a join message");
		setAllowedSenders(AllowedSenders.PLAYERS);
		addHelpSubCommand();
		setEmptyTabMode(false);
	}

	@Override
	public boolean execute(CommandSender sender, String commandLabel, String[] args) {
		Player player = (Player) sender;

		Bukkit.getServer().broadcastMessage(PlayerMessages.getJoinMessage(player));
		Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.AQUA + "The join message above for " + player.getName() + " is fake");

		return true;
	}
}