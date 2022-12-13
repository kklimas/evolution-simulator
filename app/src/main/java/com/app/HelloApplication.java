package com.app;

import com.app.exceptions.InvalidConfigurationFileException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

import static com.app.services.ConfigurationReaderService.getConfigurationFromFile;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException, InvalidConfigurationFileException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open resource file");
        File file = fileChooser.showOpenDialog(stage);
        var conf = getConfigurationFromFile(file);
        System.out.println(conf.toString());
    }

    public static void main(String[] args) {
        launch();
    }
}