package org.jackalope.study.avro;

import com.iuni.data.avro.common.AvroUtils;
import org.apache.avro.Schema;
import org.apache.avro.file.DataFileReader;
import org.apache.avro.file.DataFileWriter;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.generic.GenericDatumWriter;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.*;
import org.apache.avro.util.Utf8;
import org.junit.*;

import java.io.File;

public class AvroUtilsTest {

    private static final String schemaDescription =
            "{ \n" +
                    " \"namespace\": \"org.jackalope.study.avro\", \n" +
                    " \"name\": \"User\", \n" +
                    " \"type\": \"record\",\n" +
                    " \"fields\": [\n" +
                    " {\"name\": \"name\", \"type\": [\"string\", \"null\"] },\n" +
                    " {\"name\": \"num_likes\", \"type\": \"int\"},\n" +
                    " {\"name\": \"num_photos\", \"type\": \"int\"},\n" +
                    " {\"name\": \"num_groups\", \"type\": \"int\"} ]\n" +
                    "}";

    private static final String schemaDescriptionExt =
            " { \n" +
                    " \"namespace\": \"org.jackalope.study.avro\", \n" +
                    " \"name\": \"SpecialUser\", \n" +
                    " \"type\": \"record\",\n" +
                    " \"fields\": [\n" +
                    " {\"name\": \"user\", \"type\": \"org.jackalope.study.avro.User\" },\n" +
                    " {\"name\": \"specialData\", \"type\": \"int\"} ]\n" +
                    "}";

    private static final String userFilePath = "/user.json";
    private static final String sepUserFilePath = "/specialUser.json";
    private static final String fileName = "test.serialized";
    private static File file;
    private static Schema schema;
    private static Schema subSchema;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception{
        file = new File(fileName);
        subSchema = AvroUtils.parseSchema(new File(AvroUtilsTest.class.getResource(userFilePath).getPath()));
        schema = AvroUtils.parseSchema(new File(AvroUtilsTest.class.getResource(sepUserFilePath).getPath()));
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception{

    }

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testParseSchema() throws Exception {
        AvroUtils.parseSchema(schemaDescription);
        Schema extended = AvroUtils.parseSchema(schemaDescriptionExt);
        System.out.println(extended.toString(true));
    }

    @Test
    public void testSimpleInheritance() throws Exception {
        GenericRecord subRecord1 = new GenericData.Record(subSchema);
        subRecord1.put("name", new Utf8("Doctor Who"));
        subRecord1.put("num_likes", 1);
        subRecord1.put("num_photos", 0);
        subRecord1.put("num_groups", 423);
        GenericRecord record1 = new GenericData.Record(schema);
        record1.put("user", subRecord1);
        record1.put("specialData", 1);

        GenericRecord subRecord2 = new GenericData.Record(subSchema);
        subRecord2.put("name", new org.apache.avro.util.Utf8("Doctor WhoWho"));
        subRecord2.put("num_likes", 2);
        subRecord2.put("num_photos", 0);
        subRecord2.put("num_groups", 424);
        GenericRecord record2 = new GenericData.Record(schema);
        record2.put("user", subRecord2);
        record2.put("specialData", 2);

        DatumWriter<GenericRecord> datumWriter = new GenericDatumWriter<GenericRecord>(schema);
        DataFileWriter<GenericRecord> dataFileWriter = new DataFileWriter<GenericRecord>(datumWriter);
        dataFileWriter.create(schema, file);
        dataFileWriter.append(record1);
        dataFileWriter.append(record2);
        dataFileWriter.close();

        DatumReader<GenericRecord> datumReader = new GenericDatumReader<GenericRecord>(schema);
        DataFileReader<GenericRecord> dataFileReader = new DataFileReader<GenericRecord>(file, datumReader);
        GenericRecord user = null;
        while (dataFileReader.hasNext()) {
            user = dataFileReader.next(user);
            System.out.println(user);
        }
    }
}
