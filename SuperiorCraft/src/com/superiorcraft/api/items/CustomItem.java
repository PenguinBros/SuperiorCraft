package com.superiorcraft.api.items;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import com.superiorcraft.SuperiorCraft;
import com.superiorcraft.api.PowerCrystal;
import com.superiorcraft.api.more.UraniumIngot;

public class CustomItem implements Listener, CommandExecutor, TabCompleter {
	
	public static ArrayList<CustomItem> items = new ArrayList<CustomItem>();
	
	private String id;
	private ItemStack item;
	
	public CustomItem(ItemStack item, String id) {
		super();
		
		this.id = id.replace('&', '�');
		this.item = item;
		
		System.out.println("Item Init: " + id);
		items.add(this);
	}
	
	public CustomItem(ItemStack item, String id, String name) {
		super();
		
		this.id = id.replace('&', '�');
		
	 	ItemMeta meta = item.getItemMeta();
	 	meta.setDisplayName(name.replace('&', '�'));
	 	item.setItemMeta(meta);
		this.item = item;
		
		System.out.println("Item Init: " + id);
		items.add(this);
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public ItemStack getItem() {
		return item;
	}

	public void setItem(ItemStack item) {
		this.item = item;
	}

	public void load() {
		// Power Crystal
		
		ItemStack pcrys = new ItemStack(Material.DIAMOND);
    	ItemMeta pcrysm = pcrys.getItemMeta();
    	pcrysm.setDisplayName("&b&lPower Crystal".replace('&', '�'));
    	ArrayList<String> pcrysl = new ArrayList<String>();
    	pcrysl.add("&b&l0 / 100,000 RF".replace('&', '�'));
       	pcrysm.setLore(pcrysl);
       	pcrys.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 100);
    	pcrys.setItemMeta(pcrysm);
		
		CustomItem pc = new PowerCrystal(pcrys, "superiorcraft:power_crystal");
		
		SuperiorCraft.plugin.getServer().getPluginManager().registerEvents(pc, SuperiorCraft.plugin);
		
		// Uranium Ingot
		
		ItemStack uing = new ItemStack(Material.EMERALD);
    	ItemMeta uingm = uing.getItemMeta();
    	uingm.setDisplayName("&2Uranium Ingot".replace('&', '�'));
    	ArrayList<String> uingl = new ArrayList<String>();
    	uingl.add("&2A radioactive material".replace('&', '�'));
       	uingm.setLore(uingl);
       	uing.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 100);
    	uing.setItemMeta(uingm);
		
		CustomItem ui = new UraniumIngot(uing, "more:uranium_ingot");
		SuperiorCraft.plugin.getServer().getPluginManager().registerEvents(ui, SuperiorCraft.plugin);
	}
	
	public static ItemStack getItem(String id) {
		for (CustomItem cil : items) {
			if (cil.id.contains(id)) {
				return cil.item;
			}
		}
		
		return null;
	}
	
	public void giveItem(CustomItem cil, Player player) {
		player.getInventory().addItem(cil.item);
	}
	
	public void giveItem(CustomItem cil, Player player, int amount) {
		for (int i = 0; i < amount; i++)
			player.getInventory().addItem(cil.item);
	}
	
	@Override
    public boolean onCommand(CommandSender sender,
            Command command,
            String label,
            String[] args) {
		
		if (command.getName().equalsIgnoreCase("getitem")) {
			Player player = (Player) sender;
        	for (CustomItem cil : items) {
        		if (cil.id.equals(args[0])) {
        			if (args.length >= 2 && args[1] != null) {
        				cil.giveItem(cil, player, Integer.valueOf(args[1]));
        			}
        			
        			else{
        				cil.giveItem(cil, player);
        			}
        		}
        	}
        	
			//giveItem(args[0], player);
			
			
			return true;
		}
		
		return false;
	}
	
	@Override
    public List<String> onTabComplete(CommandSender sender,
            Command command,
            String label,
            String[] args) {
    	if (command.getName().equalsIgnoreCase("getitem")) {
        	ArrayList<String> auto = new ArrayList<String>();
        	
        	for (CustomItem cil : items) {
        		if (cil.id.startsWith(args[0])) {
        			auto.add(cil.id);
        		}
        	}
        	
        	return auto;
    	}
    	
    	ArrayList<String> auto = new ArrayList<String>();

    	return auto;
    }
	
}
