package com.imploded.redis.service.redis;

import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * @author shuai.yang
 */
public class Client {
    public static void main(String[] args) {
        Connect connect = new Connect();
        SocketChannel channel = connect.getChannel();
        if (channel == null) {
            return;
        }
        while (true) {
            Scanner scanner = new Scanner(System.in);
            try {
                channel.write(StandardCharsets.UTF_8.encode(scanner.nextLine()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
