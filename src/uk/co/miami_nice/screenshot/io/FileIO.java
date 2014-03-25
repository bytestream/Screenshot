package uk.co.miami_nice.screenshot.io;

import com.intellij.util.ui.UIUtil;
import uk.co.miami_nice.screenshot.misc.Misc;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * @author Kieran Brahney
 * @version 1.0
 * @package uk.co.miami_nice.screenshot.io
 * @since 23/03/14 12:59
 */
public class FileIO {

    /**
     * Convert an image to a file hash
     *
     * @param image A buffered image object
     * @return A file location including an MD5 hash of the image for the filename
     * @todo Set temporary directory to user configurable
     */
    public static String createFileLocation(BufferedImage image) {
        String fileName = Misc.getMD5Hash(Misc.imageToByteArray(image, "jpg"));
        return System.getProperty("java.io.tmpdir") + fileName;
    }

    /**
     * Take a screenshot of a given area on the screen
     *
     * @param area The area of the screen to capture
     * @return A bufferedimage in-memory data store of the captured image
     */
    public static BufferedImage takeScreenshot(Rectangle area) {
        try {
            return new Robot().createScreenCapture(area);
        } catch (AWTException e) {
            e.printStackTrace();
            return UIUtil.createImage(0, 0, 0);
        }
    }

    /**
     * Read an image from the file system into memory
     *
     * @param location Location of the image on the file system
     * @return A bufferedimage in-memory data store of the image file
     */
    public static BufferedImage readImage(String location) {
        try {
            BufferedImage image = ImageIO.read(new File(location));
            return image;
        } catch (IOException e) {
            e.printStackTrace();
            return UIUtil.createImage(0, 0, 0);
        }
    }

    /**
     * Write an image to the file system
     *
     * @param image    BufferedImage object of the image stored in-memory
     * @param location Where to save the image file to on the file system
     * @param type     The file type to save the image as (JPG, PNG, GIF, etc)
     * @return The location of the written file
     */
    public static String writeImage(BufferedImage image, String location, String type) {
        try {
            ImageIO.write(image, type, new File(location));
            return location;
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

}
