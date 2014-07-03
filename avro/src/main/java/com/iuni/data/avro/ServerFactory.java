package com.iuni.data.avro;

import com.google.common.base.Preconditions;
import com.iuni.data.avro.common.Constants;
import com.iuni.data.avro.exceptions.AvroException;
import com.iuni.data.avro.exceptions.AvroServerException;
import com.iuni.data.avro.server.Server;
import com.iuni.data.avro.server.ServerType;
import org.apache.avro.Protocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author Nicholas
 *         Email:   nicholas.chen@iuni.com
 */
public class ServerFactory {

    private static final Logger logger = LoggerFactory.getLogger(ServerFactory.class);

    public static Server create(String name, String type) throws AvroException {
        Preconditions.checkNotNull(name, "name");
        Preconditions.checkNotNull(type, "type");
        String avprPath = ServerFactory.class.getResource(Constants.DEFAULT_AVPR).getPath();
        return create(name, avprPath, type);
    }

    public static Server create(String name, String avprPath, String type) throws AvroException {
        Preconditions.checkNotNull(name, "name");
        Preconditions.checkNotNull(avprPath, "avprPath");
        Preconditions.checkNotNull(type, "type");
        File file = new File(avprPath);
        return create(name, file, type);
    }

    public static Server create(String name, File file, String type) throws AvroException {
        Preconditions.checkNotNull(name, "name");
        Preconditions.checkNotNull(file.getAbsolutePath(), "file");
        Preconditions.checkNotNull(type, "type");
        Protocol protocol;
        try {
            protocol = Protocol.parse(file);
        } catch (IOException e) {
            String errorStr = new StringBuilder()
                    .append("parse protocol from file error, error msg: ")
                    .append(e.getMessage())
                    .toString();
            logger.error(errorStr);
            throw new AvroException(errorStr);
        }
        return create(name, protocol, type);
    }

    public static Server create(String name, InputStream inputStream, String type) throws AvroException {
        Preconditions.checkNotNull(name, "name");
        Preconditions.checkNotNull(inputStream, "inputStream");
        Preconditions.checkNotNull(type, "type");
        Protocol protocol;
        try {
            protocol = Protocol.parse(inputStream);
        } catch (IOException e) {
            String errorStr = new StringBuilder()
                    .append("parse protocol from inputsteam error, error msg: ")
                    .append(e.getMessage())
                    .toString();
            logger.error(errorStr);
            throw new AvroException(errorStr);
        }
        return create(name, protocol, type);
    }

    public static Server create(String name, Protocol protocol, String type) throws AvroException {
        Preconditions.checkNotNull(name, "name");
        Preconditions.checkNotNull(protocol, "protocol");
        Preconditions.checkNotNull(type, "type");
        logger.info("Creating instance of server: {}, type: {}", name, type);
        Class<? extends Server> serverClass = getClass(type);
        try {
            Server server = serverClass.newInstance();
            server.setName(name);
            server.setProtocol(protocol);
            return server;
        } catch (Exception e) {
            String errorStr = new StringBuilder()
                    .append("Create server failed, type: ")
                    .append(type)
                    .append(", class: ")
                    .append(serverClass.getName())
                    .append(". error msg: ")
                    .append(e.getMessage())
                    .toString();
            logger.error(errorStr);
            throw new AvroServerException(errorStr);
        }
    }

    private static Class<? extends Server> getClass(String type) throws AvroException {
        String serverClassName = type;
        ServerType serverType = ServerType.OTHER;
        try {
            serverType = ServerType.valueOf(type.toUpperCase());
        } catch (IllegalArgumentException ex) {
            logger.debug("Server type {} is a custom type", type);
        }
        if (!serverType.equals(ServerType.OTHER)) {
            serverClassName = serverType.getServerClassName();
        }
        try {
            return (Class<? extends Server>) Class.forName(serverClassName);
        } catch (Exception e) {
            String errorStr = new StringBuilder()
                    .append("Unable to load server type:")
                    .append(type)
                    .append(", class: ")
                    .append(serverClassName)
                    .append("error msg: ")
                    .append(e.getMessage())
                    .toString();
            logger.error(errorStr);
            throw new AvroException(errorStr);
        }
    }
}
