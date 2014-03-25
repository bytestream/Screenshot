package uk.co.miami_nice.screenshot.io.video;

/*
 * Copyright (c) 1999-2001 Sun Microsystems, Inc. All Rights Reserved.
 *
 * Sun grants you ("Licensee") a non-exclusive, royalty free, license to use,
 * modify and redistribute this software in source and binary code form,
 * provided that i) this copyright notice and license appear on all copies of
 * the software; and ii) Licensee does not utilize the software in a manner
 * which is disparaging to Sun.
 *
 * This software is provided "AS IS," without a warranty of any kind. ALL
 * EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND WARRANTIES, INCLUDING ANY
 * IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE OR
 * NON-INFRINGEMENT, ARE HEREBY EXCLUDED. SUN AND ITS LICENSORS SHALL NOT BE
 * LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING
 * OR DISTRIBUTING THE SOFTWARE OR ITS DERIVATIVES. IN NO EVENT WILL SUN OR ITS
 * LICENSORS BE LIABLE FOR ANY LOST REVENUE, PROFIT OR DATA, OR FOR DIRECT,
 * INDIRECT, SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER
 * CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF THE USE OF
 * OR INABILITY TO USE SOFTWARE, EVEN IF SUN HAS BEEN ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGES.
 *
 * This software is not designed or intended for use in on-line control of
 * aircraft, air traffic, aircraft navigation or aircraft communications; or in
 * the design, construction, operation or maintenance of any nuclear
 * facility. Licensee represents and warrants that it will not use or
 * redistribute the Software for such purposes.
 */

import javax.media.Buffer;
import javax.media.Format;
import javax.media.format.VideoFormat;
import javax.media.protocol.ContentDescriptor;
import javax.media.protocol.PullBufferStream;
import java.awt.*;
import java.io.IOException;
import java.util.Vector;


/**
 * Modified version of sun microsystems class, used as
 * the source stream to go along with ImageDataSource.
 *
 * @author Kieran Brahney
 * @version 1.0
 * @package uk.co.miami_nice.screenshot.io.video
 * @since 25/03/14 15:43
 */
public class ImageSourceStream implements PullBufferStream {

    private Vector images;
    private int width, height;
    private VideoFormat format;

    private int nextImage = 0;  // index of the next image to be read.
    private boolean ended = false;

    public ImageSourceStream(int width, int height, int frameRate, Vector images) {
        this.width = width;
        this.height = height;
        this.images = images;

        format = new VideoFormat(VideoFormat.JPEG,
                new Dimension(width, height),
                Format.NOT_SPECIFIED,
                Format.byteArray,
                (float) frameRate);
    }

    /**
     * We should never need to block assuming data are read from files.
     */
    public boolean willReadBlock() {
        return false;
    }

    /**
     * This is called from the Processor to read a frame worth
     * of video data.
     */
    public void read(Buffer buf) throws IOException {
        // Check if we've finished all the frames.
        if (nextImage >= images.size()) {
            // We are done.  Set EndOfMedia.
            System.err.println("Done reading all images.");
            buf.setEOM(true);
            buf.setOffset(0);
            buf.setLength(0);
            ended = true;
            return;
        }

        byte[] image = (byte[]) images.elementAt(nextImage);
        nextImage++;

        System.err.println("  - reading image " + nextImage);

        buf.setData(image);
        buf.setOffset(0);
        buf.setLength(image.length);
        buf.setFormat(format);
        buf.setFlags(buf.getFlags() | Buffer.FLAG_KEY_FRAME);
    }

    /**
     * Return the format of each video frame.  That will be JPEG.
     */
    public Format getFormat() {
        return format;
    }

    public ContentDescriptor getContentDescriptor() {
        return new ContentDescriptor(ContentDescriptor.RAW);
    }

    public long getContentLength() {
        return 0;
    }

    public boolean endOfStream() {
        return ended;
    }

    public Object[] getControls() {
        return new Object[0];
    }

    public Object getControl(String type) {
        return null;
    }
}