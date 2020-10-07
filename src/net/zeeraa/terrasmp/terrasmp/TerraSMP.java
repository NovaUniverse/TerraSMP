package net.zeeraa.terrasmp.terrasmp;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.plugin.Plugin;

import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.FactionColl;
import com.massivecraft.factions.entity.MPlayer;

import net.zeeraa.novacore.commons.NovaCommons;
import net.zeeraa.novacore.commons.log.Log;
import net.zeeraa.novacore.spigot.command.CommandRegistry;
import net.zeeraa.novacore.spigot.novaplugin.NovaPlugin;
import net.zeeraa.terrasmp.terrasmp.commands.terrasmp.TerraSMPCommand;
import net.zeeraa.terrasmp.terrasmp.data.Continent;
import net.zeeraa.terrasmp.terrasmp.data.ContinentReader;
import net.zeeraa.terrasmp.terrasmp.data.PlayerData;
import net.zeeraa.terrasmp.terrasmp.data.PlayerDataManager;
import net.zeeraa.terrasmp.terrasmp.modules.DisableEyeOfEnder;
import net.zeeraa.terrasmp.terrasmp.modules.TerraSMPWhitelistOnJoin;
import net.zeeraa.terrasmp.terrasmp.signs.ContinentSelectorSigns;

public class TerraSMP extends NovaPlugin implements Listener {
	private static TerraSMP instance;

	private List<Continent> continents;

	private File playerDataFolder;

	private Location spawnLocation;

	public static TerraSMP getInstance() {
		return instance;
	}

	public List<Continent> getContinents() {
		return continents;
	}

	public Location getSpawnLocation() {
		return spawnLocation;
	}

	public File getPlayerDataFolder() {
		return playerDataFolder;
	}

	@Nullable
	public Continent getContinent(String name) {
		if (name != null) {
			for (Continent continent : continents) {
				if (continent.getName().equalsIgnoreCase(name)) {
					return continent;
				}
			}
		}

		return null;
	}

	@Override
	public void onEnable() {
		TerraSMP.instance = this;
		continents = new ArrayList<Continent>();
		playerDataFolder = new File(getDataFolder().getPath() + File.separator + "userdata");

		try {
			FileUtils.forceMkdir(getDataFolder());
			FileUtils.forceMkdir(playerDataFolder);

			saveDefaultConfig();
		} catch (Exception e) {
			Log.fatal("TerraSMP", "Failed to setup data directory");
			Bukkit.getPluginManager().disablePlugin(this);
			return;
		}

		spawnLocation = new Location(Bukkit.getServer().getWorlds().get(0), getConfig().getDouble("spawn_x"), getConfig().getDouble("spawn_y"), getConfig().getDouble("spawn_z"), getConfig().getLong("spawn_yaw"), getConfig().getLong("spawn_pitch"));

		File continentFile = new File(getDataFolder().getPath() + File.separator + "continents.json");
		if (continentFile.exists()) {
			try {
				continents = ContinentReader.readContinents(continentFile, Bukkit.getServer().getWorlds().get(0));
				Log.info("TerraSMP", continents.size() + " continents loaded");
			} catch (Exception e) {
				Log.fatal("Failed to read continent file " + continentFile.getPath() + ". Caused by " + e.getClass().getName());
				e.printStackTrace();
			}
		} else {
			Log.error("TerraSMP", "Continent file: " + continentFile.getPath() + " does not exist");
		}

		CommandRegistry.registerCommand(new TerraSMPCommand());

		Bukkit.getServer().getPluginManager().registerEvents(this, this);

		for (Player player : Bukkit.getServer().getOnlinePlayers()) {
			PlayerDataManager.getPlayerData(player.getUniqueId());
		}

		loadModule(TerraSMPWhitelistOnJoin.class);
		loadModule(ContinentSelectorSigns.class, true);
		loadModule(DisableEyeOfEnder.class, true);
		
		if(getConfig().getBoolean("whitelist_player_after_join")) {
			enableModule(TerraSMPWhitelistOnJoin.class);
		}
	}

	@Override
	public void onDisable() {
		HandlerList.unregisterAll((Plugin) this);
		Bukkit.getScheduler().cancelTasks(this);

		PlayerDataManager.unloadAll();
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		Player player = e.getPlayer();

		PlayerData playerData = PlayerDataManager.getPlayerData(player.getUniqueId());

		if (getContinent(playerData.getStarterContinent()) == null) {
			NovaCommons.getAbstractAsyncManager().runSync(new Runnable() {
				@Override
				public void run() {
					player.sendMessage(ChatColor.GOLD + "Please select your starter continent");
					player.teleport(spawnLocation);
				}
			}, 4L);
		}
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e) {
		Player player = e.getPlayer();

		PlayerDataManager.unloadPlayerData(player.getUniqueId());
	}

	@EventHandler
	public void onPlayerRespawn(PlayerRespawnEvent e) {
		Player player = e.getPlayer();
		if (!e.isBedSpawn()) {
			MPlayer mPlayer = MPlayer.get(player);

			Faction faction = mPlayer.getFaction();

			boolean randomRespawnLocation = false;

			if (faction.getId().equalsIgnoreCase(FactionColl.get().getNone().getId()) || faction.getId().equalsIgnoreCase(FactionColl.get().getSafezone().getId()) || faction.getId().equalsIgnoreCase(FactionColl.get().getWarzone().getId())) {
				randomRespawnLocation = true;
			} else {
				System.out.println("faction.getHome() : " + faction.getHome());
				if (faction.getHome() == null) {
					randomRespawnLocation = true;
				}
			}

			if (randomRespawnLocation) {
				Continent continent = getContinent(PlayerDataManager.getPlayerData(player.getUniqueId()).getStarterContinent());

				if (continent != null) {
					Location location = continent.getRandomSpawnLocation();
					e.setRespawnLocation(location.add(0, 2, 0));
				}
			}
		}
	}
}