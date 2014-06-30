package org.jackalope.study.avro.HelloWorld;

import org.apache.avro.Protocol;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.ipc.HttpTransceiver;
import org.apache.avro.ipc.NettyTransceiver;
import org.apache.avro.ipc.Transceiver;
import org.apache.avro.ipc.generic.GenericRequestor;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.URL;

/**
 * Created by zowie on 14-7-1.
 */
public class AvroNettyClient {
    public static void main(String[] args) throws IOException {
        Protocol protocol = Protocol.parse(new File(AvroHttpServer.class.getResource(AvroHttpServer.fileName).getPath()));
        Transceiver t = new NettyTransceiver(new InetSocketAddress(InetAddress.getByName("localhost"), 8089));
        GenericRequestor requestor = new GenericRequestor(protocol, t);

        GenericRecord requestData = new GenericData.Record(protocol.getType("nameMessage"));
        // initiate the request data
        requestData.put("name", "zowie");

        System.out.println(requestData);
        Object result = requestor.request("sayHello", requestData);
        if (result instanceof GenericData.Record) {
            GenericData.Record record = (GenericData.Record) result;
            System.out.println(record.get("name"));
        }
        System.out.println(result);

    }
}
