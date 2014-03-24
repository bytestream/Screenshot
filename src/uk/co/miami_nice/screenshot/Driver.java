package uk.co.miami_nice.screenshot;

import uk.co.miami_nice.screenshot.gui.Interface;
import uk.co.miami_nice.screenshot.io.FileIO;
import uk.co.miami_nice.screenshot.io.uploaders.Personal;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * @author Kieran Brahney
 * @version 1.0
 * @package uk.co.miami_nice.screenshot
 * @since 23/03/14 12:56
 */
public class Driver {

    /**
     * @param args
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Interface();
            }
        });
    }

    /**
     * Take a screenshot of a given area
     *
     * @param area The size of the area to 'snap'
     */
    public static void takeScreenshot(Rectangle area) {
        BufferedImage image = FileIO.takeScreenshot(area);
        // TODO: Change PNG to user configurable
        String loc = FileIO.writeImage(image, FileIO.createFileLocation(image), "png");
        // TODO: Set uploader to be configurable
        Personal uploader = new Personal();
        String response = uploader.post(new File(loc));
        uploader.openImage(response);
    }

}
