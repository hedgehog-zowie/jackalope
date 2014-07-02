package com.iuni.data.avro.common;

/**
 * @author Nicholas
 *         Email:   nicholas.chen@iuni.com
 */
public class Constants {

    /**
     * 默认主机
     */
    public static final String DEFAULT_HOST = "localhost";

    /**
     * 默认端口
     */
    public static final int DEFAULT_PORT = 8088;

    /**
     * 默认avro模式地址
     */
    public static final String DEFAULT_AVPR = "helloWorld.json";

    /**
     * 获取默认地址
     * @return
     */
    public static final String getDefaultAddress(){
        return new StringBuilder().append("http://").append(DEFAULT_HOST).append(":").append(DEFAULT_PORT).toString();
    }


}
