package org.jackalope.study;

//import org.jackalope.study.iplib.Run;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by zowie on 14-6-29.
 */
public class Test {
    public static void main(String[] args) throws UnknownHostException {
//        Run run = new Run();
//        run.testGetResourcePath();
        System.out.println(InetAddress.getByName("18.8.0.238"));
    }
}
