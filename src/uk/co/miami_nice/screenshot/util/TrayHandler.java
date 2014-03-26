package uk.co.miami_nice.screenshot.util;

import uk.co.miami_nice.screenshot.Driver;

import java.awt.*;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

/**
 * @author Kieran Brahney
 * @version 1.0
 * @package uk.co.miami_nice.screenshot.util
 * @since 26/03/14 21:21
 */
public class TrayHandler extends Handler {

    public TrayHandler() {
        setFormatter(new TrayFormatter());
    }

    @Override
    public void publish(LogRecord logRecord) {
        // Ensure that this log record should be logged by this Handler
        if (!isLoggable(logRecord))
            return;

        // Output the formatted data
        String message = getFormatter().format(logRecord);

        if (logRecord.getLevel() == Level.SEVERE) {
            Driver.getAnInterface().displayMessage(message, TrayIcon.MessageType.ERROR);
        } else if (logRecord.getLevel() == Level.WARNING) {
            Driver.getAnInterface().displayMessage(message, TrayIcon.MessageType.WARNING);
        } else if (logRecord.getLevel() == Level.INFO) {
            Driver.getAnInterface().displayMessage(message, TrayIcon.MessageType.INFO);
        } else {
            Driver.getAnInterface().displayMessage(message, TrayIcon.MessageType.NONE);
        }
    }

    @Override
    public void flush() {
    }

    @Override
    public void close() throws SecurityException {
    }

}
