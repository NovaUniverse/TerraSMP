package net.zeeraa.terrasmp.terrasmp.commands.terrasmp.fake;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionDefault;

import net.zeeraa.novacore.spigot.command.AllowedSenders;
import net.zeeraa.novacore.spigot.command.NovaSubCommand;
import net.zeeraa.terrasmp.terrasmp.misc.PlayerMessages;

public class FakeLeave extends NovaSubCommand {
	public FakeLeave() {
		super("fakeleave");

		setPermission("terrasmp.command.terrasmp.fakeleave");
		setPermissionDefaultValue(PermissionDefault.OP);
		setDescription("Fake a leave message");
		setAllowedSenders(AllowedSenders.PLAYERS);
		addHelpSubCommand();
		setEmptyTabMode(false);
	}

	@Override
	public boolean execute(CommandSender sender, String commandLabel, String[] args) {
		Player player = (Player) sender;

		Bukkit.getServer().broadcastMessage(PlayerMessages.getLeaveMessage(player));

		return true;
	}
}