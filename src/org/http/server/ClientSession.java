package org.http.server;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

class ClientSession implements Runnable {

    private final Socket mSock;
    private List<String> requestHeaders;

    public ClientSession(Socket clientSock) {
        this.mSock = clientSock;

    }

    @Override
    public void run() {
        readFromClient();
        writeToClient();
    }

    private String extractMethod() {
        return this.requestHeaders.get(0).split("\\s+")[0];
    }

    private void closeConnection() {
        try {
            mSock.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /* TODO parse http methods in another object manner */
    private void writeToClient() {
        String httpMethod = extractMethod();
        String uri = extractURI();
        ResponseBuilder currentResponse = new ResponseBuilder();
        currentResponse.buildResponce(httpMethod, uri);
        currentResponse.writeOutput(mSock);
        closeConnection();
    }

    private String extractURI() {
        return this.requestHeaders.get(0).split("\\s+")[1];
    }

    private void readFromClient() {
        try {
            this.requestHeaders = new ArrayList<String>();
            InputStream inputStream = mSock.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            while (true) {
                String header;
                header = reader.readLine();
                if (header.isEmpty() || header == null) {
                    break;
                }
                if (!this.requestHeaders.contains(header)) {
                    this.requestHeaders.add(header);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
