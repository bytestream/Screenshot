package uk.co.miami_nice.screenshot.net;

import java.io.File;

/**
 * @author Kieran Brahney
 * @version 1.0
 * @package uk.co.miami_nice.screenshot.io
 * @since 23/03/14 12:59
 */
public interface Uploader {

    /**
     * CLRF line endings for forms
     */
    final String CRLF = "\r\n";

    /**
     * Create a random unique boundary
     */
    final String boundary = Long.toHexString(System.currentTimeMillis());

    /**
     * Set the default charset to utf8
     */
    final String charset = "UTF-8";

    /**
     * Get a human meaningful name for the uploader service
     *
     * @return String name
     */
    public String getName();

    /**
     * Upload the specified binary file (image) to a given web server
     *
     * @param file Location of the file to upload
     * @return The URL to the uploaded file
     */
    public String post(File file);

    /**
     * Open the image using the appropriate software. For example, if the
     * image has been uploaded to imgur then this should open a web browser
     * pointed to http://imgur.com/image.png
     *
     * @param location URI location (file or URL)
     */
    public void openImage(String location);

}
