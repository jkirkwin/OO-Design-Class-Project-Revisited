package ca.jkirkwin.seng330.model.storage;

import ca.jkirkwin.seng330.logging.Logging;
import ca.jkirkwin.seng330.model.Hub;
import ca.jkirkwin.seng330.model.HubRegistrationException;
import ca.jkirkwin.seng330.model.Room;
import ca.jkirkwin.seng330.model.UserAccount;
import ca.jkirkwin.seng330.model.devices.Device;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.event.Level;

public class Storage {
  private static final String storageDirPath = "src" + File.separator + "storage" + File.separator;
  private static final String oldDirPath = storageDirPath + "old" + File.separator;
  private static final String deviceFileName = "devices.json";
  private static final String accountFileName = "accounts.json";
  private static final String roomFileName = "rooms.json";

  /*
   * If they exist, moves old files from storage\ to storage\old\timeStamp\
   * if they exist.
   *
   * Creates new files for devices and accounts, and fills them
   * with a JSONArray representation of these objects
   */
  public static void store(
      Collection<? extends StorageEntity> devices,
      Collection<? extends StorageEntity> accounts,
      Collection<? extends StorageEntity> rooms) {
    ensureDirExists(storageDirPath);
    ensureDirExists(oldDirPath);
    String destinationDirPath = oldDirPath + getDateStamp() + File.separator;
    cleanStorageDir(destinationDirPath);
    storeEntities(devices, storageDirPath, deviceFileName);
    storeEntities(accounts, storageDirPath, accountFileName);
    storeEntities(rooms, storageDirPath, roomFileName);
    Logging.log("Storage complete", Level.TRACE);
  }

  /*
   * @pre filePath should end in separator
   * @pre specified file must not exist
   */
  private static void storeEntities(
      Collection<? extends StorageEntity> devices, String filePath, String fileName) {
    File entityFile = new File(filePath + fileName);
    assert !entityFile.exists();
    PrintStream entityStream = null;
    try {
      entityFile.createNewFile();
      entityStream = new PrintStream(entityFile);
      entityStream.println(getJSONArray(devices));
    } catch (IOException e) {
      Logging.log("Error writing to file. Review storage directory structure!", Level.ERROR);
    } finally {
      if (entityStream != null) {
        entityStream.close();
      }
    }
  }

  private static JSONArray getJSONArray(Collection<? extends StorageEntity> devices) {
    JSONArray arr = new JSONArray();
    for (StorageEntity e : devices) {
      arr.put(e.getJSON());
    }
    return arr;
  }

  /*
   * Moves files from storage directory root into their own subfolder of
   * storage subfolder "old".
   * @pre storage file structure is as specified.
   */
  private static void cleanStorageDir(String destinationDirPath) {
    File deviceFileToMove = new File(storageDirPath + deviceFileName);
    File accountFileToMove = new File(storageDirPath + accountFileName);
    File roomFileToMove = new File(storageDirPath + roomFileName);

    if (!deviceFileToMove.exists() && !accountFileToMove.exists() && !roomFileToMove.exists()) {
      return;
    }

    ensureDirExists(destinationDirPath);
    File destinationDir = new File(destinationDirPath);
    assert destinationDir.exists() && destinationDir.isDirectory();

    File destDeviceFile = new File(destinationDirPath + deviceFileName);
    File destAccountFile = new File(destinationDirPath + accountFileName);
    File destRoomFile = new File(destinationDirPath + roomFileName);

    // handle name collisions in destination directory
    int i = 0;
    while (destDeviceFile.exists() || destAccountFile.exists() || destRoomFile.exists()) {
      destDeviceFile = new File(destinationDirPath + deviceFileName + "(" + i + ")");
      destAccountFile = new File(destinationDirPath + accountFileName + "(" + i + ")");
      destRoomFile = new File(destinationDirPath + roomFileName + "(" + i + ")");
      i++;
    }

    try {
      // move device file to destination
      if (deviceFileToMove.exists()) {
        Files.move(
            Paths.get(deviceFileToMove.getPath()),
            Paths.get(destDeviceFile.getPath()),
            StandardCopyOption.REPLACE_EXISTING);
        Logging.log("moved device file to storage\\old", Level.DEBUG);
      }
      // move account file to destination
      if (accountFileToMove.exists()) {
        Files.move(
            Paths.get(accountFileToMove.getPath()),
            Paths.get(destAccountFile.getPath()),
            StandardCopyOption.REPLACE_EXISTING);
        Logging.log("moved account file to storage\\old", Level.DEBUG);
      }
      // move room file to destination
      if (roomFileToMove.exists()) {
        Files.move(
            Paths.get(roomFileToMove.getPath()),
            Paths.get(destRoomFile.getPath()),
            StandardCopyOption.REPLACE_EXISTING);
        Logging.log("moved room file to storage\\old", Level.DEBUG);
      }

    } catch (IOException e) {
      e.printStackTrace();
      Logging.log(
          "IOException thrown when cleaning dir, attempting to move old items to "
              + destinationDirPath,
          Level.ERROR);
    }
    Logging.log("directory cleaned. old content moved to " + destinationDirPath + ".", Level.TRACE);
  }

  public static List<Device> getDevices(Hub hub) {
    // retrieve device json objects from storage, convert them into Device objects, and return them
    List<Device> javaDevices = new ArrayList<Device>();

    File deviceFile = new File(storageDirPath + deviceFileName);
    if (!deviceFile.exists() || !deviceFile.canRead()) {
      Logging.log("no storage file for devices found", Level.WARN);
      return javaDevices;
    }

    try {
      String jsonString = getFileContents(deviceFile);
      JSONArray jsonDevices = new JSONArray(jsonString);
      for (int i = 0; i < jsonDevices.length(); i++) {
        javaDevices.add(Device.getDeviceFromJSON(jsonDevices.getJSONObject(i), hub));
      }
    } catch (IOException e) {
      e.printStackTrace();
      Logging.log(
          "IOException thrown in getDevices. Consider rolling back to previous storage file set",
          Level.ERROR);
    }

    return javaDevices;
  }

  public static List<UserAccount> getAccounts(Hub hub) {
    // retrieve useraccount json objects from storage, convert them into UserAccount objects,
    // and return them
    List<UserAccount> javaAccounts = new ArrayList<UserAccount>();

    File accountFile = new File(storageDirPath + accountFileName);
    if (!accountFile.exists() || !accountFile.canRead()) {
      Logging.log("no storage file for accounts found", Level.WARN);
      return javaAccounts;
    }

    try {
      String jsonString = getFileContents(accountFile);
      JSONArray jsonAccounts = new JSONArray(jsonString);
      for (int i = 0; i < jsonAccounts.length(); i++) {
        javaAccounts.add(UserAccount.getAccountFromJSON(jsonAccounts.getJSONObject(i), hub));
      }
    } catch (IOException e) {
      e.printStackTrace();
      Logging.log(
          "IOException thrown in getAccounts. Consider rolling back to previous storage file set",
          Level.ERROR);
    }
    return javaAccounts;
  }

  public static List<Room> getRooms(Hub hub) {
    List<Room> javaRooms = new ArrayList<Room>();
    File roomFile = new File(storageDirPath + roomFileName);
    if (!roomFile.exists() || !roomFile.canRead()) {
      Logging.log("no storage file for rooms found", Level.WARN);
      return javaRooms;
    }
    try {
      String jsonString = getFileContents(roomFile);
      JSONArray jsonAccounts = new JSONArray(jsonString);
      for (int i = 0; i < jsonAccounts.length(); i++) {
        javaRooms.add(Room.getRoomFromJSON(jsonAccounts.getJSONObject(i), hub));
      }
    } catch (IOException e) {
      e.printStackTrace();
      Logging.log(
          "IOException thrown in getRooms. Consider rolling back to previous storage file set",
          Level.ERROR);
    } catch (HubRegistrationException e) {
      Logging.log(
          "Failed to register room to hub in getRooms. Consider rolling back to previous storage file set",
          Level.ERROR);
    }
    return javaRooms;
  }

  /*
   * @pre file != null
   */
  private static String getFileContents(File file) throws IOException {
    assert file != null;
    Scanner sc = new Scanner(file);
    StringBuilder sb = new StringBuilder();
    while (sc.hasNextLine()) {
      sb.append(sc.nextLine());
    }
    sc.close();
    return sb.toString();
  }

  public static void ensureDirExists(String dirPath) {
    File dir = new File(dirPath);
    if (dir.exists() && dir.isDirectory()) {
      return;
    }
    dir.mkdirs();
    Logging.log("created directory " + dirPath, Level.INFO);
  }

  private static String getDateStamp() {
    String rawDateString = new Date().toString();
    return rawDateString.replace(':', '-');
  }

  public static UUID getUUID(JSONObject jsonID) {
    assert jsonID != null;
    long low = jsonID.getLong("low");
    long high = jsonID.getLong("high");
    return new UUID(high, low);
  }

  public static JSONObject getJsonUUID(UUID id) {
    JSONObject idObj = new JSONObject();
    idObj.put("low", id.getLeastSignificantBits());
    idObj.put("high", id.getMostSignificantBits());
    return idObj;
  }
}
