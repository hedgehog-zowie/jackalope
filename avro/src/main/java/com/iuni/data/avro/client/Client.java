package com.iuni.data.avro.client;

import com.iuni.data.avro.common.Constants;
import com.iuni.data.avro.exceptions.AvroClientException;
import org.apache.avro.Protocol;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.ipc.HttpTransceiver;
import org.apache.avro.ipc.NettyTransceiver;
import org.apache.avro.ipc.Transceiver;
import org.apache.avro.ipc.generic.GenericRequestor;
import org.jackalope.study.avro.HelloWorld.AvroHttpServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URL;

/**
 * @author Nicholas
 *         Email:   nicholas.chen@iuni.com
 */
public abstract class Client {

    private static final Logger logger = LoggerFactory.getLogger(Client.class);

    private String name;
    private Transceiver t;
    private Protocol protocol;
    private GenericRequestor requestor;

    protected Client(){}

    public Client(URL url) throws AvroClientException {
        t = new HttpTransceiver(url);
        init();
    }

    public Client(InetSocketAddress inetSocketAddress) throws AvroClientException {
        try {
            t = new NettyTransceiver(inetSocketAddress);
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
        init();
    }

    private void init() throws AvroClientException {
        try {
            protocol = Protocol.parse(new File(AvroHttpServer.class.getResource(Constants.DEFAULT_AVPR).getPath()));
        } catch (IOException e) {
            String errorStr = new StringBuilder()
                    .append("create protocol failed, error msg: ")
                    .append(e.getMessage())
                    .toString();
            logger.error(errorStr);
            throw new AvroClientException(errorStr);
        }
        try {
            requestor = new GenericRequestor(protocol, t);
        } catch (IOException e) {
            String errorStr = new StringBuilder()
                    .append("create requestor failed, error msg: ")
                    .append(e.getMessage())
                    .toString();
            logger.error(errorStr);
            throw new AvroClientException(errorStr);
        }
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
    public Protocol getProtocol(){
        return protocol;
    }
    public void setProtocol(Protocol protocol){
        this.protocol = protocol;
    }
}
