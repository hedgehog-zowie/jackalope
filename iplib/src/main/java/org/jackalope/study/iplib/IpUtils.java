package org.jackalope.study.iplib;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.io.StringWriter;

import net.sf.json.JSONObject;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

/**
 * IP地址工具类 提供从本地IP库获取IP地址详细信息和从淘宝IP库获取IP地址详细信息的方法
 * <p/>
 * 使用方法：
 * IpUtils ipUtils = IpUtils.getInstance();
 * ipUtils.init();
 * 方法1.从本地读取IP，若未读到，则从TAOBAO IP库取IP信息: ipUtils.getIpInfo("1.1.1.1");
 * 方法2.直接从TAOBAO IP库读取IP信息：ipUtils.getIpInfoFromTaobao("1.1.1.1");
 *
 * @author Nicholas
 */
public class IpUtils {
    public final String iplibPath = "/ip.dat";
    public final String ipRangePath = "/ipRange.csv";
    public final String tbipliburl = "http://ip.taobao.com/service/getIpInfo.php?ip=";
    public final String UA = "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:29.0) Gecko/20100101 Firefox/29.0";

    private static final Logger logger = LoggerFactory.getLogger(IpUtils.class);
    private final Gson gson = new Gson();

    // 定义HttpClient
    private HttpClient hc = new DefaultHttpClient();
    private RandomAccessFile ipFile = null;
    private int ipBegin;
    private int ipEnd;
    private final int IP_RECORD_LENGTH = 8;

    private static int failTimes = 0;
    private static final int maxFailTime = 10;

    /**
     * 单例
     */
    private static IpUtils instance;

    private IpUtils() throws IpException {
    }

    public static IpUtils getInstance() throws IpException {
        if (instance == null)
            instance = new IpUtils();
        return instance;
    }

    /**
     * 初始化文件访问 读取文件头信息
     *
     * @throws IpException
     */
    public void init() throws IpException {
        String iplibFilePath;
        try {
            iplibFilePath = IpUtils.class.getResource(iplibPath).getPath();
        } catch (Exception e) {
            String errorStr = new StringBuilder("IpStore local apnicPath is not exsit:")
                    .append(iplibPath)
                    .append(". error msg: ")
                    .append(e.getLocalizedMessage())
                    .toString();
            logger.info(errorStr);
            // init ipstore
            IpStore.getInstance().initIpStore();
            iplibFilePath = IpUtils.class.getResource(iplibPath).getPath();
        }
        try {
            ipFile = new RandomAccessFile(iplibFilePath, "r");
        } catch (FileNotFoundException e) {
            String errorStr = new StringBuilder("IpUtils init failed, not found ip lib file. iplibPath is:")
                    .append(iplibFilePath)
                    .append(". error msg: ")
                    .append(e.getLocalizedMessage())
                    .toString();
            logger.error(errorStr);
            throw new IpException(errorStr);
        }
        try {
            ipBegin = ipFile.readInt();
            ipEnd = ipFile.readInt();
        } catch (IOException e) {
            String errorStr = new StringBuilder("IpUtils init failed, read head info failed. error msg: ")
                    .append(e.getLocalizedMessage())
                    .toString();
            logger.error(errorStr);
            throw new IpException(errorStr);
        }
    }

    /**
     * 将json格式的字符串转换成IpInfo对象
     *
     * @param ipinfoStr
     * @return
     * @throws IpException
     */
    private IpInfo getIpinfoFromJsonStr(String ipinfoStr)
            throws IpException {
        IpInfo ipinfo = null;
        try {
            ipinfo = gson.fromJson(ipinfoStr, IpInfo.class);
        } catch (Exception e) {
            String errorStr = new StringBuilder("Transfer ipstr to ipinfo error. ipstr is:")
                    .append(ipinfoStr)
                    .append(",err msg:")
                    .append(e.getLocalizedMessage())
                    .toString();
            logger.error(errorStr);
            throw new IpException(errorStr);
        }
        return ipinfo;
    }

    /**
     * 先从本地IP库获取IP信息，若未获取到，则从淘宝IP库获取。
     *
     * @param ipstr
     * @return
     * @throws IpException
     */
    public IpInfo getIpInfo(String ipstr) throws IpException {
        long endIpPos = locateIP(getIpByteArrayFromString(ipstr));
        if (endIpPos == -1) {
            logger.info("not found ip in local ip lib file, find it to taobao ip lib, ip: {}", ipstr);
            return getIpInfoFromTaobao(ipstr);
        }
        try {
            ipFile.seek(endIpPos + 4);
            String country = ipFile.readUTF();
            String area = ipFile.readUTF();
            String region = ipFile.readUTF();
            String city = ipFile.readUTF();
            String county = ipFile.readUTF();
            String isp = ipFile.readUTF();
            IpInfo ipinfo = new IpInfo();
            ipinfo.setCountry(country);
            ipinfo.setArea(area);
            ipinfo.setRegion(region);
            ipinfo.setCity(city);
            ipinfo.setCounty(county);
            ipinfo.setIsp(isp);
            return ipinfo;
        } catch (IOException e) {
            String errorStr = new StringBuilder("IpUtils getIpInfo failed, ip: ")
                    .append(ipstr)
                    .append(". endIpPos is: ")
                    .append(endIpPos)
                    .append(". error msg: ")
                    .append(e.getLocalizedMessage())
                    .toString();
            logger.error(errorStr);
            throw new IpException(errorStr);
        }
    }

    /**
     * 从ip的字符串形式得到字节数组形式
     *
     * @param ip 字符串形式的ip
     * @return 字节数组形式的ip
     * @throws IpException
     */
    public byte[] getIpByteArrayFromString(String ip) throws IpException {
        byte[] ret = new byte[4];
        java.util.StringTokenizer st = new java.util.StringTokenizer(ip, ".");
        try {
            ret[0] = (byte) (Integer.parseInt(st.nextToken()) & 0xFF);
            ret[1] = (byte) (Integer.parseInt(st.nextToken()) & 0xFF);
            ret[2] = (byte) (Integer.parseInt(st.nextToken()) & 0xFF);
            ret[3] = (byte) (Integer.parseInt(st.nextToken()) & 0xFF);
        } catch (Exception e) {
            String errorStr = new StringBuilder("IpUtils trans ip string to byte error, error msg: ")
                    .append(e.getLocalizedMessage())
                    .toString();
            logger.error(errorStr);
            throw new IpException(errorStr);
        }
        return ret;
    }

    /**
     * 从offset位置读取四个字节的ip地址放入ip数组中，读取后的ip为big-endian格式，但是
     * 文件中是little-endian形式，将会进行转换
     *
     * @param offset
     * @param ip
     * @throws IpException
     */
    private void readIP(long offset, byte[] ip) throws IpException {
        try {
            ipFile.seek(offset);
            ipFile.readFully(ip);
        } catch (IOException e) {
            String errorStr = new StringBuilder(
                    "IpUtils readip from file error, offset is:")
                    .append(offset).append(". error msg: ")
                    .append(e.getLocalizedMessage()).toString();
            logger.error(errorStr);
            throw new IpException(errorStr);
        }
    }

    /**
     * 把类成员ip和beginIp比较，注意这个beginIp是big-endian的
     *
     * @param ip      要查询的IP
     * @param beginIp 和被查询IP相比较的IP
     * @return 相等返回0，ip大于beginIp则返回1，小于返回-1。
     */
    public int compareIP(byte[] ip, byte[] beginIp) {
        for (int i = 0; i < 4; i++) {
            int r = compareByte(ip[i], beginIp[i]);
            if (r != 0)
                return r;
        }
        return 0;
    }

    /**
     * 把两个byte当作无符号数进行比较
     *
     * @param b1
     * @param b2
     * @return 若b1大于b2则返回1，相等返回0，小于返回-1
     */
    private int compareByte(byte b1, byte b2) {
        if ((b1 & 0xFF) > (b2 & 0xFF)) // 比较是否大于
            return 1;
        else if ((b1 ^ b2) == 0)// 判断是否相等
            return 0;
        else
            return -1;
    }

    /**
     * 定位IP所属IP段
     *
     * @param ip
     * @return
     * @throws IpException
     */
    private long locateIP(byte[] ip) throws IpException {
        long m = 0;
        int r;
        byte[] rip = new byte[4];
        // 比较第一个ip项
        readIP(ipBegin, rip);
        r = compareIP(ip, rip);
        if (r == 0)
            return ipBegin;
        else if (r < 0)
            return -1;
        // 开始二分搜索
        for (long i = ipBegin, j = ipEnd; i < j; ) {
            m = getMiddleOffset(i, j);
            readIP(m, rip);
            r = compareIP(ip, rip);
            if (r > 0)
                i = m;
            else if (r < 0) {
                if (m == j) {
                    j -= IP_RECORD_LENGTH;
                    m = j;
                } else
                    j = m;
            } else
                return getEndIpPos(m + 4);
        }
        // 如果循环结束了，那么i和j必定是相等的，这个记录为最可能的记录，但是并非
        // 肯定就是，还要检查一下，如果是，就返回结束地址区的绝对偏移
        m = getEndIpPos(m + 4);
        readIP(m, rip);
        r = compareIP(ip, rip);
        if (r <= 0)
            return m;
        else
            return -1;
    }

    /**
     * 从offset位置读取endip偏移量
     *
     * @param offset
     * @return 读取的long值，返回-1表示读取文件失败
     * @throws IpException
     */
    private long getEndIpPos(long offset) {
        try {
            ipFile.seek(offset);
            return ipFile.readInt();
        } catch (IOException e) {
            return -1;
        }
    }

    /**
     * 得到begin偏移和end偏移中间位置记录的偏移
     *
     * @param begin
     * @param end
     * @return
     */
    private long getMiddleOffset(long begin, long end) {
        long records = (end - begin) / IP_RECORD_LENGTH;
        records >>= 1;
        if (records == 0)
            records = 1;
        return begin + records * IP_RECORD_LENGTH;
    }

    /**
     * 从淘宝IP库获取IP信息
     *
     * @param ipstr
     * @return
     * @throws IpException
     */
    public IpInfo getIpInfoFromTaobao(String ipstr) throws IpException {
        String url = tbipliburl + ipstr;
        IpInfo result;
        try {
            // 实例化HTTP方法
            HttpGet httpGet = new HttpGet(url);
            httpGet.setHeader("User-Agent", UA);
            HttpEntity e = hc.execute(httpGet).getEntity();
            InputStream is = e.getContent();
            // 转换成IpInfo
            StringWriter sw = new StringWriter();
            IOUtils.copy(is, sw);
            is.close();
            String res = sw.toString();
            JSONObject resp = JSONObject.fromObject(res);
            if (resp.has("data")) {
                JSONObject data = resp.getJSONObject("data");
                failTimes = 0;
                result = getIpinfoFromJsonStr(data.toString());
            } else {
                String errorStr = new StringBuilder(
                        "get ipinfo from taobao iplib error. response from taobao has no ipinfo data. url is:").append(url)
                        .append(", response is: ").append(resp.toString()).toString();
                logger.error(errorStr);
                failTimes += 1;
                if (failTimes == maxFailTime)
                    throw new IpException(errorStr);
                else
                    result = getIpInfoFromTaobao(ipstr);
            }
        } catch (Exception e) {
            String errorStr = new StringBuilder(
                    "get ipinfo from taobao iplib error. url is:").append(url)
                    .append(", err msg:").append(e.getLocalizedMessage()).toString();
            logger.error(errorStr);
            failTimes += 1;
            if (failTimes == maxFailTime)
                throw new IpException(errorStr);
            else
                result = getIpInfoFromTaobao(ipstr);
        }
        return result;
    }

}
