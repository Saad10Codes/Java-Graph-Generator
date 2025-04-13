public class CountIDD{
public int Count_IDD(  int[] a,  int x){
    int s=0;
    for (int i=0; i < a.length; i++) {
      if (a[i] == x) {
        s++;
      }
    }
    return s;
  }
}

