package org.http.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Locale;

@SuppressWarnings("InfiniteLoopStatement")
class Main {
    public static void main(String[] args) throws IOException {
        ServerSocket listener = new ServerSocket(8888);
        //noinspection InfiniteLoopStatement
//
        Locale.setDefault(new Locale("en", "US"));
        //
        while(true) {
            Socket client_sock = listener.accept();
            ClientSession session;
            session = new ClientSession(client_sock);
            new Thread(session).start();
        }
    }
}
