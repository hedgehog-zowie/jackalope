package com.iuni.data.avro.client;

import com.google.common.base.Preconditions;
import com.iuni.data.avro.ProtocolFactory;
import com.iuni.data.avro.exceptions.RpcClientException;
import com.iuni.data.avro.exceptions.RpcException;
import org.apache.avro.Protocol;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.ipc.Transceiver;
import org.apache.avro.ipc.generic.GenericRequestor;
import org.jackalope.study.conf.common.Configurable;
import org.jackalope.study.conf.common.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * @author Nicholas
 *         Email:   nicholas.chen@iuni.com
 */
public abstract class Client implements Configurable {

    private static final Logger logger = LoggerFactory.getLogger(Client.class);

    protected String name;
    protected Protocol protocol;
    protected Transceiver transceiver;
    protected GenericRequestor requestor;

    protected String host;
    protected int port;
    protected String protopath;

    protected final Integer default_port = 8088;
    protected final String default_protopath = "/helloWorld.json";

    public Client() {

    }

    public void getData() throws RpcClientException {
        GenericRecord requestData = new GenericData.Record(protocol.getType("nameMessage"));
        // initiate the request data
        requestData.put("name", "zowie");

        Object result = null;
        try {
            result = requestor.request("sayHello", requestData);
        } catch (IOException e) {
            String errorStr = new StringBuilder()
                    .append("request avro server failed, error msg: ")
                    .append(e.getMessage())
                    .toString();
            logger.error(errorStr);
            throw new RpcClientException(errorStr);
        }
        if (result instanceof GenericData.Record) {
            GenericData.Record record = (GenericData.Record) result;
            logger.debug(record.get("name").toString());
        }
        logger.debug(result.toString());
    }

    public void close() throws RpcClientException {
        try {
            this.transceiver.close();
        } catch (IOException e) {
            String errorStr = new StringBuilder()
                    .append("close connection failed, error msg: ")
                    .append(e.getMessage())
                    .toString();
            logger.error(errorStr);
            throw new RpcClientException(errorStr);
        }
    }

    @Override
    public void configure(Context context) {
        host = context.getString("host");
        Preconditions.checkNotNull(host, "Host cannot be empty, please specify in configuration file");
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
}
