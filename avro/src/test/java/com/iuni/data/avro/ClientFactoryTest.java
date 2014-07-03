package com.iuni.data.avro;

import com.iuni.data.avro.client.Client;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class ClientFactoryTest {

    private String name_http = "avro http client";
    private String name_netty = "avro netty client";

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
        Client httpClient = ClientFactory.create(name_http, "http");
        Client nettyClient = ClientFactory.create(name_netty, "netty");
    }
}