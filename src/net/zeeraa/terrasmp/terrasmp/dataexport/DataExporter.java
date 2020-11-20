package net.zeeraa.terrasmp.terrasmp.dataexport;

import java.lang.management.ManagementFactory;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.json.JSONArray;
import org.json.JSONObject;

import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.FactionColl;
import com.massivecraft.factions.entity.MFlag;
import com.massivecraft.factions.entity.MPlayer;

import net.zeeraa.novacore.spigot.NovaCore;

public class DataExporter {
	public static JSONObject export() {
		JSONObject json = new JSONObject();

		TimeZone tz = TimeZone.getDefault();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		df.setTimeZone(tz);

		json.put("updated-at", df.format(new Date()));
		json.put("time-zone", tz.getDisplayName(Locale.ENGLISH));
	
		/* Server */
		JSONObject server = new JSONObject();
		
		server.put("recent-tps", NovaCore.getInstance().getVersionIndependentUtils().getRecentTps());
		server.put("whitelist", Bukkit.hasWhitelist());
		server.put("uptime", ManagementFactory.getRuntimeMXBean().getUptime() / 1000);
		
		json.put("server", server);
		
		/* Players */
		JSONObject players = new JSONObject();
		JSONArray playerList = new JSONArray();
		
		players.put("max",Bukkit.getServer().getMaxPlayers());
		players.put("online",Bukkit.getServer().getOnlinePlayers().size());
		
		for(Player player : Bukkit.getServer().getOnlinePlayers()) {
			JSONObject jp = new JSONObject();
			
			jp.put("uuid", player.getUniqueId().toString());
			jp.put("name", player.getName());
			jp.put("display-name", player.getDisplayName());
			
			playerList.put(jp);
		}
		
		players.put("list", playerList);
		
		json.put("players", players);
		
		/* Factions */
		
		JSONArray factions = new JSONArray();
		
		for(Faction faction : FactionColl.get().getAll()) {
			JSONObject fj = new JSONObject();
			
			/* Misc */
			fj.put("name", faction.getName());
			fj.put("age", faction.getAge());
			fj.put("land-count", faction.getLandCount());
			
			/* Power */
			JSONObject power = new JSONObject();
		
			power.put("current", faction.getPower());
			power.put("max", faction.getPowerMax());
			power.put("boost", faction.getPowerBoost());
				
			fj.put("power", power);
			
			/* Flags */
			JSONObject flags = new JSONObject();
			
			for(MFlag flag : faction.getFlags().keySet()) {
				flags.put(flag.getName(), faction.getFlags().get(flag));
			}
			
			fj.put("flags", flags);
			
			/* Players */
			JSONArray fPlayers = new JSONArray();
			
			for(MPlayer player : faction.getMPlayers()) {
				JSONObject jp = new JSONObject();
				
				jp.put("uuid", player.getUuid());
				jp.put("name", player.getName());
				jp.put("role", player.getRole().name());
				
				/* Power */
				JSONObject ppower = new JSONObject();
			
				ppower.put("current", player.getPower());
				ppower.put("max", player.getPowerMax());
				ppower.put("boost", player.getPowerBoost());
					
				jp.put("power", power);
				
				fPlayers.put(jp);
			}
			
			fj.put("players", fPlayers);
			
			factions.put(fj);
		}
		
		json.put("factions", factions);

		return json;
	}
}