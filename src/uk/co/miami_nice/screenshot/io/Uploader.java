package uk.co.miami_nice.screenshot.io;

/**
 * @author Kieran Brahney
 * @version 1.0
 * @package uk.co.miami_nice.screenshot.io
 * @since 23/03/14 12:59
 */
public interface Uploader {

    public void post(byte[] data);

    public void openImage(String location);

}
