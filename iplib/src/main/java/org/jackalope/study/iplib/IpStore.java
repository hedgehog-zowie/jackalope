package org.jackalope.study.iplib;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 从APNIC获取IP段 从TAOBAO IP库获取IP段详细信息，存入本地IP库文件
 * <p/>
 * 包括三个部分：头信息区，IP段区，IP段信息区
 * 1，头信息区，格式为first_start_ip_pos/last_start_ip_pos/storeSize/ipNum，格式说明：
 * first_start_ip_pos：第一个START_IP的位置，4字节
 * last_start_ip_pos：最后一个START_IP的位置，4字节
 * storeSize：IP段个数，4字节
 * ipNum：总IP个数，8字节
 * 2，IP段区，格式为start_ip/end_ip_pos，格式说明：
 * start_ip：存放起始IP地址，4字节
 * end_ip_pos：结束地址的位置，4字节
 * 3，IP段信息区，格式为end_ip/country/area/region/city/county/isp，
 * end_ip：结束IP地址，8字节
 * country：国家，不限长
 * area：地区，不限长
 * region：省，不限长
 * city：市，不限长
 * county：区/县，不限长
 * isp：网络服务提供商，不限长
 * <p/>
 * 使用方法：
 * IpStore ipRangeList = IpStore.getInstance(); ipRangeList.initIpStore();
 *
 * @author Nicholas
 */
public class IpStore {
    private static final Logger logger = LoggerFactory.getLogger(IpStore.class);
    public static final String afrinicUrl = "http://ftp.afrinic.net/pub/stats/afrinic/delegated-afrinic-latest";
    public static final String apnicUrl = "http://ftp.apnic.net/apnic/stats/apnic/delegated-apnic-latest";
    public static final String arinUrl = "http://ftp.arin.net/pub/stats/arin/delegated-arin-extended-latest";
    public static final String ripenccUrl = "http://ftp.ripe.net/ripe/stats/delegated-ripencc-latest";
    public static final String lacnicUrl = "http://ftp.lacnic.net/pub/stats/lacnic/delegated-lacnic-latest";

    public static final String afrinicPath = "/config/iplibrary/delegated-afrinic-latest";
    public static final String apnicPath = "/config/iplibrary/delegated-apnic-latest";
    public static final String arinPath = "/config/iplibrary/delegated-arin-extended-latest";
    public static final String ripenccPath = "/config/iplibrary/delegated-ripencc-latest";
    public static final String lacnicPath = "/config/iplibrary/delegated-lacnic-latest";

    public final String countryPath = "/config/iplibrary/all-country";

    private static final String splitStr = "[|]";
    private static final String ipType = "ipv4";
    private static final String ipRegex = "\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}";
    private static final int COUNTRY_ID_INDEX = 1;
    private static final int IP_TYPE_INDEX = 2;
    private static final int START_IP_INDEX = 3;
    private static final int HOST_NUM_INDEX = 4;

    /**
     * 中国区标识
     */
    private static final String COUNTRY_ID_CHINA = "CN";

    /**
     * 头信息区长度
     */
    private static final int HEAD_FIELD_LENGTH = 20;
    /**
     * IP段区每条记录长度
     */
    private static final int IP_REGION_FIELD_LENGTH = 8;
    /**
     * IP信息区每条记录长度
     */
    // private final int IPINFO_REGION_FIELD_LENGTH = 8;

    private IpUtils ipUtils;

    private static IpStore instance;

    /**
     * 国家MAP，根据域名后缀查找国家名称
     */
    private final Map<String, Country> countryMap;

    private static final int minIpRangeSize = 256;
    private static int chinaIpRangeNum = 0;

    private final String ipRangeFile = "/ipRange.csv";

    private static int firstStartIpPos;
    private static int lastStartIpPos;
    private static int ipAreaPos;
    private static int ipInfoAreaPos;
    private static long ipNum;
    private static int lastIpInfoAreaPos;
    private static IpInfo lastIpInfo;
    private static final int oneFileSize = 10000;

    private IpStore() throws IpException {
        ipUtils = IpUtils.getInstance();
        countryMap = new HashMap<String, Country>();
    }

    public static IpStore getInstance() throws IpException {
        if (instance == null)
            instance = new IpStore();
        return instance;
    }

    /**
     * 获取ip范围
     *
     * @return
     * @throws IpException
     */
    private void saveIpRange() throws IpException {
        String[] filePaths = {afrinicPath, apnicPath, arinPath, ripenccPath, lacnicPath};
        String[] urls = {afrinicUrl, apnicUrl, arinUrl, ripenccUrl, lacnicUrl};
        List<IpRange> ipRangeList = new ArrayList<IpRange>();
        for (int i = 0; i < 5; i++) {
            InputStream inputstream = null;
            try {
                inputstream = readResourceIntoStream(filePaths[i]);
            } catch (Exception e) {
                String errorStr = new StringBuilder(
                        "IpStore local filepath is not exsit:")
                        .append(filePaths[i]).append(". error msg: ")
                        .append(e.getMessage()).toString();
                logger.info(errorStr);
            }
            if (inputstream != null)
                ipRangeList.addAll(getIpRange(inputstream));
            else {
                URL url;
                try {
                    url = new URL(urls[i]);
                } catch (MalformedURLException e) {
                    String errorStr = new StringBuilder("IpStore get url error, url is:")
                            .append(urls[i])
                            .append(". error msg: ")
                            .append(e.getMessage())
                            .toString();
                    logger.error(errorStr);
                    throw new IpException(errorStr);
                }
                URLConnection con;
                try {
                    con = url.openConnection();
                } catch (IOException e) {
                    String errorStr = new StringBuilder("IpStore open url error, url is:")
                            .append(urls[i])
                            .append(". error msg: ")
                            .append(e.getMessage())
                            .toString();
                    logger.error(errorStr);
                    throw new IpException(errorStr);
                }
                BufferedReader reader;
                try {
                    reader = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    String errorStr = new StringBuilder("IpStore read input stream error, encoding is UTF-8. error msg: ")
                            .append(e.getMessage())
                            .toString();
                    logger.error(errorStr);
                    throw new IpException(errorStr);
                } catch (IOException e) {
                    String errorStr = new StringBuilder("IpStore read input stream error, error msg: ")
                            .append(e.getMessage())
                            .toString();
                    logger.error(errorStr);
                    throw new IpException(errorStr);
                }
                ipRangeList.addAll(getIpRange(inputstream));
            }
        }
        Collections.sort(ipRangeList, new IpComparator());
        // save ip range to iprange.cvs
        // save all ip range
        String filepath = System.getProperty("user.dir") + ipRangeFile;
        File file = new File(filepath);
        if (file.exists())
            file.delete();
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(filepath, true);
        } catch (IOException e) {
            String errorStr = new StringBuilder("IpStore save ipRange error, error msg: ")
                    .append(e.getMessage())
                    .toString();
            logger.error(errorStr);
            throw new IpException(errorStr);
        }
        for (IpRange ipRange : ipRangeList) {
            String str = new StringBuilder()
                    .append(ipRange.getStartIpStr())
                    .append(",")
                    .append(ipRange.getHostNum())
                    .append(",")
                    .append(ipRange.getCountryId())
                    .append("\n")
                    .toString();
            try {
                fileWriter.write(str);
            } catch (IOException e) {
                String errorStr = new StringBuilder("IpStore save ipRange error, error msg: ")
                        .append(e.getMessage())
                        .toString();
                logger.error(errorStr);
                throw new IpException(errorStr);
            }
            try {
                fileWriter.flush();
            } catch (IOException e) {
                String errorStr = new StringBuilder("IpStore save ipRange error, error msg: ")
                        .append(e.getMessage())
                        .toString();
                logger.error(errorStr);
                throw new IpException(errorStr);
            }
        }
        try {
            fileWriter.close();
        } catch (IOException e) {
            String errorStr = new StringBuilder("IpStore save ipRange error, close ipRange file error, error msg: ")
                    .append(e.getMessage())
                    .toString();
            logger.error(errorStr);
            throw new IpException(errorStr);
        }
    }

    /**
     * 从文件中获取ip范围
     *
     * @return
     * @throws IpException
     * @throws java.io.IOException
     */
    private List<IpRange> getIpRange(InputStream inputstream) throws IpException {
        BufferedReader reader;
        reader = new BufferedReader(new InputStreamReader(inputstream));
        return getIpRange(reader);
    }

    /**
     * get iprange from reader
     * @param reader
     * @return
     * @throws IpException
     */
    private List<IpRange> getIpRange(BufferedReader reader) throws IpException {
        String info = null;
        List<IpRange> ipRangeList = new ArrayList<IpRange>();
        try {
            while ((info = reader.readLine()) != null) {
                if (info.startsWith("afrinic|") || info.startsWith("apnic|")
                        || info.startsWith("arin|") || info.startsWith("lacnic|")
                        || info.startsWith("ripencc|")) {
                    String[] str = info.split(splitStr);
                    if (!"".equals(str[COUNTRY_ID_INDEX].trim())
                            && str[IP_TYPE_INDEX].equals(ipType)
                            && str[START_IP_INDEX].matches(ipRegex)) {
                        String startIpStr = str[START_IP_INDEX].trim();
                        byte[] startIp = ipUtils.getIpByteArrayFromString(startIpStr);
                        int hostNum = Integer.parseInt(str[HOST_NUM_INDEX].trim());
                        ipRangeList.add(new IpRange(startIpStr, startIp, hostNum, str[COUNTRY_ID_INDEX].trim()));
                        if (COUNTRY_ID_CHINA.equals(str[COUNTRY_ID_INDEX].trim())) {
                            chinaIpRangeNum += (hostNum / 256);
                        }
                    }
                }
            }
        } catch (IOException e) {
            String errorStr = new StringBuilder("IpStore get ip range error. error msg: ")
                    .append(e.getMessage())
                    .toString();
            logger.error(errorStr);
            throw new IpException(errorStr);
        }
        try {
            reader.close();
        } catch (IOException e) {
            String errorStr = new StringBuilder("IpStore close buffer error. error msg: ")
                    .append(e.getMessage())
                    .toString();
            logger.error(errorStr);
            throw new IpException(errorStr);
        }
        return ipRangeList;
    }

    /**
     * 通过ip从淘宝IP库中获取ip数据，转换成IpInfo
     *
     * @param ip
     * @return
     * @throws java.io.IOException
     * @throws IpException
     */
    public IpInfo getIpInfoFromTaobao(String ip) throws IpException {
        return ipUtils.getIpInfoFromTaobao(ip);
    }

    /**
     * 根据本地存储的countryId与country信息获取IpInfo，只包含国家信息，用来处理中国以外的国家IP信息
     *
     * @param countryId
     * @return
     */
    public IpInfo getIpInfoFromCountryFile(String countryId) {
        Country c = countryMap.get(countryId);
        IpInfo ipinfo = new IpInfo();
        if (c == null) {
            logger.info("can not found the country info from local country file. countryId is : {}", countryId);
            return ipinfo;
        }
        ipinfo.setCountry(c.getChsName());
        return ipinfo;
    }

    /**
     * 根据startIp和hostNum取得endIp
     *
     * @param startIp
     * @param hostNum
     */
    private byte[] getEndIp(byte[] startIp, long hostNum) {
        int[] ipInt = new int[4];
        ipInt[0] = (int) (startIp[0] & 0xFF);
        ipInt[1] = startIp[1] & 0xFF;
        ipInt[2] = startIp[2] & 0xFF;
        ipInt[3] = startIp[3] & 0xFF;
        long ipLong = ipInt[0] * (1l << 24) + ipInt[1] * (1l << 16) + ipInt[2] * (1l << 8) + ipInt[3];
        ipLong += hostNum;
        byte[] endIp = new byte[4];
        endIp[3] = (byte) (ipLong % 256);
        ipLong >>= 8;
        endIp[2] = (byte) (ipLong % 256);
        ipLong >>= 8;
        endIp[1] = (byte) (ipLong % 256);
        ipLong >>= 8;
        endIp[0] = (byte) (ipLong % 256);
        return endIp;
    }

    /**
     * byte转换成string
     *
     * @param ip
     * @return
     */
    private String byteToIpString(byte[] ip) {
        int ip0 = ip[0] & 0xFF;
        int ip1 = ip[1] & 0xFF;
        int ip2 = ip[2] & 0xFF;
        int ip3 = ip[3] & 0xFF;
        return new StringBuilder()
                .append(ip0).append(".")
                .append(ip1).append(".")
                .append(ip2).append(".")
                .append(ip3).toString();
    }

    private void mutiSaveIpStore() throws IpException {
        List<List<IpRange>> ipRangeListList = new ArrayList<List<IpRange>>();
        File file = new File(System.getProperty("") + ipRangeFile);
        BufferedReader reader = null;
        int line = 0;
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            while ((tempString = reader.readLine()) != null) {
                if((line % oneFileSize) == 0){
                    List<IpRange> ipRangeList = new ArrayList<IpRange>();
                    ipRangeListList.add(ipRangeList);
                }
                String[] tmpStrs = tempString.split(",");
                String startIpStr = tmpStrs[0];
                byte[] startIp = ipUtils.getIpByteArrayFromString(startIpStr);
                int ipNum = Integer.parseInt(tmpStrs[1]);
                String countryId = tmpStrs[2];
                IpRange ipRange = new IpRange(startIpStr, startIp, ipNum, countryId);
                ipRangeListList.get(ipRangeListList.size()).add(ipRange);
                line++;
            }
            reader.close();
            for(List<IpRange> ipRangeList : ipRangeListList){
                saveIpStore(ipRangeList);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
    }

    /**
     * 存储IP信息到IP库文件
     *
     * @throws IpException
     */
    private void saveIpStore(List<IpRange> ipRangeList) throws IpException {
        // ipstore大小为0，直接返回
        if (ipRangeList.size() == 0) {
            logger.error("ipRangeList is zero.");
            throw new IpException("ipRangeList is zero.");
        }
        String iplibFilePath;
        try {
            iplibFilePath = System.getProperty("user.dir") + ipUtils.iplibPath;
        } catch (Exception e) {
            String errorStr = new StringBuilder("IpStore local apnicPath is not exsit:")
                    .append(ipUtils.iplibPath)
                    .append(". error msg: ")
                    .append(e.getMessage())
                    .toString();
            logger.error(errorStr);
            throw new IpException(errorStr);
        }
        RandomAccessFile randomFile;
        try {
            randomFile = new RandomAccessFile(iplibFilePath, "rw");
        } catch (FileNotFoundException e) {
            String errorStr = new StringBuilder("IpStore get ip range error, file not found, file path is:")
                    .append(ipUtils.iplibPath)
                    .append(". error msg: ")
                    .append(e.getMessage())
                    .toString();
            logger.error(errorStr);
            throw new IpException(errorStr);
        }
        // 存储头信息
        // first start ip
        firstStartIpPos = HEAD_FIELD_LENGTH;
        try {
            randomFile.writeInt(firstStartIpPos);
        } catch (IOException e) {
            String errorStr = new StringBuilder("IpStore save first start ip error. error msg: ")
                    .append(e.getMessage())
                    .toString();
            logger.error(errorStr);
            throw new IpException(errorStr);
        }
        // last start ip
        // int lastStartIpPos = HEAD_FIELD_LENGTH + (ipRangeList.size() - 1) * IP_REGION_FIELD_LENGTH;
        lastStartIpPos = firstStartIpPos - IP_REGION_FIELD_LENGTH;
        try {
            randomFile.writeInt(lastStartIpPos);
        } catch (IOException e) {
            String errorStr = new StringBuilder("IpStore save last start ip error. error msg: ")
                    .append(e.getMessage())
                    .toString();
            logger.error(errorStr);
            throw new IpException(errorStr);
        }
        // store size
        int storeSize = 0;
        try {
            randomFile.writeInt(storeSize);
        } catch (IOException e) {
            String errorStr = new StringBuilder("IpStore save store size error. error msg: ")
                    .append(e.getMessage())
                    .toString();
            logger.error(errorStr);
            throw new IpException(errorStr);
        }
        // ip num
        ipNum = 0;
        try {
            randomFile.writeLong(ipNum);
        } catch (IOException e) {
            String errorStr = new StringBuilder("IpStore save ip number error. error msg: ")
                    .append(e.getMessage())
                    .toString();
            logger.error(errorStr);
            throw new IpException(errorStr);
        }

        ipAreaPos = HEAD_FIELD_LENGTH;
        // int ipInfoAreaPos = lastStartIpPos + IP_REGION_FIELD_LENGTH;
        ipInfoAreaPos = chinaIpRangeNum * minIpRangeSize;
        // 获取ip信息
        for (IpRange ipRange : ipRangeList) {
            try {
                ipNum += ipRange.getHostNum();
                byte[] startIp = ipRange.getStartIp();
                long n = ipRange.getHostNum() / minIpRangeSize;
                lastIpInfo = new IpInfo();
                lastIpInfoAreaPos = ipInfoAreaPos;
                for (int i = 0; i < n; i++) {
                    byte[] tmpStartIp = getEndIp(startIp, i * minIpRangeSize);
                    byte[] tmpEndIp = getEndIp(startIp, (i + 1) * minIpRangeSize);
                    IpInfo ipInfo;
                    if (COUNTRY_ID_CHINA.equals(ipRange.getCountryId())) {
                        ipInfo = getIpInfoFromTaobao(byteToIpString(tmpStartIp));
                    } else {
                        ipInfo = getIpInfoFromCountryFile(ipRange.getCountryId());
                    }
                    logger.info(ipInfo.toString());

                    // 如果IP地址信息同上一条记录不同，则写入文件
                    if (!ipInfo.equals(lastIpInfo)) {
                        // 写入startIp
                        randomFile.seek(ipAreaPos);
                        randomFile.write(tmpStartIp);
                        // 写入end ip位置
                        randomFile.writeInt(ipInfoAreaPos);
                        // 修改ipAreaPos位置
                        ipAreaPos += IP_REGION_FIELD_LENGTH;
                        // 写入endIp
                        randomFile.seek(ipInfoAreaPos);
                        randomFile.write(tmpEndIp);
                        // 写入IP地址详细信息
                        randomFile.writeUTF(ipInfo.getCountry());
                        randomFile.writeUTF(ipInfo.getArea());
                        randomFile.writeUTF(ipInfo.getRegion());
                        randomFile.writeUTF(ipInfo.getCity());
                        randomFile.writeUTF(ipInfo.getCounty());
                        randomFile.writeUTF(ipInfo.getIsp());
                        // 保存上一次位置
                        lastIpInfoAreaPos = ipInfoAreaPos;
                        // 保存上一次取得的ip信息
                        lastIpInfo = ipInfo;
                        ipInfoAreaPos = (int) randomFile.length();
                        // 更新lastStartIpPos
                        lastStartIpPos += IP_REGION_FIELD_LENGTH;
                        // 更新storeSize
                        storeSize += 1;
                    }
                    // 如果IP地址信息同上一条记录相同，则修改endip，但endIp和ipInfo的位置不变
                    else {
                        randomFile.seek(lastIpInfoAreaPos);
                        randomFile.write(tmpEndIp);
                    }
                }
            } catch (Exception e) {
                String errorStr = new StringBuilder("IpStore save ip info error. error msg: ")
                        .append(e.getMessage())
                        .toString();
                logger.error(errorStr);
                throw new IpException(errorStr);
            }
        }

        // update lastIpInfoAreaPos, storeSize, ipNum
        try {
            randomFile.seek(4);
            randomFile.writeInt(lastStartIpPos);
            randomFile.writeInt(storeSize);
            randomFile.writeLong(ipNum);
        } catch (IOException e) {
            String errorStr = new StringBuilder("IpStore update ip number error. error msg: ")
                    .append(e.getMessage())
                    .toString();
            logger.error(errorStr);
            throw new IpException(errorStr);
        }

        try {
            randomFile.close();
        } catch (IOException e) {
            String errorStr = new StringBuilder("IpStore close file error: ")
                    .append(e.getMessage())
                    .toString();
            logger.error(errorStr);
            throw new IpException(errorStr);
        }
    }

    /**
     * 准备国家信息
     *
     * @throws IpException
     */
    private void prepCountryInfo() throws IpException {
        InputStream countryFilePath = readResourceIntoStream(countryPath);
        BufferedReader reader;
        reader = new BufferedReader(new InputStreamReader(countryFilePath));
        String info;
        String engRegex = "[^(A-Za-z)]";
        String chsRegex = "[^(\\u4e00-\\u9fa5)]";
        try {
            while ((info = reader.readLine()) != null) {
                String countryId = info.replaceAll(engRegex, "").toUpperCase();
                String chsName = info.replaceAll(chsRegex, "");
                countryMap.put(countryId, new Country(countryId, "", chsName));
            }
        } catch (IOException e) {
            String errorStr = new StringBuilder("IpStore prepare country info error. error msg: ")
                    .append(e.getMessage())
                    .toString();
            logger.error(errorStr);
            throw new IpException(errorStr);
        }
    }

    /**
     * 准备库文件
     *
     * @throws IpException
     */
    private void prepIpStoreFile() throws IpException {
        // 清空旧文件
        File file = new File(System.getProperty("user.dir") + ipUtils.iplibPath);
        if (file.exists())
            file.delete();
        // 创建新文件
        try {
            file.createNewFile();
        } catch (IOException e) {
            String errorStr = new StringBuilder("IpStore prepare ip lib file error, create lib file failed. error msg: ")
                    .append(e.getMessage())
                    .toString();
            logger.error(errorStr);
            throw new IpException(errorStr);
        }
    }

    /**
     * 初始化IP库
     *
     * @throws IpException
     */
    public void initIpStore() throws IpException {
        // 准备库文件
        prepIpStoreFile();
        // 初始化国家信息
        prepCountryInfo();
        // 获取ip段
        saveIpRange();
        // 存储IP信息
//        saveIpStore();
    }

    /**
     * ipRange比较器
     *
     * @author Nicholas
     */
    private class IpComparator implements Comparator<IpRange> {
        public int compare(IpRange ipRange1, IpRange ipRange2) {
            byte[] ip1 = ipRange1.getStartIp();
            byte[] ip2 = ipRange2.getStartIp();
            return ipUtils.compareIP(ip1, ip2);
        }
    }

    /**
     * 读取资源文件
     * @param filePath
     * @return
     */
    private InputStream readResourceIntoStream(String filePath){
        return this.getClass().getResourceAsStream(filePath);
    }

}