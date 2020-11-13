package net.zeeraa.terrasmp.terrasmp.commands.terrasmp;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.PermissionDefault;

import net.zeeraa.novacore.spigot.command.NovaCommand;
import net.zeeraa.terrasmp.terrasmp.TerraSMP;
import net.zeeraa.terrasmp.terrasmp.commands.terrasmp.debug.TerraSMPDebugCommand;
import net.zeeraa.terrasmp.terrasmp.commands.terrasmp.fake.FakeJoin;
import net.zeeraa.terrasmp.terrasmp.commands.terrasmp.fake.FakeLeave;

public class TerraSMPCommand extends NovaCommand {
	public TerraSMPCommand() {
		super("terrasmp", TerraSMP.getInstance());
		
		setDescription("Command to manage TerraSMP");
		setPermission("terrasmp.command.terrasmp");
		setPermissionDefaultValue(PermissionDefault.OP);
		setEmptyTabMode(true);
		
		addSubCommand(new TerraSMPDebugCommand());
		
		addSubCommand(new FakeJoin());
		addSubCommand(new FakeLeave());
		
		addHelpSubCommand();
	}
	
	@Override
	public boolean execute(CommandSender sender, String commandLabel, String[] args) {
		sender.sendMessage(ChatColor.GOLD + "use /terrasmp help for help");
		return true;
	}
}