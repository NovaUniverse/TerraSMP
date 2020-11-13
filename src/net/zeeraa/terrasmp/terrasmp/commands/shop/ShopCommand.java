package net.zeeraa.terrasmp.terrasmp.commands.shop;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionDefault;

import net.zeeraa.novacore.spigot.command.AllowedSenders;
import net.zeeraa.novacore.spigot.command.NovaCommand;
import net.zeeraa.terrasmp.terrasmp.TerraSMP;
import net.zeeraa.terrasmp.terrasmp.modules.TerraSMPShop;

public class ShopCommand extends NovaCommand {
	public ShopCommand() {
		super("shop", TerraSMP.getInstance());

		setAllowedSenders(AllowedSenders.PLAYERS);
		setDescription("Open the shop");
		setUseage("/shop");
		setPermission("terrasmp.command.shop");
		setPermissionDefaultValue(PermissionDefault.TRUE);
		setPermissionDescription("Access to the shop");
		setEmptyTabMode(true);
		addHelpSubCommand();
	}

	@Override
	public boolean execute(CommandSender sender, String commandLabel, String[] args) {
		if (TerraSMPShop.getInstance().isEnabled()) {
			Player player = (Player) sender;
			
			return TerraSMPShop.getInstance().open(player);
		} else {
			sender.sendMessage(ChatColor.RED + "The shop is not enabled right now");
		}
		return true;
	}
}