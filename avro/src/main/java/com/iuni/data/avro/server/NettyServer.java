package com.iuni.data.avro.server;

import com.iuni.data.avro.common.Constants;
import com.iuni.data.avro.exceptions.AvroServerException;
import org.apache.avro.Protocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * @author Nicholas
 *         Email:   nicholas.chen@iuni.com
 */
public class NettyServer extends Server {

    private static final Logger logger = LoggerFactory.getLogger(NettyServer.class);

    protected NettyServer(Protocol protocol) throws AvroServerException {
        server = new org.apache.avro.ipc.NettyServer(
                new Handler(protocol), new InetSocketAddress(Constants.DEFAULT_PORT));
    }

    public NettyServer(String avprFile) throws AvroServerException {
        File file = new File(NettyServer.class.getResource(avprFile).getPath());
        try {
            new NettyServer(Protocol.parse(file));
        } catch (IOException e) {
            String errorStr = new StringBuilder()
                    .append("start avro netty server failed, error msg: ")
                    .append(e.getMessage())
                    .toString();
            logger.error(errorStr);
            throw new AvroServerException(errorStr);
        }
    }

    public NettyServer() throws AvroServerException {
        File file = new File(NettyServer.class.getResource(Constants.DEFAULT_AVPR).getPath());
        try {
            new NettyServer(Protocol.parse(file));
        } catch (IOException e) {
            String errorStr = new StringBuilder()
                    .append("start avro netty server failed, error msg: ")
                    .append(e.getMessage())
                    .toString();
            logger.error(errorStr);
            throw new AvroServerException(errorStr);
        }
    }

}
