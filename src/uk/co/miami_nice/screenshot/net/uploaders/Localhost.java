package uk.co.miami_nice.screenshot.net.uploaders;

import uk.co.miami_nice.screenshot.net.Uploader;

import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * @author Kieran Brahney
 * @version 1.0
 * @package uk.co.miami_nice.screenshot.net.uploaders
 * @since 23/03/14 17:36
 */
public class Localhost implements Uploader {

    /**
     * Get a human meaningful name for the uploader service
     *
     * @return String name
     */
    public String getName() {
        return "Localhost";
    }

    /**
     * This function is a dummy for the localhost, meaning that the file will be
     * stored locally and not uploaded to anywhere remote
     *
     * @param file
     * @return Empty string
     */
    public String post(File file) {
        // Nothing to post
        return file.getAbsolutePath();
    }

    /**
     * Open the image using the appropriate software. For example, if the
     * image has been uploaded to imgur then this should open a web browser
     * pointed to http://imgur.com/image.png
     *
     * @param location URI location (file or URL)
     */
    public void openImage(String location) {
        try {
            Desktop.getDesktop().open(new File(location));
        } catch (IOException e) {
            // TODO: Error reporting
            e.printStackTrace();
        }
    }

}
