package ca.uvic.seng330.assn3.model.storage;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;

import ca.uvic.seng330.assn3.model.UserAccount;
import ca.uvic.seng330.assn3.model.devices.Device;

public class Storage {
  private final String storageDirPath = "storage" + File.pathSeparator;
  private final String oldPath = storageDirPath + "old" + File.pathSeparator;
  private final String deviceFileName = "devices.json";
  private final String accountFileName = "accounts.json";
  
  /*
   * Moves old files from storage\ directory if they exist.
   * Creates new files for devices and accounts, and fills them 
   * with a JSONArray representation of these objects
   */
  public void store(Collection<StorageEntity> devices, Collection<StorageEntity> accounts) {
    cleanStorageDir();
    storeEntities(devices, deviceFileName);
    storeEntities(accounts, accountFileName);
  }
  
  private void storeEntities(Collection<StorageEntity> entities, String fileName) {
    File entityFile = new File(fileName);
    assert !entityFile.exists();
    PrintStream entityStream = null;
    try {
      entityFile.createNewFile();
      entityStream = new PrintStream(entityFile);
      entityStream.println(getJSONArray(entities));
    } catch (IOException e) {
      // TODO Log error creating/writing to file and consider handling procedures
      e.printStackTrace();
    } finally {
      if(entityStream != null) {
        entityStream.close();
      }
    }
  }

  private JSONArray getJSONArray(Collection<StorageEntity> entities) {
    JSONArray arr = new JSONArray();
    for(StorageEntity e : entities) {
      arr.put(e.getJSON());
    }
    return arr;
  }

  /*
   * Moves files from storage directory root into their own subfolder of 
   * storage subfolder "old".
   * @pre storage file structure is as specified.
   */
  private void cleanStorageDir() {
    File deviceFile = new File(storageDirPath + deviceFileName);
    File accountFile = new File(storageDirPath + accountFileName);
    String dateStamp = getDateStamp();
    File destinationDir = new File(oldPath + dateStamp + File.pathSeparator);
    boolean deviceFileExists = deviceFile.exists();
    boolean accountFileExists = deviceFile.exists();
    
    if(deviceFileExists || accountFileExists) {
      // make new storage\old\ subfolder for the files
      if(destinationDir.exists()) {
        // handle the possibility that some file will already have this name
        int i = 1;
        File collisionFile = new File(destinationDir.getPath());
        while(destinationDir.exists()) {
          collisionFile = new File(destinationDir.getPath() + " (" + i + ")");
          i++;    
        }
        destinationDir = collisionFile;
      }
    }
    
    // move device file to destination
    if(deviceFileExists) {
      deviceFile.renameTo(new File(destinationDir.getPath() + File.pathSeparator + deviceFileName));
    }
    
    // move account file to destination
    if(accountFileExists) {
      accountFile.renameTo(new File(destinationDir.getPath() + File.pathSeparator + accountFileName));
    }
  }

  private String getDateStamp() {
    Date d = new Date();
    String stamp = d.toString();
    return stamp;
  }

  public Collection<Device> getDevices() {
    // retrieve device json objects from storage, convert them into Device objects, and return them
    List<Device> javaDevices = new ArrayList<Device>();

    File deviceFile = new File(deviceFileName);
    if(!deviceFile.exists() || !deviceFile.canRead()) {
      return javaDevices;
    }
    
    try {
      String jsonString = getFileContents(deviceFile);      
      JSONArray jsonDevices = new JSONArray(jsonString);
      for(int i = 0; i < jsonDevices.length(); i++) {
        // TODO decide how devices and users should be created from a JSONObject
        // Options include a static factory method for each in StorageEntity
        // A static factory method for each in Device and UserAccount respectively
        // A constructor that takes a JSONObject in Device, all subclasses of Device, and in UserAccount
        
      }
    } catch(IOException e) {
      e.printStackTrace();
      // TODO Log error
      // Consider handling options
    }
    
    return javaDevices;
  }
  
  /*
   * @pre file != null
   */
  private String getFileContents(File file) throws IOException {
    assert file != null;
    Scanner sc = new Scanner(file);
    String content = "";
    while(sc.hasNextLine()) {
      content = content + sc.nextLine();
    }
    sc.close();
    return content;
  }

  public Collection<UserAccount> getAccounts() {
    // TODO retrieve useraccount json objects from storage, convert them into UserAccount objects, and return them
    return null;
  }
  
}
