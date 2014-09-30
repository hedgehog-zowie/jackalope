package Hbase;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HRegionInfo;
import org.apache.hadoop.hbase.ServerName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.log4j.Appender;
import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggingEvent;
import org.databene.contiperf.PerfTest;
import org.databene.contiperf.junit.ContiPerfRule;
import org.jackalope.study.hbase.HBaseHelper;
import org.jackalope.study.hbase.HexStringSplit;
import org.jackalope.study.hbase.IuniRowKeyGenerator;
import org.junit.*;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Nicholas
 *         Email:   nicholas.chen@iuni.com
 */
public class ScannerTest {
    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(ScannerTest.class);

    private static HBaseHelper helper;
    private static byte[][] splitKeys;
    private static int defaultRegionNum = 100;
    private static Configuration conf;

    private static String cfName = "f";
    private final static String columnNameHost = "host";
    private final static String columnNameTimeY = "time_y";
    private final static String columnNameTimeM = "time_m";
    private final static String columnNameTimeD = "time_d";
    private final static String columnNameTimeHH = "time_hh";
    private final static String columnNameTimeMM = "time_mm";
    private final static String columnNameTimeSS = "time_ss";
    private final static String columnNameTimeW = "time_w";
    private final static String columnNameADID = "ad_id";
    private final static String columnNameCountry = "country";
    private final static String columnNameArea = "area";
    private final static String columnNameRegion = "region";
    private final static String columnNameCity = "city";
    private final static String columnNameCounty = "county";
    private final static String columnNameIsp = "isp";
    private static String idxName = "idx";

    private static String tableName = "iunilog";
    private static String timeIdxTableName = "timeIdx";
    private static String userIdxTableName = "userIdx";
    private static String reqIdxTableName = "reqIdx";

    private static String tableNameDev = "iunilogDev";
    private static String timeIdxTableNameDev = "timeIdxDev";
    private static String userIdxTableNameDev = "userIdxDev";
    private static String reqIdxTableNameDev = "reqIdxDev";

    private static HTable table1 = null;
    private static String table1Name = "t_coo1";
    private static HTable table2 = null;
    private static String table2Name = "t_coo2";
    private static HTable idxTable1 = null;
    private static String idxTable1Name = "t_coo1_idx";
    private static HTable idxTable2 = null;
    private static String idxTable2Name = "t_coo2_idx";
    private static HTable nocooTable = null;
    private static String noCooTableName = "t_coo_noidx";

    private Map<String, String> resultMap = new HashMap<>();
    private Lock lock = new ReentrantLock();

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
//        System.setProperty("hadoop.home.dir", "D:\\hadoop\\hadoop-2.3.0-cdh5.0.0");
        conf = HBaseConfiguration.create();
        conf.set("hbase.zookeeper.quorum", "physical.hadoop, vir-vm1.hadoop, vir-vm2.hadoop");
        helper = HBaseHelper.getHelper(conf);
        splitKeys = HexStringSplit.split(defaultRegionNum);

        table1 = new HTable(conf, table1Name);
        table2 = new HTable(conf, table2Name);
        idxTable1 = new HTable(conf, idxTable1Name);
        idxTable2 = new HTable(conf, idxTable2Name);
        nocooTable = new HTable(conf, noCooTableName);
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        table1.close();
    }

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Rule
    public ContiPerfRule i = new ContiPerfRule();

    @Test
    public void createTable() throws IOException {
        if (helper.existsTable(tableName))
            helper.dropTable(tableName);
        helper.createTableWithCoprocessor(tableName, splitKeys, "com.iuni.data.analyze.hbase.sindex.IuniIndexRegionObserve",
                HBaseHelper.defaultCoJarFilePath, HBaseHelper.defaultCoPriority, null, cfName);

        if (helper.existsTable(timeIdxTableName))
            helper.dropTable(timeIdxTableName);
        helper.createTable(timeIdxTableName, splitKeys, cfName);
        if (helper.existsTable(userIdxTableName))
            helper.dropTable(userIdxTableName);
        helper.createTable(userIdxTableName, splitKeys, cfName);
        if (helper.existsTable(reqIdxTableName))
            helper.dropTable(reqIdxTableName);
        helper.createTable(reqIdxTableName, splitKeys, cfName);

//        if (helper.existsTable(tableNameDev))
//            helper.dropTable(tableNameDev);
//        helper.createTableWithCoprocessor(tableNameDev, splitKeys, "com.iuni.data.analyze.hbase.sindex.IuniIndexRegionObserveDev",
//                HBaseHelper.defaultCoJarFilePath, HBaseHelper.defaultCoPriority, null, cfName);
//
//        if (helper.existsTable(timeIdxTableNameDev))
//            helper.dropTable(timeIdxTableNameDev);
//        helper.createTable(timeIdxTableNameDev, splitKeys, cfName);
//        if (helper.existsTable(userIdxTableNameDev))
//            helper.dropTable(userIdxTableNameDev);
//        helper.createTable(userIdxTableNameDev, splitKeys, cfName);
//        if (helper.existsTable(reqIdxTableNameDev))
//            helper.dropTable(reqIdxTableNameDev);
//        helper.createTable(reqIdxTableNameDev, splitKeys, cfName);
    }

    @Test
    public void testCreate() throws IOException {
        if (helper.existsTable(table1Name))
            helper.dropTable(table1Name);
        helper.createTableWithCoprocessor(table1Name, splitKeys, "com.iuni.data.analyze.hbase.sindex.IuniIndexRegionObserveT1",
                HBaseHelper.defaultCoJarFilePath, HBaseHelper.defaultCoPriority, null, cfName);

        if (helper.existsTable(idxTable1Name))
            helper.dropTable(idxTable1Name);
        helper.createTable(idxTable1Name, splitKeys, cfName);

        if (helper.existsTable(table2Name))
            helper.dropTable(table2Name);
        helper.createTableWithCoprocessor(table2Name, splitKeys, "com.iuni.data.analyze.hbase.sindex.IuniIndexRegionObserveT2",
                HBaseHelper.defaultCoJarFilePath, HBaseHelper.defaultCoPriority, null, cfName);

        if (helper.existsTable(idxTable2Name))
            helper.dropTable(idxTable2Name);
        helper.createTable(idxTable2Name, splitKeys, cfName);

        if (helper.existsTable(noCooTableName))
            helper.dropTable(noCooTableName);
        helper.createTable(noCooTableName, splitKeys, cfName);
    }

    @Test
    public void testSplit() throws IOException {
        System.out.println("splitKeys.length:" + splitKeys.length);
        for (int i = 0; i < splitKeys.length; i++)
            System.out.println("i:" + Bytes.toString(splitKeys[i]));
    }

    @Test
    public void showRegionInfo() throws IOException {
        NavigableMap<HRegionInfo, ServerName> regions = table1.getRegionLocations();
        for (HRegionInfo hRegionInfo : regions.keySet()) {
            String startKey = Bytes.toString(hRegionInfo.getStartKey());
            if ("".equals(startKey))
                startKey = HexStringSplit.DEFAULT_MIN_HEX;
            System.out.println("startKey: " + startKey);
//            System.out.println("HRegionInfo: " + hRegionInfo + "\tServerName: " + regions.get(hRegionInfo));
        }
    }

    @Test
    public void addDataNoCoo() throws IOException {
//        HTable table = new HTable(conf, noCooTableName);
        byte[] cfb = Bytes.toBytes(cfName);
        byte[] cb = Bytes.toBytes(columnNameHost);
        List<Put> putList = new ArrayList<>();
        for (int j = 0; j < 10000; j++) {
            Put put = new Put(IuniRowKeyGenerator.getReverseTimestampKey(String.format("%09d", j)));
            put.add(cfb, cb, Bytes.toBytes(String.valueOf(j)));
            putList.add(put);
//            nocooTable.put(put);
        }
        nocooTable.put(putList);
    }

    @Test
    public void getValue() throws IOException {
        String rowkey = "75d53b60-99d6-48a4-9964-008397a4ff8d-001272737";
        Get get = new Get(Bytes.toBytes(rowkey));
        Result result = table1.get(get);
        byte[] val = result.getValue(Bytes.toBytes(cfName), Bytes.toBytes(columnNameHost));
        System.out.println("Value: " + Bytes.toString(val));
    }

    @Test
    public void addDataWithCoo1() throws IOException {
        byte[] cfb = Bytes.toBytes(cfName);
        byte[] chost = Bytes.toBytes(columnNameHost);
        byte[] cyear = Bytes.toBytes(columnNameTimeY);
        byte[] cmonth = Bytes.toBytes(columnNameTimeM);
        byte[] cday = Bytes.toBytes(columnNameTimeD);
        byte[] chour = Bytes.toBytes(columnNameTimeHH);
        byte[] cminute = Bytes.toBytes(columnNameTimeMM);
        byte[] csecond = Bytes.toBytes(columnNameTimeSS);
        byte[] cweekday = Bytes.toBytes(columnNameTimeW);
        byte[] cadid = Bytes.toBytes(columnNameADID);
        byte[] ccountry = Bytes.toBytes(columnNameCountry);
        byte[] carea = Bytes.toBytes(columnNameArea);
        byte[] cregion = Bytes.toBytes(columnNameRegion);
        byte[] ccity = Bytes.toBytes(columnNameCity);
        byte[] ccounty = Bytes.toBytes(columnNameCounty);
        byte[] cisp = Bytes.toBytes(columnNameIsp);
        String[] is = {"101", "102", "103", "104", "105", "106", "107"};
        Integer[] ws = {1, 2, 3, 4, 5, 6, 7};
        for (int j = 0; j < 1; j++) {
            Put put = new Put(IuniRowKeyGenerator.getReverseTimestampKey(String.valueOf(System.currentTimeMillis())));
            put.add(cfb, chost, Bytes.toBytes("18.8.0.238"));
            Calendar calendar = Calendar.getInstance();
            put.add(cfb, cyear, Bytes.toBytes(String.format("%04d", calendar.get(Calendar.YEAR))));
            put.add(cfb, cmonth, Bytes.toBytes(String.format("%02d", calendar.get(Calendar.MONTH) + 1)));
            put.add(cfb, cday, Bytes.toBytes(String.format("%02d", calendar.get(Calendar.DAY_OF_MONTH))));
            put.add(cfb, chour, Bytes.toBytes(String.format("%02d", calendar.get(Calendar.HOUR_OF_DAY))));
            put.add(cfb, cminute, Bytes.toBytes(String.format("%02d", calendar.get(Calendar.MINUTE))));
            put.add(cfb, csecond, Bytes.toBytes(String.format("%02d", calendar.get(Calendar.SECOND))));
            int i = (int) (Math.random() * 7);
            put.add(cfb, cweekday, Bytes.toBytes(ws[i]));
            put.add(cfb, cadid, Bytes.toBytes(is[i]));
            put.add(cfb, ccountry, Bytes.toBytes("中国"));
            put.add(cfb, carea, Bytes.toBytes("华南"));
            put.add(cfb, cregion, Bytes.toBytes("广东省"));
            put.add(cfb, ccity, Bytes.toBytes("深圳市"));
            put.add(cfb, ccounty, Bytes.toBytes("福田区"));
            put.add(cfb, cisp, Bytes.toBytes("电信"));
            table1.put(put);
        }
    }

    @Test
    @PerfTest(invocations = 1, threads = 10)
    public void addDataWithCoo2() throws IOException {
        byte[] cfb = Bytes.toBytes(cfName);
        byte[] chost = Bytes.toBytes(columnNameHost);
        byte[] cyear = Bytes.toBytes(columnNameTimeY);
        byte[] cmonth = Bytes.toBytes(columnNameTimeM);
        byte[] cday = Bytes.toBytes(columnNameTimeD);
        byte[] chour = Bytes.toBytes(columnNameTimeHH);
        byte[] cminute = Bytes.toBytes(columnNameTimeMM);
        byte[] csecond = Bytes.toBytes(columnNameTimeSS);
        byte[] cweekday = Bytes.toBytes(columnNameTimeW);
        byte[] cadid = Bytes.toBytes(columnNameADID);
        byte[] ccountry = Bytes.toBytes(columnNameCountry);
        byte[] carea = Bytes.toBytes(columnNameArea);
        byte[] cregion = Bytes.toBytes(columnNameRegion);
        byte[] ccity = Bytes.toBytes(columnNameCity);
        byte[] ccounty = Bytes.toBytes(columnNameCounty);
        byte[] cisp = Bytes.toBytes(columnNameIsp);
        String[] is = {"101,102,103,104,105,106,107,108"};
        Integer[] ws = {1, 2, 3, 4, 5, 6, 7};
        List<Put> putList = new ArrayList<>();
        for (int j = 0; j < 1; j++) {
            Put put = new Put(IuniRowKeyGenerator.getReverseTimestampKey(String.valueOf(System.currentTimeMillis())));
            put.add(cfb, chost, Bytes.toBytes("18.8.0.238"));
            put.add(cfb, cyear, Bytes.toBytes(String.format("%04d", Calendar.YEAR)));
            put.add(cfb, cmonth, Bytes.toBytes(String.format("%02d", Calendar.MONTH + 1)));
            put.add(cfb, cday, Bytes.toBytes(String.format("%02d", Calendar.DAY_OF_MONTH)));
            put.add(cfb, chour, Bytes.toBytes(String.format("%02d", Calendar.HOUR_OF_DAY)));
            put.add(cfb, cminute, Bytes.toBytes(String.format("%02d", Calendar.MINUTE)));
            put.add(cfb, csecond, Bytes.toBytes(String.format("%02d", Calendar.SECOND)));
            int i = (int) Math.random() % 8;
            put.add(cfb, cweekday, Bytes.toBytes(ws[i]));
            put.add(cfb, cadid, Bytes.toBytes(is[i]));
            put.add(cfb, ccountry, Bytes.toBytes("中国"));
            put.add(cfb, carea, Bytes.toBytes("华南"));
            put.add(cfb, cregion, Bytes.toBytes("广东省"));
            put.add(cfb, ccity, Bytes.toBytes("深圳市"));
            put.add(cfb, ccounty, Bytes.toBytes("福田区"));
            put.add(cfb, cisp, Bytes.toBytes("电信"));
            putList.add(put);
        }
        try {
            lock.lock();
            table2.put(putList);
        } finally {
            lock.unlock();
        }
        logger.info("done.");
    }

    @Test
    @PerfTest(invocations = 10000, threads = 10)
    public void testCounter() throws IOException {
        HTable table = new HTable(conf, tableName);
        table.incrementColumnValue(Bytes.toBytes("totalRow"), Bytes.toBytes("f"), Bytes.toBytes("iCol"), 1);
        Get get = new Get(Bytes.toBytes("totalRow"));
        Result result = table.get(get);
        long totalRow = Bytes.toLong(result.getValue(Bytes.toBytes("f"), Bytes.toBytes("iCol")));
        System.out.println("totalRow: " + totalRow);
    }

    @Test
    public void testGetCounter() throws IOException {
        int ll = 0;
        long ln1 = 0, ln2 = 0, ln3 = 0, ln4 = 0;

        HTable table = new HTable(conf, tableName);
        Get get = new Get(Bytes.toBytes("totalRow"));
        Result result = table.get(get);
        byte[] value = result.getValue(Bytes.toBytes("f"), Bytes.toBytes("iCol"));
        if (value != null)
            ln1 = Bytes.toLong(value);

        HTable timetable = new HTable(conf, timeIdxTableName);
        Get timeGet = new Get(Bytes.toBytes("totalRow"));
        Result timeResult = timetable.get(timeGet);
        byte[] timeValue = timeResult.getValue(Bytes.toBytes("f"), Bytes.toBytes("iCol"));
        if(timeValue != null)
            ln2 = Bytes.toLong(timeValue);

        HTable userTable = new HTable(conf, userIdxTableName);
        Get userGet = new Get(Bytes.toBytes("totalRow"));
        Result userResult = userTable.get(userGet);
        byte[] userValue = userResult.getValue(Bytes.toBytes("f"), Bytes.toBytes("iCol"));
        if(userValue != null)
            ln3 = Bytes.toLong(userValue);

        HTable reqTable = new HTable(conf, reqIdxTableName);
        Get reqGet = new Get(Bytes.toBytes("totalRow"));
        Result reqResult = reqTable.get(reqGet);
        byte[] reqValue = reqResult.getValue(Bytes.toBytes("f"), Bytes.toBytes("iCol"));
        if(reqValue != null)
            ln4 = Bytes.toLong(reqValue);

        long lt1 = System.currentTimeMillis(), lt2 = System.currentTimeMillis(), lt3 = System.currentTimeMillis(), lt4 = System.currentTimeMillis(), ct = System.currentTimeMillis();
        long nn;
        double nt, speed;
        while (true) {
            try {
                Thread.sleep(1000 * 10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println("==========" + ll + "==========");

            result = table.get(get);
            value = result.getValue(Bytes.toBytes("f"), Bytes.toBytes("iCol"));
            long i = 0;
            if (value != null)
                i = Bytes.toLong(value);
            ct = System.currentTimeMillis();
            nn = i - ln1;
            nt = (ct - lt1) / 1000;
            speed = (double) nn / nt;
            System.out.println("table total:" + i + "，写入条数：" + nn + "，用时：" + nt + "s，速度：" + String.format("%.2f", speed) + "/s，当前时间：" + Calendar.getInstance().getTime());
            lt1 = ct;
            ln1 = i;

            timeResult = timetable.get(timeGet);
            timeValue = timeResult.getValue(Bytes.toBytes("f"), Bytes.toBytes("iCol"));
            long j = 0;
            if (timeValue != null)
                j = Bytes.toLong(timeValue);
            ct = System.currentTimeMillis();
            nn = j - ln2;
            nt = (ct - lt2) / 1000;
            speed = (double) nn / nt;
            System.out.println("time idx table total:" + j + "，写入条数：" + nn + "，用时：" + nt + "s，速度：" + String.format("%.2f", speed) + "/s，当前时间：" + Calendar.getInstance().getTime());
            lt2 = ct;
            ln2 = j;

            userResult = userTable.get(userGet);
            userValue = userResult.getValue(Bytes.toBytes("f"), Bytes.toBytes("iCol"));
            long k = 0;
            if (userValue != null)
                k = Bytes.toLong(userValue);
            ct = System.currentTimeMillis();
            nn = k - ln3;
            nt = (ct - lt3) / 1000;
            speed = (double) nn / nt;
            System.out.println("user idx table total:" + k + "，写入条数：" + nn + "，用时：" + nt + "s，速度：" + String.format("%.2f", speed) + "/s，当前时间：" + Calendar.getInstance().getTime());
            lt3 = ct;
            ln3 = k;

            reqResult = reqTable.get(reqGet);
            reqValue = reqResult.getValue(Bytes.toBytes("f"), Bytes.toBytes("iCol"));
            long l = 0;
            if (reqValue != null)
                l = Bytes.toLong(reqValue);
            ct = System.currentTimeMillis();
            nn = l - ln4;
            nt = (ct - lt4) / 1000;
            speed = (double) nn / nt;
            System.out.println("req idx table total:" + l + "，写入条数：" + nn + "，用时：" + nt + "s，速度：" + String.format("%.2f", speed) + "/s，当前时间：" + Calendar.getInstance().getTime());
            lt4 = ct;
            ln4 = l;

            ll++;
        }
    }

    @Test
    public void testScanAll() throws IOException {

        HTable timeTable = new HTable(conf, timeIdxTableName);
        Scan timeScan = new Scan();
        ResultScanner resultScanner = timeTable.getScanner(timeScan);
        long i = 0;
        for (Result res : resultScanner)
            i++;
        System.out.println("time table total:" + i);

//        Scan scan1 = new Scan();
//        ResultScanner resultScanner = table1.getScanner(scan1);
//        long i = 0;
//        for (Result res : resultScanner)
//            i++;
//        System.out.println("table1 total:" + i);
//
//        Scan scan2 = new Scan();
//        resultScanner = table2.getScanner(scan2);
//        long j = 0;
//        for (Result res : resultScanner)
//            j++;
//        System.out.println("table2 total:" + j);

//        Scan nocooScan = new Scan();
//        HTable nocooTable = new HTable(conf, noCooTableName);
//        ResultScanner nocooResultScanner = nocooTable.getScanner(nocooScan);
//        long l = 0;
//        for (Result res : nocooResultScanner)
//            l++;
//        System.out.println("nocoo table total:" + l);
//        nocooResultScanner.close();
//        nocooTable.close();
    }

    public void basicScan(HTable idxTable, HTable table, Scan idxScan, String startKey, String endKey) throws IOException {
        if (idxTable == null || idxScan == null)
            return;
        idxScan.setStartRow(Bytes.toBytes(startKey)).
                setStopRow(Bytes.toBytes(endKey));
        ResultScanner idxScanner = idxTable.getScanner(idxScan);
//        List<String> rowkeyList = new ArrayList<>();

        for (Result res : idxScanner) {
//            rowkeyList.add(Bytes.toString(res.getValue(Bytes.toBytes(cfName), Bytes.toBytes(columnNameHost))));
            byte[] value = res.getValue(Bytes.toBytes(cfName), Bytes.toBytes("value"));
            if(value == null)
                continue;
            Get get = new Get(value);
            Result result = table.get(get);
            String rowkeyStr = Bytes.toString(result.getRow());
            if (rowkeyStr != null)
                resultMap.put(rowkeyStr, Bytes.toString(value));
        }

        idxScanner.close();
    }

    @Test
    public void scanTableDevWithIdx() throws IOException{
        HTable table = new HTable(conf, "iunilogDev");
        HTable timeIdxTable = new HTable(conf, "timeIdxDev");
        String startTime = "20140701000000";
        String endTime = "20140701010000";
        Scan idxScan = new Scan();
        idxScan.addColumn(Bytes.toBytes(cfName), Bytes.toBytes("value"));
        String startKey = new StringBuilder().append(HexStringSplit.DEFAULT_MIN_HEX).append("timeidx").append(startTime).toString();
        String endKey = new StringBuilder().append(HexStringSplit.DEFAULT_MIN_HEX).append("timeidx").append(endTime).toString();
        basicScan(timeIdxTable, table, idxScan, startKey, endKey);
        for (int i = 0; i < splitKeys.length; i++) {
            startKey = new StringBuilder().append(Bytes.toString(splitKeys[i])).append("timeidx").append(startTime).toString();
            endKey = new StringBuilder().append(Bytes.toString(splitKeys[i])).append("timeidx").append(endTime).toString();
            basicScan(timeIdxTable, table, idxScan, startKey, endKey);
        }
        List<Map.Entry<String, String>> list = new ArrayList(resultMap.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<String, String>>() {
            public int compare(Map.Entry<String, String> o1, Map.Entry<String, String> o2) {
                return ((Comparable) ((Map.Entry) o1).getValue())
                        .compareTo(((Map.Entry) o2).getValue());
            }
        });
        for (Map.Entry<String, String> entry : list) {
            System.out.println(entry.getKey() + ":" + entry.getValue());
        }
        table.close();
        timeIdxTable.close();
    }

    @Test
    public void testScan() throws IOException {
        int startValue = 0;
        int endValue = 10;
        String startStr = String.format("%09d", startValue);
        String endStr = String.format("%09d", endValue);

        Scan idxScan = new Scan();
        idxScan.addColumn(Bytes.toBytes(cfName), Bytes.toBytes(columnNameHost));

        // start key is 00000000
        String startKey = new StringBuilder().append(HexStringSplit.DEFAULT_MIN_HEX).append(idxName).append(startStr).toString();
        String endKey = new StringBuilder().append(HexStringSplit.DEFAULT_MIN_HEX).append(idxName).append(endStr).toString();
        basicScan(idxTable2, table2, idxScan, startKey, endKey);
        for (int i = 0; i < splitKeys.length; i++) {
            startKey = new StringBuilder().append(Bytes.toString(splitKeys[i])).append(idxName).append(startStr).toString();
            endKey = new StringBuilder().append(Bytes.toString(splitKeys[i])).append(idxName).append(endStr).toString();
            basicScan(idxTable2, table2, idxScan, startKey, endKey);
        }

        List<Map.Entry<String, String>> list = new ArrayList(resultMap.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<String, String>>() {
            public int compare(Map.Entry<String, String> o1, Map.Entry<String, String> o2) {
                return ((Comparable) ((Map.Entry) o1).getValue())
                        .compareTo(((Map.Entry) o2).getValue());
            }
        });
        for (Map.Entry<String, String> entry : list) {
            System.out.println(entry.getKey() + ":" + entry.getValue());
        }
        table1.close();
        table2.close();
        idxTable1.close();
        idxTable2.close();
    }

    @Test
    public void testDeleteByCondition() throws Exception {
        String startStr = "18.8.0.238";
        String endStr = "18.8.0.239";

        List<Delete> deleteList = new ArrayList<>();
        List<Delete> idxDeleteList = new ArrayList<>();

        String startKey = new StringBuilder().append(HexStringSplit.DEFAULT_MIN_HEX).append(idxName).append(startStr).toString();
        String endKey = new StringBuilder().append(HexStringSplit.DEFAULT_MIN_HEX).append(idxName).append(endStr).toString();
        Scan idxScan = new Scan();
//        idxScan.addColumn(Bytes.toBytes(cfName), Bytes.toBytes(columnNameHost));
        idxScan.addColumn(Bytes.toBytes(cfName), Bytes.toBytes(columnNameHost));
        idxScan.setStartRow(Bytes.toBytes(startKey)).
                setStopRow(Bytes.toBytes(endKey));

        ResultScanner idxScanner = idxTable1.getScanner(idxScan);
        for (Result res : idxScanner) {
            Delete idxDelete = new Delete(res.getRow());
            idxDelete.deleteColumn(Bytes.toBytes(cfName), Bytes.toBytes(columnNameHost));
            idxDeleteList.add(idxDelete);
            Delete delete = new Delete(res.getValue(Bytes.toBytes(cfName), Bytes.toBytes(columnNameHost)));
            delete.deleteColumn(Bytes.toBytes(cfName), Bytes.toBytes(columnNameHost));
            deleteList.add(delete);
        }
        for (int i = 0; i < splitKeys.length; i++) {
            startKey = new StringBuilder().append(Bytes.toString(splitKeys[i])).append(idxName).append(startStr).toString();
            endKey = new StringBuilder().append(Bytes.toString(splitKeys[i])).append(idxName).append(endStr).toString();

            Scan fidxScan = new Scan();
            fidxScan.addColumn(Bytes.toBytes(cfName), Bytes.toBytes(columnNameHost));
            fidxScan.setStartRow(Bytes.toBytes(startKey)).
                    setStopRow(Bytes.toBytes(endKey));

            ResultScanner fidxScanner = idxTable1.getScanner(idxScan);
            fidxScanner = idxTable1.getScanner(fidxScan);

            for (Result res : fidxScanner) {
                Delete idxDelete = new Delete(res.getRow());
                idxDelete.deleteColumn(Bytes.toBytes(cfName), Bytes.toBytes(columnNameHost));
                idxDeleteList.add(idxDelete);
                Delete delete = new Delete(res.getValue(Bytes.toBytes(cfName), Bytes.toBytes(columnNameHost)));
                delete.deleteColumn(Bytes.toBytes(cfName), Bytes.toBytes(columnNameHost));
                deleteList.add(delete);
            }
        }
        table1.delete(deleteList);
        idxTable1.delete(idxDeleteList);

    }

    @Test
    public void testBasicDelete() throws Exception {
        byte[] rowkey = Bytes.toBytes("b553db1e-9ad8-4767-a8be-af934a2c5fee-1408004851295");
        Delete delete = new Delete(rowkey);
        delete.deleteColumn(Bytes.toBytes(cfName), Bytes.toBytes(columnNameHost));
        table1.delete(delete);
//        table1.checkAndDelete(rowkey,Bytes.toBytes(cfName),Bytes.toBytes(columnNameHost),Bytes.toBytes("aaaahost"),delete);
        for (Map.Entry<byte[], List<Cell>> entry : delete.getFamilyCellMap().entrySet()) {
            for (Cell cell : entry.getValue()) {
                System.out.println(Bytes.toString(cell.getRow()));
                System.out.println(Bytes.toString(cell.getFamily()));
                System.out.println(Bytes.toString(cell.getQualifier()));
                System.out.println(Bytes.toString(cell.getValue()));
            }
        }
    }

    @Test
    public void testScanCache() throws IOException {
        Logger log = Logger.getLogger("org.apache.hadoop");
        int caching = 1, batch = 1;

        final int[] counters = {0, 0};
        Appender appender = new AppenderSkeleton() {
            @Override
            protected void append(LoggingEvent event) {
                String msg = event.getMessage().toString();
                if (msg != null && msg.contains("Call: next")) {
                    counters[0]++;
                }
            }

            @Override
            public void close() {
            }

            @Override
            public boolean requiresLayout() {
                return false;
            }
        };
        log.removeAllAppenders();
        log.setAdditivity(false);
        log.addAppender(appender);
        log.setLevel(Level.DEBUG);

        Scan scan = new Scan();
        scan.setCaching(caching);  // co ScanCacheBatchExample-1-Set Set caching and batch parameters.
        scan.setBatch(batch);
        ResultScanner scanner = idxTable1.getScanner(scan);
        for (Result result : scanner) {
            counters[1]++; // co ScanCacheBatchExample-2-Count Count the number of Results available.
        }
        scanner.close();
        System.out.println("Caching: " + caching + ", Batch: " + batch +
                ", Results: " + counters[1] + ", RPCs: " + counters[0]);
    }

}
