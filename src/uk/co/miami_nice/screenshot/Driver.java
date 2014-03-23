package uk.co.miami_nice.screenshot;

import uk.co.miami_nice.screenshot.io.FileIO;
import uk.co.miami_nice.screenshot.misc.Misc;

import java.awt.*;
import java.awt.image.BufferedImage;

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
        BufferedImage image = FileIO.takeScreenshot(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
        String fileName = Misc.getMD5Hash(Misc.imageToByteArray(image));
        String location = System.getProperty("java.io.tmpdir") + fileName;
        System.out.println(FileIO.writeImage(image, location, "png"));
    }

}
