package fr.heavencraft.heavencore.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.heavencraft.heavencore.HeavenCore;
import fr.heavencraft.heavencore.PERMISSIONS;
import fr.heavencraft.heavencore.exceptions.HeavenException;

public class TphereCommand extends HeavenCommand
{
	public TphereCommand(HeavenCore plugin)
	{
		super(plugin, PERMISSIONS.TP_HERE);
	}

	@Override
	protected void onPlayerCommand(Player p, String[] args) throws HeavenException
	{
		if (args.length != 1)
		{
			sendUsage(p);
			return;
		}
		
		Player p1 = HeavenCore.getPlayer(args[0]);
		
		p1.teleport(p);
		HeavenCore.sendMessage(p1, "Téléportation vers {" + p.getDisplayName() + "}.");
		
		HeavenCore.sendMessage(p, "Téléportation de {" + p1.getDisplayName() + "}.");
	}

	@Override
	protected void onConsoleCommand(CommandSender s, String[] args) throws HeavenException
	{
		throw new HeavenException("Cette commande n'est pas utilisable via la console.");
	}

	@Override
	protected void sendUsage(CommandSender s)
	{
		HeavenCore.sendMessage(s, "Téléportation :");
		HeavenCore.sendMessage(s, "{/tp} <joueur> : se téléporter vers un joueur");
		HeavenCore.sendMessage(s, "{/tp} <joueur1> <joueur2> : téléporter un joueur vers un autre joueur");
		HeavenCore.sendMessage(s, "{/tphere} <joueur> : téléporter un joueur vers vous");
	}
}