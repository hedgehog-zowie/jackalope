import org.databene.contiperf.PerfTest;
import org.databene.contiperf.Required;
import org.databene.contiperf.junit.ContiPerfRule;
import org.junit.*;

/**
 * @author Nicholas
 *         Email:   nicholas.chen@iuni.com
 */
public class BasicTest {

    private String aString = "str";
    private Integer anInt = 123;
    private Long aLong = (long) 9999;

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
}