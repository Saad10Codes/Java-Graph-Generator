package info.bytecraft.bytes;

import info.bytecraft.bytes.listeners.JoinListener;
import info.bytecraft.paper.Paper;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.PersistenceException;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class Bytes extends JavaPlugin {
	
	public static Bytes bytes;
	
	@Override
	public void onEnable(){
		bytes = this;
		
		this.setUpDatabase();
		this.getCommand("wallet").setExecutor(new WalletCommand());
		this.getServer().getPluginManager().registerEvents(new JoinListener(), this);
	}
	
	@Override
	public void onDisable(){
		bytes = null;
	}
	
	
	private void setUpDatabase(){
		try{
			this.getDatabase().find(Account.class).findRowCount();
		}catch(PersistenceException ex){
			this.installDDL();
		}
	}
	
	@Override
	public List<Class<?>> getDatabaseClasses(){
		List<Class<?>> list = new ArrayList<Class<?>>();
		list.add(Account.class);
		return list;
	}

	public static Account getAccount(Player player) {
		return bytes.getDatabase().find(Account.class).where().ieq("playerName", player.getName()).findUnique();
	}
	
	public static int getValue(ItemStack item){
		int i = 0;
		int x = i * item.getAmount();
		Material mat = item.getType();
		switch(mat){
		case STONE: i = 10; 
			break; 
		case IRON_BLOCK: i = 100; 
			break;
		case DIAMOND_BLOCK: i = 500;
			break;
		
		}
		return x;
	}
	
	public static int getBlockValue(Block block){
		if(Paper.wasBroken(block)){
			return 0;
		}
		int i = 0;
		Material mat = block.getType();
		switch(mat){
		case COBBLESTONE: i = 3; break;
		case DIRT: i = 3; break;
		case WOOD: i = 5; break;
		case SAND: i = 5; break;
		case GRAVEL: i = 5; break;
		case GOLD_ORE: i = 25; break;
		}
		
		return i;
	}
}
