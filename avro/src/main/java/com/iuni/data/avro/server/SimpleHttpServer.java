package com.iuni.data.avro.server;

import com.iuni.data.avro.common.Constants;
import com.iuni.data.avro.exceptions.AvroServerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * @author Nicholas
 *         Email:   nicholas.chen@iuni.com
 */
public class SimpleHttpServer extends Server {

    private static final Logger logger = LoggerFactory.getLogger(SimpleHttpServer.class);

    @Override
    public void start() throws AvroServerException {
        try {
            server = new org.apache.avro.ipc.HttpServer(
                    new Handler(protocol), Constants.DEFAULT_PORT);
        } catch (IOException e) {
            String errorStr = new StringBuilder()
                    .append("start avro http server failed, error msg: ")
                    .append(e.getMessage())
                    .toString();
            logger.error(errorStr);
            throw new AvroServerException(errorStr);
        }
        super.start();
    }

}
