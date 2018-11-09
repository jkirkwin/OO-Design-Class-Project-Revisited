package ca.uvic.seng330.assn3.model.storage;

import ca.uvic.seng330.assn3.model.Hub;
import ca.uvic.seng330.assn3.model.UserAccount;
import ca.uvic.seng330.assn3.model.devices.Device;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;
import org.json.JSONArray;
import org.json.JSONObject;

public class Storage {
  private static final String storageDirPath = "storage" + File.pathSeparator;
  private static final String oldPath = storageDirPath + "old" + File.pathSeparator;
  private static final String deviceFileName = "devices.json";
  private static final String accountFileName = "accounts.json";

  /*
   * Moves old files from storage\ directory if they exist.
   * Creates new files for devices and accounts, and fills them
   * with a JSONArray representation of these objects
   */
  public static void store(Collection<? extends StorageEntity> devices, Collection<? extends StorageEntity> accounts) {
    cleanStorageDir();
    storeEntities(devices, deviceFileName);
    storeEntities(accounts, accountFileName);
  }

  private static void storeEntities(Collection<? extends StorageEntity> devices, String fileName) {
    File entityFile = new File(fileName);
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
  private static void cleanStorageDir() {
    File deviceFile = new File(storageDirPath + deviceFileName);
    File accountFile = new File(storageDirPath + accountFileName);
    String dateStamp = getDateStamp();
    File destinationDir = new File(oldPath + dateStamp + File.pathSeparator);
    boolean deviceFileExists = deviceFile.exists();
    boolean accountFileExists = deviceFile.exists();

    if (deviceFileExists || accountFileExists) {
      // make new storage\old\ subfolder for the files
      if (destinationDir.exists()) {
        // handle the possibility that some file will already have this name
        int i = 1;
        File collisionFile = new File(destinationDir.getPath());
        while (destinationDir.exists()) {
          collisionFile = new File(destinationDir.getPath() + " (" + i + ")");
          i++;
        }
        destinationDir = collisionFile;
      }
    }

    // move device file to destination
    if (deviceFileExists) {
      deviceFile.renameTo(new File(destinationDir.getPath() + File.pathSeparator + deviceFileName));
    }

    // move account file to destination
    if (accountFileExists) {
      accountFile.renameTo(
          new File(destinationDir.getPath() + File.pathSeparator + accountFileName));
    }
  }

  private static String getDateStamp() {
    Date d = new Date();
    String stamp = d.toString();
    return stamp;
  }

  public static Collection<Device> getDevices(Hub hub) {
    // retrieve device json objects from storage, convert them into Device objects, and return them
    List<Device> javaDevices = new ArrayList<Device>();

    File deviceFile = new File(deviceFileName);
    if (!deviceFile.exists() || !deviceFile.canRead()) {
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
    String content = "";
    while (sc.hasNextLine()) {
      content = content + sc.nextLine();
    }
    sc.close();
    return content;
  }

  public static Collection<UserAccount> getAccounts(Hub hub) {
    // retrieve useraccount json objects from storage, convert them into UserAccount objects, 
    // and return them

    List<UserAccount> javaAccounts = new ArrayList<UserAccount>();

    File accountFile = new File(accountFileName);
    if (!accountFile.exists() || !accountFile.canRead()) {
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
