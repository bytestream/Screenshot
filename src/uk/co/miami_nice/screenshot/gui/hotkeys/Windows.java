package uk.co.miami_nice.screenshot.gui.hotkeys;

import com.melloware.jintellitype.HotkeyListener;
import com.melloware.jintellitype.JIntellitype;
import uk.co.miami_nice.screenshot.CaptureType;
import uk.co.miami_nice.screenshot.Driver;
import uk.co.miami_nice.screenshot.gui.RegionSelection;

/**
 * @author Kieran Brahney
 * @version 1.0
 * @package uk.co.miami_nice.screenshot.gui.hotkeys
 * @since 30/03/14 17:40
 */
public class Windows implements HotkeyListener {

    /**
     * Initialise JIntellitype and set the hot keys for screen capture
     */
    public Windows() {
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

}
