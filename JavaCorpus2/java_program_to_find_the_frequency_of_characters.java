public class FrequencyCharacter   
{  
     public static void main(String[] args) {  
        String str = "picture perfect";  
        int[] freq = new int[str.length()];  
        int i, j;  
          
        //Converts given string into character array  
        char string[] = str.toCharArray();  
          
        for(i = 0; i