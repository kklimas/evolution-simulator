package com.app;

import com.app.configurations.CustomConfiguration;
import com.app.exceptions.InvalidConfigurationFileException;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.Arrays;

import static com.app.configurations.DefaultConfiguration.*;
import static com.app.utils.ConfigurationReaderService.*;

public class App extends Application {

    private static final String CONFIG_FOLDER_PATH = "src/main/resources/configuration/%s";
    private static final String CSV_FOLDER_PATH = "src/main/resources/csv";

    private CustomConfiguration configuration;
    private VBox layout;
    private Button filePicker;
    private ChoiceBox<String> choiceBox;
    private Label pickedFile;
    private Button runBtn;
    private boolean generateCSV = false;
    private int currentSimulationId = 1;

    @Override
    public void start(Stage stage) {

        var directoryCleared = clearCSVDirectory();
        if (!directoryCleared) {
            System.out.println("Directory not cleaned up :((");
        }

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

        stage.setOnCloseRequest(event -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Close application");
            alert.setHeaderText("You are about to close whole application!");
            alert.setContentText("Are you sure to do this?");

            var showAlert = alert.showAndWait();
            if (showAlert.isPresent() && showAlert.get() == ButtonType.OK) {
                System.exit(0);
            }
        });
    }
    @Override
    public void init() {
        layout = new VBox();
        layout.setAlignment(Pos.TOP_CENTER);
        layout.setPadding(new Insets(20, 20, 20, 20));
        layout.setSpacing(25);

        Label greetingLabel = new Label("Hello!");
        greetingLabel.setFont(new Font(32));

        Label infoLabel = new Label("You are currently using the app to simulate evolution process.\n The only thing you need to do is picking configuration with which app will start.");
        infoLabel.setTextAlignment(TextAlignment.CENTER);
        infoLabel.setFont(new Font(16));

        Label label = new Label("Set up configuration, then start program");

        var checkBox = new CheckBox("Generate CSV file");
        checkBox.setOnAction(event -> generateCSV = checkBox.isSelected());

        runBtn = new Button("Run");
        runBtn.setDisable(true);
        runBtn.setPrefWidth(100);
        runBtn.setOnAction(actionEvent -> launchSimulation());

        var filePickers = setupFilePickers();

        layout.getChildren().addAll(greetingLabel, infoLabel, label, filePickers, checkBox, runBtn);
    }
    public static void main(String[] args) {
        launch();
    }

    private void launchSimulation() {
        if (configuration == null) {
            configuration = getDefaultConfiguration();
        }
        Platform.runLater(new Simulation(configuration, currentSimulationId, generateCSV));
        currentSimulationId++;
        runBtn.setDisable(true);
        pickedFile.setText("");
        choiceBox = setupChoiceBox();
        configuration = null;
    }

    private HBox setupFilePickers() {
        HBox filePickers = new HBox();

        filePicker = new Button("Choose custom configuration file");
        pickedFile = new Label();

        VBox filePicker1 = new VBox();
        filePicker1.setAlignment(Pos.CENTER);
        filePicker1.setSpacing(8);
        filePicker1.getChildren().addAll(filePicker, pickedFile);

        choiceBox = setupChoiceBox();
        var label = new Label("Choose file app configuration");

        VBox filePicker2 = new VBox();
        filePicker2.setAlignment(Pos.CENTER);
        filePicker2.setSpacing(8);
        filePicker2.getChildren().addAll(choiceBox, label);


        filePickers.setAlignment(Pos.CENTER);
        filePickers.setSpacing(20);
        filePickers.getChildren().addAll(filePicker1, filePicker2);

        return filePickers;
    }

    private ChoiceBox<String> setupChoiceBox() {
        var box = new ChoiceBox<String>();
        box.setPrefWidth(200);

        box.getItems().add("Huge world");
        box.getItems().add("Small world");
        box.getItems().add("Medium world");

        box.setOnAction(event -> {
            int selectedIndex = box.getSelectionModel().getSelectedIndex();

            String relativePath = switch (selectedIndex) {
                case 1 -> "small.txt";
                case 2 -> "medium.txt";
                default -> "huge.txt";
            };
            File file = new File(CONFIG_FOLDER_PATH.formatted(relativePath));
            try {
                configuration = getConfigurationFromFile(file);
                runBtn.setDisable(false);
            } catch (InvalidConfigurationFileException e) {
                throw new RuntimeException(e);
            }
        });
        return box;
    }

    private boolean clearCSVDirectory() {
        File dir = new File(CSV_FOLDER_PATH);
        var filesToRemove = dir.listFiles();
        if (filesToRemove == null) {
            return true;
        }
        return Arrays.stream(filesToRemove)
                .map(File::delete)
                .allMatch(e -> e.equals(true));
    }
}
