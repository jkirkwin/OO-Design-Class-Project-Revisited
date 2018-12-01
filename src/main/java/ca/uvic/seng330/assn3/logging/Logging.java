package ca.uvic.seng330.assn3.logging;

import ca.uvic.seng330.assn3.model.storage.Storage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Scanner;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;

public class Logging {

  private static final String logDir = "src" + File.separator + "logging" + File.separator;
  private static final String sessionFileName = logDir + "session.log";
  private static final String historicalFileName = logDir + "historical.log";
  private static Logger logger;
  private static boolean isInitialized = false;

  // append any existing content to the historical log file, set up system properties
  public static void init() {
    if (!isInitialized) {
      Scanner sc = null;
      PrintStream ps = null;
      Storage.ensureDirExists(logDir);
      try {
        File sessionLogFile = new File(sessionFileName);
        File historicalLogFile = new File(historicalFileName);
        if(!historicalLogFile.exists()) {
          historicalLogFile.createNewFile();
        }
        if(!sessionLogFile.exists()) {
          sessionLogFile.createNewFile();
        } else {
          // log file exists already
          ps = new PrintStream(new FileOutputStream(historicalFileName, true));
          sc = new Scanner(sessionLogFile);
          while(sc.hasNext()) {
            ps.println(sc.nextLine());
          }
          ps.println("--------- NEW SESSION ---------");
        }
      } catch (IOException e) {
        e.printStackTrace();
      } finally {
        if(sc != null) {
          sc.close();
        }
        if(ps != null) {
          ps.close();
        }
      }
      
      System.setProperty(org.slf4j.impl.SimpleLogger.LOG_FILE_KEY, sessionFileName);
      System.setProperty(
          org.slf4j.impl.SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "trace"); // Change to info
      logger = LoggerFactory.getLogger(Logging.class);
      isInitialized = true;
      log("Logger Created", Level.DEBUG);
    }
  }

  public static Logger getLogger() {
    return logger;
  }

  public static void logWithID(String message, UUID id, Level level) {
    assert message != null;
    assert id != null;
    log("UUID: " + id + "\t" + message, level);
  }

  public static void log(String message, Level level) {
    assert message != null;
    assert level != null;
    switch (level) {
      case DEBUG:
        logger.debug(message);
        break;
      case ERROR:
        logger.error(message);
        break;
      case INFO:
        logger.info(message);
        break;
      case TRACE:
        logger.trace(message);
        break;
      case WARN:
        logger.warn(message);
        break;
      default:
        logger.error(
            "Failed to log message due to invalid level.\tLevel: "
                + level
                + "\tMessage: "
                + message);
    }
  }
}
