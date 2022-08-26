package org.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.example.sql.DBAccess;

import java.io.IOException;
import java.util.Objects;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;
    private static DBAccess dbAccess = null;


    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("primary"), 450, 720);
        stage.getIcons().add(new Image(Objects.requireNonNull(getClass()
                .getResource("/images/quiz_logo.png")).toExternalForm()));
        stage.setTitle("QUIZ! True or False? ");
        stage.setScene(scene);
        stage.show();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/views/" + fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static DBAccess getDbAccess() {
        if (dbAccess == null) {
            dbAccess = new DBAccess("QuizGameTrueOrFalse", "QuizTrueFalseAdmin", "QuizPW");
        }
        return dbAccess;
    }

    public static void main(String[] args) {
        launch();
    }

}