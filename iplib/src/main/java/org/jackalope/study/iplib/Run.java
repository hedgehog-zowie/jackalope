package org.jackalope.study.iplib;

import java.io.File;

/**
 * Created by Administrator on 2014/6/26 0026.
 */
public class Run {

    public static void main(String[] args) throws IpException {
        IpStore ipStore = IpStore.getInstance();
        ipStore.initIpStore();

    }

    public void testGetResourcePath(){
        System.out.println("1:" + Thread.currentThread().getContextClassLoader().getResource(""));
        System.out.println("2:" + Run.class.getClassLoader().getResource(""));
        System.out.println("3:" + ClassLoader.getSystemResource(""));
        System.out.println("4:" + this.getClass().getResource(""));//IdcardClient.class文件所在路径
        System.out.println("5:" + this.getClass().getResource("/")); // Class包所在路径，得到的是URL对象，用url.getPath()获取绝对路径String
        System.out.println("6:" + new File("/").getAbsolutePath());
        System.out.println("7:" + System.getProperty("user.dir"));
        System.out.println("8:" + System.getProperty("file.encoding"));//获取文件编码
    }

}
