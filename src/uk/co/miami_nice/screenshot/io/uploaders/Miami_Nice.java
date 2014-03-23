package uk.co.miami_nice.screenshot.io.uploaders;

import com.google.gson.Gson;
import uk.co.miami_nice.screenshot.io.Uploader;

import java.awt.*;
import java.io.*;
import java.net.*;
import java.util.ArrayList;

/**
 * @author Kieran Brahney
 * @version 1.0
 * @package uk.co.miami_nice.screenshot.io.uploaders
 * @since 23/03/14 18:20
 */
public class Miami_Nice implements Uploader {

    /**
     * Upload URL
     */
    private final String URL = "http://uk1.focushosting.com/upload.php";

    @Override
    public ArrayList post(File binaryFile) {
        URLConnection connection = null;
        PrintWriter writer = null;

        try {
            connection = new URL(URL).openConnection();
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type",
                    "multipart/form-data; boundary=" + boundary);

            OutputStream output = connection.getOutputStream();
            writer = new PrintWriter(new OutputStreamWriter(output, charset), true); // true = autoFlush

            // Send form headers
            writer.append("--" + boundary).append(CRLF);
            writer.append("Content-Disposition: form-data; name=\"image\"; filename=\"" + binaryFile.getName() + "\"").append(CRLF);
            writer.append("Content-Type: " + URLConnection.guessContentTypeFromName(binaryFile.getName())).append(CRLF);
            writer.append("Content-Transfer-Encoding: binary").append(CRLF);
            writer.append(CRLF).flush();

            // Send binary data
            InputStream input = new FileInputStream(binaryFile);
            try {
                byte[] buffer = new byte[1024];
                for (int length; (length = input.read(buffer)) > 0; ) {
                    output.write(buffer, 0, length);
                }
                output.flush();
            } finally {
                try {
                    input.close();
                } catch (IOException ioe) {
                }
            }
            writer.append(CRLF).flush();

            // End of multipart/form-data.
            writer.append("--" + boundary + "--").append(CRLF);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (writer != null) writer.close();
        }

        // Get the response text
        Gson gson = new Gson();
        ArrayList response = gson.fromJson(getResponse(connection), ArrayList.class);
        return response;
    }

    private String getResponse(URLConnection connection) {
        String response = "";

        String contentType = connection.getHeaderField("Content-Type");
        String charset = null;
        for (String param : contentType.replace(" ", "").split(";")) {
            if (param.startsWith("charset=")) {
                charset = param.split("=", 2)[1];
                break;
            }
        }

        if (charset != null) {
            BufferedReader reader = null;
            try {
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), charset));
                for (String line; (line = reader.readLine()) != null; ) {
                    response += line + "\n";
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    reader.close();
                } catch (IOException logOrIgnore) {
                }
            }
        } else {
            // TODO: Binary content - unexpected output...

        }

        return response;
    }

    @Override
    public void openImage(String location) {
        try {
            Desktop.getDesktop().browse(new URI(location));
        } catch (IOException e) {
            // TODO: Error reporting
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

}
