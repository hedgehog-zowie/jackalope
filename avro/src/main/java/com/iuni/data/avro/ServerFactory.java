package com.iuni.data.avro;

import com.google.common.base.Preconditions;
import com.iuni.data.avro.exceptions.AvroException;
import com.iuni.data.avro.exceptions.AvroServerException;
import com.iuni.data.avro.server.Server;
import com.iuni.data.avro.server.ServerType;
import org.apache.avro.Protocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Nicholas
 *         Email:   nicholas.chen@iuni.com
 */
public class ServerFactory {

    private static final Logger logger = LoggerFactory.getLogger(ServerFactory.class);

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
