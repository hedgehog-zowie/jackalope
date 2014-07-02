package com.iuni.data.avro.server;

import com.iuni.data.avro.ServerType;
import com.iuni.data.avro.common.Constants;
import com.iuni.data.avro.exceptions.AvroClientException;
import com.iuni.data.avro.exceptions.AvroServerException;
import org.apache.avro.Protocol;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.ipc.generic.GenericResponder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

/**
 * @author Nicholas
 *         Email:   nicholas.chen@iuni.com
 */
public abstract class Server extends GenericResponder {

    private static final Logger logger = LoggerFactory.getLogger(Server.class);

    protected org.apache.avro.ipc.Server server;

    public Server(Protocol protocol) {
        super(protocol);
    }

    public void start() throws AvroServerException {

    }

    public void stop() throws AvroServerException{

    }

    @Override
    public Object respond(Protocol.Message message, Object request) {
        GenericRecord req = (GenericRecord) request;
        GenericRecord reMessage = null;
        if (message.getName().equals("sayHello")) {
            Object name = req.get("name");
            //  do something...
            //取得返回值的类型
            reMessage = new GenericData.Record(super.getLocal().getType("nameMessage"));
            //直接构造回复
            reMessage.put("name", "Hello, " + name.toString());
            logger.info(reMessage.toString());
        }
        return reMessage;
    }


}
