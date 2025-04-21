public class DuplicateCharacters {  
     public static void main(String[] args) {  
        String string1 = "Great responsibility";  
        int count;  
          
        //Converts given string into character array  
        char string[] = string1.toCharArray();  
          
        System.out.println("Duplicate characters in a given string: ");  
        //Counts each character present in the string  
        for(int i = 0; i  1 && string[i] != '0')  
                System.out.println(string[i]);  
        }  
    }  
}