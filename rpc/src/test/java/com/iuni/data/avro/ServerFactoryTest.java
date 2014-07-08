package com.iuni.data.avro;

import com.iuni.data.avro.exceptions.RpcException;
import com.iuni.data.avro.server.Server;
import org.apache.avro.Protocol;
import org.junit.*;

public class ServerFactoryTest {

    private final String name_http = "avro http server";
    private final String name_netty = "avro netty server";
    private static Protocol protocol;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        protocol = ProtocolFactory.create();
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
    public void testCreateHttp() throws Exception {
        Server httpServer = DefaultServerFactory.create(name_http, protocol, "http");
        httpServer.start();
    }

    @Test
    public void testCreateNetty() throws RpcException {
        Server nettyServer = DefaultServerFactory.create(name_netty, protocol, "netty");
        nettyServer.start();
    }
}