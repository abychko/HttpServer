import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by nerff on 16.09.13.
 */
public class HttpRequestHeader implements HttpHeader {
    List<String> headerList = new ArrayList<String>();
    private String httpMethod;
    private String path;

    @Override
    public void addHeaderField(String header) {
        if (!headerList.contains(header)) {
            headerList.add(header);
        }
    }

    @Override
    public void printHeader() {
        for (String header : headerList) {
            // new Throwable().printStackTrace();
            System.out.println(header);
        }
    }

    HttpRequestHeader readRequest(Socket mSock) {
        HttpRequestHeader requestHeader = null;
        try {

            InputStream stream_in = mSock.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream_in));
            requestHeader = new HttpRequestHeader();

            while (true) {
                String line = reader.readLine();
                if (line == null || line.isEmpty()) {
                    break;
                }
                this.addHeaderField(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return requestHeader;
    }

    public String getHttpMethod() {
        return headerList.get(0).split("\\s+")[0];
    }

    public String getURI() {
        return headerList.get(0).split("\\s+")[1];
    }
}
