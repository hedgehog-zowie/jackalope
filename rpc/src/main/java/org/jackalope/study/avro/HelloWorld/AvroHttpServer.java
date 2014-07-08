package org.jackalope.study.avro.HelloWorld;

import org.apache.avro.Protocol;
import org.apache.avro.Protocol.Message;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.ipc.HttpServer;
import org.apache.avro.ipc.Server;
import org.apache.avro.ipc.generic.GenericResponder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * Created by zowie on 14-6-30.
 */
public class AvroHttpServer extends GenericResponder {

    private static final Logger logger = LoggerFactory.getLogger(AvroHttpServer.class);

    public static final String fileName = "/helloWorld.json";

    public AvroHttpServer(Protocol protocol) {
        super(protocol);
    }

    public Object respond(Message message, Object request) throws Exception {
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
            System.out.println(reMessage);
        }
        return reMessage;
    }

    public static void main(String[] args) throws Exception {
        int port = 8088;
        try {
            Server server = new HttpServer(
                    new AvroHttpServer(Protocol.parse(
                            new File(AvroHttpServer.class.getResource(fileName).getPath()))),
                    port);
            server.start();
            server.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}