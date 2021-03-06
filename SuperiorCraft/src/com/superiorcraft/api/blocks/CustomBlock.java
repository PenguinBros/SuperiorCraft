package com.superiorcraft.api.blocks;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import com.superiorcraft.SuperiorCraft;
import com.superiorcraft.api.ElevatorInstance;
import com.superiorcraft.api.Flag;
import com.superiorcraft.api.more.UraniumFuelRod;
import com.superiorcraft.api.more.UraniumOre;
import com.superiorcraft.city.StopBlock;
import com.superiorcraft.trollcraft.BlockBreaker;
import com.superiorcraft.trollcraft.GhostBlock;
import com.superiorcraft.trollcraft.Keycode;
import com.superiorcraft.trollcraft.SlickDoor;
import com.superiorcraft.trollcraft.SlickDoorFrame;

public class CustomBlock implements Listener, CommandExecutor, TabCompleter {
	
	public static ArrayList<CustomBlock> blocks = new ArrayList<CustomBlock>();
	
	private Material itemMaterial = Material.MONSTER_EGG;
	private String name;
	private String id;
	
	public CustomBlock(String name, String id) {
		super();
		this.name = name.replace('&', '�');
		this.id = id;
		
		registerInstances();
		System.out.println("Block Init: " + id);
		
		CustomBlock.blocks.add(this);
	}
	
	public void registerInstances() {
		for (World w : Bukkit.getServer().getWorlds()) {
			for (ArmorStand e : w.getEntitiesByClass(ArmorStand.class)) {
				if (e.getCustomName() != null && e.getCustomName().equals(getName())) {
					ArmorStand te = null;
					for (Entity en : e.getNearbyEntities(0.5, 0.5, 0.5)) {
						if (en.getCustomName() != null && en.getCustomName().equals("CustomBlock")) {
							te = (ArmorStand) en;
						}
					}
					//CustomBlockTexture t =
					if (te != null) {
						CustomBlockInstance.addBlockInstance(new CustomBlockInstance(e));
					}
					else {
					//	new CustomBlockInstance(e);
					}
					Bukkit.getScheduler().scheduleSyncRepeatingTask(SuperiorCraft.plugin, new Runnable() {
						@Override
						public void run() {
							if (e.getCustomName() != null && e.getCustomName().equals(getName()) && e.getLocation().add(-0.5, 0, -0.5).getBlock().getType() == Material.AIR) {
								removeBlock(new BlockBreakEvent(e.getLocation().add(-0.5, 0, -0.5).getBlock(), null));
								//System.out.println("A");
							}
						}
					}, 0, 0);
					System.out.println("Block Instance Init: " + this.getName() + ":" + e.getUniqueId());
				}
			}
		}
	}
	
	public CustomBlockInstance getInstance(ArmorStand s) {
		//System.out.println(s.getLocation().add(-0.5, 0, -0.5).getBlock().getType());
		Bukkit.getScheduler().scheduleSyncRepeatingTask(SuperiorCraft.plugin, new Runnable() {
			@Override
			public void run() {
				if (s.getCustomName() != null && s.getCustomName().equals(getName()) && s.getLocation().add(-0.5, 0, -0.5).getBlock().getType() == Material.AIR) {
					removeBlock(new BlockBreakEvent(s.getLocation().add(-0.5, 0, -0.5).getBlock(), null));
					//System.out.println("A");
				}
			}
		}, 0, 0);
		return null;
	}
	
	public void setItemMaterial(Material itemMaterial) {
		this.itemMaterial = itemMaterial;
	}
	
	public Material getItemMaterial() {
		return itemMaterial;
	}
	
	public String getName() {
		return name;
	}

	public String getId() {
		return id;
	}

	public void load() {
		// Ghost Block
		
		CustomBlock gb = new GhostBlock("&f&lGhost Block", "trollcraft:ghost_block");
    	SuperiorCraft.plugin.getServer().getPluginManager().registerEvents(gb, SuperiorCraft.plugin);
    	
		// Slick Door
		
		CustomBlock sd = new SlickDoor("&f&lSlick Door", "trollcraft:slick_door");
    	SuperiorCraft.plugin.getServer().getPluginManager().registerEvents(sd, SuperiorCraft.plugin);
    	
		// Slick Door Frame
		
		CustomBlock sdf = new SlickDoorFrame("&f&lSlick Door Frame", "trollcraft:slick_door_frame");
    	SuperiorCraft.plugin.getServer().getPluginManager().registerEvents(sdf, SuperiorCraft.plugin);
    	
		// Block Breaker
    	
    	SuperiorCraft.plugin.getServer().getScheduler().scheduleSyncRepeatingTask(SuperiorCraft.plugin, new Runnable() {
    		@Override
    		public void run() {
    			for (World world : SuperiorCraft.plugin.getServer().getWorlds()) {
    				for (Entity en : world.getEntities()) {
    					if (en.getCustomName() != null && en.getCustomName().contains("Block Breaker")) {
    						Location l = en.getLocation();
    						l.add(-0.5, 0, -0.5);
    						
    						if (en.getWorld().getBlockAt(l).isBlockPowered()) {
    							l.add(0, -1, 0);
    							en.getWorld().getBlockAt(l).breakNaturally();
    						}
    					}
    				}
    			}
    		}
    	}, 0L, 2L);
		
		CustomBlock bb = new BlockBreaker("&f&lBlock Breaker", "trollcraft:block_breaker");
    	SuperiorCraft.plugin.getServer().getPluginManager().registerEvents(bb, SuperiorCraft.plugin);
    	
    	// Keypad
		
    	CustomBlock kc = new Keycode("&f&lKey Pad", "trollcraft:key_pad");
    	SuperiorCraft.plugin.getServer().getPluginManager().registerEvents(kc, SuperiorCraft.plugin);
    	
    	// Flag
    	
    	CustomBlock flag = new Flag("&f&lFlag", "ctf:flag");
    	SuperiorCraft.plugin.getServer().getPluginManager().registerEvents(flag, SuperiorCraft.plugin);
    	
    	/* MicroBlocks
    	
    	CustomBlockLoader mb = new MicroBlocks("&f&lMicro Block", "forge:micro_block");
    	Main.plugin.getServer().getPluginManager().registerEvents(mb, Main.plugin);*/
    	
    	// Uranium Ore
    	
    	CustomBlock urore = new UraniumOre("Uranium Ore", "more:uranium_ore");
    	SuperiorCraft.plugin.getServer().getPluginManager().registerEvents(urore, SuperiorCraft.plugin);
    	
    	// Uranium Fuel Rod
    
    	CustomBlock ufrore = new UraniumFuelRod("Uranium Fuel Rod", "more:uranium_fuel_rod");
    	SuperiorCraft.plugin.getServer().getPluginManager().registerEvents(ufrore, SuperiorCraft.plugin);
    	
    	// Stop Block
    	
    	CustomBlock hbsb = new StopBlock("Stop Block", "city:stop_block");
    	SuperiorCraft.plugin.getServer().getPluginManager().registerEvents(hbsb, SuperiorCraft.plugin);
	}
	
	public boolean placeBlock(ArmorStand e, Player p) {
		//e.setHelmet(new ItemStack(Material.STONE, 1));
		
		e.getWorld().getBlockAt(e.getLocation()).setType(Material.STONE);
		
		return true;
	}
	
	//@EventHandler
	public void removeBlock(BlockBreakEvent e) {
			for (Entity en : e.getBlock().getWorld().getEntities()) {
				if (en.getCustomName() != null && en.getCustomName().equals(getName()) && en.getLocation().add(-0.5, 0, -0.5).equals(e.getBlock().getLocation())) {
					en.remove();
					en.getWorld().getBlockAt(en.getLocation().add(-0.5, 0, -0.5)).setType(Material.AIR);
					
					ItemStack block = new ItemStack(Material.MONSTER_EGG, 1);
					
					ItemMeta bmeta = block.getItemMeta();
					
					bmeta.setDisplayName(name);
					
					block.setItemMeta(bmeta);
					
					if (e.getPlayer() != null && e.getPlayer().getGameMode().equals(GameMode.CREATIVE)) {
						e.getPlayer().getInventory().addItem(block);
					} else {
						e.getBlock().getWorld().dropItemNaturally(en.getLocation().add(-0.5, 0, -0.5), block);
					}
				}
			}
		//}
	}
	
	public void onInteract(PlayerInteractEvent e, Entity en) {
		
	}
	
	@EventHandler()
	public void onPlayerInteract(PlayerInteractEvent e) {
		if (e.getAction() != null && e.getAction() == Action.RIGHT_CLICK_BLOCK && e.getItem() != null && e.getItem().getType() != null && e.getItem().getType().equals(itemMaterial) && e.getItem().getItemMeta() != null && e.getItem().getItemMeta().getDisplayName() != null && e.getItem().getItemMeta().getDisplayName().equals(name)) {
			if (e.getItem().getAmount() - 1 != 0) {
				ItemStack it = e.getItem();
				it.setAmount(e.getItem().getAmount() - 1);
				e.getPlayer().getInventory().setItem(e.getPlayer().getInventory().first(e.getItem()), it);
			}
			
			else {
				e.getPlayer().getInventory().clear(e.getPlayer().getInventory().first(e.getItem()));
			}
			
			//ArmorStand block = (ArmorStand) e.getClickedBlock().getLocation().getWorld().spawnEntity(e.getClickedBlock().getLocation().add(0.5, 0, 0.5), EntityType.ARMOR_STAND);
			ArmorStand block = null;
			if (e.getBlockFace() == BlockFace.UP) {
				block = (ArmorStand) e.getClickedBlock().getLocation().getWorld().spawnEntity(e.getClickedBlock().getLocation().add(0.5, 1, 0.5), EntityType.ARMOR_STAND);
			} else if (e.getBlockFace() == BlockFace.DOWN) {
				block = (ArmorStand) e.getClickedBlock().getLocation().getWorld().spawnEntity(e.getClickedBlock().getLocation().add(0.5, -1, 0.5), EntityType.ARMOR_STAND);
			} else if (e.getBlockFace() == BlockFace.NORTH) {
				block = (ArmorStand) e.getClickedBlock().getLocation().getWorld().spawnEntity(e.getClickedBlock().getLocation().add(0.5, 0, 0.5 - 1), EntityType.ARMOR_STAND);
			} else if (e.getBlockFace() == BlockFace.SOUTH) {
				block = (ArmorStand) e.getClickedBlock().getLocation().getWorld().spawnEntity(e.getClickedBlock().getLocation().add(0.5, 0, 0.5 + 1), EntityType.ARMOR_STAND);
			} else if (e.getBlockFace() == BlockFace.EAST) {
				block = (ArmorStand) e.getClickedBlock().getLocation().getWorld().spawnEntity(e.getClickedBlock().getLocation().add(0.5 + 1, 0, 0.5), EntityType.ARMOR_STAND);
			} else if (e.getBlockFace() == BlockFace.WEST) {
				block = (ArmorStand) e.getClickedBlock().getLocation().getWorld().spawnEntity(e.getClickedBlock().getLocation().add(0.5 - 1, 0, 0.5), EntityType.ARMOR_STAND);
			}
			block.setSmall(true);
			block.setGravity(false);
			block.setCustomName(name);
			block.setMarker(true);
			block.setVisible(false);
			block.addScoreboardTag("cblock");
			
			if (placeBlock(block, e.getPlayer()) == false) {
				block.remove();
				//e.getPlayer().sendMessage("a");
				
				ItemStack it = new ItemStack(itemMaterial, 1);
				
				ItemMeta bmeta = it.getItemMeta();
				
				bmeta.setDisplayName(name);
				
				it.setItemMeta(bmeta);
				
				e.getPlayer().getInventory().addItem(it);
			}
			else {
				getInstance(block);
			}
		}
		
		for (Entity en : e.getPlayer().getWorld().getEntities()) {
			if (e.getAction() != null && e.getHand() == EquipmentSlot.HAND && e.getAction() == Action.RIGHT_CLICK_BLOCK && en.getCustomName() != null && en.getCustomName().equals(getName()) && en.getLocation().add(-0.5, 0, -0.5).equals(e.getClickedBlock().getLocation())) {
				onInteract(e, en);
			}
		}
		
		
		/*else if (e.getPlayer().isSneaking()) {
			for (Entity en : e.getPlayer().getNearbyEntities(0, 0, 0)) {
				if (en.getCustomName().equals()) {
					en.remove();
					ItemStack wire = new ItemStack(Material.MONSTER_EGG, 64);
					
					ItemMeta wmeta = wire.getItemMeta();
					
					wmeta.setDisplayName("&4&lWire".replace('&', '�'));
					
					wire.setItemMeta(wmeta);
					
					e.getPlayer().getInventory().addItem(wire);
				}
			} 
		}*/
	}
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent e) {
		removeBlock(e);
		//System.out.println("a");
	}
	
	public static ItemStack getBlock(String id) {
		for (CustomBlock cil : blocks) {
			if (cil.id.contains(id)) {
				return cil.getItem();
			}
		}
		
		return null;
	}
	
	public ItemStack getItem() {
		ItemStack block = new ItemStack(itemMaterial, 1);
		
		ItemMeta bmeta = block.getItemMeta();
			
		bmeta.setDisplayName(name);
			
		block.setItemMeta(bmeta);
			
		return block;
	}
	
	public ItemStack getItem(String id) {
		for (CustomBlock cbl : blocks) {
    		if (cbl.id.equals(id)) {
    			ItemStack block = new ItemStack(itemMaterial, 1);
    			
    			ItemMeta bmeta = block.getItemMeta();
    				
    			bmeta.setDisplayName(name);
    				
    			block.setItemMeta(bmeta);
    				
    			return block;
    		}
    	}
		
		return null;
	}
	
	public void giveItem(CustomBlock cbl, Player player) {
		ItemStack block = new ItemStack(itemMaterial, 1);
		
		ItemMeta bmeta = block.getItemMeta();
			
		bmeta.setDisplayName(cbl.name);
			
		block.setItemMeta(bmeta);
			
		player.getInventory().addItem(block);
	}
	
	public void giveItem(CustomBlock cbl, Player player, int amount) {
		ItemStack block = new ItemStack(itemMaterial, amount);
		
		ItemMeta bmeta = block.getItemMeta();
			
		bmeta.setDisplayName(cbl.name);
			
		block.setItemMeta(bmeta);
			
		player.getInventory().addItem(block);
	}
	
	@Override
    public boolean onCommand(CommandSender sender,
            Command command,
            String label,
            String[] args) {
		
		if (command.getName().equalsIgnoreCase("getblock")) {
        	Player player = (Player) sender;
        	for (CustomBlock cbl : blocks) {
        		if (cbl.id.equals(args[0])) {
        			cbl.giveItem(cbl, player, 64);
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
    	if (command.getName().equalsIgnoreCase("getblock")) {
        	ArrayList<String> auto = new ArrayList<String>();
        	
        	for (CustomBlock cbl : blocks) {
        		if (cbl.id.startsWith(args[0])) {
        			auto.add(cbl.id);
        		}
        	}
        	
        	return auto;
    	}
    	
    	ArrayList<String> auto = new ArrayList<String>();

    	return auto;
    }
	
	@SuppressWarnings("deprecation")
	public static void particle(Location l, Entity e, Effect ef, Material param) {
		for (Player p : e.getWorld().getPlayers()) {
			p.playEffect(l, ef, param);
		}
	}
	
	public static ArrayList<Entity> getBlockByName(String name, Entity e) {
		ArrayList<Entity> r = new ArrayList<Entity>();
		
		for (Entity en : e.getWorld().getEntities()) {
			if (en.getCustomName() != null && en.getCustomName().equals(name)) {
				r.add(en);
			}
		}
		
		return r;
	}
	
}
