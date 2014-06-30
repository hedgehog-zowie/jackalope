package org.jackalope.study.iplib.pure;

import org.junit.*;

public class IPSeekerTest {

    private static IPSeeker ipSeeker;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        ipSeeker = IPSeeker.getInstance();
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
    public void testGetArea() throws Exception {
        System.out.println(ipSeeker.getArea("113.106.195.231"));
    }

    @Test
    public void testGetLocation() throws Exception {
        System.out.println(ipSeeker.getLocation("113.106.195.231"));
    }
}