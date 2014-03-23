package uk.co.miami_nice.screenshot.io;

import com.intellij.util.ui.UIUtil;

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

    public static BufferedImage takeScreenshot(Rectangle area) {
        try {
            return new Robot().createScreenCapture(area);
        } catch (AWTException e) {
            e.printStackTrace();
            return UIUtil.createImage(0, 0, 0);
        }
    }

    public static void readImage(String location) {
        try {
            BufferedImage image = ImageIO.read(new File(location));
        } catch (IOException e) {
            e.printStackTrace();
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
