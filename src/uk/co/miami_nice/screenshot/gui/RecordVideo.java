package uk.co.miami_nice.screenshot.gui;

import uk.co.miami_nice.screenshot.Driver;
import uk.co.miami_nice.screenshot.io.FileIO;
import uk.co.miami_nice.screenshot.io.video.JpegImagesToMovie;
import uk.co.miami_nice.screenshot.misc.Misc;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Vector;

/**
 * @author Kieran Brahney
 * @version 1.0
 * @package uk.co.miami_nice.screenshot.gui
 * @since 29/03/14 01:05
 */
public class RecordVideo extends SwingWorker<Void, Void> {

    /**
     * Area of the screen to capture
     */
    private Rectangle area;

    /**
     * Measure when the background process starts and when it finishes
     */
    private long tStart = 1, tEnd = 1;

    /**
     * Vector of every screenshot
     */
    private Vector<byte[]> inputImages = new Vector<byte[]>();

    /**
     * Initialise a record video object
     *
     * @param area Area of the screen to capture
     */
    public RecordVideo(Rectangle area) {
        this.area = area;
    }

    @Override
    /**
     * Take screenshots until the background task is cancelled
     */
    protected Void doInBackground() throws Exception {
        tStart = System.currentTimeMillis();

        while (!isCancelled()) {
            BufferedImage img = FileIO.takeScreenshot(area);
            inputImages.add(Misc.imageToByteArray(img, "jpg"));

//            Driver.getAnInterface().getRegionSelection().setVisibility(false);
//            EventQueue.invokeLater(new Runnable() {
//                @Override
//                public void run() {
//                    BufferedImage img = FileIO.takeScreenshot(area);
//                    inputImages.add(Misc.imageToByteArray(img, "jpg"));
//                }
//            });
//            Driver.getAnInterface().getRegionSelection().setVisibility(true);

            tEnd = System.currentTimeMillis();
        }

        return null;
    }

    @Override
    protected void done() {
        super.done();

        // Calculate framerate
        int frameRate = (int) (Math.round(inputImages.size() / ((tEnd - tStart) / 1000.0)));

        // Set file location
        String fileLocation = "file:" + Driver.getConfig().getOutputDirectory() + Misc.getUniqueHash(inputImages.get(0)) + ".mov";

        // Convert to movie
        JpegImagesToMovie imageToMovie = new JpegImagesToMovie();
        imageToMovie.doIt((int) area.getWidth(), (int) area.getHeight(), frameRate, inputImages, fileLocation);

        // Kill it with fire
        Driver.getAnInterface().getRegionSelection().dispose();
    }

}
