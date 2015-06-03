package org.jackalope.study.avro.HelloWorld;

import org.apache.avro.Protocol;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.ipc.NettyServer;
import org.apache.avro.ipc.Server;
import org.apache.avro.ipc.generic.GenericResponder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * Created by zowie on 14-7-1.
 */
public class AvroNettyServer extends GenericResponder {

    private static final Logger logger = LoggerFactory.getLogger(AvroNettyServer.class);

    public AvroNettyServer(Protocol local) {
        super(local);
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        System.out.println("Starting server");
        Server server = new NettyServer(
                new AvroNettyServer(Protocol.parse(AvroNettyServer.class.getResourceAsStream(AvroHttpServer.fileName))),
                new InetSocketAddress(8089));
        System.out.println("Server started");
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
            System.out.println(reMessage);
        }
        return reMessage;
    }
}
