package com.iuni.data.avro.client;

import com.iuni.data.avro.common.Constants;
import com.iuni.data.avro.exceptions.AvroClientException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

/**
 * @author Nicholas
 *         Email:   nicholas.chen@iuni.com
 */
public class NettyClient extends Client {

    private static final Logger logger = LoggerFactory.getLogger(HttpClient.class);

    public NettyClient() throws AvroClientException {
        new NettyClient(Constants.DEFAULT_HOST, Constants.DEFAULT_PORT);
    }

    public NettyClient(int port) throws AvroClientException {
        new NettyClient(Constants.DEFAULT_HOST, port);
    }

    public NettyClient(String host) throws AvroClientException {
        new NettyClient(host, Constants.DEFAULT_PORT);
    }

    public NettyClient(String host, int port) throws AvroClientException {
        InetAddress inetAddress;
        try {
            inetAddress = InetAddress.getByName(host);
        } catch (UnknownHostException e) {
            String errorStr = new StringBuilder()
                    .append("create netty client failed: unknown host, host is: ")
                    .append(host)
                    .append("error msg: ")
                    .append(e.getMessage())
                    .toString();
            logger.error(errorStr);
            throw new AvroClientException(errorStr);
        }
        new NettyClient(inetAddress, port);
    }

    public NettyClient(InetAddress inetAddress, int Port) throws AvroClientException {
        InetSocketAddress inetSocketAddress = new InetSocketAddress(inetAddress, Port);
        new NettyClient(inetSocketAddress);
    }

    public NettyClient(InetSocketAddress inetSocketAddress) throws AvroClientException {
        super(inetSocketAddress);
    }

}
