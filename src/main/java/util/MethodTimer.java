package util;

import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class MethodTimer implements AutoCloseable {

    private static final Logger log = Logger.getLogger("timerlog");

    static {
        try {
            FileHandler fileHandler = new FileHandler("./timerlog.log");
            SimpleFormatter simple = new SimpleFormatter();
            fileHandler.setFormatter(simple);
            log.addHandler(fileHandler);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String method;
    private String className;
    private long startTime;

    public MethodTimer(Class theClass, String method) {
        this.className = theClass.getName();
        this.method = method;
        startTime = System.currentTimeMillis();
    }

    public void close() {
        log.info(className + ":" + method + ": " + (System.currentTimeMillis() - startTime));
    }
}