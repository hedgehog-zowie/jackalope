package com.iuni.data.avro;

import com.iuni.data.avro.client.Client;
import com.iuni.data.avro.client.ClientType;
import com.iuni.data.avro.common.Constants;
import com.iuni.data.avro.exceptions.AvroException;
import com.iuni.data.avro.exceptions.AvroServerException;
import org.apache.avro.Protocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

import java.lang.reflect.Constructor;

/**
 * @author Nicholas
 *         Email:   nicholas.chen@iuni.com
 */
public class ClientFactory {

    private static final Logger logger = LoggerFactory.getLogger(ClientFactory.class);

    public static Client create(String name, Protocol protocol, String type) throws AvroException {
        return create(name, protocol, Constants.DEFAULT_HOST, Constants.DEFAULT_PORT, type);
    }

    public static Client create(String name, Protocol protocol, Integer port, String type) throws AvroException {
        return create(name, protocol, Constants.DEFAULT_HOST, port, type);
    }

    public static Client create(String name, Protocol protocol, String host, String type) throws AvroException {
        return create(name, protocol, host, Constants.DEFAULT_PORT, type);
    }

    public static Client create(String name, Protocol protocol, String host, Integer port, String type) throws AvroException {
        Preconditions.checkNotNull(name, "name");
        Preconditions.checkNotNull(protocol, "protocol");
        Preconditions.checkNotNull(host, "host");
        Preconditions.checkNotNull(port, "port");
        Preconditions.checkNotNull(type, "type");
        logger.info("Creating instance of client: {}, type: {}", name, type);
        Class<? extends Client> clientClass = getClass(type);
        Constructor<?> constructor;
        try {
            constructor = clientClass.getConstructor(String.class, Integer.class, Protocol.class);
            constructor.setAccessible(true);
        } catch (NoSuchMethodException e) {
            String errorStr = new StringBuilder()
                    .append("constructor is not exist, error msg: ")
                    .append(e.getMessage())
                    .toString();
            logger.error(errorStr);
            throw new AvroException(errorStr);
        }
        try {
            Client abstractClient = (Client) constructor.newInstance(host, port, protocol);
            abstractClient.setName(name);
            return abstractClient;
        } catch (Exception e) {
            String errorStr = new StringBuilder()
                    .append("Create client failed, type: ")
                    .append(type)
                    .append(", class: ")
                    .append(clientClass.getName())
                    .append(". error msg: ")
                    .append(e.getMessage())
                    .toString();
            logger.error(errorStr);
            throw new AvroServerException(errorStr);
        }
    }

    private static Class<? extends Client> getClass(String type) throws AvroException {
        String clientClassName = type;
        ClientType clientType = ClientType.OTHER;
        try {
            clientType = ClientType.valueOf(type.toUpperCase());
        } catch (IllegalArgumentException ex) {
            logger.debug("Client type {} is a custom type", type);
        }
        if (!clientType.equals(ClientType.OTHER)) {
            clientClassName = clientType.getClientClassName();
        }
        try {
            return (Class<? extends Client>) Class.forName(clientClassName);
        } catch (Exception e) {
            String errorStr = new StringBuilder()
                    .append("Unable to load client type:")
                    .append(type)
                    .append(", class: ")
                    .append(clientClassName)
                    .append("error msg: ")
                    .append(e.getMessage())
                    .toString();
            logger.error(errorStr);
            throw new AvroException(errorStr);
        }
    }

}
