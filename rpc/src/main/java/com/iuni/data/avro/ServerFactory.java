package com.iuni.data.avro;

import com.iuni.data.avro.exceptions.RpcServerException;
import com.iuni.data.avro.server.Server;
import org.apache.avro.Protocol;

/**
 * @author Nicholas
 *         Email:   nicholas.chen@iuni.com
 */
public interface ServerFactory {
    Server create(String name, Protocol protocol, String type) throws RpcServerException;
    Class<? extends Server> getClass(String type) throws RpcServerException;
}
