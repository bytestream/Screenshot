package uk.co.miami_nice.screenshot.misc;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.logging.Logger;

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
     * @param image     BufferedImage object stored in-memory
     * @param imageType
     * @return The converted object as a byte array
     */
    public static byte[] imageToByteArray(BufferedImage image, String imageType) {
        byte[] res = new byte[0];

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ImageIO.write(image, imageType, baos);
            baos.flush();
            res = baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                baos.close();
            } catch (IOException e) {
                // Log unable to close stream but nothing fatal
                Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).finest(e.getMessage());
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
    public static String getUniqueHash(byte[] inBytes) {
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

    /**
     * Remove duplicate items from an array of strings
     *
     * @param array Array of strings
     * @return Unique elements of the array
     */
    public static String[] unique(String[] array) {
        for (int i = 0; i < array.length; i++)
            array[i] = array[i].toLowerCase();

        return new HashSet<String>(Arrays.asList(array)).toArray(new String[0]);
    }

}
