package org.jackalope.study.avro.quickStart;

import org.apache.avro.Schema;
import org.apache.avro.file.DataFileReader;
import org.apache.avro.file.DataFileWriter;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.generic.GenericDatumWriter;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.DatumWriter;

import java.io.File;
import java.io.IOException;

/**
 * Created by zowie on 14-6-30.
 */
public class QuickStart {

    private static Schema schema;
    private static final String fileName = "/user.json";
    private static String filepath;
    private static final String nFileName = "/user.serialized";
    private static File nfile;

    public QuickStart() throws IOException {
        filepath = QuickStart.class.getResource(fileName).getPath();
        schema = new Schema.Parser().parse(new File(filepath));
        nfile = new File(nFileName);
    }

    /**
     * 序列化
     *
     * @throws IOException
     */
    public void serialize() throws IOException {
        GenericRecord user1 = new GenericData.Record(schema);
        user1.put("name", "Alyssa");
        user1.put("num_groups", 128);

        GenericRecord user2 = new GenericData.Record(schema);
        user2.put("name", "Ben");
        user2.put("num_likes", 32);
        user2.put("num_photos", 64);
        user2.put("num_groups", 16);

        DatumWriter<GenericRecord> datumWriter = new GenericDatumWriter<GenericRecord>(schema);
        DataFileWriter<GenericRecord> dataFileWriter = new DataFileWriter<GenericRecord>(datumWriter);
        dataFileWriter.create(schema, nfile);
        dataFileWriter.append(user1);
        dataFileWriter.append(user2);
        dataFileWriter.close();
    }

    /**
     * 反序列化
     *
     * @throws IOException
     */
    public void deserialize() throws IOException {
        DatumReader<GenericRecord> datumReader = new GenericDatumReader<GenericRecord>(schema);
        DataFileReader<GenericRecord> dataFileReader = new DataFileReader<GenericRecord>(nfile, datumReader);
        GenericRecord user = null;
        while (dataFileReader.hasNext()) {
            user = dataFileReader.next(user);
            System.out.println(user);
        }
    }
}
