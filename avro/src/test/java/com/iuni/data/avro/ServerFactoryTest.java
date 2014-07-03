package com.iuni.data.avro;

import com.iuni.data.avro.server.Server;
import org.jackalope.study.avro.quickStart.QuickStart;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

public class ServerFactoryTest {

    private String name_http = "avro http server";
    private String name_netty = "avro netty server";

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {

    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {

    }

    @Before
    public void setUp() throws Exception {

    }

    @AfterClass
    public void tearDown() throws Exception {

    }

    @Test
    public void testCreate() throws Exception {
        Server httpServer = ServerFactory.create(name_http, "http");
        httpServer.start();
        Server nettyServer = ServerFactory.create(name_netty, "netty");
        nettyServer.start();
    }
}