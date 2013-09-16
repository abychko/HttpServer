import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class HttpServer {

    final int SERVER_PORT = 8888;
    ServerSocket mListener;

    HttpServer(int listenerPort) throws IOException {
        mListener = new ServerSocket(listenerPort);
    }

    HttpServer() throws IOException {
        mListener = new ServerSocket(SERVER_PORT);
    }

    void serveRequests() {
        while (true) {
            Socket clientSocket = null;
            try {
                clientSocket = mListener.accept();
            } catch (IOException e) {
                e.printStackTrace();
            }
            HttpSession session = null;
            try {
                session = new HttpSession(clientSocket);
            } catch (IOException e) {
                e.printStackTrace();
            }
            new Thread(session).start();
        }
    }
}
