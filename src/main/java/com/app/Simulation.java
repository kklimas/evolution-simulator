package com.app;
import com.app.configurations.CustomConfiguration;
import com.app.models.*;
import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.*;

import static com.app.configurations.DefaultConfiguration.*;

public class Simulation implements Runnable {

    private final CustomConfiguration configuration;
    private final IWorldMap worldMap;
    private HBox layout;

    private final int tileSize;

    public Simulation(CustomConfiguration configuration) {
        this.configuration = configuration;
        this.worldMap = new WorldMap(configuration);
        this.tileSize = (WINDOW_HEIGHT - INFO_HEIGHT) / configuration.mapHeight();

        var engine = new Engine(this, configuration, worldMap);
        var engineThread = new Thread(engine);
        engineThread.start();
    }

    @Override
    public void run() {
        layout = new HBox();
        layout.setAlignment(Pos.TOP_CENTER);
        layout.setPadding(new Insets(25));
        Scene scene = new Scene(layout, WINDOW_WIDTH, WINDOW_HEIGHT);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Simulation");
        stage.show();

        draw();
    }

    public void draw() {
        Platform.runLater(() -> {
            layout.getChildren().clear();

            var grid = new GridPane();
            var leftBottom = new Vector2d(0, 0);
            var topRight = new Vector2d(configuration.mapWidth(), configuration.mapHeight());

            var rows = topRight.getY() - leftBottom.getY() + 2;
            var cols = topRight.getX() - leftBottom.getX() + 2;
            var rowConstr = new RowConstraints(tileSize);
            rowConstr.setValignment(VPos.CENTER);
            var colConstr = new ColumnConstraints(tileSize);
            colConstr.setHalignment(HPos.CENTER);
            grid.setGridLinesVisible(true);
            for (var i = 0; i < rows; i++) {
                grid.getRowConstraints().add(rowConstr);
            }
            for (var i = 0; i < cols; i++) {
                grid.getColumnConstraints().add(colConstr);
            }

            // draw objects
            worldMap.getTakenPlaces().forEach(vector2d -> {
                var entity = worldMap.objectsAt(vector2d).first();
                grid.add(entity.getShape(), entity.getPosition().getX(), entity.getPosition().getY());
            });

            layout.getChildren().add(grid);

            // vbox for map info / graphs stopping starting etc.
            var info = new VBox();
            info.getChildren().add(new Label("abv"));
            layout.getChildren().add(info);
        });
    }

}