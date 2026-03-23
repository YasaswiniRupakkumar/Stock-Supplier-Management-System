package Controllers;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class HelloApplicationTest extends ApplicationTest {

    private HelloApplication app;
    private Stage stage;

    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;
        app = new HelloApplication();
        app.start(stage);
    }

    @Test
    public void testApplicationStart() {
        // Verify the stage and scene are initialized correctly
        assertNotNull(stage);
        Scene scene = stage.getScene();
        assertNotNull(scene);
        Parent root = scene.getRoot();
        assertNotNull(root);

        // Optionally test for title
        org.junit.jupiter.api.Assertions.assertEquals("John's Super Market", stage.getTitle());
    }
}