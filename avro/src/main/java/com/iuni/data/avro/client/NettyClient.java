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
public class NettyClient extends Client {

    private static final Logger logger = LoggerFactory.getLogger(HttpClient.class);

    public NettyClient() throws AvroException {
        Protocol protocol = ProtocolFactory.create();
        new NettyClient(Constants.DEFAULT_HOST, Constants.DEFAULT_PORT, protocol);
    }

    public NettyClient(String host, Integer port, Protocol protocol) throws AvroClientException {
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
        new NettyClient(inetAddress, port, protocol);
    }

    public NettyClient(InetAddress inetAddress, int Port, Protocol protocol) throws AvroClientException {
        InetSocketAddress inetSocketAddress = new InetSocketAddress(inetAddress, Port);
        new NettyClient(inetSocketAddress, protocol);
    }

    public NettyClient(InetSocketAddress inetSocketAddress, Protocol protocol) throws AvroClientException {
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
