package uk.co.miami_nice.screenshot;

/**
 * @author Kieran Brahney
 * @version 1.0
 * @package uk.co.miami_nice.screenshot
 * @since 24/03/14 18:25
 */
public class Config {

    private static final String SSLTrustedStore = System.getProperty("java.io.tmpdir") + "jssecacerts";

    public static String getSSLTrustedStore() {
        return SSLTrustedStore;
    }
}
