import java.util.HashMap;
import java.util.Map;

/**
 * @author zowie
 *         Email: nicholas@iuni.com
 */
public class Test {

    public static void main(String args[]){
        int i = 12345;
        Object o = new Object();
        System.out.println(o.hashCode());
        System.out.println(Integer.toBinaryString(o.hashCode() >>> 16));
        String s = "123";
        System.out.println(s.hashCode());
        System.out.println(Integer.toBinaryString(s.hashCode() >>> 16));
        Map<String, String> map = new HashMap<String, String>();
        map.put(s, s);
    }

}
