package fr.heavencraft.heavencore.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.heavencraft.heavencore.HeavenCore;
import fr.heavencraft.heavencore.PERMISSIONS;
import fr.heavencraft.heavencore.exceptions.HeavenException;

public class SetspawnCommand extends HeavenCommand
{
	public SetspawnCommand(HeavenCore plugin)
	{
		super(plugin, PERMISSIONS.SET_SPAWN);
	}

	@Override
	protected void onPlayerCommand(Player p, String[] args) throws HeavenException
	{
		plugin.getWorldsManager().setSpawn(p.getLocation());
	}

	@Override
	protected void onConsoleCommand(CommandSender s, String[] args) throws HeavenException
	{
		throw new HeavenException("Cette commande n'est pas utilisable via la console.");
	}

	@Override
	protected void sendUsage(CommandSender s)
	{
	}
}