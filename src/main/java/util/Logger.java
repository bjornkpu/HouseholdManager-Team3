package util;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.SimpleFormatter;
/**
 * -Description of the class-
 * Taken from https://github.com/nilstes/NotSoSecureBank/blob/master/src/main/java/util/Logger.java
 *
 * @author nilstes
 */
public class Logger {

    private static Logger instance = new Logger();
    private static java.util.logging.Logger logger;

    static {
        try {
            logger = java.util.logging.Logger.getLogger("HouseholdManager3");
            FileHandler fileHandler = new FileHandler("./HM3.log");
            SimpleFormatter simple = new SimpleFormatter();
            fileHandler.setFormatter(simple);
            logger.addHandler(fileHandler);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Logger getLogger() {
        return instance;
    }

    public void info(String info) {
        logger.info(info);
    }

    public void error(String error, Throwable t) {
        logger.log(Level.SEVERE, error, t);
    }
}
