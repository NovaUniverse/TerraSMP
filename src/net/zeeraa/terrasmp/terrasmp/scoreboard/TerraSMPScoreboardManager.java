package net.zeeraa.terrasmp.terrasmp.scoreboard;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.massivecraft.factions.Rel;
import com.massivecraft.factions.entity.BoardColl;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.FactionColl;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.massivecore.ps.PS;

import net.zeeraa.novacore.commons.tasks.Task;
import net.zeeraa.novacore.spigot.module.NovaModule;
import net.zeeraa.novacore.spigot.module.modules.scoreboard.NetherBoardScoreboard;
import net.zeeraa.novacore.spigot.tasks.SimpleTask;

public class TerraSMPScoreboardManager extends NovaModule {
	public static final int FACTION_LINE = 1;
	public static final int PLAYER_FACTION_LINE = 3;
	public static final int PLAYER_POWER_LINE = 4;
	public static final int FACTION_POWER_LINE = 5;

	private Task updateTask;

	@Override
	public String getName() {
		return "TerraSMPScoreboardManager";
	}

	@Override
	public void onLoad() {
		updateTask = new SimpleTask(new Runnable() {
			@Override
			public void run() {
				for (Player player : Bukkit.getServer().getOnlinePlayers()) {
					MPlayer mPlayer = MPlayer.get(player);

					Faction faction = BoardColl.get().getFactionAt(PS.valueOf(player.getLocation()));
					Faction playerFaction = mPlayer.getFaction();

					if (faction == null) {
						NetherBoardScoreboard.getInstance().setPlayerLine(FACTION_LINE, player, ChatColor.GOLD + "at: " + ChatColor.YELLOW + "Wilderness");
					} else {
						if (faction.getId().equalsIgnoreCase(FactionColl.get().getWarzone().getId())) {
							NetherBoardScoreboard.getInstance().setPlayerLine(FACTION_LINE, player, ChatColor.GOLD + "at: " + ChatColor.RED + "Warzone");
						} else if (faction.getId().equalsIgnoreCase(FactionColl.get().getSafezone().getId())) {
							NetherBoardScoreboard.getInstance().setPlayerLine(FACTION_LINE, player, ChatColor.GOLD + "at: " + ChatColor.GREEN + "Safezone");
						} else if (faction.getId().equalsIgnoreCase(FactionColl.get().getNone().getId())) {
							NetherBoardScoreboard.getInstance().setPlayerLine(FACTION_LINE, player, ChatColor.GOLD + "at: " + ChatColor.YELLOW + "Wilderness");
						} else {
							if (playerFaction != null) {
								if (playerFaction.getId() != FactionColl.get().getNone().getId()) {
									if (faction.getId() == playerFaction.getId()) {
										NetherBoardScoreboard.getInstance().setPlayerLine(FACTION_LINE, player, ChatColor.GOLD + "at: " + ChatColor.GREEN + faction.getName());
									} else {
										Rel rel = faction.getRelationTo(playerFaction);

										ChatColor color = ChatColor.AQUA;

										if (rel == Rel.ALLY) {
											color = ChatColor.AQUA;
										} else if (rel == Rel.ENEMY) {
											color = ChatColor.RED;
										} else if (rel == Rel.NEUTRAL) {
											color = ChatColor.YELLOW;
										}

										NetherBoardScoreboard.getInstance().setPlayerLine(FACTION_LINE, player, ChatColor.GOLD + "at: " + color + faction.getName());
									}
								} else {
									NetherBoardScoreboard.getInstance().setPlayerLine(FACTION_LINE, player, ChatColor.GOLD + "at: " + ChatColor.YELLOW + faction.getName());
								}
							} else {
								NetherBoardScoreboard.getInstance().setPlayerLine(FACTION_LINE, player, ChatColor.GOLD + "at: " + ChatColor.YELLOW + faction.getName());
							}
						}
					}

					String playerPower = ChatColor.GOLD + "pwr: " + ChatColor.AQUA + ((int) mPlayer.getPower()) + "/" + ((int) mPlayer.getPowerMax());
					String factionPower = ChatColor.GOLD + "fp: " + ChatColor.AQUA;

					boolean hasFaction = false;

					if (playerFaction != null) {
						if (playerFaction.getId() != FactionColl.get().getNone().getId()) {
							hasFaction = true;
						}
					}

					String in = ChatColor.GOLD + "Faction: ";

					if (hasFaction) {
						factionPower += ((int) playerFaction.getPower()) + "/" + ((int) playerFaction.getPowerMax());
						in += ChatColor.AQUA + playerFaction.getName();
					} else {
						factionPower += "---/---";
						in += ChatColor.RED + "No faction";
					}

					NetherBoardScoreboard.getInstance().setPlayerLine(PLAYER_FACTION_LINE, player, in);
					NetherBoardScoreboard.getInstance().setPlayerLine(PLAYER_POWER_LINE, player, playerPower);
					NetherBoardScoreboard.getInstance().setPlayerLine(FACTION_POWER_LINE, player, factionPower);
				}
			}
		}, 20L);
	}

	@Override
	public void onEnable() throws Exception {
		updateTask.start();
	}

	@Override
	public void onDisable() throws Exception {
		Task.tryStopTask(updateTask);
	}
}