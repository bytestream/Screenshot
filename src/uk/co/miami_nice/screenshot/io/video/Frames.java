package uk.co.miami_nice.screenshot.io.video;

import java.util.Vector;

/**
 * @author Kieran Brahney
 * @version 1.0
 * @package uk.co.miami_nice.screenshot.io.video
 * @since 25/03/14 17:00
 */
public class Frames {

    private Vector<byte[]> frames = new Vector<byte[]>();

    public void addFrame(byte[] frame) {
        frames.add(frame);
    }

    public Vector<byte[]> getFrames() {
        return frames;
    }
}
