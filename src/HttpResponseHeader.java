import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

class HttpResponseHeader implements HttpHeader {
    List<String> headerList = new ArrayList<String>();

    @Override
    public void addHeaderField(String header) {
        headerList.add(header);
    }

    @Override
    public void printHeader() {
        for (String header : headerList) {
            // new Throwable().printStackTrace();
            System.out.println(header);
        }
    }
     public void printHeader(OutputStream outputStream){
        for (String header : headerList) {
            try {
                header = header += "\n";
                outputStream.write(header.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }

            System.out.println(header);
        }
    }

    public void endHeader() {
        headerList.add("\n");
    }
}
