package com.app;

import com.app.configurations.CustomConfiguration;
import com.app.dtos.DrawDTO;
import com.app.entities.Animal;
import com.app.models.CSVRecord;
import com.app.utils.Engine;
import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.util.*;

import static com.app.configurations.DefaultConfiguration.*;
import static com.app.utils.CSVWriterService.writeToCSV;

public class Simulation implements Runnable {

    private final int id;
    private final CustomConfiguration configuration;
    private final boolean generateCSV;
    private final String FILE_PATH;
    private GridPane layout;
    private HBox map;
    private HBox charts;
    private VBox info;
    private HBox buttons;
    private Button runBtn;

    private int tileSize;

    public Simulation(CustomConfiguration configuration, int id, boolean generateCSV) {
        this.id = id;
        this.configuration = configuration;
        this.generateCSV = generateCSV;
        this.FILE_PATH = "src/main/resources/csv/simulation-%d.csv".formatted(id);
    }

    @Override
    public void run() {
        layout = new GridPane();
        layout.setAlignment(Pos.TOP_CENTER);
        layout.setPadding(new Insets(25));
        Scene scene = new Scene(layout, WINDOW_WIDTH, WINDOW_HEIGHT);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Simulation %d".formatted(id));
        stage.show();

        runBtn = new Button("Start / Stop Simulation");
        var engine = new Engine(this, configuration, stage, runBtn);
        var engineThread = new Thread(engine);
        engineThread.start();

        initWindow();
    }

    private void initWindow() {
        map = new HBox();
        map.setAlignment(Pos.CENTER);

        info = new VBox();
        info.setAlignment(Pos.TOP_CENTER);
        info.setSpacing(24);

        charts = new HBox();
        charts.setAlignment(Pos.TOP_CENTER);
        charts.setSpacing(24);

        buttons = new HBox();
        buttons.getChildren().add(runBtn);

        var col1 = new ColumnConstraints();
        col1.setPrefWidth(WINDOW_WIDTH - INFO_HEIGHT);
        var col2 = new ColumnConstraints();
        col2.setPrefWidth(INFO_HEIGHT);
        layout.getColumnConstraints().addAll(col1, col2);

        var row1 = new RowConstraints();
        row1.setPrefHeight(WINDOW_HEIGHT - CHARTS_HEIGHT);
        var row2 = new RowConstraints();
        row2.setPrefHeight(CHARTS_HEIGHT);
        layout.getRowConstraints().addAll(row1, row2);
        layout.setGridLinesVisible(true);
        layout.add(map, 0, 0);
        layout.add(charts, 0, 1);
        layout.add(info, 1, 0);
        layout.add(buttons, 1, 1);

        var w = (WINDOW_WIDTH - INFO_HEIGHT) / configuration.mapWidth();
        var h = (WINDOW_HEIGHT - CHARTS_HEIGHT) / configuration.mapHeight();
        tileSize = Math.min(w, h);
    }

    public void draw(DrawDTO drawDTO, boolean showDominant) {
        Platform.runLater(() -> {
            refreshMap(drawDTO, showDominant);
            refreshInfo(drawDTO);
            refreshCharts();
        });
    }

    public void updateCSV(DrawDTO drawDTO) {
        if (generateCSV) {
            var csvRecord = new CSVRecord(
                    drawDTO.currentDay(),
                    drawDTO.animalsNumber(),
                    drawDTO.averageEnergy(),
                    drawDTO.averageAnimalLifespan(),
                    drawDTO.plantsNumber(),
                    drawDTO.freePlaces(),
                    drawDTO.deadAnimals()
            );

            writeToCSV(FILE_PATH, csvRecord);
        }
    }

    private void refreshMap(DrawDTO data, boolean showDominant) {
        var entities = data.entitiesToDraw();

        map.getChildren().clear();
        var grid = new GridPane();

        var rowConstr = new RowConstraints(tileSize);
        rowConstr.setValignment(VPos.CENTER);
        var colConstr = new ColumnConstraints(tileSize);
        colConstr.setHalignment(HPos.CENTER);
        grid.setGridLinesVisible(true);
        for (var i = 0; i < configuration.mapHeight(); i++) {
            grid.getRowConstraints().add(rowConstr);
        }
        for (var i = 0; i < configuration.mapWidth(); i++) {
            grid.getColumnConstraints().add(colConstr);
        }

        // draw objects
        entities.forEach(entity -> {
            if (entity.getClass().equals(Animal.class)) {
                var animalGenotype = ((Animal) entity).getGenotype().toString();
                var shape = entity.getShape();
                if (animalGenotype.equals(data.dominantGenotype()) && showDominant) {
                    shape.setFill(Color.YELLOW);
                }
                grid.add(shape, entity.getPosition().getX(), entity.getPosition().getY());
            } else {
                grid.add(entity.getShape(), entity.getPosition().getX(), entity.getPosition().getY());
            }
        });
        grid.getChildren().forEach(item -> item.setOnMouseClicked(System.out::println));

        map.getChildren().add(grid);
    }

    private void refreshInfo(DrawDTO drawDTO) {
        info.getChildren().clear();

        var currentDay = "Current day:%n %d".formatted(drawDTO.currentDay());
        var animalsInfo = "Current animals number:%n %d".formatted(drawDTO.animalsNumber());
        var averageEnergy = "Average animal energy:%n %d".formatted(drawDTO.averageEnergy());
        var averageAnimalLifespan = "Average animal lifespan:%n %d".formatted(drawDTO.averageAnimalLifespan());
        var plantsInfo = "Current plants number:%n %d".formatted(drawDTO.plantsNumber());
        var freePlaces = "Current free places:%n %d".formatted(drawDTO.freePlaces());
        var deadAnimals = "Dead animals number:%n %d".formatted(drawDTO.deadAnimals());
        var mostFamousGenotype = "Most famous genotype:%n %s".formatted(drawDTO.dominantGenotype());

        var messages = List.of(
                currentDay,
                animalsInfo,
                averageEnergy,
                averageAnimalLifespan,
                plantsInfo,
                freePlaces,
                deadAnimals,
                mostFamousGenotype
        );
        info.getChildren().addAll(getInfoLabels(messages));
    }

    private void refreshCharts() {
//        charts.getChildren().removeAll();
//        //defining the axes
//        var xAxis = new NumberAxis();
//        var yAxis = new NumberAxis();
//        xAxis.setLabel("Number of Month");
//        //creating the chart
//        var lineChart = new LineChart<>(xAxis,yAxis);
//
//        lineChart.setTitle("Stock Monitoring, 2010");
//        //defining a series
//        var series = new XYChart.Series();
//        series.setName("My portfolio");
//        //populating the series with data
//        series.getData().add(new XYChart.Data(1, 23));
//        series.getData().add(new XYChart.Data(2, 14));
//        series.getData().add(new XYChart.Data(3, 15));
//        series.getData().add(new XYChart.Data(4, 24));
//        series.getData().add(new XYChart.Data(5, 34));
//        series.getData().add(new XYChart.Data(6, 36));
//        series.getData().add(new XYChart.Data(7, 22));
//        series.getData().add(new XYChart.Data(8, 45));
//        series.getData().add(new XYChart.Data(9, 43));
//        series.getData().add(new XYChart.Data(10, 17));
//        series.getData().add(new XYChart.Data(11, 29));
//        series.getData().add(new XYChart.Data(12, 25));
//
//        lineChart.getData().add(series);
//
//        charts.getChildren().add(lineChart);
    }

    private List<Label> getInfoLabels(List<String> messages) {
        return messages.stream().map(message -> {
            var label = new Label(message);
            label.setStyle("-fx-font: 16 arial;");
            label.setTextAlignment(TextAlignment.CENTER);
            return label;
        }).toList();
    }

}