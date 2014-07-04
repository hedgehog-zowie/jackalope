package com.iuni.data.avro.client;

import com.iuni.data.avro.ProtocolFactory;
import com.iuni.data.avro.common.Constants;
import com.iuni.data.avro.exceptions.AvroClientException;
import com.iuni.data.avro.exceptions.AvroException;
import org.apache.avro.Protocol;
import org.apache.avro.ipc.NettyTransceiver;
import org.apache.avro.ipc.generic.GenericRequestor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

/**
 * @author Nicholas
 *         Email:   nicholas.chen@iuni.com
 */
public class SimpleNettyClient extends Client {

    private static final Logger logger = LoggerFactory.getLogger(SimpleHttpClient.class);

    public SimpleNettyClient() throws AvroException {
        this(Constants.DEFAULT_HOST, Constants.DEFAULT_PORT, ProtocolFactory.create());
    }

    public SimpleNettyClient(String host, Integer port, Protocol protocol) throws AvroClientException {
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
        InetSocketAddress inetSocketAddress = new InetSocketAddress(inetAddress, port);
        initNettyClient(inetSocketAddress, protocol);
    }

    public SimpleNettyClient(InetAddress inetAddress, int port, Protocol protocol) throws AvroClientException {
        this(new InetSocketAddress(inetAddress, port), protocol);
    }

    public SimpleNettyClient(InetSocketAddress inetSocketAddress, Protocol protocol) throws AvroClientException {
        initNettyClient(inetSocketAddress, protocol);
    }

    /**
     * 初始化netty client
     * @param inetSocketAddress
     * @param protocol
     * @throws AvroClientException
     */
    private void initNettyClient(InetSocketAddress inetSocketAddress, Protocol protocol) throws AvroClientException {
        this.protocol = protocol;
        try {
            transceiver = new NettyTransceiver(inetSocketAddress);
        } catch (IOException e) {
            String errorStr = new StringBuilder()
                    .append("create netty transceiver failed, host is: ")
                    .append(inetSocketAddress.getHostString())
                    .append(", port is: ")
                    .append(inetSocketAddress.getPort())
                    .append("error msg: ")
                    .append(e.getMessage())
                    .toString();
            logger.error(errorStr);
            throw new AvroClientException(errorStr);
        }
        try {
            requestor = new GenericRequestor(protocol, transceiver);
        } catch (IOException e) {
            String errorStr = new StringBuilder()
                    .append("create requestor failed, error msg: ")
                    .append(e.getMessage())
                    .toString();
            logger.error(errorStr);
            throw new AvroClientException(errorStr);
        }
    }



}
