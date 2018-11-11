package ca.uvic.seng330.assn3.model.storage;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.UUID;

import org.junit.Test;

import ca.uvic.seng330.assn3.model.Hub;
import ca.uvic.seng330.assn3.model.devices.Camera;
import ca.uvic.seng330.assn3.model.devices.CameraFullException;
import ca.uvic.seng330.assn3.model.devices.Device;
import ca.uvic.seng330.assn3.model.devices.Lightbulb;

public class TestStorage {

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
    // TODO implement
    assertTrue(false);
  }

  public boolean testFieldsIdentical(Field[] fields, Object oracle, Object result) {
    for(Field f : fields) {
      f.setAccessible(true);
      try {
        if(!f.get(oracle).equals(f.get(result))) {
          return false;
        }
      } catch (IllegalArgumentException | IllegalAccessException e) {
        e.printStackTrace();
      }
    }
    return true;
  }
  
  @Test
  public void testCameraRecreation() {
    Hub h = new Hub();
    Camera oracle1 = new Camera(h);
    Camera oracle2 = new Camera("a really weirdly long label", h);
    Camera oracle3 = null;
    @SuppressWarnings("rawtypes")
    Class[] arg = new Class[6];
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
      oracle3 = (Camera) protectedConstructor.newInstance(10, 15, true, UUID.randomUUID(), "someLabel", h);
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
    @SuppressWarnings("rawtypes")
    Class[] arg = new Class[4];
    arg[0] = Boolean.TYPE;
    arg[1] = UUID.class;
    arg[2] = String.class;
    arg[3] = Hub.class;
    
    Constructor<? extends Lightbulb> protectedConstructor = null;
    try {
      protectedConstructor = oracle1.getClass().getDeclaredConstructor(arg);
      protectedConstructor.setAccessible(true);
      oracle3 = (Lightbulb) protectedConstructor.newInstance(true, UUID.randomUUID(), "someLabel", h);
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
    // TODO implement
    assertTrue(false);
  }

  @Test
  public void testThermostatRecreation() {
    // TODO implement
    assertTrue(false);
  }
  
  @Test
  public void testGetJSONArray() {
    // TODO implement
    assertTrue(false);
  }
}
