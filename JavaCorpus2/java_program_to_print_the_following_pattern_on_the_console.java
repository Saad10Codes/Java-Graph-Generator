public class pattern
 {
     public static void main(String[] args) {
     int lines=4;
     int i,j;
     int space=0;
     for(i=0;i0){// this loop is used to print numbers in a line
             if(j>lines-i)
             System.out.print("*");
             else
             System.out.print(j);
             j--;
         }
         if((lines-i)>9)// this loop is used to increment space
         space=space+1;
     System.out.println("");
     }
 }
}