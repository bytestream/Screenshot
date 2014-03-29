package uk.co.miami_nice.screenshot;

import com.google.gson.Gson;
import uk.co.miami_nice.screenshot.gui.Interface;
import uk.co.miami_nice.screenshot.util.TrayHandler;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Kieran Brahney
 * @version 1.0
 * @package uk.co.miami_nice.screenshot
 * @since 23/03/14 12:56
 */
public class Driver {

    /**
     * Hold the user configuration data
     */
    private static Config config = new Config();

    /**
     * Interface
     */
    private static Interface anInterface = new Interface();

    /**
     * @param args
     */
    public static void main(String[] args) {
        // Load the logger
        Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).addHandler(new TrayHandler());
        Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).setLevel(Level.INFO);

        // Load the configuration
        try {
            BufferedReader br = new BufferedReader(new FileReader(config.getCONFIG_LOCATION()));
            config = new Gson().fromJson(br, Config.class);
        } catch (FileNotFoundException e) {
            Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).warning("Unable to read configuration file, resorting to default.");
        }
    }

    /**
     * Static access to configuration data
     *
     * @return User configuration data for this instance
     */
    public static Config getConfig() {
        return config;
    }

    /**
     * Static access to the interface object
     *
     * @return User interface object
     */
    public static Interface getAnInterface() {
        return anInterface;
    }

}
