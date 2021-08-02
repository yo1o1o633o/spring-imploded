package com.imploded.redis.service.redis;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;

/**
 * @author shuai.yang
 */
public class Service {
    private static ByteBuffer byteBuffer = ByteBuffer.allocate(10240);

    public static void main(String[] args) {
        try {
            Selector selector = Selector.open();

            ServerSocketChannel socketChannel1 = ServerSocketChannel.open();
            socketChannel1.configureBlocking(false);

            ServerSocket serverSocket = socketChannel1.socket();

            InetSocketAddress address = new InetSocketAddress(15000);
            serverSocket.bind(address);

            socketChannel1.register(selector, SelectionKey.OP_ACCEPT);

            while (selector.select() > 0) {
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    if (key.isAcceptable()) {
                        // 获取客户端连接
                        ServerSocketChannel server = (ServerSocketChannel) key.channel();
                        SocketChannel socketChannel = server.accept();
                        if (socketChannel == null) {
                            continue;
                        }
                        // 切换为非组塞模式
                        socketChannel.configureBlocking(false);
                        // 将客户端新连接注册到选择器中
                        socketChannel.register(selector, SelectionKey.OP_READ);
                        System.out.println("链接成功");
                    }
                    if (key.isReadable()) {
                        SocketChannel channel = (SocketChannel) key.channel();
                        while (channel.read(byteBuffer) > 0) {
                            byteBuffer.flip();
                            String fileName = StandardCharsets.UTF_8.decode(byteBuffer).toString();
                            System.out.println(fileName);
                            byteBuffer.clear();
                        }
                    }
                    iterator.remove();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
