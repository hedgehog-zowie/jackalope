import org.apache.hadoop.classification.InterfaceAudience;
import org.junit.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Nicholas
 *         Email:   nicholas.chen@iuni.com
 */
public class SizeOfTest {

    private static Runtime rTime = Runtime.getRuntime();

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {

    }

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void calSize2() {
        runGC();

        long heap1 = 0;
        final int count = 100000;
        Object[] objs = new Object[count];
        final String key = "asdf";
        final String value = "01/Jul/2014:23:59:51 +0800 {]";
        for (int i = -1; i < count; i++) {
//            Object obj = null;
//            obj = new Object();                 // 8
//          obj = new Integer( i );             // 16
//          obj = new Short( (short)i );        // 16
//          obj = new Long( i );                // 16
//          obj = new Byte( (byte)0 );          // 16
//          obj = new Character( (char)i );     // 16
//          obj = new Float( i );               // 16
//          obj = new Double( i );              // 16
//          obj = new Boolean( true );          // 16
//          obj = new String();                 // 40

            Map obj = new HashMap<Object, Object>();
            obj.put(key, value);
            obj.put(key, value);
            obj.put(key, value);
            obj.put(key, value);
            obj.put(key, value);
            obj.put(key, value);
            obj.put(key, value);
            obj.put(key, value);
            obj.put(key, value);
            obj.put(key, value);
            obj.put(key, value);
            obj.put(key, value);
            obj.put(key, value);
            obj.put(key, value);
            obj.put(key, value);
            obj.put(key, value);
            obj.put(key, value);
            obj.put(key, value);
            obj.put(key, value);
            obj.put(key, value);
            obj.put(key, value);
            obj.put(key, value);
            obj.put(key, value);
            obj.put(key, value);
            obj.put(key, value);
            obj.put(key, value);
            obj.put(key, value);
            obj.put(key, value);
            obj.put(key, value);
            obj.put(key, value);
            if (i < 0) {
                obj = null;
                runGC();
                heap1 = usedMemory();   // before memory size
            } else {
                objs[i] = obj;
            }
        }

        runGC();
        long heap2 = usedMemory();      // after memory size

        final int size = (int) Math.round((heap2 - heap1) / (double) count);
        System.out.println("heap1 = " + heap1 + "; heap2 = " + heap2);
        System.out.println("heap2-heap1 = " + (heap2 - heap1) + "; " + objs[0].getClass().getSimpleName() + " size = " + size);

        for (int i = 0; i < count; i++) {
            objs[i] = null;
        }
        objs = null;
        runGC();
    }

    private static void runGC() {
        for (int i = 0; i < 4; i++) {
            long usedMem1 = usedMemory();
            long usedMem2 = Long.MAX_VALUE;

            for (int j = 0; (usedMem1 < usedMem2) && (j < 500); j++) {
                rTime.runFinalization();
                rTime.gc();
                Thread.yield();

                usedMem2 = usedMem1;
                usedMem1 = usedMemory();
            }
        }
    }

    private static long usedMemory() {
        return rTime.totalMemory() - rTime.freeMemory();
    }

}
