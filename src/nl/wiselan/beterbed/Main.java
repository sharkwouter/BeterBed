package nl.wiselan.beterbed;

import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBedEnterEvent.BedEnterResult;
import org.bukkit.event.player.PlayerBedLeaveEvent;
import org.bukkit.plugin.java.JavaPlugin;

import org.bukkit.attribute.Attribute;

public class Main extends JavaPlugin implements Listener {

	@Override
	public void onEnable() {
		getServer().getPluginManager().registerEvents(this, this);
	}

	@Override
	public void onDisable() {
		
	}

	/*
	 * Allow players to enter a bed during the day and remove armor and weapons laying in bed
	 */
	@EventHandler
	public void OnPlayerBedEnterEvent(PlayerBedEnterEvent e) {
		BedEnterResult result = e.getBedEnterResult();
	
		if (result == BedEnterResult.NOT_POSSIBLE_NOW) {
			//Allow players to use beds during the day
			e.setUseBed(Result.ALLOW);
		}
	}

	/*
	 * Reset the player's health and returns the armor and weapons when exiting the bed
	 */
	@EventHandler
	public void OnPlayerBedLeaveEvent(PlayerBedLeaveEvent e) {
		Player player = e.getPlayer();

		//Heal the player to full
		player.setHealth(player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
	}
}
