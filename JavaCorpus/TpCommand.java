package fr.heavencraft.heavencore.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.heavencraft.heavencore.HeavenCore;
import fr.heavencraft.heavencore.PERMISSIONS;
import fr.heavencraft.heavencore.exceptions.HeavenException;

public class TpCommand extends HeavenCommand
{
	public TpCommand(HeavenCore plugin)
	{
		super(plugin, PERMISSIONS.TP);
	}

	@Override
	protected void onPlayerCommand(Player p, String[] args) throws HeavenException
	{
		switch (args.length)
		{
			// /tp <joueur>
			case 1:
				Player toPlayer = HeavenCore.getPlayer(args[0]);
				
				p.teleport(toPlayer);
				HeavenCore.sendMessage(p, "Téléportation vers {" + toPlayer.getDisplayName() + "}.");
				break;
			// /tp <joueur1> <joueur2>
			case 2:
				onConsoleCommand(p, args);
				break;
			default:
				sendUsage(p);
		}
	}

	@Override
	protected void onConsoleCommand(CommandSender s, String[] args) throws HeavenException
	{
		if (args.length != 2)
		{
			sendUsage(s);
			return;
		}
		
		Player p1 = HeavenCore.getPlayer(args[0]);
		Player p2 = HeavenCore.getPlayer(args[1]);
		
		p1.teleport(p2);
		
		HeavenCore.sendMessage(s, "Téléportation de {" + p1.getDisplayName() + "} vers {" + p2.getDisplayName() + "}.");
		HeavenCore.sendMessage(p1, "Téléportation vers {" + p2.getDisplayName() + "}.");
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