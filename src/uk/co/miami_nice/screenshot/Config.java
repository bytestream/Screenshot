package uk.co.miami_nice.screenshot;

import uk.co.miami_nice.screenshot.io.Uploader;
import uk.co.miami_nice.screenshot.util.JavaClassFinder;

import javax.imageio.ImageIO;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

/**
 * @author Kieran Brahney
 * @version 1.0
 * @package uk.co.miami_nice.screenshot
 * @since 24/03/14 18:25
 */
public class Config implements Serializable {

    private static final String SSLTrustedStore = System.getProperty("java.io.tmpdir") + "jssecacerts";

    private static String imageFormat = "jpg";

    private static String uploadMethod = "Personal";

    private static boolean autoUpload = true;

    private static String outputDirectory = System.getProperty("java.io.tmpdir");

    public static String getSSLTrustedStore() {
        return SSLTrustedStore;
    }

    public static String getImageFormat() {
        return imageFormat;
    }

    public static void setImageFormat(String imageFormat) {
        String[] array = ImageIO.getWriterFormatNames();
        if (Arrays.asList(array).contains(imageFormat.toLowerCase())) {
            Config.imageFormat = imageFormat;
        }
    }

    public static String getUploadMethod() {
        return uploadMethod;
    }

    public static void setUploadMethod(String uploadMethod) {
        JavaClassFinder classFinder = new JavaClassFinder();
        List<Class<? extends Uploader>> classes = classFinder.findAllMatchingTypes(Uploader.class);
        for (Class c : classes) {
            try {
                Method m = c.getMethod("getName", null);
                Object res = m.invoke(c.newInstance());
                String s = (String) res;

                if (s.equals(uploadMethod))
                    Config.uploadMethod = uploadMethod;
            } catch (Exception e) {
                // No associated upload class
                e.printStackTrace();
                System.err.println("No associated upload class for: " + uploadMethod);
                return;
            }
        }
    }

    public static boolean isAutoUpload() {
        return autoUpload;
    }

    public static void setAutoUpload(boolean autoUpload) {
        Config.autoUpload = autoUpload;
    }

    public static String getOutputDirectory() {
        return outputDirectory;
    }

    public static void setOutputDirectory(String outputDirectory) {
        Config.outputDirectory = outputDirectory;
    }
}
