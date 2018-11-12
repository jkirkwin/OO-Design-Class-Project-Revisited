package ca.uvic.seng330.assn3.model.storage;

import ca.uvic.seng330.assn3.model.Hub;
import ca.uvic.seng330.assn3.model.UserAccount;
import ca.uvic.seng330.assn3.model.devices.Device;
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

public class Storage {
  private static final String storageDirPath = "src" + File.separator + "storage" + File.separator;
  private static final String oldDirPath = storageDirPath + "old" + File.separator;
  private static final String deviceFileName = "devices.json";
  private static final String accountFileName = "accounts.json";

  /*
   * If they exist, moves old files from storage\ to storage\old\timeStamp\
   * if they exist.
   *
   * Creates new files for devices and accounts, and fills them
   * with a JSONArray representation of these objects
   */
  public static void store(
      Collection<? extends StorageEntity> devices, Collection<? extends StorageEntity> accounts) {
    ensureDirExists(storageDirPath);
    ensureDirExists(oldDirPath);
    String destinationDirPath = oldDirPath + getDateStamp() + File.separator;
    cleanStorageDir(destinationDirPath);
    storeEntities(devices, storageDirPath, deviceFileName);
    storeEntities(accounts, storageDirPath, accountFileName);
  }

  /*
   * filePath should end in separator
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
      // TODO Log error creating/writing to file and consider handling procedures
      e.printStackTrace();
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

    if (!deviceFileToMove.exists() && !accountFileToMove.exists()) {
      return;
    }

    ensureDirExists(destinationDirPath);
    File destinationDir = new File(destinationDirPath);
    assert destinationDir.exists() && destinationDir.isDirectory();

    File destDeviceFile = new File(destinationDirPath + deviceFileName);
    File destAccountFile = new File(destinationDirPath + accountFileName);

    // handle name collisions in destination directory
    int i = 0;
    while (destDeviceFile.exists() || destAccountFile.exists()) {
      destDeviceFile = new File(destinationDirPath + deviceFileName + "(" + i + ")");
      destAccountFile = new File(destinationDirPath + accountFileName + "(" + i + ")");
      i++;
    }

    try {
      // move device file to destination
      if (deviceFileToMove.exists()) {
        Files.move(
            Paths.get(deviceFileToMove.getPath()),
            Paths.get(destDeviceFile.getPath()),
            StandardCopyOption.REPLACE_EXISTING);
      }
      // move account file to destination
      if (accountFileToMove.exists()) {
        Files.move(
            Paths.get(accountFileToMove.getPath()),
            Paths.get(destAccountFile.getPath()),
            StandardCopyOption.REPLACE_EXISTING);
      }
    } catch (IOException e) {
      e.printStackTrace();
      // TODO Log this error
    }
  }

  private static String getDateStamp() {
    String rawDateString = new Date().toString();
    return rawDateString.replace(':', '-');
  }

  public static Collection<Device> getDevices(Hub hub) {
    // retrieve device json objects from storage, convert them into Device objects, and return them
    List<Device> javaDevices = new ArrayList<Device>();

    File deviceFile = new File(storageDirPath + deviceFileName);
    if (!deviceFile.exists() || !deviceFile.canRead()) {
      // TODO Log that no device file was found
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
      // TODO Log error
      // Consider handling options
    }

    return javaDevices;
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

  public static Collection<UserAccount> getAccounts(Hub hub) {
    // retrieve useraccount json objects from storage, convert them into UserAccount objects,
    // and return them

    List<UserAccount> javaAccounts = new ArrayList<UserAccount>();

    File accountFile = new File(storageDirPath + accountFileName);
    if (!accountFile.exists() || !accountFile.canRead()) {
      // TODO Log that no Accounts file was found
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
      // TODO Log error
      // Consider handling options
    }

    return javaAccounts;
  }

  private static void ensureDirExists(String dirPath) {
    File dir = new File(dirPath);
    if (dir.exists() && dir.isDirectory()) {
      return;
    }
    dir.mkdirs();
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
