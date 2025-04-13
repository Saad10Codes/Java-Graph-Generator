package info.bytecraft.bytes;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.bukkit.entity.Player;

import com.avaje.ebean.validation.NotNull;

@Table(name="bytecraft_accounts")
@Entity()
public class Account {
	
	@Id
	private int id;
	
	@NotNull
	private String playerName;
	
	@NotNull
	private int amount;
	
	public int getId(){
		return this.id;
	}
	
	public void setId(int id){
		this.id = id;
	}

	public String getPlayerName() {
		return playerName;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}
	
	public static void createAccount(Player player){
		Account act = Bytes.bytes.getDatabase().find(Account.class).where().ieq("playerName", player.getName()).findUnique();
		if(act != null){
			Bytes.bytes.getLogger().warning(player.getName() + " already has an account [" + act.getAmount()+"]");
		}else{
			act = new Account();
			
			act.setPlayerName(player.getName());
			act.setAmount(1000);
			
			Bytes.bytes.getDatabase().save(act);
		}
	}
	
	public String getAmountToString(){
		int i = getAmount();
		DecimalFormat df = new DecimalFormat();
		DecimalFormatSymbols dfs = new DecimalFormatSymbols();
		dfs.setGroupingSeparator(',');
		df.setDecimalFormatSymbols(dfs);
		
		return df.format(i);
	}
}
