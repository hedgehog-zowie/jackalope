package org.jackalope.study.avro.quickStart;

import org.junit.*;

import static org.junit.Assert.*;

public class QuickStartTest {

    private static QuickStart quickStart;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception{
        quickStart = new QuickStart();
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception{
        quickStart = null;
    }

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testSerialize() throws Exception {
        quickStart.serialize();
    }

    @Test
    public void testDeserialize() throws Exception {
        quickStart.deserialize();
    }
}