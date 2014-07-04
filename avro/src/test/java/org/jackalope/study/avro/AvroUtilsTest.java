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
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
    private static final String fileName = "test.json.serialized";
    private static File file;
    private static Schema schema;
    private static Schema subSchema;

    private static final String requestEvtPath = "/avsc/requestEvt.json";
    private static final String requestEvtIpPath = "/avsc/requestEvtIp.json";
    private static final String requestEvtPvPath = "/avsc/requestEvtPv.json";
    private static final String requestEvtUvPath = "/avsc/requestEvtUv.json";
    private static final String requestEvtAreaPath = "/avsc/requestEvtArea.json";
    private static final String requestEvtUserPagePath = "/avsc/requestEvtUserPage.json";

    private static final String locationPath = "/avsc/Location.json";
    private static final String kpiPvPath = "/avsc/KpiPV.json";
    private static final String kpiUvPath = "/avsc/KpiUV.json";
    private static final String kpiIpPath = "/avsc/KpiIP.json";
    private static final String kpiAreaPath = "/avsc/KpiArea.json";
    private static final String kpiUserPagePath = "/avsc/KpiUserPage.json";

    private static final String responseEvtPath = "/avsc/responseEvt.json";
    private static final String responseEvtIpPath = "/avsc/responseEvtIp.json";
    private static final String responseEvtPvPath = "/avsc/responseEvtPv.json";
    private static final String responseEvtUvPath = "/avsc/responseEvtUv.json";
    private static final String responseEvtAreaPath = "/avsc/responseEvtArea.json";
    private static final String responseEvtUserPagePath = "/avsc/responseEvtUserPage.json";

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        file = new File(fileName);
        subSchema = AvroUtils.parseSchema(new File(AvroUtilsTest.class.getResource(userFilePath).getPath()));
        schema = AvroUtils.parseSchema(new File(AvroUtilsTest.class.getResource(sepUserFilePath).getPath()));
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {

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
    public void testParseRequestEvt() throws IOException {
        Schema requestEvtSchema = AvroUtils.parseSchema(AvroUtilsTest.class.getResourceAsStream(requestEvtPath));
        System.out.println(requestEvtSchema);
        System.out.println("==================================================");
        Schema requestEvtPvSchema = AvroUtils.parseSchema(AvroUtilsTest.class.getResourceAsStream(requestEvtPvPath));
        System.out.println(requestEvtPvSchema);
        System.out.println("==================================================");
        Schema requestEvtUvSchema = AvroUtils.parseSchema(AvroUtilsTest.class.getResourceAsStream(requestEvtUvPath));
        System.out.println(requestEvtUvSchema);
        System.out.println("==================================================");
        Schema requestEvtIpSchema = AvroUtils.parseSchema(AvroUtilsTest.class.getResourceAsStream(requestEvtIpPath));
        System.out.println(requestEvtIpSchema);
        System.out.println("==================================================");
        Schema requestEvtAreaSchema = AvroUtils.parseSchema(AvroUtilsTest.class.getResourceAsStream(requestEvtAreaPath));
        System.out.println(requestEvtAreaSchema);
        System.out.println("==================================================");
        Schema requestEvtUserPageSchema = AvroUtils.parseSchema(AvroUtilsTest.class.getResourceAsStream(requestEvtUserPagePath));
        System.out.println(requestEvtUserPageSchema);
        System.out.println("==================================================");

        List<GenericRecord> recordList = new ArrayList<GenericRecord>();
        GenericRecord record = new GenericData.Record(requestEvtSchema);

        record.put("transactionId", new Utf8("123456"));
        record.put("kpiType", new Utf8("PV"));
        record.put("startTime", new Utf8("0123456789"));
        record.put("endTime", new Utf8("9876543210"));
        record.put("timeType", "hh");
        recordList.add(record);

        GenericRecord pvEvt = new GenericData.Record(requestEvtSchema);
        pvEvt.put("transactionId", new Utf8("123456"));
        pvEvt.put("kpiType", new Utf8("UV"));
        pvEvt.put("startTime", new Utf8("0123456789"));
        pvEvt.put("endTime", new Utf8("9876543210"));
        pvEvt.put("timeType", "hh");
        recordList.add(pvEvt);

        GenericRecord uvEvt = new GenericData.Record(requestEvtSchema);
        uvEvt.put("transactionId", new Utf8("123456"));
        uvEvt.put("kpiType", new Utf8("IP"));
        uvEvt.put("startTime", new Utf8("0123456789"));
        uvEvt.put("endTime", new Utf8("9876543210"));
        uvEvt.put("timeType", "hh");
        recordList.add(uvEvt);

        GenericRecord ipEvt = new GenericData.Record(requestEvtSchema);
        ipEvt.put("transactionId", new Utf8("123456"));
        ipEvt.put("kpiType", new Utf8("USERPAGE"));
        ipEvt.put("startTime", new Utf8("0123456789"));
        ipEvt.put("endTime", new Utf8("9876543210"));
        ipEvt.put("timeType", "hh");
        recordList.add(ipEvt);

        GenericRecord areaEvt = new GenericData.Record(requestEvtSchema);
        areaEvt.put("transactionId", new Utf8("123456"));
        areaEvt.put("kpiType", new Utf8("AREA"));
        areaEvt.put("startTime", new Utf8("0123456789"));
        areaEvt.put("endTime", new Utf8("9876543210"));
        areaEvt.put("timeType", "hh");
        recordList.add(areaEvt);

        encodeAndDecode(requestEvtSchema, recordList);
    }

    @Test
    public void testParseResponseEvt() throws IOException {
        // parse kpi
        Schema kpipvSchema = AvroUtils.parseSchema(AvroUtilsTest.class.getResourceAsStream(kpiPvPath));
        System.out.println(kpipvSchema);
        System.out.println("==================================================");
        Schema kpiuvSchema = AvroUtils.parseSchema(AvroUtilsTest.class.getResourceAsStream(kpiUvPath));
        System.out.println(kpiuvSchema);
        System.out.println("==================================================");
        Schema kpiipSchema = AvroUtils.parseSchema(AvroUtilsTest.class.getResourceAsStream(kpiIpPath));
        System.out.println(kpiipSchema);
        System.out.println("==================================================");
        Schema locationSchema = AvroUtils.parseSchema(AvroUtilsTest.class.getResourceAsStream(locationPath));
        System.out.println(locationSchema);
        System.out.println("==================================================");
        Schema kpiareaSchema = AvroUtils.parseSchema(AvroUtilsTest.class.getResourceAsStream(kpiAreaPath));
        System.out.println(kpiareaSchema);
        System.out.println("==================================================");
        Schema kpiuserPageSchema = AvroUtils.parseSchema(AvroUtilsTest.class.getResourceAsStream(kpiUserPagePath));
        System.out.println(kpiuserPageSchema);
        System.out.println("==================================================");

        // parse response
        Schema responseEvtSchema = AvroUtils.parseSchema(AvroUtilsTest.class.getResourceAsStream(responseEvtPath));
        System.out.println(responseEvtSchema);
        System.out.println("==================================================");
        Schema responseEvtPvSchema = AvroUtils.parseSchema(AvroUtilsTest.class.getResourceAsStream(responseEvtPvPath));
        System.out.println(responseEvtPvSchema);
        System.out.println("==================================================");
        Schema responseEvtUvSchema = AvroUtils.parseSchema(AvroUtilsTest.class.getResourceAsStream(responseEvtUvPath));
        System.out.println(responseEvtUvSchema);
        System.out.println("==================================================");
        Schema responseEvtIpSchema = AvroUtils.parseSchema(AvroUtilsTest.class.getResourceAsStream(responseEvtIpPath));
        System.out.println(responseEvtIpSchema);
        System.out.println("==================================================");
        Schema responseEvtAreaSchema = AvroUtils.parseSchema(AvroUtilsTest.class.getResourceAsStream(responseEvtAreaPath));
        System.out.println(responseEvtAreaSchema);
        System.out.println("==================================================");
        Schema responseEvtUserPageSchema = AvroUtils.parseSchema(AvroUtilsTest.class.getResourceAsStream(responseEvtUserPagePath));
        System.out.println(responseEvtUserPageSchema);
        System.out.println("==================================================");

        // encode and decode
        List<GenericRecord> recordList = new ArrayList<GenericRecord>();

        GenericRecord kpipv = new GenericData.Record(kpipvSchema);
        kpipv.put("time", new Utf8("0123456789"));
        kpipv.put("timeType", "hh");
        kpipv.put("num", (long) 10000);
        recordList.add(kpipv);
        encodeAndDecode(kpipvSchema, recordList);

        recordList.clear();
        GenericRecord kpiuv = new GenericData.Record(kpiuvSchema);
        kpiuv.put("time", new Utf8("0123456789"));
        kpiuv.put("timeType", "hh");
        kpiuv.put("num", (long) 10000);
        recordList.add(kpiuv);
        encodeAndDecode(kpiuvSchema, recordList);

        recordList.clear();
        GenericRecord kpiip = new GenericData.Record(kpiipSchema);
        kpiip.put("time", new Utf8("0123456789"));
        kpiip.put("timeType", "hh");
        kpiip.put("num", (long) 10000);
        recordList.add(kpiip);
        encodeAndDecode(kpiipSchema, recordList);

        recordList.clear();
        GenericRecord location = new GenericData.Record(locationSchema);
        location.put("country", new Utf8("中国"));
        location.put("area", new Utf8("华南"));
        location.put("province", new Utf8("广东省"));
        location.put("city", new Utf8("深圳市"));
        location.put("county", new Utf8("福田区"));
        location.put("isp", new Utf8("电信"));
        recordList.add(location);
        encodeAndDecode(locationSchema, recordList);

        recordList.clear();
        GenericRecord kpiArea = new GenericData.Record(kpiareaSchema);
        kpiArea.put("timeType", "hh");
        kpiArea.put("location", location);
        kpiArea.put("totalCount", (long) 10000);
        kpiArea.put("totalTime", (long) 10000);
        kpiArea.put("totalSize", (long) 10000);
        kpiArea.put("avrSpeed", (double) 1.23);
        recordList.add(kpiArea);
        encodeAndDecode(kpiareaSchema, recordList);

        recordList.clear();
        GenericRecord kpiUserPage = new GenericData.Record(kpiuserPageSchema);
        kpiUserPage.put("time", new Utf8("0123456789"));
        kpiUserPage.put("timeType", "hh");
        kpiUserPage.put("userId", new Utf8("123456"));
        kpiUserPage.put("pageUrl", new Utf8("http://localhost:8088"));
        kpiUserPage.put("num", (long) 10000);
        recordList.add(kpiUserPage);
        encodeAndDecode(kpiuserPageSchema, recordList);

        recordList.clear();
        GenericRecord responseEvt = new GenericData.Record(responseEvtSchema);
        responseEvt.put("transactionId", new Utf8("123456"));
        responseEvt.put("resultCode", "R00000");
        responseEvt.put("description", "haha test");
        recordList.add(responseEvt);
        encodeAndDecode(responseEvtSchema, recordList);

        recordList.clear();
        GenericRecord responseEvtPv = new GenericData.Record(responseEvtPvSchema);
        responseEvtPv.put("evt", responseEvt);
        List<GenericRecord> pvdatalist = new ArrayList<GenericRecord>();
        pvdatalist.add(kpipv);
        responseEvtPv.put("data", pvdatalist);
        recordList.add(responseEvtPv);
        encodeAndDecode(responseEvtPvSchema, recordList);

        recordList.clear();
        GenericRecord responseEvtUv = new GenericData.Record(responseEvtUvSchema);
        responseEvtUv.put("evt", responseEvt);
        List<GenericRecord> uvdatalist = new ArrayList<GenericRecord>();
        uvdatalist.add(kpiuv);
        responseEvtUv.put("data", uvdatalist);
        recordList.add(responseEvtUv);
        encodeAndDecode(responseEvtUvSchema, recordList);

        recordList.clear();
        GenericRecord responseEvtIp = new GenericData.Record(responseEvtIpSchema);
        responseEvtIp.put("evt", responseEvt);
        List<GenericRecord> ipdatalist = new ArrayList<GenericRecord>();
        ipdatalist.add(kpiip);
        responseEvtIp.put("data", ipdatalist);
        recordList.add(responseEvtIp);
        encodeAndDecode(responseEvtIpSchema, recordList);

        recordList.clear();
        GenericRecord responseEvtArea = new GenericData.Record(responseEvtAreaSchema);
        responseEvtArea.put("evt", responseEvt);
        List<GenericRecord> areadatalist = new ArrayList<GenericRecord>();
        areadatalist.add(kpiArea);
        responseEvtArea.put("data", areadatalist);
        recordList.add(responseEvtArea);
        encodeAndDecode(responseEvtAreaSchema, recordList);

        recordList.clear();
        GenericRecord responseEvtUserPage = new GenericData.Record(responseEvtUserPageSchema);
        responseEvtUserPage.put("evt", responseEvt);
        List<GenericRecord> userpagedatalist = new ArrayList<GenericRecord>();
        userpagedatalist.add(kpiUserPage);
        responseEvtUserPage.put("data", userpagedatalist);
        recordList.add(responseEvtUserPage);
        encodeAndDecode(responseEvtUserPageSchema, recordList);

    }

    private void encodeAndDecode(Schema schema, List<GenericRecord> recordList) throws IOException {
        // encode
        DatumWriter<GenericRecord> datumWriter = new GenericDatumWriter<GenericRecord>(schema);
        DataFileWriter<GenericRecord> dataFileWriter = new DataFileWriter<GenericRecord>(datumWriter);
        dataFileWriter.create(schema, file);
        for (GenericRecord record : recordList)
            dataFileWriter.append(record);
        dataFileWriter.close();
        // decode
        DatumReader<GenericRecord> datumReader = new GenericDatumReader<GenericRecord>(schema);
        DataFileReader<GenericRecord> dataFileReader = new DataFileReader<GenericRecord>(file, datumReader);
        GenericRecord genericRecord = null;
        while (dataFileReader.hasNext()) {
            genericRecord = dataFileReader.next(genericRecord);
            System.out.println(genericRecord);
        }
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
