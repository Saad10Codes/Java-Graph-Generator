package fr.heavencraft.heavencore.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.heavencraft.heavencore.HeavenCore;
import fr.heavencraft.heavencore.exceptions.HeavenException;

public abstract class HeavenCommand implements CommandExecutor
{
	protected HeavenCore plugin;
	protected String permission;
	
	public HeavenCommand(HeavenCore plugin, String permission)
	{
		this.plugin = plugin;
		this.permission = permission;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
	{
		if (!sender.hasPermission(permission))
		{
			HeavenCore.sendError(sender, "Vous n'avez pas le droit d'exécuter cette commande.");
			return true;
		}
		
		try
		{
			if (sender instanceof Player)
				onPlayerCommand((Player) sender, args);
			else
				onConsoleCommand(sender, args);
		}
		catch (HeavenException ex)
		{
			sender.sendMessage(ChatColor.RED + ex.getMessage());
		}
		catch (NumberFormatException ex)
		{
			sender.sendMessage(ChatColor.RED + ex.getMessage());
		}
		
		return true;
	}
	
	protected abstract void onPlayerCommand(Player p, String[] args) throws HeavenException;
	
	protected abstract void onConsoleCommand(CommandSender s, String[] args) throws HeavenException;
	
	protected abstract void sendUsage(CommandSender s);
}