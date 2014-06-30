package org.jackalope.study.iplib;

import java.io.IOException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class IpUtilsTest {
	
	private static IpUtils ipUtils;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		ipUtils = IpUtils.getInstance();
		ipUtils.init();
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
	public void testIpInfoFromTaobao() throws IpException {
		System.out.println(ipUtils.getIpInfoFromTaobao("113.106.195.231"));
	}
	
	@Test
	public void testGetIpInfo() throws IpException, IOException {
		System.out.println(ipUtils.getIpInfo("113.105.131.231"));
	}
	

}
