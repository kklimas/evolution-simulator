package com.app;
import com.app.configurations.CustomConfiguration;
import com.app.models.*;
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

    private final WorldMap map = new WorldMap();
    private List<Animal> animals = new ArrayList<>();
    private List<Plant> plants = new ArrayList<>();
    private HBox layout;

    private final int TILE_SIZE;

    public Simulation(CustomConfiguration configuration) {
        this.configuration = configuration;
        this.TILE_SIZE = (WINDOW_HEIGHT - INFO_HEIGHT) / configuration.mapHeight();
    }

    @Override
    public void run() {
        init();
    }

    private void init() {
        layout = new HBox();
        layout.setAlignment(Pos.TOP_CENTER);
        layout.setPadding(new Insets(25));
        Scene scene = new Scene(layout, WINDOW_WIDTH, WINDOW_HEIGHT);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Simulation");
        stage.show();

        placeEntities();
        start();
        draw();
    }

    private void placeEntities() {
        // animals
        for (int i = 0; i < configuration.animalsStartNumber(); i++) {
            var animal = new Animal(configuration);
            map.place(animal);
            animals.add(animal);
        }
        // plants
        for (int i = 0; i < configuration.startPlantsNumber(); i++) {
            var plant = new Plant(configuration.mapWidth(), configuration.mapHeight());
            map.place(plant);
            plants.add(plant);
        }
    }


    private void start() {
        die();
        move();
        eat();
        reproduce();
        grow();
    }

    private void die() {
        List<IMapEntity> deadMark = new ArrayList<>();
        animals.forEach(animal -> {
            if (animal.getCurrentEnergy() == 0) {
                deadMark.add((IMapEntity) animal);
            }
        });
        animals.removeAll(deadMark);
        map.removeAll(deadMark);

    }
    private void move() {

    }
    private void eat() {

    }
    private void reproduce() {

    }
    private void grow() {
        for (int i = 0; i < configuration.newPlantsEveryDay(); i++) {
            map.place(new Plant(configuration.mapWidth(), configuration.mapHeight()));
        }
    }

    private void draw() {
        layout.getChildren().clear();

        var grid = new GridPane();
        var leftBottom = new Vector2d(0, 0);
        var topRight = new Vector2d(configuration.mapWidth(), configuration.mapHeight());

        var rows = topRight.y - leftBottom.y + 2;
        var cols = topRight.x - leftBottom.x + 2;
        var rowConstr = new RowConstraints(TILE_SIZE);
        rowConstr.setValignment(VPos.CENTER);
        var colConstr = new ColumnConstraints(TILE_SIZE);
        colConstr.setHalignment(HPos.CENTER);
        grid.setGridLinesVisible(true);
        for (int i = 0; i < rows; i++) {
            grid.getRowConstraints().add(rowConstr);
        }
        for (int i = 0; i < cols; i++) {
            grid.getColumnConstraints().add(colConstr);
        }
        layout.getChildren().add(grid);

        // vbox for map info / graphs stopping starting etc.
        var info = new VBox();
        info.getChildren().add(new Label("abv"));
        layout.getChildren().add(info);
    }

}