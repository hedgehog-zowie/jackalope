import org.databene.contiperf.PerfTest;
import org.databene.contiperf.Required;
import org.databene.contiperf.junit.ContiPerfRule;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runner.Runner;
import org.junit.runners.Parameterized;
import sun.plugin2.applet.Applet2ClassLoader;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Nicholas
 *         Email:   nicholas.chen@iuni.com
 */
//@RunWith(Parameterized.class)
public class BasicTest {

    private String aString = "str";
    private Integer anInt = 123;
    private Long aLong = (long) 9999;

    private String requesField;

    private static int aint = 0;
    private Lock alock = new ReentrantLock();

    private static BlockingQueue<Integer> blockingQueue = new LinkedBlockingQueue<>();

//    public BasicTest(String requesField){
//        this.requesField = requesField;
//    }

    @Parameterized.Parameters
    public static Collection addValues() {
        return Arrays.asList(
                new Object[][]{
                        {" GET /api/zt/admission_letter/letter_list1?AD_ID=105&xx=xx HTTP/1.1 "},
                        {"GET /api/zt/admission_letter/letter_list2?xx=xx HTTP/1.1"},
                        {"GET /api/zt/admission_letter/letter_list3 HTTP/1.1"},
                        {"GET /zt/2014/0716/index.shtml4?AD_ID=107 HTTP/1.1"},
                        {"GET /zt/2014/0716/index.shtml5?AD_ID=106&qz_gdt=k3i5guykaaaohwemw42q HTTP/1.1"},
                        {"asdfasf"}
                }
        );
    }

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

    @Rule
    public ContiPerfRule i = new ContiPerfRule();

    @Test
    public void testSimpleName(){
        System.out.println("this.getClass() = " + this.getClass());
        System.out.println("this.getClass().getSimpleName() = " + this.getClass().getSimpleName());
    }

    @Test
    @PerfTest(invocations = 1000, threads = 2, duration = 1000)
    @Required(max = 100, average = 5)
    public void testStringBuider() throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append(aString).append(" ").append(anInt).append(" ").append(aLong)
                .append(aString).append(" ").append(anInt).append(" ").append(aLong).toString();
    }

    @Test
    @PerfTest(invocations = 1000, threads = 2, duration = 1000)
    @Required(max = 100, average = 5)
    public void testStringFormat() throws Exception {
        String.format("%s %d %d %s %d %d", aString, anInt, aLong, aString, anInt, aLong);
    }

    @Test
    public void testSubString() {
        String AD_ID = "AD_ID";
        String[] requesFields = requesField.split(" ");
        System.out.println("----------");
        System.out.println("requesFields.length: " + requesFields.length);
        System.out.println("----------");
        String method = "";
        String requestUrl = "";
        String protocol = "";
        String adidstr = "";
        if (requesFields.length == 3) {
            method = requesFields[0];
            protocol = requesFields[2];
            String request = requesFields[1];
            String[] arrayOfReqUrl = request.split("\\?");
            requestUrl = arrayOfReqUrl[0];
            if (arrayOfReqUrl.length == 2) {
                String paraStr = arrayOfReqUrl[1];
                String[] params = paraStr.split("&");
                for (int i = 0; i < params.length; i++) {
                    String param = params[i];
                    String[] arrayOfParam = param.split("=");
                    if (AD_ID.equals(arrayOfParam[0]) && arrayOfParam.length == 2) {
                        adidstr = arrayOfParam[1];
                        break;
                    }
                }
            }
        }
        System.out.println("================");
        System.out.println(requesField);
        System.out.println("method: " + method);
        System.out.println("requestUrl: " + requestUrl);
        System.out.println("protocol: " + protocol);
        System.out.println("adidstr: " + adidstr);
        System.out.println("================");
    }

    @Test
    public void testSwitch() {
        String asdf = "";
        switch (asdf) {
            case "a":
                System.out.println("== " + asdf + " ==");
                break;
            case "b":
                System.out.println("== " + asdf + " ==");
                break;
            default:
                String r = asdf == null ? "null" : asdf;
                System.out.println("== " + r + " ==");
                break;
        }
    }

    @Test
    @PerfTest(invocations = 1000, threads = 100)
    public void testLock() {
        alock.lock();
        try {
            System.out.println(aint++);
        } finally {
            alock.unlock();
        }
    }

    @Test
    public void testString() {
        System.out.println((double) 5 / 3);
        System.out.println("".isEmpty());
    }

    @Test
    public void testThread() throws InterruptedException {
        ExecutorService excutorService_a = Executors.newFixedThreadPool(10);
        ExecutorService excutorService_b = Executors.newFixedThreadPool(10);
        int i = 0;
        while (i < 10) {
            excutorService_a.execute(new AThread("aThread" + i));
            excutorService_b.execute(new BThread("bThread" + i));
            i++;
        }
        while (true) {
            if (excutorService_b.isShutdown()) {
                return;
            }
            Thread.sleep(1000);
        }
    }

    static class AThread extends Thread {
        AThread(String name) {
            setName(name);
        }

        @Override
        public void run() {
            while (aint != 100) {
                blockingQueue.add(aint);
                aint++;
                System.out.println(getName() + " add: " + aint);
//                try {
//                    Thread.sleep(1000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
            }
        }

    }

    static class BThread extends Thread {
        boolean interrupt = false;

        BThread(String name) {
            setName(name);
        }

        @Override
        public void run() {
            while (!interrupt) {
                try {
                    Integer i = blockingQueue.take();
                    System.out.println(getName() + " get i from blockingQueue: " + i);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    interrupt = true;
                }
            }
        }

    }

    @Test
    public void getResourcePath(){
        System.out.println("this.getClass().getClassLoader().getResource(\"\"):\t" + this.getClass().getClassLoader().getResource(""));
        System.out.println("this.getClass().getResource(\"\"):\t" + this.getClass().getResource(""));
    }

}