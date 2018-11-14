package ca.uvic.seng330.assn3.model.storage;

import static org.junit.Assert.assertTrue;

import ca.uvic.seng330.assn3.model.AccessLevel;
import ca.uvic.seng330.assn3.model.Hub;
import ca.uvic.seng330.assn3.model.JSONMessaging;
import ca.uvic.seng330.assn3.model.UserAccount;
import ca.uvic.seng330.assn3.model.devices.Camera;
import ca.uvic.seng330.assn3.model.devices.Device;
import ca.uvic.seng330.assn3.model.devices.Lightbulb;
import ca.uvic.seng330.assn3.model.devices.SmartPlug;
import ca.uvic.seng330.assn3.model.devices.Temperature;
import ca.uvic.seng330.assn3.model.devices.Temperature.Unit;
import ca.uvic.seng330.assn3.model.devices.Thermostat;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Stack;
import java.util.UUID;
import org.json.JSONObject;
import org.junit.Test;

public class TestStorage {

  /*
   * Delete the directory and all contents
   * @pre dir is a directory
   */
  private static void deleteDir(File dir) {
    assert dir.isDirectory();
    for(File f : dir.listFiles()) {
      if(f.isDirectory()) {
        deleteDir(f);
      } else {
        f.delete();
      }
    }
    dir.delete();
  }
  
  private static void editStaticFinalField(Field f, Object newValue)
      throws NoSuchFieldException, SecurityException, IllegalArgumentException,
          IllegalAccessException {
    Field modifiers = Field.class.getDeclaredField("modifiers");
    modifiers.setAccessible(true);
    modifiers.setInt(f, f.getModifiers() & ~Modifier.FINAL);
    f.set(null, newValue);
  }

  public static boolean testAllFieldsEqual(Object[] oracles, Object[] results) {
    assert oracles != null;
    assert results != null;
    assert oracles.length == results.length;
    Field[] fields;
    for (Class<?> c = oracles[0].getClass(); c != null; c = c.getSuperclass()) {
      fields = c.getDeclaredFields();
      for (int i = 0; i < oracles.length; i++) {
        assertTrue(checkFieldsEqual(fields, oracles[i], results[i]));
      }
    }
    return false;
  }

  public static boolean checkFieldsEqual(Field[] fields, Object oracle, Object result) {
    for (Field f : fields) {
      f.setAccessible(true);
      try {
        if (f.get(oracle) == null || f.get(result) == null) {
          return f.get(oracle) == f.get(result);
        }
        if (!f.get(oracle).equals(f.get(result))) {
          return false;
        }
      } catch (IllegalArgumentException | IllegalAccessException e) {
        e.printStackTrace();
      }
    }
    return true;
  }

  @Test
  public void testStoreAndRetrieveUserAccounts() {
    Hub h = new Hub();
    List<ArrayList<StorageEntity>> oracles = new ArrayList<ArrayList<StorageEntity>>();
    ArrayList<StorageEntity> oracle1 = new ArrayList<StorageEntity>();

    ArrayList<StorageEntity> oracle2 = new ArrayList<StorageEntity>();
    oracle2.add(new UserAccount(h, AccessLevel.ADMIN, "user", "pw"));
    oracle2.add(new UserAccount(h, AccessLevel.BASIC, "some username", "some password"));

    ArrayList<StorageEntity> oracle3 = new ArrayList<StorageEntity>();
    String username, password;
    AccessLevel level;
    for (int i = 0; i < 100; i++) {
      username = "aUsername" + i;
      password = i + "A quite long password";
      level = AccessLevel.values()[i % 2];
      oracle3.add(new UserAccount(h, level, username, password));
    }
    oracles.add(oracle1);
    oracles.add(oracle2);
    oracles.add(oracle3);
    String fileName = "accounts.json";
    String filePath =
        "src.test.java.ca.uvic.seng330.assn3.model.storage.resources.".replace(".", File.separator);
    File tempDir = new File(filePath);
    File tempFile = new File(filePath + fileName);

    try {
      Method storeEntities =
          Storage.class.getDeclaredMethod(
              "storeEntities", Collection.class, String.class, String.class);
      Field storageDirPath = Storage.class.getDeclaredField("storageDirPath");
      storeEntities.setAccessible(true);
      storageDirPath.setAccessible(true);
      editStaticFinalField(storageDirPath, filePath);

      List<UserAccount> result;
      for (int i = 0; i < oracles.size(); i++) {
        if (!tempDir.exists()) {
          tempDir.mkdirs();
        }
        if (tempFile.exists()) {
          tempFile.delete();
        }
        storeEntities.invoke(null, oracles.get(i), filePath, fileName);
        result = Storage.getAccounts(h);
        assertTrue(result.equals(oracles.get(i)));
      }
    } catch (NoSuchMethodException
        | SecurityException
        | IllegalAccessException
        | IllegalArgumentException
        | InvocationTargetException
        | NoSuchFieldException e) {
      e.printStackTrace();
    } finally {
      if (tempFile.exists()) {
        tempFile.delete();
      }
      if (tempDir.exists()) {
        tempDir.delete();
      }
    }
  }

  @Test
  public void testStoreAndRetrieveDevices() {
    Hub h = new Hub();
    List<ArrayList<Device>> oracles = new ArrayList<ArrayList<Device>>();
    ArrayList<Device> oracle1 = new ArrayList<Device>();

    ArrayList<Device> oracle2 = new ArrayList<Device>();
    oracle2.add(new Camera(h));
    oracle2.add(new Thermostat(h));

    ArrayList<Device> oracle3 = new ArrayList<Device>();
    String label;
    Device d = null;
    for (int i = 0; i < 100; i++) {
      label = "aLabel" + i;
      switch (i % 4) {
        case 0:
          d = new Lightbulb(label, h);
          break;
        case 1:
          d = new Camera(label, h);
          break;
        case 2:
          d = new Thermostat(label, h);
          break;
        case 3:
          d = new SmartPlug(label, h);
          break;
      }
      oracle3.add(d);
    }
    oracles.add(oracle1);
    oracles.add(oracle2);
    oracles.add(oracle3);
    String fileName = "devices.json";
    String filePath =
        "src.test.java.ca.uvic.seng330.assn3.model.storage.resources.".replace(".", File.separator);
    File tempDir = new File(filePath);
    File tempFile = new File(filePath + fileName);

    try {
      Method storeEntities =
          Storage.class.getDeclaredMethod(
              "storeEntities", Collection.class, String.class, String.class);
      Field storageDirPath = Storage.class.getDeclaredField("storageDirPath");
      storeEntities.setAccessible(true);
      storageDirPath.setAccessible(true);
      editStaticFinalField(storageDirPath, filePath);

      List<Device> result;
      for (int i = 0; i < oracles.size(); i++) {
        if (!tempDir.exists()) {
          tempDir.mkdirs();
        }
        if (tempFile.exists()) {
          tempFile.delete();
        }
        storeEntities.invoke(null, oracles.get(i), filePath, fileName);
        result = Storage.getDevices(h);
        assertTrue(result.size() == oracles.get(i).size());
        for (int j = 0; j < result.size(); j++) {
          // Need only compare the labels. We are testing for ordering and completeness of the list.
          // The reconstruction process is tested in test[deviceType]Recreation
          assertTrue(result.get(j).getLabel().equals(oracles.get(i).get(j).getLabel()));
        }
      }
    } catch (NoSuchMethodException
        | SecurityException
        | IllegalAccessException
        | IllegalArgumentException
        | InvocationTargetException
        | NoSuchFieldException e) {
      e.printStackTrace();
    } finally {
      if (tempFile.exists()) {
        tempFile.delete();
      }
      if (tempDir.exists()) {
        tempDir.delete();
      }
    }
  }

  // Depends on ensureDirExists
  @Test
  public void testCleanStorageDir() {
    String tempStorageDirPath = "src.test.java.ca.uvic.seng330.assn3.model.storage.temp.".replace(".",  File.separator);
    String tempOldDirPath = tempStorageDirPath + "old";
    File tempStorageDir = new File(tempStorageDirPath);
    try {
      if(tempStorageDir.exists()) {
        deleteDir(tempStorageDir);
      }
      
      Method cleanStorageDir = Storage.class.getDeclaredMethod("cleanStorageDir", String.class);
      cleanStorageDir.setAccessible(true);
      Method ensureDirExists = Storage.class.getDeclaredMethod("ensureDirExists", String.class);
      ensureDirExists.setAccessible(true);
      Field storageDirPath = Storage.class.getDeclaredField("storageDirPath");
      storageDirPath.setAccessible(true);
      editStaticFinalField(storageDirPath, tempStorageDirPath);

      ensureDirExists.invoke(null, tempStorageDirPath);
      ensureDirExists.invoke(null, tempStorageDirPath + "old" + File.separator);
      
      // test functionality when dir has no files, accounts file, devices file, both files
      File accountFile = new File(tempStorageDirPath + File.separator + "accounts.json");
      File deviceFile = new File(tempStorageDirPath + File.separator + "devices.json");

      // TODO It is 1am and I just had surgery. Re-factor to use a loop instead of this nastiness. 
      cleanStorageDir.invoke(null, tempOldDirPath + "1" + File.separator);
      assertTrue(!accountFile.exists() && !deviceFile.exists());
      assertTrue(new File(tempOldDirPath + "1").listFiles() == null);
      
      accountFile.createNewFile();
      cleanStorageDir.invoke(null, tempOldDirPath + "2" + File.separator);
      assertTrue(!accountFile.exists() && !deviceFile.exists());
      System.out.println(new File(tempOldDirPath + "2").listFiles().length); 
      assertTrue(new File(tempOldDirPath + "2").listFiles().length == 1);
      
      deviceFile.createNewFile();
      cleanStorageDir.invoke(null, tempOldDirPath + "3" + File.separator);
      assertTrue(!accountFile.exists() && !deviceFile.exists());
      assertTrue(new File(tempOldDirPath + "3").listFiles().length == 1);

      accountFile.createNewFile();
      deviceFile.createNewFile();
      cleanStorageDir.invoke(null, tempOldDirPath + "4" + File.separator);
      assertTrue(!accountFile.exists() && !deviceFile.exists());
      assertTrue(new File(tempOldDirPath + "4").listFiles().length == 2);

    } catch (NoSuchFieldException
        | SecurityException
        | IllegalArgumentException
        | IllegalAccessException 
        | NoSuchMethodException 
        | InvocationTargetException 
        | IOException e) {
      e.printStackTrace();
    } finally {
      if(tempStorageDir.exists()) {
        deleteDir(tempStorageDir);
      }
    }
  }

  @Test
  public void testEnsureDirExists() {
    String testDirPath =
        "src.test.java.ca.uvic.seng330.assn3.model.storage".replace(".", File.separator);
    File testDir = new File(testDirPath);
    if (testDir.exists()) {
      testDir.delete();
    }
    try {
      Method ensureDirExists = Storage.class.getDeclaredMethod("ensureDirExists", String.class);
      ensureDirExists.setAccessible(true);
      ensureDirExists.invoke(null, testDirPath);
      assertTrue(testDir.exists() && testDir.isDirectory());
      ensureDirExists.invoke(null, testDirPath);
      assertTrue(testDir.exists() && testDir.isDirectory());
    } catch (NoSuchMethodException
        | SecurityException
        | IllegalAccessException
        | IllegalArgumentException
        | InvocationTargetException e) {
      e.printStackTrace();
    } finally {
      if (testDir.exists()) {
        testDir.delete();
      }
    }
  }

  @Test
  public void testUUIDRecreation() {
    UUID idOracle = UUID.randomUUID();
    UUID idTest = Storage.getUUID(Storage.getJsonUUID(idOracle));
    assertTrue(idOracle.equals(idTest));
  }

  @Test
  public void testUserAccountRecreation() {
    Hub h = new Hub();
    UserAccount oracle1 = new UserAccount(h, AccessLevel.BASIC, "username123", "issa password");
    UserAccount oracle2 = null;
    Class<?>[] arg = new Class<?>[7];
    arg[0] = Hub.class;
    arg[1] = AccessLevel.class;
    arg[2] = String.class;
    arg[3] = String.class;
    arg[4] = UUID.class;
    arg[5] = Stack.class;
    arg[6] = ArrayList.class;
    Constructor<? extends UserAccount> protectedConstructor = null;

    // Build sample blacklist and notification stack for oracle2
    Stack<JSONMessaging> notifications = new Stack<JSONMessaging>();
    ArrayList<UUID> blackList = new ArrayList<UUID>();
    Device[] devices = new Device[4];
    devices[0] = new Camera(h);
    devices[1] = new SmartPlug(h);
    devices[2] = new Thermostat(h);
    devices[3] = new Lightbulb(h);
    for (int i = 0; i < devices.length; i++) {
      JSONMessaging notifA = new JSONMessaging(devices[i], "message " + i + "a");
      JSONMessaging notifB = new JSONMessaging(devices[i], "message " + i + "b");
      notifications.push(notifA);
      notifications.push(notifB);
      blackList.add(devices[i].getIdentifier());
    }
    try {
      protectedConstructor = oracle1.getClass().getDeclaredConstructor(arg);
      protectedConstructor.setAccessible(true);
      oracle2 =
          (UserAccount)
              protectedConstructor.newInstance(
                  h,
                  AccessLevel.BASIC,
                  "some username",
                  "some password",
                  UUID.randomUUID(),
                  notifications,
                  blackList);
    } catch (NoSuchMethodException
        | InstantiationException
        | IllegalAccessException
        | IllegalArgumentException
        | InvocationTargetException e) {
      e.printStackTrace();
    }
    UserAccount result1 = UserAccount.getAccountFromJSON(oracle1.getJSON(), h);
    UserAccount result2 = UserAccount.getAccountFromJSON(oracle2.getJSON(), h);

    assertTrue(oracle1.equals(result1));
    assertTrue(oracle2.equals(result2));
  }

  @Test
  public void testCameraRecreation() {
    Hub h = new Hub();
    Camera oracle1 = new Camera(h);
    Camera oracle2 = new Camera("a really weirdly long label", h);
    Camera oracle3 = null;
    Class<?>[] arg = new Class<?>[6];
    arg[0] = Integer.TYPE;
    arg[1] = Integer.TYPE;
    arg[2] = Boolean.TYPE;
    arg[3] = UUID.class;
    arg[4] = String.class;
    arg[5] = Hub.class;

    Constructor<? extends Camera> protectedConstructor = null;
    try {
      protectedConstructor = oracle1.getClass().getDeclaredConstructor(arg);
      protectedConstructor.setAccessible(true);
      oracle3 =
          (Camera)
              protectedConstructor.newInstance(10, 15, true, UUID.randomUUID(), "someLabel", h);
    } catch (NoSuchMethodException
        | SecurityException
        | InstantiationException
        | IllegalAccessException
        | IllegalArgumentException
        | InvocationTargetException e) {
      e.printStackTrace();
    }
    Camera[] oracles = new Camera[] {oracle1, oracle2, oracle3};
    Camera[] results = new Camera[3];
    results[0] = (Camera) Camera.getDeviceFromJSON(oracle1.getJSON(), h);
    results[1] = (Camera) Camera.getDeviceFromJSON(oracle2.getJSON(), h);
    results[2] = (Camera) Camera.getDeviceFromJSON(oracle3.getJSON(), h);
    testAllFieldsEqual(oracles, results);
  }

  @Test
  public void testLightbulbRecreation() {
    Hub h = new Hub();
    Lightbulb oracle1 = new Lightbulb(h);
    Lightbulb oracle2 = new Lightbulb("a really weirdly long label", h);
    Lightbulb oracle3 = null;
    Class<?>[] arg = new Class<?>[4];
    arg[0] = Boolean.TYPE;
    arg[1] = UUID.class;
    arg[2] = String.class;
    arg[3] = Hub.class;

    Constructor<? extends Lightbulb> protectedConstructor = null;
    try {
      protectedConstructor = oracle1.getClass().getDeclaredConstructor(arg);
      protectedConstructor.setAccessible(true);
      oracle3 =
          (Lightbulb) protectedConstructor.newInstance(true, UUID.randomUUID(), "someLabel", h);
    } catch (NoSuchMethodException
        | SecurityException
        | InstantiationException
        | IllegalAccessException
        | IllegalArgumentException
        | InvocationTargetException e) {
      e.printStackTrace();
    }
    Lightbulb[] oracles = new Lightbulb[] {oracle1, oracle2, oracle3};
    Lightbulb[] results = new Lightbulb[3];
    results[0] = (Lightbulb) Lightbulb.getDeviceFromJSON(oracle1.getJSON(), h);
    results[1] = (Lightbulb) Lightbulb.getDeviceFromJSON(oracle2.getJSON(), h);
    results[2] = (Lightbulb) Lightbulb.getDeviceFromJSON(oracle3.getJSON(), h);
    testAllFieldsEqual(oracles, results);
  }

  @Test
  public void testSmartPlugRecreation() {
    Hub h = new Hub();
    SmartPlug oracle1 = new SmartPlug(h);
    SmartPlug oracle2 = new SmartPlug("a really weirdly long label", h);
    SmartPlug oracle3 = null;
    Class<?>[] arg = new Class<?>[4];
    arg[0] = Boolean.TYPE;
    arg[1] = UUID.class;
    arg[2] = String.class;
    arg[3] = Hub.class;

    Constructor<? extends SmartPlug> protectedConstructor = null;
    try {
      protectedConstructor = oracle1.getClass().getDeclaredConstructor(arg);
      protectedConstructor.setAccessible(true);
      oracle3 =
          (SmartPlug) protectedConstructor.newInstance(true, UUID.randomUUID(), "someLabel", h);
    } catch (NoSuchMethodException
        | SecurityException
        | InstantiationException
        | IllegalAccessException
        | IllegalArgumentException
        | InvocationTargetException e) {
      e.printStackTrace();
    }
    SmartPlug[] oracles = new SmartPlug[] {oracle1, oracle2, oracle3};
    SmartPlug[] results = new SmartPlug[3];
    results[0] = (SmartPlug) SmartPlug.getDeviceFromJSON(oracle1.getJSON(), h);
    results[1] = (SmartPlug) SmartPlug.getDeviceFromJSON(oracle2.getJSON(), h);
    results[2] = (SmartPlug) SmartPlug.getDeviceFromJSON(oracle3.getJSON(), h);
    testAllFieldsEqual(oracles, results);
  }

  @Test
  public void testThermostatRecreation() {
    Hub h = new Hub();
    Thermostat oracle1 = new Thermostat(h);
    Thermostat oracle2 = new Thermostat("a really weirdly long label", h);
    Thermostat oracle3 = null;
    Class<?>[] arg = new Class<?>[4];
    arg[0] = Temperature.class;
    arg[1] = UUID.class;
    arg[2] = String.class;
    arg[3] = Hub.class;

    Constructor<? extends Thermostat> protectedConstructor = null;
    try {
      protectedConstructor = oracle1.getClass().getDeclaredConstructor(arg);
      protectedConstructor.setAccessible(true);
      oracle3 =
          (Thermostat)
              protectedConstructor.newInstance(
                  new Temperature(101.1, Unit.FAHRENHEIT), UUID.randomUUID(), "someLabel", h);
    } catch (NoSuchMethodException
        | SecurityException
        | InstantiationException
        | IllegalAccessException
        | IllegalArgumentException
        | InvocationTargetException e) {
      e.printStackTrace();
    }
    Thermostat[] oracles = new Thermostat[] {oracle1, oracle2, oracle3};
    Thermostat[] results = new Thermostat[3];
    results[0] = (Thermostat) Thermostat.getDeviceFromJSON(oracle1.getJSON(), h);
    results[1] = (Thermostat) Thermostat.getDeviceFromJSON(oracle2.getJSON(), h);
    results[2] = (Thermostat) Thermostat.getDeviceFromJSON(oracle3.getJSON(), h);
    testAllFieldsEqual(oracles, results);
  }

  @Test
  public void TestTemperatureRecreation() {
    Method getTempFromJSON = null;
    try {
      Class<?>[] arg = new Class<?>[] {JSONObject.class};
      getTempFromJSON = Temperature.class.getDeclaredMethod("getTemperatureFromJSON", arg);
      getTempFromJSON.setAccessible(true);
    } catch (NoSuchMethodException | SecurityException e) {
      e.printStackTrace();
    }
    Temperature oracle1 = new Temperature(12.09, Unit.CELSIUS);
    Temperature oracle2 = new Temperature(112.10, Unit.FAHRENHEIT);
    Temperature result1 = null;
    Temperature result2 = null;

    try {
      result1 = (Temperature) getTempFromJSON.invoke(oracle1, oracle1.getJSON());
      result2 = (Temperature) getTempFromJSON.invoke(oracle2, oracle2.getJSON());
      getTempFromJSON.invoke(result2, oracle2.getJSON());
    } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
      e.printStackTrace();
    }
    assertTrue(result1.equals(oracle1));
    assertTrue(result2.equals(oracle2));
  }
}
