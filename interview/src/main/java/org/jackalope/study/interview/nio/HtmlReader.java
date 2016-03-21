package org.jackalope.study.interview.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

/**
 * @author zowie
 *         Email: nicholas@iuni.com
 */
public class HtmlReader {

    private static String url = "www.qq.com";
    private static int port = 80;
    private static int byteSize = 1024;

    public static void main(String args[]) throws IOException {
        InetSocketAddress address = new InetSocketAddress(url ,port);
        SocketChannel socketChannel = SocketChannel.open(address);
        Charset charset = Charset.defaultCharset();
        socketChannel.write(charset.encode("GET " + "/ HTTP/1.1" + "\r\n\r\n"));
        ByteBuffer byteBuffer = ByteBuffer.allocate(byteSize);
        while(socketChannel.read(byteBuffer) != -1){
            byteBuffer.flip();
            System.out.println(charset.decode(byteBuffer));
            byteBuffer.clear();
        }
    }

}
