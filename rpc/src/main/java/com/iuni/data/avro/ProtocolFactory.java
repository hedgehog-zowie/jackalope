package com.iuni.data.avro;

import com.iuni.data.avro.common.Constants;
import com.iuni.data.avro.exceptions.RpcException;
import org.apache.avro.Protocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author zowie
 *         Email:   hedgehog.zowie@gmail.com
 */
public class ProtocolFactory {

    private static final Logger logger = LoggerFactory.getLogger(ProtocolFactory.class);

    public static Protocol create() throws RpcException {
        String avprPath = DefaultServerFactory.class.getResource(Constants.DEFAULT_AVPR).getPath();
        return create(avprPath);
    }

    public static Protocol create(String avprPath) throws RpcException {
        File file = new File(avprPath);
        return create(file);
    }

    public static Protocol create(File file) throws RpcException {
        try {
            return Protocol.parse(file);
        } catch (IOException e) {
            String errorStr = new StringBuilder()
                    .append("parse protocol from file error, error msg: ")
                    .append(e.getMessage())
                    .toString();
            logger.error(errorStr);
            throw new RpcException(errorStr);
        }
    }

    public static Protocol create(InputStream inputStream) throws RpcException {
        try {
            return Protocol.parse(inputStream);
        } catch (IOException e) {
            String errorStr = new StringBuilder()
                    .append("parse protocol from file error, error msg: ")
                    .append(e.getMessage())
                    .toString();
            logger.error(errorStr);
            throw new RpcException(errorStr);
        }
    }

}
