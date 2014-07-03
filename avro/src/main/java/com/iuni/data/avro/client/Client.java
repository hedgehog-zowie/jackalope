package com.iuni.data.avro.client;

import com.iuni.data.avro.exceptions.AvroClientException;
import org.apache.avro.Protocol;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.ipc.HttpTransceiver;
import org.apache.avro.ipc.NettyTransceiver;
import org.apache.avro.ipc.Transceiver;
import org.apache.avro.ipc.generic.GenericRequestor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URL;

/**
 * @author Nicholas
 *         Email:   nicholas.chen@iuni.com
 */
public abstract class Client {

    private static final Logger logger = LoggerFactory.getLogger(Client.class);

    protected String name;
    protected Transceiver transceiver;
    protected Protocol protocol;
    protected GenericRequestor requestor;

    public Client() {

    }

    public void analyzeData() throws AvroClientException {
        GenericRecord requestData = new GenericData.Record(protocol.getType("nameMessage"));
        // initiate the request data
        requestData.put("name", "zowie");

        System.out.println(requestData);
        Object result = null;
        try {
            result = requestor.request("sayHello", requestData);
        } catch (IOException e) {
            String errorStr = new StringBuilder()
                    .append("request avro server failed, error msg: ")
                    .append(e.getMessage())
                    .toString();
            logger.error(errorStr);
            throw new AvroClientException(errorStr);
        }
        if (result instanceof GenericData.Record) {
            GenericData.Record record = (GenericData.Record) result;
            logger.debug(record.get("name").toString());
            System.out.println(record.get("name"));
        }
        logger.debug(result.toString());
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
