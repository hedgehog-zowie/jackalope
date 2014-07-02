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
public class HttpServer extends Server {

    private static final Logger logger = LoggerFactory.getLogger(HttpServer.class);

    protected HttpServer(Protocol protocol) throws AvroServerException {
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
    }

    public HttpServer(String avprFile) throws AvroServerException {
        File file = new File(HttpServer.class.getResource(avprFile).getPath());
        try {
            new HttpServer(Protocol.parse(file));
        } catch (IOException e) {
            String errorStr = new StringBuilder()
                    .append("start avro http server failed, error msg: ")
                    .append(e.getMessage())
                    .toString();
            logger.error(errorStr);
            throw new AvroServerException(errorStr);
        }
    }

    public HttpServer() throws AvroServerException {
        File file = new File(HttpServer.class.getResource(Constants.DEFAULT_AVPR).getPath());
        try {
            new HttpServer(Protocol.parse(file));
        } catch (IOException e) {
            String errorStr = new StringBuilder()
                    .append("start avro http server failed, error msg: ")
                    .append(e.getMessage())
                    .toString();
            logger.error(errorStr);
            throw new AvroServerException(errorStr);
        }
    }

}
