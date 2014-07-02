package com.iuni.data.avro.server;

import com.iuni.data.avro.exceptions.AvroServerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Nicholas
 *         Email:   nicholas.chen@iuni.com
 */
public abstract class Server {

    private static final Logger logger = LoggerFactory.getLogger(Server.class);

    protected org.apache.avro.ipc.Server server;

    public Server() {
    }

    public void start() throws AvroServerException {
        try {
            server.start();
            server.join();
        } catch (InterruptedException e) {
            String errorStr = new StringBuilder()
                    .append("start avro http serverl failed, error msg: ")
                    .append(e.getMessage())
                    .toString();
            logger.error(errorStr);
            throw new AvroServerException(errorStr);
        }
    }

    public void stop() throws AvroServerException {
        try{
            server.close();
        }catch (Exception e){
            String errorStr = new StringBuilder()
                    .append("stop avro http server failed, error msg: ")
                    .append(e.getMessage())
                    .toString();
            logger.error(errorStr);
            throw new AvroServerException(errorStr);
        }
    }

}
