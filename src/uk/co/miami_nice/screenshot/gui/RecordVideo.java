package uk.co.miami_nice.screenshot.gui;

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

    private Rectangle area;

    private long tStart = 1, tEnd = 1;

    private Vector<byte[]> inputImages = new Vector<byte[]>();

    public RecordVideo(Rectangle area) {
        this.area = area;
    }

    @Override
    protected Void doInBackground() throws Exception {
        tStart = System.currentTimeMillis();

        while (!isCancelled()) {
            BufferedImage img = FileIO.takeScreenshot(area);
            inputImages.add(Misc.imageToByteArray(img, "jpg"));

            tEnd = System.currentTimeMillis();
        }

        return null;
    }

    @Override
    protected void done() {
        super.done();

        // Calculate framerate
        int frameRate = (int) (Math.round(inputImages.size() / ((tEnd - tStart) / 1000.0)));

        // Convert to movie
        JpegImagesToMovie imageToMovie = new JpegImagesToMovie();
        imageToMovie.doIt((int) area.getWidth(), (int) area.getHeight(), frameRate, inputImages, "movie.mov");
    }

}
