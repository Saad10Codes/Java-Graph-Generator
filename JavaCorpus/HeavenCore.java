package fr.heavencraft.heavencore;

import java.sql.Connection;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import fr.heavencraft.heavencore.api.UsersManager;
import fr.heavencraft.heavencore.api.WorldsManager;
import fr.heavencraft.heavencore.commands.SetspawnCommand;
import fr.heavencraft.heavencore.commands.SpawnCommand;
import fr.heavencraft.heavencore.commands.TpCommand;
import fr.heavencraft.heavencore.commands.TphereCommand;
import fr.heavencraft.heavencore.exceptions.PlayerNotFoundException;
import fr.heavencraft.heavencore.listeners.PlayerListener;

public class HeavenCore extends JavaPlugin
{
	private static final String DB_HOST = "localhost";
	private static final String DB_NAME = "minecraft";
	private static final String DB_USER = "root";
	private static final String DB_PASS = "";
	
	private static Connection connection;
	
	private UsersManager usersManager;
	private WorldsManager worldsManager;
	
	@Override
	public void onEnable()
	{
		try
		{
			Class.forName("com.mysql.jdbc.Driver");
			connection = java.sql.DriverManager.getConnection("jdbc:mysql://" + DB_HOST + "/" + DB_NAME + "?user=" + DB_USER + "&password=" + DB_PASS);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		
		registerCommands();
		registerListeners();
	}
	
	
	private void registerCommands()
	{
		getCommand("setspawn").setExecutor(new SetspawnCommand(this));
		getCommand("spawn").setExecutor(new SpawnCommand(this));
		getCommand("tp").setExecutor(new TpCommand(this));
		getCommand("tphere").setExecutor(new TphereCommand(this));
	}
	
	private void registerListeners()
	{
		getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
	}
	
	public UsersManager getUsersManager()
	{
		if (usersManager == null)
			usersManager = new UsersManager();
		
		return usersManager;
	}
	
	public WorldsManager getWorldsManager()
	{
		if (worldsManager == null)
			worldsManager = new WorldsManager(this);
		
		return worldsManager;
	}
	
	public static Connection getConnexion()
	{			
		return connection;
	}
	
	public static Player getPlayer(String playerName) throws PlayerNotFoundException
	{
		Player p = Bukkit.getPlayer(playerName);
		
		if (p == null)
			throw new PlayerNotFoundException(playerName);
		
		return p;
	}
	
	public static void broadcastMessage(String message)
	{
		Bukkit.broadcastMessage(ChatColor.GOLD + " * " + message.replace("{", ChatColor.RED.toString()).replace("}", ChatColor.GOLD.toString()));
	}
	
	public static void sendMessage(CommandSender s, String message)
	{
		s.sendMessage(ChatColor.GOLD + message.replace("{", ChatColor.RED.toString()).replace("}", ChatColor.GOLD.toString()));
	}
	
	public static void sendError(CommandSender s, String error)
	{
		s.sendMessage(ChatColor.RED + error);
	}
}