package uk.co.miami_nice.screenshot;

import uk.co.miami_nice.screenshot.net.Uploader;
import uk.co.miami_nice.screenshot.util.JavaClassFinder;

import javax.imageio.ImageIO;
import java.io.File;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

/**
 * @author Kieran Brahney
 * @version 1.0
 * @package uk.co.miami_nice.screenshot
 * @since 24/03/14 18:25
 */
public class Config {

    private final String CONFIG_LOCATION = System.getProperty("user.home") + File.separator + ".sc-resources";

    private final String SSL_TRUST_STORE_LOCATION = System.getProperty("user.home") + File.separator + ".sc-cacerts";

    private String imageFormat = "jpg";

    private String uploadMethod = "Localhost";

    private boolean autoUpload = true;

    private String outputDirectory = System.getProperty("java.io.tmpdir");

    public String getCONFIG_LOCATION() {
        return CONFIG_LOCATION;
    }

    public String getSSL_TRUST_STORE_LOCATION() {
        return SSL_TRUST_STORE_LOCATION;
    }

    public String getImageFormat() {
        return imageFormat;
    }

    public void setImageFormat(String imageFormat) {
        String[] array = ImageIO.getWriterFormatNames();
        if (Arrays.asList(array).contains(imageFormat.toLowerCase())) {
            this.imageFormat = imageFormat;
        }
    }

    public String getUploadMethod() {
        return uploadMethod;
    }

    public Class uploadMethodToClass() {
        JavaClassFinder classFinder = new JavaClassFinder();
        List<Class<? extends Uploader>> classes = classFinder.findAllMatchingTypes(Uploader.class);
        for (Class c : classes) {
            try {
                Method m = c.getMethod("getName", null);
                Object res = m.invoke(c.newInstance());
                String s = (String) res;

                if (s.equals(uploadMethod))
                    return c;
            } catch (Exception e) {
                // No associated upload class
                e.printStackTrace();
            }
        }

        return null;
    }

    public void setUploadMethod(String uploadMethod) {
        JavaClassFinder classFinder = new JavaClassFinder();
        List<Class<? extends Uploader>> classes = classFinder.findAllMatchingTypes(Uploader.class);
        for (Class c : classes) {
            try {
                Method m = c.getMethod("getName", null);
                Object res = m.invoke(c.newInstance());
                String s = (String) res;

                if (s.equals(uploadMethod))
                    this.uploadMethod = uploadMethod;
            } catch (Exception e) {
                // No associated upload class
                e.printStackTrace();
                System.err.println("No associated upload class for: " + uploadMethod);
                return;
            }
        }
    }

    public boolean isAutoUpload() {
        return autoUpload;
    }

    public void setAutoUpload(boolean autoUpload) {
        this.autoUpload = autoUpload;
    }

    public String getOutputDirectory() {
        return outputDirectory + File.separator;
    }

    public void setOutputDirectory(String outputDirectory) {
        this.outputDirectory = outputDirectory + File.separator;
    }

}
