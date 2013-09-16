import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {

        HttpServer httpServer = new HttpServer(8888);
        httpServer.serveRequests();

    }
}
