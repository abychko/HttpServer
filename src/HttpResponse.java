import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Date;

/**
 * Created by nerff on 16.09.13.
 */
class HttpResponse {
    //выяснить что хочет клиент
    FileGetRequest fileGetRequest;
    HttpResponseHeader responseHeader = new HttpResponseHeader();
    private OutputStream out;
    //отдать запрошенный файл, если есть

    public void sendResponse(Socket socket) {
        // int count = 0;
        try {
            out = socket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        responseHeader.printHeader(out);
        try {
            fileGetRequest.writeFile(out);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void prepareResponse(HttpRequestHeader requestHeader) {

        String method = requestHeader.getHttpMethod();
        String path = requestHeader.getURI();
        if (method.equals("GET")) {
            fileGetRequest = new FileGetRequest(path);
            responseHeader.addHeaderField("HTTP/1.1 " + fileGetRequest.getHttpCode());
            responseHeader.addHeaderField("Server: ZX Spectrum (Sinclair Research Ltd)");
            responseHeader.addHeaderField("Date: " + new Date().toString());
            responseHeader.addHeaderField("Content-Type: text/html; charset=UTF-8");
            responseHeader.addHeaderField("Connection: close");
            responseHeader.addHeaderField("Cache-Control: no-cache,no-store,max-age=0,must-revalidate");
            responseHeader.addHeaderField("Last-Modified: " + new Date(fileGetRequest.getModTime())); //workaround?
            responseHeader.addHeaderField("Content-Length: " + fileGetRequest.getFileLenght());
            responseHeader.endHeader();
        }

    }
}
