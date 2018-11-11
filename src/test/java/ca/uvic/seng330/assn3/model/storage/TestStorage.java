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

  @Test
  public void testCameraRecreation() {
      Hub h = new Hub();
      Camera oracle1 = new Camera(h);
      Camera oracle2 = null;
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
      } catch (NoSuchMethodException | SecurityException e) {
        e.printStackTrace();
      }
      protectedConstructor.setAccessible(true);
      try {
        oracle2 = (Camera) protectedConstructor.newInstance(10, 15, true, UUID.randomUUID(), "someLabel", h);
      } catch (InstantiationException
          | IllegalAccessException
          | IllegalArgumentException
          | InvocationTargetException e) {
        e.printStackTrace();
      }
      
      Camera result1 = (Camera) Camera.getDeviceFromJSON(oracle1.getJSON(), h);
      Camera result2 = (Camera) Camera.getDeviceFromJSON(oracle2.getJSON(), h);
      
      // TODO iterate through all fields of each Camera and compare the appropriate pair      
      for (Class<?> c = oracle1.getClass(); c != null; c = c.getSuperclass()) {
        Field fields[] = oracle1.getClass().getDeclaredFields();
        for(Field f : fields) {
          f.setAccessible(true);
          try {
            assertTrue(f.get(oracle1).equals(f.get(result1)));
            assertTrue(f.get(oracle2).equals(f.get(result2)));
          } catch (IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
          }
        }        
      }
    }   
  
  @Test
  public void testLightbulbRecreation() {
    // TODO implement
    assertTrue(true);
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
