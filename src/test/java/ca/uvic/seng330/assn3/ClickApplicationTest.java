package ca.uvic.seng330.assn3;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import org.junit.Before;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;

import ca.uvic.seng330.assn3.intro.ClickApplication;

import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.control.LabeledMatchers.hasText;

public class ClickApplicationTest extends ApplicationTest {
  @Override public void start(Stage stage) {
    Parent sceneRoot = new ClickApplication.ClickPane();
    Scene scene = new Scene(sceneRoot, 100, 100);
    stage.setScene(scene);
    stage.show();
  }
  
  //sleep half a second
  	@Before
  	public void hold() {
  		try {
  			Thread.sleep(500);
  		} 
  		catch (InterruptedException e){
  			// TODO Auto-generated catch block
  			e.printStackTrace();
  		} 
  	}

  @Test public void should_contain_button() {
    // expect:
    verifyThat(".button", hasText("click me!"));
  }

  @Test public void should_click_on_button() {
    // when:
    clickOn(".button");

    // then:
    verifyThat(".button", hasText("clicked!"));
  }
}
