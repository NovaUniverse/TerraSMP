package net.zeeraa.terrasmp.terrasmp.modules;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import net.zeeraa.novacore.commons.log.Log;
import net.zeeraa.novacore.spigot.module.NovaModule;

public class TerraSMPWhitelistOnJoin extends NovaModule implements Listener {
	@Override
	public String getName() {
		return "TerraSMPWhitelistOnJoin";
	}

	@Override
	public void onEnable() throws Exception {
		Log.warn("TerraSMPWhitelistOnJoin", "Players will be whitelisted when they join");
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerJoin(PlayerJoinEvent e) {
		Player player = e.getPlayer();

		if (!player.isWhitelisted()) {
			Log.info("TerraSMPWhitelistOnJoin", "Added player " + player.getName() + " to the whitelist");
			player.setWhitelisted(true);
		}
	}
}