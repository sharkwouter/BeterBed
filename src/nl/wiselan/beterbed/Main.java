package nl.wiselan.beterbed;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBedEnterEvent.BedEnterResult;
import org.bukkit.event.player.PlayerBedLeaveEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.ChatColor;

public class Main extends JavaPlugin implements Listener {
	
	@Override
	public void onEnable() {
		getServer().getPluginManager().registerEvents(this, this);
	}
	
	/*
	 * When trying to enter a bed during the day, set you spawn point anyway
	 * Normally this would not be possible
	 */
	@EventHandler
	public void OnPlayerBedEnterEvent(PlayerBedEnterEvent e) {
		BedEnterResult result = e.getBedEnterResult();
		
		if (result == BedEnterResult.NOT_POSSIBLE_NOW) {
			Player player = e.getPlayer();
			Block bed = e.getBed();
			player.setBedSpawnLocation(bed.getLocation());
			player.sendMessage(ChatColor.YELLOW + "Spawn point has been set");
		}
		
	}
	
	/*
	 * Reset the player's health when exiting a bed
	 */
	@EventHandler
	public void OnPlayerBedLeaveEvent(PlayerBedLeaveEvent e) {
		Player player = e.getPlayer();
		player.setHealth(20.0);
	}
}
