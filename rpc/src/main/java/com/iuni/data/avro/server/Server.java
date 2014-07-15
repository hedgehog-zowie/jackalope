package com.iuni.data.avro.server;

import com.google.common.base.Preconditions;
import com.iuni.data.avro.ProtocolFactory;
import com.iuni.data.avro.exceptions.RpcException;
import com.iuni.data.avro.exceptions.RpcServerException;
import org.apache.avro.Protocol;
import org.jackalope.study.conf.common.Configurable;
import org.jackalope.study.conf.common.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Nicholas
 *         Email:   nicholas.chen@iuni.com
 */
public abstract class Server implements Configurable {

    private static final Logger logger = LoggerFactory.getLogger(Server.class);

    protected String name;
    protected Integer port;
    protected String protopath;
    protected Protocol protocol;
    protected org.apache.avro.ipc.Server server;

    protected final Integer default_port = 8088;
    protected final String default_protopath = "/helloWorld.json";

    public void start() throws RpcServerException {
//        try {
//            server.start();
//            server.join();
//        } catch (InterruptedException e) {
//            String errorStr = new StringBuilder()
//                    .append("start avro http serverl failed, error msg: ")
//                    .append(e.getMessage())
//                    .toString();
//            logger.error(errorStr);
//            throw new RpcServerException(errorStr);
//        }
        server.start();
    }

    public void stop() throws RpcServerException {
        try{
            server.close();
        }catch (Exception e){
            String errorStr = new StringBuilder()
                    .append("stop avro http server failed, error msg: ")
                    .append(e.getMessage())
                    .toString();
            logger.error(errorStr);
            throw new RpcServerException(errorStr);
        }
    }

    @Override
    public void configure(Context context) {
        port = context.getInteger("port", default_port);
        Preconditions.checkNotNull(port, "Port name cannot be empty, please specify in configuration file");
        if(port <=0 || port > 65535) {
            port = default_port;
            logger.warn("Invalid port specified, initializing client to default capacity of {}", default_port);
        }
        protopath = context.getString("protopath", default_protopath);
        try {
            protocol = ProtocolFactory.create(this.getClass().getResourceAsStream(protopath));
        } catch (RpcException e) {
            e.printStackTrace();
        }
    }

    /***************************/
    /****getters and setters****/
    /***************************/
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Protocol getProtocol(){
        return protocol;
    }
    public void setProtocol(Protocol protocol){
        this.protocol = protocol;
    }
}
