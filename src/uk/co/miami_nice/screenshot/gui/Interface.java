package uk.co.miami_nice.screenshot.gui;

import uk.co.miami_nice.screenshot.Driver;

import javax.swing.*;
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

    private final PopupMenu popup = new PopupMenu();
    private final TrayIcon trayIcon = new TrayIcon(Toolkit.getDefaultToolkit().getImage("img/icon.png"));
    private final SystemTray tray = SystemTray.getSystemTray();

    private MenuItem aboutItem = new MenuItem("About");
    private MenuItem configureItem = new MenuItem("Configure");
    private MenuItem fullScreenItem = new MenuItem("Fullscreen Capture");
    private MenuItem regionItem = new MenuItem("Regional Capture");
    private MenuItem exitItem = new MenuItem("Exit");

    public Interface() {
        // Check the SystemTray support
        if (!SystemTray.isSupported()) {
            System.out.println("SystemTray is not supported");
            return;
        }

        // Auto resize the tray icon
        trayIcon.setImageAutoSize(true);

        // Add components to popup menu
        popup.add(aboutItem);
        aboutItem.addActionListener(this);
        popup.add(configureItem);
        configureItem.addActionListener(this);
        popup.addSeparator();
        popup.add(fullScreenItem);
        fullScreenItem.addActionListener(this);
        popup.add(regionItem);
        regionItem.addActionListener(this);
        popup.addSeparator();
        popup.add(exitItem);
        exitItem.addActionListener(this);

        trayIcon.setPopupMenu(popup);

        try {
            tray.add(trayIcon);
        } catch (AWTException e) {
            System.out.println("TrayIcon could not be added.");
        }
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        if (actionEvent.getSource() == aboutItem) {
            JOptionPane.showMessageDialog(null,
                    "This dialog box is run from the About menu item");
        } else if (actionEvent.getSource() == configureItem) {
            JOptionPane.showMessageDialog(null,
                    "Not yet implemented");
        } else if (actionEvent.getSource() == fullScreenItem) {
            Rectangle area = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
            Driver.takeScreenshot(area);
        } else if (actionEvent.getSource() == regionItem) {
            new RegionSelection();
        } else if (actionEvent.getSource() == exitItem) {
            tray.remove(trayIcon);
            System.exit(0);
        }
    }
}
