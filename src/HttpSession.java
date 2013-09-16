import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

class HttpSession implements Runnable {

    Socket mSock = null;
    private OutputStream stream_out = null;

    public HttpSession(Socket clientSocket) throws IOException {
        this.mSock = clientSocket;
        stream_out = mSock.getOutputStream();
    }

    @Override
    public void run() {
        HttpRequestHeader requestHeader = new HttpRequestHeader();
        requestHeader.readRequest(mSock);
        //System.out.println(requestHeader.getURI());
        //
        HttpResponse response = new HttpResponse();
        response.prepareResponse(requestHeader);
        response.sendResponse(mSock);
    }

}
