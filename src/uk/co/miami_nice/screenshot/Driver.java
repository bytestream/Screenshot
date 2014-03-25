package uk.co.miami_nice.screenshot;

import com.google.gson.Gson;
import uk.co.miami_nice.screenshot.gui.Interface;
import uk.co.miami_nice.screenshot.io.FileIO;
import uk.co.miami_nice.screenshot.io.video.JpegImagesToMovie;
import uk.co.miami_nice.screenshot.misc.Misc;
import uk.co.miami_nice.screenshot.net.Uploader;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Vector;

/**
 * @author Kieran Brahney
 * @version 1.0
 * @package uk.co.miami_nice.screenshot
 * @since 23/03/14 12:56
 */
public class Driver {

    /**
     * Hold the user configuration data
     */
    private static Config config = new Config();

    /**
     * @param args
     */
    public static void main(String[] args) {
        // Load the configuration
        try {
            BufferedReader br = new BufferedReader(new FileReader(config.getCONFIG_LOCATION()));
            config = new Gson().fromJson(br, Config.class);
        } catch (FileNotFoundException e) {
            System.err.println("Error processing configuration file, resorting to default..");
        }

        // Load the interface
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Interface();
            }
        });
    }

    /**
     * @param area
     * @param type
     */
    public static void capture(Rectangle area, CaptureType type) {
        switch (type) {
            case VIDEO:
                Vector inputFiles = new Vector();

                long tStart = System.currentTimeMillis();
                int i;
                for (i = 0; i < 300; i++) {
                    BufferedImage img = FileIO.takeScreenshot(area);
                    inputFiles.add(Misc.imageToByteArray(img, "jpg"));
                }
                long tEnd = System.currentTimeMillis();
                int frameRate = (int) (Math.round(i / ((tEnd - tStart) / 1000.0)));

                JpegImagesToMovie imageToMovie = new JpegImagesToMovie();
                imageToMovie.doIt((int) area.getWidth(), (int) area.getHeight(), frameRate, inputFiles, "movie.mov");

                break;
            default:
                BufferedImage image = FileIO.takeScreenshot(area);
                String loc = FileIO.writeImage(image, FileIO.createFileLocation(image), getConfig().getImageFormat());
                try {
                    Uploader uploader = (Uploader) getConfig().uploadMethodToClass().newInstance();
                    String response = uploader.post(new File(loc));
                    uploader.openImage(response);
                } catch (Exception e) {
                    e.printStackTrace();
                }
        }
    }

    /**
     * Static access to configuration data
     *
     * @return User configuration data for this instance
     */
    public static Config getConfig() {
        return config;
    }

}
