package info.bytecraft.bytes;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class WalletCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
		if(cmd.getName().equalsIgnoreCase("wallet") && args.length > 0){
			if(cs instanceof Player){
				Player player = (Player)cs;
				Account account = Bytes.getAccount(player);
				
				String usage = args[0].toLowerCase();
				if(args.length == 1){
					if(usage.equalsIgnoreCase("balance")){
						player.sendMessage(ChatColor.AQUA + "You have " + ChatColor.GOLD + account.getAmountToString().replaceAll(",", ChatColor.WHITE + "," + ChatColor.GOLD) + ChatColor.AQUA + " bytes");
					}
					
					if(usage.equalsIgnoreCase("tell")){
						Bukkit.broadcastMessage(player.getDisplayName() + " has " + ChatColor.GOLD + account.getAmountToString().replaceAll(",", ChatColor.WHITE + "," + ChatColor.GOLD) + ChatColor.AQUA + " bytes");
					}
				}
				
				if(args.length == 3){
					if(usage.equalsIgnoreCase("give")){
						Player target = Bukkit.getPlayer(args[1]);
						if(target != null){
							int amount = Integer.parseInt(args[2]);
							if(amount <= account.getAmount()){
								Account acc = Bytes.getAccount(target);
								if(acc == null){
									target.sendMessage(ChatColor.RED + "Your wallet has been deleted, please contact a senior admin");
									player.sendMessage(ChatColor.RED + target.getName() + " is experiencing a technical difficulty with their wallet");
									return true;
								}
								acc.setAmount(acc.getAmount() + amount);
								account.setAmount(account.getAmount() - amount);
								
								Bytes.bytes.getDatabase().save(account);
								Bytes.bytes.getDatabase().save(acc);
								player.sendMessage(ChatColor.AQUA + "You gave " + target.getDisplayName() + " " + ChatColor.GOLD + format(amount) + ChatColor.AQUA + " bytes");
								target.sendMessage(player.getDisplayName() + ChatColor.AQUA + " gave you " + ChatColor.GOLD + format(amount) + ChatColor.AQUA + " bytes");
							}else{
								player.sendMessage(ChatColor.RED + "Don't try to give more than you have!");
							}
						}
					}
				}
			}
		}
		return true;
	}
	
	private String format(int i){
		DecimalFormat df = new DecimalFormat();
		DecimalFormatSymbols dfs = new DecimalFormatSymbols();
		dfs.setGroupingSeparator(',');
		df.setDecimalFormatSymbols(dfs);
		
		return df.format(i).replaceAll(",", ChatColor.WHITE + "," + ChatColor.GOLD);
	}

}
