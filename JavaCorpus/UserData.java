package pkg360;
/**
 * @author Caleb Morris
 */
public class UserData {
   private static UserData instance = null;
   public String uName;         // Username. Same as login
   public String uName2;
   public Board uBoard_;        // Board info storage. See Board.java
   public int uBoardSize;       // ([\d]{1,}) | \0 > 1 && < 15
   public String uSaveName;     // SaveState name : save[\d]{1,}
   public Hint[] uHints;
   public int uDifficulty;      // Easy, Medium, Hard: 1,2,3
   public int uNumPlayers;      // 1,2
   protected UserData() {
      // Exists only to defeat instantiation.
   }
   public static UserData getInstance() {
      if(instance == null) {
         instance = new UserData();
      }
      return instance;
   }
}
