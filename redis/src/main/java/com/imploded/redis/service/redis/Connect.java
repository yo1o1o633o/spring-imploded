package com.imploded.redis.service.redis;

import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;

/**
 * @author shuai.yang
 */
public class Connect {

    public SocketChannel getChannel() {
        SocketChannel socketChannel = null;
        try {
            socketChannel = SocketChannel.open();
            socketChannel.socket().connect(new InetSocketAddress("127.0.0.1", 15000));
            socketChannel.configureBlocking(false);

            while (!socketChannel.finishConnect()) {
                System.out.println("链接中......");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return socketChannel;
    }
}
