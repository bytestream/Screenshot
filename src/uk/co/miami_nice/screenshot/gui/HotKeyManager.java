package uk.co.miami_nice.screenshot.gui;

import uk.co.miami_nice.screenshot.gui.hotkeys.Windows;

/**
 * @author Kieran Brahney
 * @version 1.0
 * @package uk.co.miami_nice.screenshot.gui
 * @since 30/03/14 16:41
 */
public class HotKeyManager {

    public enum OS {Unknown, Windows, Mac, Linux}

    /**
     * Setup hot keys depending on OS
     */
    public HotKeyManager() {
        switch (getOperatingSystem()) {
            case Windows:
                new Windows();
                break;
        }
    }

    /**
     * Figure out what operating system we're running on
     *
     * @return an OS object (Windows, Linux, Mac, Unknown)
     */
    private static OS getOperatingSystem() {
        String osname = System.getProperty("os.name");

        OS currentOS = OS.Unknown;

        if (osname.contains("Mac")) {
            currentOS = OS.Mac;
        } else if (osname.contains("Windows")) {
            currentOS = OS.Windows;
        } else if (osname.contains("Linux")) {
            currentOS = OS.Linux;
        }

        return currentOS;
    }

}
