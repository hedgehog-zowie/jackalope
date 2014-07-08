package com.iuni.data.avro.server;

import org.apache.avro.Protocol;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.ipc.generic.GenericResponder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author zowie
 *         Email:   hedgehog.zowie@gmail.com
 */
public class Handler extends GenericResponder {

    private static final Logger logger = LoggerFactory.getLogger(Handler.class);

    public Handler(Protocol protocol) {
        super(protocol);
    }

    @Override
    public Object respond(Protocol.Message message, Object request) throws Exception {
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
