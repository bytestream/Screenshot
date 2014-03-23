package uk.co.miami_nice.screenshot.io.uploaders;

import uk.co.miami_nice.screenshot.io.Uploader;

import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * @author Kieran Brahney
 * @version 1.0
 * @package uk.co.miami_nice.screenshot.io.uploaders
 * @since 23/03/14 17:36
 */
public class Local implements Uploader {

    public String post(File file) {
        // Nothing to post
        return "";
    }

    public void openImage(String location) {
        try {
            Desktop.getDesktop().open(new File(location));
        } catch (IOException e) {
            // TODO: Error reporting
            e.printStackTrace();
        }
    }

}
