package uk.co.miami_nice.screenshot.io;

import java.io.File;

/**
 * @author Kieran Brahney
 * @version 1.0
 * @package uk.co.miami_nice.screenshot.io
 * @since 23/03/14 12:59
 */
public interface Uploader {

    final String CRLF = "\r\n";

    final String boundary = Long.toHexString(System.currentTimeMillis());

    final String charset = "UTF-8";

    public String post(File file);

    public void openImage(String location);

}
