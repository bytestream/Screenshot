package uk.co.miami_nice.screenshot.misc;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author Kieran Brahney
 * @version 1.0
 * @package uk.co.miami_nice.screenshot.misc
 * @since 23/03/14 13:32
 */
public class Misc {

    /**
     * Convert a bufferedimage object to a byte array
     *
     * @param image BufferedImage object stored in-memory
     * @return The converted object as a byte array
     */
    public static byte[] imageToByteArray(BufferedImage image) {
        byte[] res = new byte[0];

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ImageIO.write(image, "png", baos);
            baos.flush();
            res = baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                baos.close();
            } catch (IOException e) {
                // Log unable to close stream but nothing fatal
                e.printStackTrace();
            }
        }

        return res;
    }

    /**
     * Calculate the MD5 of a byte array
     *
     * @param inBytes Byte array
     * @return A string representing the MD5 hash of the byte array
     */
    public static String getMD5Hash(byte[] inBytes) {
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }

        digest.update(inBytes);
        byte[] md5 = digest.digest();

        String hash = "";
        for (byte b : md5) {
            hash += Integer.toString((b & 0xff) + 0x100, 16).substring(1);
        }

        return hash;
    }

}
