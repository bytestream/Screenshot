package uk.co.miami_nice.screenshot.net.uploaders;

import com.google.gson.Gson;
import uk.co.miami_nice.screenshot.Driver;
import uk.co.miami_nice.screenshot.net.InstallCert;
import uk.co.miami_nice.screenshot.net.Uploader;

import java.awt.*;
import java.io.*;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Logger;

/**
 * @author Kieran Brahney
 * @version 1.0
 * @package uk.co.miami_nice.screenshot.net.uploaders
 * @since 23/03/14 18:20
 */
public class Personal implements Uploader {

    /**
     * Upload URL
     */
    private final String URL = "https://uk1.miami-nice.co.uk/upload.php";

    /**
     * Get a human meaningful name for the uploader service
     *
     * @return String name
     */
    public String getName() {
        return "Personal";
    }

    @Override
    /**
     * Upload the specified binary file (image) to a given web server
     * @param file Location of the file to upload
     * @return The URL to the uploaded file
     */
    public String post(File binaryFile) {
        // Get the self-signed SSL certificate
        try {
            //
            new InstallCert(URL.substring(URL.indexOf("/") + 2, URL.lastIndexOf("/")));
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
        System.setProperty("javax.net.ssl.trustStore", Driver.getConfig().getSSL_TRUST_STORE_LOCATION());

        URLConnection connection = null;
        PrintWriter writer = null;

        try {
            connection = new URL(URL).openConnection();
            connection.setDoInput(true);
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
        } catch (Exception e) {
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).severe(e.getMessage());
        } finally {
            if (writer != null) writer.close();
        }

        // Get the response text
        Gson gson = new Gson();
        Response response = gson.fromJson(getResponse(connection), Response.class);
        return response.getURL();
    }

    /**
     * Get the response body from a given URL connection
     *
     * @param connection Connection to get the input stream from
     * @return The response body as a string
     */
    private String getResponse(URLConnection connection) {
        String response = "";

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), charset));
            for (String line; (line = reader.readLine()) != null; ) {
                response += line + "\n";
            }
        } catch (IOException e) {
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).severe(e.getMessage());
        }

        return response;
    }

    @Override
    /**
     * Open the image using the appropriate software. For example, if the
     * image has been uploaded to imgur then this should open a web browser
     * pointed to http://imgur.com/image.png
     * @param location URI location (file or URL)
     */
    public void openImage(String location) {
        try {
            Desktop.getDesktop().browse(new URI(location));
        } catch (Exception e) {
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).severe(e.getMessage());
        }
    }

    private class Response {

        private String URL;

        private String size;

        private String getURL() {
            return URL;
        }

        private String getSize() {
            return size;
        }
    }

}
