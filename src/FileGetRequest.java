import java.io.*;

/**
 * Created by nerff on 16.09.13.
 */
public class FileGetRequest {

    final String DEFAULT_FILES_DIR = "/srv/www";
    String httpCode = "200 Ok";
    RandomAccessFile outfile = null;
    String mPath = null;
    private long fileLenght = 0;

    public FileGetRequest(String path) {
        mPath = path;
        try {
            outfile = new RandomAccessFile(DEFAULT_FILES_DIR + path, "r");
            fileLenght = outfile.length();

        } catch (FileNotFoundException e) {
            httpCode = "404 Not Found";
        } catch (IOException e) {
            httpCode = "500 Internal Server Error";
            e.printStackTrace();
        }
    }

    public String getHttpCode() {
        return httpCode;
    }

    public long getFileLenght() {
        return fileLenght;
    }

    public RandomAccessFile getOutfile() {
        return outfile;
    }

    public void writeFile(OutputStream outputStream) throws IOException {
        int retcode = 0;
        try {

            while (retcode != -1) {
                outputStream.write(outfile.readByte());
            }
        } catch (IOException e) {
            retcode = -1;
            outputStream.close();
            // e.printStackTrace();
        }
    }

    public long getModTime() {
        File mOut;
        long mtime = 0;
        try {
            mOut = new File(DEFAULT_FILES_DIR + mPath, "r");
            mtime = mOut.lastModified();
            System.out.println("Success: " + mtime + " " + DEFAULT_FILES_DIR + mPath);
        } catch (Exception e) {

        }

        return mtime;
    }
}
