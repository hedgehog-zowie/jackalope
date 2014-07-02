package com.iuni.data.avro.client;

import org.junit.*;

public class ClientTest {

    private static Client httpClient, nettyClient;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        httpClient = new HttpClient();
        nettyClient = new NettyClient();
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        httpClient = null;
        nettyClient = null;
    }

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testAnalyzeData() throws Exception {
        httpClient.analyzeData();
        System.out.println("-------------------------");
        nettyClient.analyzeData();
    }
}