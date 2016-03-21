package org.jackalope.study.interview.nio;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author zowie
 *         Email: nicholas@iuni.com
 */
public class CopyFile {

    private static String inFilePath = "E:\\workspace-ideaI\\jackalope\\question\\src\\main\\resources\\inFileOfCopyFile";
    private static String outFilePath = "E:\\workspace-ideaI\\jackalope\\question\\src\\main\\resources\\outFileOfCopyFile";
    private static int byteSize = 1024;

    public static void main(String args[]) throws IOException {
        FileInputStream inputStream = new FileInputStream(inFilePath);
        FileOutputStream outputStream = new FileOutputStream(outFilePath);
        FileChannel cin = inputStream.getChannel();
        FileChannel cout = outputStream.getChannel();
        ByteBuffer byteBuffer = ByteBuffer.allocate(byteSize);
        while (cin.read(byteBuffer) != -1) {
            byteBuffer.flip();
            cout.write(byteBuffer);
            byteBuffer.clear();
        }
    }
}
