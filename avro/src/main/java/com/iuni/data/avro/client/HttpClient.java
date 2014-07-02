package com.iuni.data.avro.client;

import com.iuni.data.avro.common.Constants;
import com.iuni.data.avro.exceptions.AvroClientException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author Nicholas
 *         Email:   nicholas.chen@iuni.com
 */
public class HttpClient extends Client {

    private static final Logger logger = LoggerFactory.getLogger(HttpClient.class);

    public HttpClient() throws AvroClientException {
        new HttpClient(Constants.getDefaultAddress());
    }

    public HttpClient(String address) throws AvroClientException {
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
        new HttpClient(url);
    }

    public HttpClient(URL url) throws AvroClientException {
        super(url);
    }

}
