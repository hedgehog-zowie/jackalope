package com.iuni.data.avro.server;

import com.iuni.data.avro.common.Constants;
import com.iuni.data.avro.exceptions.AvroServerException;
import org.apache.avro.Protocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

/**
 * @author Nicholas
 *         Email:   nicholas.chen@iuni.com
 */
public class NettyServer extends Server {

    private static final Logger logger = LoggerFactory.getLogger(NettyServer.class);

    public NettyServer(Protocol protocol) {
        super(protocol);
    }

    @Override
    public void start() throws AvroServerException {
        try {
            server = new org.apache.avro.ipc.HttpServer(
                    new HttpServer(Protocol.parse(new File(HttpServer.class.getResource(Constants.DEFAULT_AVPR).getPath()))),
                    Constants.DEFAULT_PORT);
            server.start();
            server.join();
        } catch (IOException e) {
            String errorStr = new StringBuilder()
                    .append("start avro netty server failed, error msg: ")
                    .append(e.getMessage())
                    .toString();
            logger.error(errorStr);
            throw new AvroServerException(errorStr);
        }
        catch (InterruptedException e) {
            String errorStr = new StringBuilder()
                    .append("start avro netty serverl failed, error msg: ")
                    .append(e.getMessage())
                    .toString();
            logger.error(errorStr);
            throw new AvroServerException(errorStr);
        }
    }

    @Override
    public void stop() throws AvroServerException {
        try{
            server.close();
        }catch (Exception e){
            String errorStr = new StringBuilder()
                    .append("stop avro netty server failed, error msg: ")
                    .append(e.getMessage())
                    .toString();
            logger.error(errorStr);
            throw new AvroServerException(errorStr);
        }
    }

}
