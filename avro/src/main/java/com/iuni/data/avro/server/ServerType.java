package com.iuni.data.avro.server;

/**
 * 服务端类型
 * @author Nicholas
 *         Email:   nicholas.chen@iuni.com
 */
public enum ServerType {
    /**
     * custom type
     */
    OTHER(null),

    /**
     * http server type
     * @see com.iuni.data.avro.server.HttpServer
     */
    HTTP("com.iuni.data.avro.server.HttpServer"),

    /**
     * netty server type
     * @see com.iuni.data.avro.server.NettyServer
     */
    NETTY("com.iuni.data.avro.server.NettyServer");

    private final String serverClassName;

    private ServerType(String serverClassName) {
        this.serverClassName = serverClassName;
    }

    public String getServerClassName() {
        return serverClassName;
    }

}
