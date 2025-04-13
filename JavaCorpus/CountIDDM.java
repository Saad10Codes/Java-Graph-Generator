public class CountIDDM{
public int CountIDDWhile(  int[] a,  int x){
    int s=0;
    int i=0;
    int omega=-1;
    while (i < a.length) {
      if (a[i] == x) {
        s++;
        omega++;
      }
      i++;
    }
    return s;
  }
}

