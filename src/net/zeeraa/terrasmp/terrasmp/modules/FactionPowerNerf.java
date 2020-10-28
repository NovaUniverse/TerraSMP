package net.zeeraa.terrasmp.terrasmp.modules;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.FactionColl;
import com.massivecraft.factions.entity.MPlayer;
import net.zeeraa.novacore.commons.log.Log;
import net.zeeraa.novacore.spigot.module.NovaModule;
import net.zeeraa.terrasmp.terrasmp.misc.FactionUtils;

public class FactionPowerNerf extends NovaModule implements Listener {
	private static FactionPowerNerf instance;
	private int playerLimit;

	public static FactionPowerNerf getInstance() {
		return instance;
	}

	@Override
	public String getName() {
		return "FactionPowerNerf";
	}

	@Override
	public void onLoad() {
		FactionPowerNerf.instance = this;
		this.playerLimit = 10;
	}

	public int getPlayerLimit() {
		return playerLimit;
	}

	public void setPlayerLimit(int playerLimit) {
		this.playerLimit = playerLimit;
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerDeath(PlayerDeathEvent e) {
		Player player = e.getEntity();

		if (!FactionUtils.hasFaction(player)) {
			return;
		}

		Faction faction = FactionColl.get().get(player);

		int players = faction.getMPlayers().size();

		if (players > playerLimit) {
			int extra = players - playerLimit;

			if (extra <= 0) {
				Log.error(getName(), "Value extra has an invalid value of: " + extra + " (extra <= 0)");
				return;
			}

			int loss = extra * 10;

			MPlayer mPlayer = MPlayer.get(player);

			double newPower = mPlayer.getPower() - ((double) loss);

			if (newPower < 0) {
				newPower = 0;
			}

			double powerLost = Math.abs(newPower - mPlayer.getPower());

			mPlayer.setPower(newPower);
			player.sendMessage(ChatColor.RED + "You lost an additional " + powerLost + " power since your faction is " + extra + "player" + (extra == 1 ? "" : "s") + " above the faction power nerf limit of " + playerLimit + " player" + (playerLimit == 1 ? "" : "s"));
		}
	}
}