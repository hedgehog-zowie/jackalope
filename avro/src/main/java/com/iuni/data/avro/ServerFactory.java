package com.iuni.data.avro;

import com.iuni.data.avro.common.Constants;
import org.apache.avro.Protocol;
import org.apache.avro.ipc.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Nicholas on 2014/7/1 0001.
 */
public class ServerFactory {

    private static final Logger logger = LoggerFactory.getLogger(ServerFactory.class);

    private Protocol protocol;
    private Server server;

    public void initProtocol() throws IOException {
        String avprPath = ServerFactory.class.getResource(Constants.DEFAULT_AVPR).getPath();
        initProtocol(avprPath);
    }

    public void initProtocol(String avprPath) throws IOException {
        File file = new File(avprPath);
        initProtocol(file);
    }

    public void initProtocol(File file) throws IOException {
        protocol = Protocol.parse(file);
    }

    public void initProtocol(InputStream inputStream) throws IOException {
        protocol = Protocol.parse(inputStream);
    }

}
