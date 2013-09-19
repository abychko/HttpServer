package org.http.server;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

@SuppressWarnings("UnusedAssignment")
class ResponseBuilder {

    private final String _host;
    private final String _uri;
    private final Socket _socket;
    private String _outHeaders = null;
    private int httpCode = 200;
    private long contentLength;
    private String lastModified;
    private String message = "Ok";


    public ResponseBuilder(String host, String uri, String method, Socket socket) {
        this._socket = socket;
        this._host = host;
        String _method = method;
        this._uri = uri;
    }

    public void Response() {
        String ROOTDIR = "/srv/www";
        File out = new File(ROOTDIR + _uri);
        OutputStream outputStream = null;
        try {
            outputStream = _socket.getOutputStream();
            if (out.exists()) { //200
                if (out.canRead() && out.getAbsolutePath().contains(ROOTDIR)) {
                    if (out.isDirectory()) {
                        String dirList = getDirectoryListing(ROOTDIR + _uri);
                        contentLength = dirList.length();
                        lastModified = getGmtTime(new Date(out.lastModified()));
                        prepareHeaders(out, "text/html");
                        try {
                            outputStream.write(_outHeaders.getBytes());
                            outputStream.write(dirList.getBytes());

                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            if (outputStream != null) {
                                outputStream.close();
                            }
                        }
                    } else if (out.isFile()) { //is file
                        contentLength = out.length();
                        lastModified = getGmtTime(new Date(out.lastModified()));
                        prepareHeaders(out, "realType");
                        outputStream.write(_outHeaders.getBytes());
                        try {
                            int retcode = 0;
                            RandomAccessFile outfile = new RandomAccessFile(ROOTDIR + _uri, "r");
                            try {
                                while (retcode != -1) {
                                    outputStream.write(outfile.readByte());
                                }
                            } catch (IOException e) {
                                retcode = -1;
                                outputStream.close();
                                // e.printStackTrace();
                            }
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                } else { //403
                    httpCode = 403;
                    message = "Forbidden";
                }

            } else { //404
                httpCode = 404;
                message = "Not Found";
            }
            //System.out.println(_outHeaders);

        } catch (IOException e) {
            e.printStackTrace();
        }
        //System.exit(0);
    }

    private String getContentType(File file) {
        Path source = Paths.get(file.getAbsolutePath());
        String contentType = "application/octet-stream";
        try {
            contentType = Files.probeContentType(source);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return contentType;
    }

    private void prepareHeaders(File file, String forcedType) {
        String cType = "";
        if (forcedType.equals("realType")) {
            cType = getContentType(file);
        } else {
            cType = forcedType;
        }
        _outHeaders = "HTTP/1.1" + " " + httpCode + " " + message + "\n";
        _outHeaders += "Server: ZX Spectrum\n";
        _outHeaders += "Date: " + getGmtTime(new Date()) + "\n";
        _outHeaders += "Content-Type: " + cType + "; charset=UTF-8\n";
        _outHeaders += "Connection: close\n";
        _outHeaders += "Cache-Control: no-cache,no-store,max-age=0,must-revalidate\n";
        _outHeaders += "Content-Length: " + contentLength + "\n";
        _outHeaders += "Last-Modified: " + lastModified + "\n";
        _outHeaders += "\n";
    }

    private String getDirectoryListing(String path) {
        String output = "<html>\n" +
                "\t<head><title>Directory Listing</title></head>\n" +
                "\t\t<body>\n" + "<h1>Directory listing for: <b>" + _uri + "</b></h1><hr>" +
                "\t\t\t<table>\n";

        File directory = new File(path);
        String dirs = "";
        String files = "";

        for (String item : directory.list()) {
            File curFile = new File(path + File.separator + item);
            if (curFile.isDirectory()) {
                dirs += buildTableRow("DIR:", item, curFile.length(), getContentType(curFile));
            } else if (curFile.isFile()) {
                files += buildTableRow("File:", item, curFile.length(), getContentType(curFile));
            } else {
                files += buildTableRow("Unknown:", item, 0, "unknown");
            }
        }
        output += dirs;
        output += files;
        output += "\t\t\t</table>\n" +
                "\t\t\t<hr><i><b>ZX Spectrum</b>(Sinclair Research Ltd)\t\t\t" + new Date().toString() + "</i>\n" +
                "\t\t</body>\n" +
                "</html>";
        return output;
    }

    private String buildTableRow(String prefix, String item, long size, String type) {
        String sep = "/";
        if (_uri.equals("/")) {
            sep = "";
        }
        return "\t\t\t\t<tr>\n" +
                "\t\t\t\t\t<td>" + prefix + "</td>\n" +
                "\t\t\t\t\t<td><a href=http://" + _host + _uri.replaceAll("//", "/") + sep + item + ">" + item + "</td>\n" +
                "\t\t\t\t\t<td>Size: " + size + "</td>\n" +
                "\t\t\t\t\t<td>" + type + "</td>\n" +
                "\t\t\t\t</tr>\n";
    }

    private String getGmtTime(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy H:mm:ss z");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        return sdf.format(date);
    }
}
