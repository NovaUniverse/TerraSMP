package net.zeeraa.terrasmp.terrasmp.signs;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import net.zeeraa.novacore.commons.log.Log;
import net.zeeraa.novacore.spigot.module.NovaModule;
import net.zeeraa.terrasmp.terrasmp.TerraSMP;
import net.zeeraa.terrasmp.terrasmp.data.Continent;
import net.zeeraa.terrasmp.terrasmp.data.PlayerDataManager;
import net.zeeraa.terrasmp.terrasmp.modules.HiddenPlayers;

public class ContinentSelectorSigns extends NovaModule implements Listener {
	@Override
	public String getName() {
		return "ContinentSelectorSigns";
	}

	@EventHandler
	public void onSignChange(SignChangeEvent e) {
		if (e.getPlayer().hasPermission("terrasmp.sign.selectcontinent")) {
			if (e.getLine(0).equalsIgnoreCase("[select continent]")) {
				Continent continent = TerraSMP.getInstance().getContinent(e.getLine(1));

				if (continent == null) {
					e.getPlayer().sendMessage(ChatColor.RED + "Could not find continent " + e.getLine(1));
					return;
				}

				e.setLine(0, ChatColor.BLUE + "[Select Continent]");
				e.setLine(1, continent.getDisplayName());
				e.setLine(2, "");
				e.setLine(3, "");
			}
		}
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = false)
	public void onPlayerInteract(PlayerInteractEvent e) {
		if (e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.LEFT_CLICK_BLOCK) {
			if (e.getClickedBlock().getType() == Material.SIGN_POST || e.getClickedBlock().getType() == Material.WALL_SIGN) {
				Log.trace(getName(), e.getPlayer() + " interacted with sign. " + e.getAction().name() + ". Canceled: " + e.isCancelled());

				if (e.getPlayer().getGameMode() != GameMode.SPECTATOR) {
					if (e.getClickedBlock().getState() instanceof Sign) {
						Sign sign = (Sign) e.getClickedBlock().getState();

						if (sign.getLines()[0].equalsIgnoreCase(ChatColor.BLUE + "[Select Continent]")) {
							Log.trace("ContinentSelectorSigns", e.getPlayer().getName() + " clicked a continent selector sign");

							for (Continent continent : TerraSMP.getInstance().getContinents()) {
								if (continent.getDisplayName().equalsIgnoreCase(sign.getLine(1))) {
									PlayerDataManager.getPlayerData(e.getPlayer().getUniqueId()).setStarterContinent(continent.getName());
									PlayerDataManager.savePlayerData(e.getPlayer().getUniqueId());

									HiddenPlayers.getInstance().showPlayer(e.getPlayer());

									Location location = continent.getRandomSpawnLocation();

									if (location != null) {
										e.getPlayer().teleport(location.add(0, 2, 0));
										return;
									}

									e.getPlayer().sendMessage(ChatColor.RED + "Failed to find a spawn location. Please try again");

									return;
								}
							}

							e.getPlayer().sendMessage(ChatColor.RED + "This sign seems to be broken. Please contact an admin about this");
						}
					}
				}
			}
		}
	}
}
