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
        String fileName = Misc.getMD5Hash(Misc.imageToByteArray(image));
        return System.getProperty("java.io.tmpdir") + fileName;
    }

    public static BufferedImage takeScreenshot(Rectangle area) {
        try {
            return new Robot().createScreenCapture(area);
        } catch (AWTException e) {
            e.printStackTrace();
            return UIUtil.createImage(0, 0, 0);
        }
    }

    public static BufferedImage readImage(String location) {
        try {
            BufferedImage image = ImageIO.read(new File(location));
            return image;
        } catch (IOException e) {
            e.printStackTrace();
            return UIUtil.createImage(0, 0, 0);
        }
    }

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
