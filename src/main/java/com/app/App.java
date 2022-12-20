package com.app;

import com.app.configurations.CustomConfiguration;
import com.app.exceptions.InvalidConfigurationFileException;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

import static com.app.configurations.DefaultConfiguration.*;
import static com.app.services.ConfigurationReaderService.*;

public class App extends Application {

    private CustomConfiguration configuration;
    private VBox layout;
    private Button filePicker;
    private Label pickedFile;
    private Button runBtn;

    @Override
    public void start(Stage stage) {
        filePicker.setOnAction(actionEvent -> {
            FileChooser fileChooser = new FileChooser();
            File file = fileChooser.showOpenDialog(stage);
            try {
                this.configuration = getConfigurationFromFile(file);
                pickedFile.setText(file.getName());
                runBtn.setDisable(false);
            } catch (InvalidConfigurationFileException e) {
                throw new RuntimeException(e);
            }
        });

        Scene scene = new Scene(layout, INIT_WINDOW_WIDTH, INIT_WINDOW_HEIGHT);
        stage.setTitle(TITLE);
        stage.setScene(scene);
        stage.show();
    }
    @Override
    public void init() {
        layout = new VBox();
        layout.setAlignment(Pos.TOP_CENTER);
        layout.setPadding(new Insets(20, 20, 20, 20));
        layout.setSpacing(25);
        Label label = new Label("Set up configuration, then start program");
        layout.getChildren().add(label);

        filePicker = new Button("Choose file");
        pickedFile = new Label();

        runBtn = new Button("Run");
        runBtn.setOnAction(actionEvent -> launchSimulation());


        layout.getChildren().addAll(filePicker, pickedFile, runBtn);
    }
    public static void main(String[] args) {
        launch();
    }

    private void launchSimulation() {
        if (configuration == null) {
            configuration = getDefaultConfiguration();
        }
        Platform.runLater(new Simulation(configuration));

        pickedFile.setText("");
        configuration = null;
    }
}
