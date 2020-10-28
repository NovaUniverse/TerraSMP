package net.zeeraa.terrasmp.terrasmp.modules;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import net.zeeraa.novacore.spigot.module.NovaModule;

public class HiddenJoinQuitMessages extends NovaModule implements Listener {
	@Override
	public String getName() {
		return "HiddenJoinQuitMessages";
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerJoin(PlayerJoinEvent e) {
		Player player = e.getPlayer();
		
		if(player.hasPermission("terrasmp.nojoinmessage")) {
			e.setJoinMessage(null);
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerQuit(PlayerQuitEvent e) {
		Player player = e.getPlayer();
		
		if(player.hasPermission("terrasmp.noquitmessage")) {
			e.setQuitMessage(null);
		}
	}
}