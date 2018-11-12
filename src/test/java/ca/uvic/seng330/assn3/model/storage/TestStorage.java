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
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Stack;
import java.util.UUID;
import org.json.JSONObject;
import org.junit.Test;

public class TestStorage {

  public static boolean testFieldsIdentical(Field[] fields, Object oracle, Object result) {
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
  public void testCleanDir() {
    // TODO implement
    assertTrue(false);
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

    Camera result1 = (Camera) Camera.getDeviceFromJSON(oracle1.getJSON(), h);
    Camera result2 = (Camera) Camera.getDeviceFromJSON(oracle2.getJSON(), h);
    Camera result3 = (Camera) Camera.getDeviceFromJSON(oracle3.getJSON(), h);

    // Iterate through all fields and compare
    for (Class<?> c = oracle1.getClass(); c != null && c != Object.class; c = c.getSuperclass()) {
      Field fields[] = c.getDeclaredFields();
      assertTrue(testFieldsIdentical(fields, oracle1, result1));
      assertTrue(testFieldsIdentical(fields, oracle2, result2));
      assertTrue(testFieldsIdentical(fields, oracle3, result3));
    }
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

    Lightbulb result1 = (Lightbulb) Lightbulb.getDeviceFromJSON(oracle1.getJSON(), h);
    Lightbulb result2 = (Lightbulb) Lightbulb.getDeviceFromJSON(oracle2.getJSON(), h);
    Lightbulb result3 = (Lightbulb) Lightbulb.getDeviceFromJSON(oracle3.getJSON(), h);

    // Iterate through all fields and compare
    for (Class<?> c = oracle1.getClass(); c != null && c != Object.class; c = c.getSuperclass()) {
      Field fields[] = c.getDeclaredFields();
      assertTrue(testFieldsIdentical(fields, oracle1, result1));
      assertTrue(testFieldsIdentical(fields, oracle2, result2));
      assertTrue(testFieldsIdentical(fields, oracle3, result3));
    }
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

    SmartPlug result1 = (SmartPlug) SmartPlug.getDeviceFromJSON(oracle1.getJSON(), h);
    SmartPlug result2 = (SmartPlug) SmartPlug.getDeviceFromJSON(oracle2.getJSON(), h);
    SmartPlug result3 = (SmartPlug) SmartPlug.getDeviceFromJSON(oracle3.getJSON(), h);

    // Iterate through all fields and compare
    for (Class<?> c = oracle1.getClass(); c != null && c != Object.class; c = c.getSuperclass()) {
      Field fields[] = c.getDeclaredFields();
      assertTrue(testFieldsIdentical(fields, oracle1, result1));
      assertTrue(testFieldsIdentical(fields, oracle2, result2));
      assertTrue(testFieldsIdentical(fields, oracle3, result3));
    }
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

    Thermostat result1 = (Thermostat) Thermostat.getDeviceFromJSON(oracle1.getJSON(), h);
    Thermostat result2 = (Thermostat) Thermostat.getDeviceFromJSON(oracle2.getJSON(), h);
    Thermostat result3 = (Thermostat) Thermostat.getDeviceFromJSON(oracle3.getJSON(), h);

    // Iterate through all fields and compare
    for (Class<?> c = oracle1.getClass(); c != null && c != Object.class; c = c.getSuperclass()) {
      Field fields[] = c.getDeclaredFields();
      assertTrue(testFieldsIdentical(fields, oracle1, result1));
      assertTrue(testFieldsIdentical(fields, oracle2, result2));
      assertTrue(testFieldsIdentical(fields, oracle3, result3));
    }
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

  @Test
  public void testGetJSONArray() {
    // TODO implement
    assertTrue(false);
  }
}
