package uk.co.miami_nice.screenshot.net;

import org.reflections.Reflections;
import uk.co.miami_nice.screenshot.CaptureType;
import uk.co.miami_nice.screenshot.Driver;
import uk.co.miami_nice.screenshot.gui.RecordVideo;
import uk.co.miami_nice.screenshot.io.FileIO;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Set;

/**
 * @author Kieran Brahney
 * @version 1.0
 * @package uk.co.miami_nice.screenshot.net
 * @since 29/03/14 00:16
 */
public class UploadManager {

    /**
     * Get all the classes implementing the Uploader interface
     */
    private static final Set<Class<? extends Uploader>> availableUploadMethods = new Reflections(UploadManager.class.getPackage()).getSubTypesOf(Uploader.class);

    /**
     * Reference to the swing worker thread for recording / stopping video
     */
    private static RecordVideo videoWorker;

    /**
     * @param area
     * @param type
     */
    public static void capture(Rectangle area, CaptureType type) {
        switch (type) {
            case VIDEO:
                // Capture the video
                videoWorker = new RecordVideo(area);
                videoWorker.execute();

                break;
            default:
                BufferedImage image = FileIO.takeScreenshot(area);
                String loc = FileIO.writeImage(image, FileIO.createFileLocation(image), Driver.getConfig().getImageFormat());
                try {
                    Uploader uploader = (Uploader) Driver.getConfig().uploadMethodToClass().newInstance();
                    String response = uploader.post(new File(loc));

                    // Is auto upload enabled?
                    if (Driver.getConfig().isAutoUpload())
                        uploader.openImage(response);
                } catch (Exception e) {
                    e.printStackTrace();
                }
        }
    }

    /**
     * A set of the available upload methods. Upload methods are determined
     * by reflection, looking for all classes which implement the Uploader
     * interface.
     *
     * @return A set of classes
     */
    public static Set<Class<? extends Uploader>> getAvailableUploadMethods() {
        return availableUploadMethods;
    }

    public static RecordVideo getVideoWorker() {
        return videoWorker;
    }

}
