/*
 * Copyright HomerBond005
 * 
 *  Published under CC BY-NC-ND 3.0
 *  http://creativecommons.org/licenses/by-nc-nd/3.0/
 */
package de.HomerBond005.GroupBasedPVP;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Logger;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import ru.tehkode.permissions.PermissionGroup;
import ru.tehkode.permissions.PermissionManager;
import ru.tehkode.permissions.bukkit.PermissionsEx;
import com.platymuus.bukkit.permissions.*;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import de.HomerBond005.GroupBasedPVP.ConfigurationHolderSet.ConfigurationType;
import de.bananaco.bpermissions.api.ApiLayer;
import de.bananaco.bpermissions.api.util.CalculableType;
import org.anjocaido.groupmanager.GroupManager;
import org.anjocaido.groupmanager.permissions.AnjoPermissionsHandler;

public class GBP extends JavaPlugin{
	private File settingsFile;
	private FileConfiguration settings;
	private GBPListener playerlistener;
	private Metrics metrics;
    private PluginManager pm;
    private PermissionManager pexmanager;
    private PermissionsPlugin pbplugin;
    private GroupManager groupmanager;
    private int permSys;
    private boolean logConsole;
    private Logger log;
    private Updater updater;
    private Permission vault;
    private ConfigurationHolderSet confHolder;
    private WorldGuardPlugin wgp;
    private Map<String, Map<String, Set<String>>> worldCache;
    private Map<String, Map<Integer, Boolean>> modes;
    private boolean completelyDisabledPVP;
    
    /**
     * @see org.bukkit.plugin.java.JavaPlugin#onEnable()
     */
    @Override
	public void onEnable(){
		log = getLogger();
		pm = getServer().getPluginManager();
		settingsFile = new File (getDataFolder() + File.separator + "settings.yml");
		if(!setupPermissions()){
			return;
		}
		reload();
		playerlistener = new GBPListener(this);
	    pm.registerEvents(playerlistener, this);
		try {
			metrics = new Metrics(this);
			metrics.start();
		} catch (IOException e) {
			log.warning("Error while enabling Metrics.");
		}
		updater = new Updater(this, settings.getBoolean("updateReminderEnabled", true));
		getServer().getPluginManager().registerEvents(updater, this);
		log.info("is enabled.");
	}
    
    /**
     * @see org.bukkit.plugin.java.JavaPlugin#onDisable()
     */
    @Override
	public void onDisable(){
		log.info("is disabled.");
	}
    
    /**
     * Reload all the settings and config files
     */
    private void reload(){
    	if(!new File(getDataFolder(), "config.yml").exists()){
    		getConfig().options().header("Global configuration file for GroupBasedPVP\n" +
    				"All values inside this file will be inherited to all worlds and all regions\n" +
    				"The values inside this file will disallow pvp for the defined groups\n" +
    				"Example configuration:\n" +
    				"AttackerGroup: AttackedGroup # The AttackerGroup can't attack the AttackedGroup\n" +
    				"'*': admin # Noone can attack the group admin\n" +
    				"guest: '*' # The group guest can't attack anyone");
			getConfig().set("AttackerGroup", "AttackedGroup");
			getConfig().set("*", "admin");
			getConfig().set("guest", "*");
			saveConfig();
			log.info("config.yml created.");
		}
    	if(!settingsFile.exists()){
			File penaltiesold = new File(getDataFolder() + File.separator + "penalties.yml");
			if(penaltiesold.exists()){
				penaltiesold.renameTo(settingsFile);
				log.info("penalties.yml renamed to settings.yml.");
			}else{
				try{
					settingsFile.createNewFile();
					log.info("settings.yml created.");
				}catch(IOException e){
					e.printStackTrace();
				}
			}
		}
    	settings = YamlConfiguration.loadConfiguration(settingsFile);
		settings.options().copyDefaults(true);
		settings.addDefault("HealthAttackedPlayer", "0");
		settings.addDefault("HealthAttackingPlayer", "-5");
		settings.addDefault("CannotBeAttacked", "The player %p can't be attacked by anyone.");
		settings.addDefault("NoPermAttackAnyone", "You are not allowed to attack anyone.");
		settings.addDefault("GroupNoPermAttackAnyone", "The group %g is not allowed to attack anyone!");
		settings.addDefault("Group1NoPermAttackGroup2", "The group %g1 is not allowed to attack the group %g2!");
		settings.addDefault("logInConsole", true);
		settings.addDefault("completelyDisabledPVP", false);
		settings.addDefault("updateReminderEnabled", true);
		try {
			settings.save(settingsFile);
		} catch (IOException e) {
			log.warning("Error while saving to settings.yml!");
		}
		log.info("config.yml and settings.yml loaded.");
		
		if (pm.getPlugin("WorldGuard") != null || pm.getPlugin("WorldGuard") instanceof WorldGuardPlugin) {
	        wgp = (WorldGuardPlugin) pm.getPlugin("WorldGuard");
	        log.info("WorldGuard region handling enabled!");
	    }else{
	    	wgp = null;
	    	log.info("WorldGuard region handling disabled!");
	    }
		
		confHolder = new ConfigurationHolderSet();
		modes = new HashMap<String, Map<Integer, Boolean>>();
		worldCache = new HashMap<String, Map<String, Set<String>>>();
		
		List<World> worlds = getServer().getWorlds();
		for(World world : worlds){
			File worldFile = new File(getDataFolder()+File.separator+"worlds"+File.separator+world.getName()+".yml");
			boolean isNew = !worldFile.exists();
			if(isNew)
				try{
					worldFile.createNewFile();
				}catch (IOException e){}
			FileConfiguration temp = YamlConfiguration.loadConfiguration(worldFile);
			String header = "Configuration for the world '"+world.getName()+"'\n" +
					"All values will expand the values from the global config.\n" +
					"Use the same format as in the global config. Example configuration:\n" +
					"modeWorld: disallow # all values in world be added to the global configuration\n" +
					"modeRegions: allow # all values in the regions will be subtracted from th global/world configurations\n" +
					"world:\n" +
					"  guest: admin # guest is not allowed to attack admin\n" +
					"regions:\n" +
					"  pvpzone: # a zone where everyone should attack everyone\n" +
					"    guest: admin # allow guest to attack admin";
			if(isNew){
				temp.options().header(header);
			}
			temp.addDefault("modeWorld", "disallow");
			temp.addDefault("modeRegions", "disallow");
			temp.addDefault("world", new HashMap<String, Object>());
			temp.addDefault("regions", new HashMap<String, Object>());
			temp.options().copyDefaults(true);
			try {
				temp.save(worldFile);
			} catch (IOException e) {}
			temp = YamlConfiguration.loadConfiguration(worldFile);
			Map<String, Set<String>> tempWorldConf = new HashMap<String, Set<String>>();
			for(String group : temp.getConfigurationSection("world").getKeys(false)){
				tempWorldConf.put(group.toLowerCase(), new HashSet<String>(Arrays.asList(temp.getString("global."+group).toLowerCase().split(", "))));
			}
			confHolder.add(new ConfigurationHolder(world.getName(), world.getName(), ConfigurationType.WORLD, tempWorldConf));
			for(String region : temp.getConfigurationSection("regions").getKeys(false)){
				Map<String, Set<String>> regionConf = new HashMap<String, Set<String>>();
				for(String group : temp.getConfigurationSection("regions."+region).getKeys(false)){
					regionConf.put(group.toLowerCase(), new HashSet<String>(Arrays.asList(temp.getString("regions."+region+"."+group).toLowerCase().split(", "))));
				}
				confHolder.add(new ConfigurationHolder(world.getName(), region, ConfigurationType.REGION, regionConf));
			}
			Map<Integer, Boolean> modesforworld = new HashMap<Integer, Boolean>();
			if(temp.getString("modeWorld").equalsIgnoreCase("allow")){
				modesforworld.put(0, true);
				worldCache.put(world.getName(), subtractConfiguration(getGlobalConfiguration(), getWorldConfiguration(world.getName())));
			}else{
				modesforworld.put(0, false);
				worldCache.put(world.getName(), addConfiguration(getGlobalConfiguration(), getWorldConfiguration(world.getName())));
			}
			if(temp.getString("modeRegions").equalsIgnoreCase("allow"))
				modesforworld.put(1, true);
			else
				modesforworld.put(1, false);
			modes.put(world.getName(), modesforworld);
			completelyDisabledPVP = settings.getBoolean("completelyDisabledPVP");
			log.info("Loaded world configuration in '"+world.getName()+".yml'.");
		}
		logConsole = settings.getBoolean("logInConsole", true);
    }
    
    /**
     * Setup the permission system in GroupBasedPVP
     * @return False if no permission plugin was found
     */
    private boolean setupPermissions(){
    	if(pm.getPlugin("Vault") != null){
    		RegisteredServiceProvider<Permission> permissionProvider = Bukkit.getServer().getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);
			if (permissionProvider != null) {
	            vault = permissionProvider.getProvider();
	            log.info("Using Vault!");
	            permSys = 5;
	        }
    	}else if(pm.getPlugin("PermissionsBukkit") != null){
    		pbplugin = (PermissionsPlugin)pm.getPlugin("PermissionsBukkit");
    		permSys = 1;
    		log.info("using PermissionsBukkit.");
    	}else if(pm.getPlugin("PermissionsEx") != null){
    		pexmanager = PermissionsEx.getPermissionManager();
    		permSys = 2;
    		log.info("using PermissionsEx.");
    	}else if(pm.getPlugin("bPermissions") != null){
			permSys = 3;
			log.info("using bPermissions.");
    	}else if(pm.getPlugin("GroupManager") != null){
    		groupmanager = (GroupManager) pm.getPlugin("GroupManager");
			permSys = 4;
			log.info("using GroupManager.");
    	}else{
    		log.warning("Please install PermissionsBukkit, PermissionsEx, bPermissions, GroupManager!");
    		pm.disablePlugin(this);
    		return false;
    	}
		return true;
    }
    
    /**
     * Is PVP completely disabled?
     * @return A boolean
     */
    public boolean isPVPCompletelyDisabled(){
    	return completelyDisabledPVP;
    }
    
    /**
     * Get all groups where the player is a member
     * @param player A player
     * @return An array with the group names
     */
	public String[] getGroups(Player player){
		if(permSys == 1){
			PermissionInfo playerinfo = pbplugin.getPlayerInfo(player.getName());
			if(playerinfo == null)
				return new String[0];
			List<com.platymuus.bukkit.permissions.Group> groups = playerinfo.getGroups();
			List<String> groupnames = new ArrayList<String>();
			for(com.platymuus.bukkit.permissions.Group group : groups){
				groupnames.add(group.getName());
			}
			return groupnames.toArray(new String[0]);
		}else if(permSys == 2){
			PermissionGroup[] groups = pexmanager.getUser(player).getGroups(player.getWorld().getName());
			String[] groupnames = new String[groups.length];
			for(int i = 0; i < groups.length; i++){
				groupnames[i] = groups[i].getName();
			}
			return groupnames;
		}else if(permSys == 3){
			return ApiLayer.getGroups(player.getWorld().getName(), CalculableType.USER, player.getName());
		}else if(permSys == 4){
			AnjoPermissionsHandler holder = groupmanager.getWorldsHolder().getWorldPermissionsByPlayerName(player.getName());
			if (holder == null) {
	            return new String[0];
	        }
	        return holder.getGroups(player.getName());
		}else if(permSys == 5){
			return vault.getPlayerGroups(player);
		}else{
			return new String[0];
		}
	}
	
	/**
	 * Check if a player is a member in a group
	 * @param player The player
	 * @param group The name of the group
	 * @return Is the player a member of this group?
	 */
	public boolean inGroup(Player player, String group){
		String[] groups = getGroups(player);
		for(String groupGot : groups){
			if(groupGot.equalsIgnoreCase(group))
				return true;
		}
		return false;
	}
	
	/**
	 * Check if a player has a permission
	 * @param player The player
	 * @param permission The permission node that should be checked
	 * @return Does the player has the permission?
	 */
	public boolean hasPermission(Player player, String permission){
		if(permSys == 1){
			return player.hasPermission(permission);
		}else if(permSys == 2){
			return pexmanager.has(player, permission);
		}else if(permSys == 3){
			return ApiLayer.hasPermission(player.getWorld().getName(), CalculableType.USER, player.getName(), permission);
		}else if(permSys == 4){
			AnjoPermissionsHandler holder = groupmanager.getWorldsHolder().getWorldPermissionsByPlayerName(player.getName());
			if (holder == null) {
	            return false;
	        }
	        return holder.permission(player.getName(), permission);
		}else if(permSys == 5){
			return vault.has(player, permission);
		}else{
			return false;
		}
	}
	
	/**
	 * Print a console message
	 * @param msg The message that should be printed into the console
	 */
	public void printConsoleMsg(String msg){
		if(logConsole)
			getServer().getConsoleSender().sendMessage("[GroupBasedPVP]: "+msg);
	}
	
	/**
	 * Get all values from settings.yml
	 * @return A string array that contains all data from the settings.yml
	 */
	public String[] getSettings(){
		String[] temparr = new String[6];
		temparr[0] = settings.getString("HealthAttackingPlayer", "-5");
		temparr[1] = settings.getString("HealthAttackedPlayer", "0");
		temparr[2] = settings.getString("CannotBeAttacked", "The player %p can't be attacked by anyone.");
		temparr[3] = settings.getString("NoPermAttackAnyone", "You are not allowed to attack anyone.");
		temparr[4] = settings.getString("GroupNoPermAttackAnyone", "The group %g is not allowed to attack anyone!");
		temparr[5] = settings.getString("Group1NoPermAttackGroup2", "The group %g1 is not allowed to attack the group %g2!");
		return temparr;
	}
	
	
	
	/**
	 * Get the matching configuration at the given location with the region configuration, the world configuration and the global configuration
	 * @param loc The location that should be checked
	 * @return The configuration as Map with the group name as key and the disallowed groups as value
	 */
	public Map<String, Set<String>> getMatchingConfigurationAtLocation(Location loc){
		Map<String, Set<String>> conf = getWorldConfigurationInCache(loc.getWorld().getName());
		if(wgp != null){
			RegionManager rm = wgp.getRegionManager(loc.getWorld());
			if(rm != null){
				ApplicableRegionSet regions = rm.getApplicableRegions(loc);
				for(ProtectedRegion region : regions){
					if(!modes.get(loc.getWorld().getName()).get(1))
						conf = addConfiguration(conf, getRegionConfiguration(region.getId(), loc.getWorld().getName()));
					else
						conf = subtractConfiguration(conf, getRegionConfiguration(region.getId(), loc.getWorld().getName()));
				}
			}
		}
		return conf;
	}
	
	/**
	 * Get all disallowed groups for one group at a location with all configurations
	 * @param loc The location where the matching world and region configurations should be loaded
	 * @param group The name of the group
	 * @return A Set with all disallowed groups
	 */
	public Set<String> getDisallowedGroupsAtLocationForGroup(Location loc, String group){
		group = group.toLowerCase();
		Map<String, Set<String>> groups = getMatchingConfigurationAtLocation(loc);
		if(groups.containsKey(group))
			return groups.get(group);
		return new HashSet<String>();
	}
	
	
	
	/**
	 * Get the global configuration without the per-world and per-region configurations
	 * @return A Map with the name of a group as key and a Set with disallowed groups as value
	 */
	public Map<String, Set<String>> getGlobalConfiguration(){
		Map<String, Set<String>> temp = new HashMap<String, Set<String>>();
		Set<String> groups = getConfig().getKeys(false);
		for(String group : groups){
			temp.put(group, new HashSet<String>(Arrays.asList(getConfig().getString(group).split(", "))));
		}
		return temp;
	}
	
	/**
	 * Get the configuration for a specified world without the global and the per-region configuration
	 * @param world The name of the world
	 * @return A Map with the name of a group as key and a Set with disallowed groups as value
	 */
	public Map<String, Set<String>> getWorldConfiguration(String world){
		ConfigurationHolder container = confHolder.getExact(world, world, ConfigurationType.WORLD);
		if(container != null)
			return container.getConfiguration();
		else
			return new HashMap<String, Set<String>>();
	}
	
	/**
	 * Get the configuration for the given world with the global configuration from the cache
	 * @param world The name of the world
	 * @return The configuration for the world, or an empty configuration
	 */
	public Map<String, Set<String>> getWorldConfigurationInCache(String world){
		if(worldCache.containsKey(world))
			return worldCache.get(world);
		return new HashMap<String, Set<String>>();
	}
	
	/**
	 * Get the region configuration for one region
	 * @param region The region name
	 * @param world The world where the region is located
	 * @return A Map with the name of a group as key and a Set with disallowed groups as value
	 */
	public Map<String, Set<String>> getRegionConfiguration(String region, String world){
		ConfigurationHolder match = confHolder.getExact(world, region, ConfigurationType.REGION);
		if(match != null)
			return match.getConfiguration();
		else
			return new HashMap<String, Set<String>>();
	}
	
	
	
	/**
	 * Match two configuration together
	 * @param configurationOne The first configuration
	 * @param configurationTwo The second configuration
	 * @return All values from both configurations mixed together
	 */
	private Map<String, Set<String>> addConfiguration(Map<String, Set<String>> configurationOne, Map<String, Set<String>> configurationTwo){
		for(Entry<String, Set<String>> group : configurationTwo.entrySet()){
			if(configurationOne.containsKey(group.getKey())){
				Set<String> newDG = configurationOne.get(group.getKey());
				newDG.addAll(group.getValue());
				configurationOne.put(group.getKey(), newDG);
			}else
				configurationOne.put(group.getKey(), group.getValue());
		}
		return configurationOne;
	}
	
	/**
	 * Remove values from a configuration
	 * @param configurationOne The base configuration
	 * @param configurationTwo The values that should be subtracted from configurationOne
	 * @return The leftover from configurationOne
	 */
	private Map<String, Set<String>> subtractConfiguration(Map<String, Set<String>> configurationOne, Map<String, Set<String>> configurationTwo){
		for(Entry<String, Set<String>> group : configurationTwo.entrySet()){
			if(configurationOne.containsKey(group.getKey())){
				Set<String> newDG = configurationOne.get(group.getKey());
				newDG.removeAll(group.getValue());
				configurationOne.put(group.getKey(), newDG);
			}
		}
		return configurationOne;
	}
}
