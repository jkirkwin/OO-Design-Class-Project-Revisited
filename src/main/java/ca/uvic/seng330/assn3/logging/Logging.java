package ca.uvic.seng330.assn3.logging;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;

import ca.uvic.seng330.assn3.model.storage.Storage;

public class Logging {

  private static final String logDir = "src" +File.separator+ "logging" +File.separator;   
  private final static String fileName = logDir + "log.log";
  private static Logger logger;
  private static boolean isInitialized = false;
  
  
  public static void init() {
    if(!isInitialized) {      
      Storage.ensureDirExists(logDir);  
      try {
        File logFile = new File(fileName);
        logFile.createNewFile();
      } catch (IOException e) {
        e.printStackTrace();
      }
      System.setProperty(org.slf4j.impl.SimpleLogger.LOG_FILE_KEY, fileName);
      System.setProperty(org.slf4j.impl.SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "trace"); // Change to info 
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
    switch(level) {
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
        logger.error("Failed to log message due to invalid level.\tLevel: " + level + "\tMessage: " + message);
    }
  }
}
