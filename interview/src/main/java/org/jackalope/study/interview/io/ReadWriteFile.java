package org.jackalope.study.interview.io;

import java.io.*;

/**
 * @author zowie
 *         Email: nicholas@iuni.com
 */
public class ReadWriteFile {

    private static String srcFile = "/io/srcFile";
    private static String byteOneByOne = "/io/byteOneByOne";
    private static String byteByBatch = "/io/byteByBatch";
    private static String charOneByOne = "/io/charOneByOne";
    private static String charByBatch = "/io/charByBatch";

    /**
     * 按字节挨个读取
     */
    private static void byteOneByOne() throws IOException {
        InputStream inputStream = new FileInputStream(new File(ReadWriteFile.class.getResource(srcFile).getPath()));
        OutputStream outputStream = new FileOutputStream(new File(ReadWriteFile.class.getResource(byteOneByOne).getPath()));
        int c;
        while ((c = inputStream.read()) != -1) {
            outputStream.write(c);
        }
        outputStream.flush();
        inputStream.close();
        outputStream.close();
    }

    /**
     * 一次读取多个字节
     *
     * @param size
     * @throws IOException
     */
    private static void byteByBatch(int size) throws IOException {
        InputStream inputStream = new FileInputStream(new File(ReadWriteFile.class.getResource(srcFile).getPath()));
        OutputStream outputStream = new FileOutputStream(new File(ReadWriteFile.class.getResource(byteByBatch).getPath()));
        byte[] bytes = new byte[size];
        while (inputStream.read(bytes) != -1) {
            outputStream.write(bytes);
        }
        outputStream.flush();
        inputStream.close();
        outputStream.close();
    }

    /**
     * 按字符挨个读取
     */
    private static void charOneByOne() throws IOException {
        Reader reader = new InputStreamReader(new FileInputStream(ReadWriteFile.class.getResource(srcFile).getPath()));
        Writer writer = new OutputStreamWriter(new FileOutputStream(ReadWriteFile.class.getResource(charOneByOne).getPath()));
        int c;
        while ((c = reader.read()) != -1) {
            writer.write(c);
        }
        writer.flush();
        reader.close();
        writer.close();
    }

    /**
     * 按字符挨个读取
     */
    private static void charByBatch(int size) throws IOException {
        Reader reader = new InputStreamReader(new FileInputStream(ReadWriteFile.class.getResource(srcFile).getPath()));
        Writer writer = new OutputStreamWriter(new FileOutputStream(ReadWriteFile.class.getResource(charByBatch).getPath()));
        char[] chars = new char[size];
        while (reader.read(chars) != -1) {
            writer.write(chars);
        }
        writer.flush();
        reader.close();
        writer.close();
    }

    public static void main(String args[]) throws Exception {
        byteOneByOne();
        byteByBatch(1024);
        charOneByOne();
        charByBatch(1024);
    }

}
