package uk.co.miami_nice.screenshot.util;

import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/**
 * @author Kieran Brahney
 * @version 1.0
 * @package uk.co.miami_nice.screenshot.util
 * @since 26/03/14 22:00
 */
public class TrayFormatter extends Formatter {

    @Override
    public String format(LogRecord logRecord) {
        return formatMessage(logRecord);
    }

}
