package com.iuni.data.avro.client;

/**
 * 客户端类型
 * @author Nicholas
 *         Email:   nicholas.chen@iuni.com
 */
public enum ClientType {
    /**
     * custom type
     */
    OTHER(null),

    /**
     * http client type
     *
     * @see com.iuni.data.avro.client.HttpClient
     */
    HTTP("com.iuni.data.avro.client.HttpClient"),

    /**
     * netty client type
     *
     * @see com.iuni.data.avro.client.NettyClient
     */
    NETTY("com.iuni.data.avro.client.NettyClient");

    private final String clientClassName;

    private ClientType(String clientClassName){
        this.clientClassName = clientClassName;
    }

    public String getClientClassName() {
        return clientClassName;
    }

}
