package uk.co.miami_nice.screenshot.gui;

import uk.co.miami_nice.screenshot.CaptureType;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author Kieran Brahney
 * @version 1.0
 * @package uk.co.miami_nice.screenshot.gui
 * @since 23/03/14 13:02
 */
public class Interface implements ActionListener {

    private final Configure configureInterface = new Configure();

    private RegionSelection regionSelection;

    private final String title = "Screen Capture";

    private final PopupMenu popup = new PopupMenu();
    private final TrayIcon trayIcon = new TrayIcon(Toolkit.getDefaultToolkit().getImage(Interface.class.getResource("/img/icon.png")));
    private final SystemTray tray = SystemTray.getSystemTray();

    private MenuItem configureItem = new MenuItem("Configure");
    private MenuItem imageItem = new MenuItem("Image Capture");
    private MenuItem videoItem = new MenuItem("Video Capture");
    private MenuItem exitItem = new MenuItem("Exit");

    public Interface() {
        // Check the SystemTray support
        if (!SystemTray.isSupported()) {
            // TODO: error reporting
            System.err.println("SystemTray is not supported");
            return;
        }

        // Auto resize the tray icon
        trayIcon.setImageAutoSize(true);

        // Add components to popup menu
        popup.add(configureItem);
        configureItem.addActionListener(this);
        popup.addSeparator();
        popup.add(imageItem);
        imageItem.addActionListener(this);
        popup.add(videoItem);
        videoItem.addActionListener(this);
        popup.addSeparator();
        popup.add(exitItem);
        exitItem.addActionListener(this);

        trayIcon.setPopupMenu(popup);

        try {
            tray.add(trayIcon);
        } catch (AWTException e) {
            // TODO: error reporting
            System.err.println("TrayIcon could not be added.");
        }

        trayIcon.setToolTip(title);
        displayMessage("Successfully started application.", TrayIcon.MessageType.NONE);
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        if (actionEvent.getSource() == configureItem) {
            configureInterface.setVisible(true);

        } else if (actionEvent.getSource() == imageItem) {
            regionSelection = new RegionSelection(CaptureType.IMAGE);

        } else if (actionEvent.getSource() == videoItem) {
            regionSelection = new RegionSelection(CaptureType.VIDEO);

        } else if (actionEvent.getSource() == exitItem) {
            tray.remove(trayIcon);
            System.exit(0);
        }
    }

    public void displayMessage(String message, TrayIcon.MessageType messageType) {
        trayIcon.displayMessage(title, message, messageType);
    }

    public RegionSelection getRegionSelection() {
        return regionSelection;
    }

}
