package uk.co.miami_nice.screenshot.gui;

import com.melloware.jintellitype.HotkeyListener;
import com.melloware.jintellitype.JIntellitype;
import uk.co.miami_nice.screenshot.CaptureType;
import uk.co.miami_nice.screenshot.Driver;

/**
 * @author Kieran Brahney
 * @version 1.0
 * @package uk.co.miami_nice.screenshot.gui
 * @since 30/03/14 16:41
 */
public class HotKeyManager implements HotkeyListener {

    public enum OS {Unknown, Windows, Mac, Linux}

    ;

    public HotKeyManager() {
        switch (getOperatingSystem()) {
            case Windows:
                initWindows();
                break;
        }
    }

    /**
     * Initialise JIntellitype and set the hot keys for screen capture
     */
    private void initWindows() {
        JIntellitype.getInstance();

        // Assign global hotkeys to CTRL+SHIFT+S
        JIntellitype.getInstance().registerHotKey(1, JIntellitype.MOD_CONTROL + JIntellitype.MOD_SHIFT, (int) 'S');

        //assign this class to be a HotKeyListener
        JIntellitype.getInstance().addHotKeyListener(this);
    }

    /**
     * Event fired when a WM_HOTKEY message is received that was initiated
     * by this application.
     * <p/>
     *
     * @param identifier the unique Identifer the Hotkey was assigned
     */
    @Override
    public void onHotKey(int identifier) {
        Driver.getAnInterface().setRegionSelection(new RegionSelection(CaptureType.IMAGE));
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
