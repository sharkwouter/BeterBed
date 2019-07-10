package nl.wiselan.beterbed;

import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBedEnterEvent.BedEnterResult;
import org.bukkit.event.player.PlayerBedLeaveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.attribute.Attribute;

public class Main extends JavaPlugin implements Listener {

	//Hashmaps used for temporarily saving player items
	static HashMap<Player, ItemStack[]> armorHashMap = new HashMap<Player, ItemStack[]>();
	static HashMap<Player, ItemStack> mainhandHashmap = new HashMap<Player, ItemStack>();
	static HashMap<Player, ItemStack> offhandHashmap = new HashMap<Player, ItemStack>();


	@Override
	public void onEnable() {
		getServer().getPluginManager().registerEvents(this, this);
	}

	@Override
	public void onDisable() {
		for(Map.Entry<Player, ItemStack[]> entry : armorHashMap.entrySet()){
		    restoreItems(entry.getKey(), false);
		}
	}

	/*
	 * Allow players to enter a bed during the day and remove armor and weapons laying in bed
	 */
	@EventHandler
	public void OnPlayerBedEnterEvent(PlayerBedEnterEvent e) {
		BedEnterResult result = e.getBedEnterResult();
		
		if (result == BedEnterResult.OK) {
			Player player = e.getPlayer();
			saveItems(player);
			removeItems(player);
		} else if (result == BedEnterResult.NOT_POSSIBLE_NOW) {
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

		restoreItems(player);
	}

	private void saveItems(Player player) {
		PlayerInventory inventory = player.getInventory();

		//Save the armor and weapons the player has equipped
		armorHashMap.put(player,  inventory.getArmorContents());
		mainhandHashmap.put(player, inventory.getItemInMainHand());
		offhandHashmap.put(player, inventory.getItemInOffHand());
	}

	private void removeItems(Player player) {
		PlayerInventory inventory = player.getInventory();

		//Remove armor and weapons from the player
		inventory.setArmorContents(new ItemStack[]{});
		inventory.setItemInMainHand(null);
		inventory.setItemInOffHand(null);
	}

	private void restoreItems(Player player, boolean cleanup) {
		PlayerInventory inventory = player.getInventory();

		//Return the items taken from the player when entering the bed
		//Give the player their armor back
		ItemStack[] armor = (ItemStack[]) armorHashMap.get(player);
		inventory.setArmorContents(armor);

		//Give the player their main hand item back
		ItemStack mainhand = (ItemStack) mainhandHashmap.get(player);
		inventory.setItemInMainHand(mainhand);

		//Give the player their off hand item back
		ItemStack offhand = (ItemStack) offhandHashmap.get(player);
		inventory.setItemInOffHand(offhand);

		//Has to be seperate to prevent the function from deleting entries while looping
		if (cleanup) {
			//Remove the entries from the hashmap
			armorHashMap.remove(player);
			mainhandHashmap.remove(player);
			offhandHashmap.remove(player);
		}
	}

	//This function is used when not looped
	private void restoreItems(Player player) {
		restoreItems(player, true);
	}
}
