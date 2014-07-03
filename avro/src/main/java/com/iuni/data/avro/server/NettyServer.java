package com.iuni.data.avro.server;

import com.iuni.data.avro.common.Constants;
import com.iuni.data.avro.exceptions.AvroServerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

/**
 * @author Nicholas
 *         Email:   nicholas.chen@iuni.com
 */
public class NettyServer extends Server {

    private static final Logger logger = LoggerFactory.getLogger(NettyServer.class);

    @Override
    public void start() throws AvroServerException {
        server = new org.apache.avro.ipc.NettyServer(
                new Handler(protocol), new InetSocketAddress(Constants.DEFAULT_PORT));
        super.start();
    }

}
