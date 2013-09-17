package org.http.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

class Main {
    public static void main(String[] args) throws IOException {
        ServerSocket listener = new ServerSocket(8888);
        //noinspection InfiniteLoopStatement
        while(true) {
            Socket client_sock = listener.accept();
            ClientSession session;
            session = new ClientSession(client_sock);
            new Thread(session).start();
        }
    }
}
