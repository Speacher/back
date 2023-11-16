package gdsc.speacher.converter;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

public class UrlToFileConvertor {
    public static File getFileFromURL(String urlStr) throws IOException {
        URL url = new URL(urlStr);
        InputStream in = url.openStream();

        Path tempPath = Files.createTempFile(null, null);
        File tempFile = tempPath.toFile();
        tempFile.deleteOnExit(); // Ensure the file is deleted when JVM exits

        OutputStream out = new FileOutputStream(tempFile);
        byte[] buffer = new byte[1024];
        int bytesRead;

        while ((bytesRead = in.read(buffer)) != -1) {
            out.write(buffer, 0, bytesRead);
        }

        in.close();
        out.close();

        return tempFile;
    }
}
