package com.iuni.data.avro.client;

import com.iuni.data.avro.ProtocolFactory;
import com.iuni.data.avro.common.Constants;
import com.iuni.data.avro.exceptions.AvroClientException;
import com.iuni.data.avro.exceptions.AvroException;
import org.apache.avro.Protocol;
import org.apache.avro.ipc.HttpTransceiver;
import org.apache.avro.ipc.generic.GenericRequestor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author Nicholas
 *         Email:   nicholas.chen@iuni.com
 */
public class SimpleHttpClient extends Client {

    private static final Logger logger = LoggerFactory.getLogger(SimpleHttpClient.class);

    public SimpleHttpClient() throws AvroException {
        this(Constants.getDefaultAddress(), ProtocolFactory.create());
    }

    public SimpleHttpClient(String host, Integer port, Protocol protocol) throws AvroClientException {
        this(new StringBuilder().append("http://").append(host).append(":").append(port).toString(), protocol);
    }

    public SimpleHttpClient(String address, Protocol protocol) throws AvroClientException {
        URL url;
        try {
            url = new URL(address);
        } catch (MalformedURLException e) {
            String errorStr = new StringBuilder()
                    .append("create url failed, address is: ")
                    .append(address)
                    .append(". error msg: ")
                    .append(e.getMessage())
                    .toString();
            logger.error(errorStr);
            throw new AvroClientException(errorStr);
        }
        initHttpClient(url, protocol);
    }

    public SimpleHttpClient(URL url, Protocol protocol) throws AvroClientException {
        initHttpClient(url, protocol);
    }

    private void initHttpClient(URL url, Protocol protocol) throws AvroClientException {
        this.transceiver = new HttpTransceiver(url);
        this.protocol = protocol;
        try {
            requestor = new GenericRequestor(this.protocol, transceiver);
        } catch (IOException e) {
            String errorStr = new StringBuilder()
                    .append("create requestor failed, error msg: ")
                    .append(e.getMessage())
                    .toString();
            logger.error(errorStr);
            throw new AvroClientException(errorStr);
        }
    }

}
