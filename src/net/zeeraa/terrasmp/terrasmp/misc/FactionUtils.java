package net.zeeraa.terrasmp.terrasmp.misc;

import java.util.UUID;

import org.bukkit.entity.Player;

import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.FactionColl;

public class FactionUtils {
	public static boolean hasFaction(Player player) {
		return FactionUtils.hasFaction(player.getUniqueId());
	}

	public static boolean hasFaction(UUID uuid) {
		Faction faction = FactionColl.get().get(uuid);
		
		if(faction == null) {
			return false;
		}

		return !isSystemFaction(faction);
	}
	
	public static boolean isSystemFaction(Faction faction) {
		if (faction.getId().equals(FactionColl.get().getWarzone().getName())) {
			return true;
		}

		if (faction.getId().equals(FactionColl.get().getSafezone().getName())) {
			return true;
		}

		if (faction.getId().equals(FactionColl.get().getNone().getName())) {
			return true;
		}
		
		return false;
	}
}