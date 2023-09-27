package com.myapp;

import com.opencsv.exceptions.CsvValidationException;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.text.Font;
import manager.MoodEntryManager;


import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Map;

public class MoodTrackingApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException, CsvValidationException {


        //#region Reading the data & preparing the home page
        MoodEntryManager.readCSV("src/main/resources/com/myapp/moodData.csv");
        AnchorPane homeRoot = new AnchorPane();
        Scene homesScene = new Scene(homeRoot);
        stage.setTitle("Mood Tracker");
        stage.setScene(homesScene);
        stage.setMinHeight(600);
        stage.setMinWidth(1000);
        //#endregion

        //region Home button format
        Text homeButton = new Text("Home");
        homeRoot.getChildren().add(homeButton);
        AnchorPane.setRightAnchor(homeButton, 25.0);
        AnchorPane.setTopAnchor(homeButton, 25.0);
        homeButton.setFont(Font.font("American Typewriter", 16.0));
        //endregion

        //region Data button format
        Text dataButton = new Text("Data");
        homeRoot.getChildren().add(dataButton);
        AnchorPane.setRightAnchor(dataButton, 25.0);
        AnchorPane.setTopAnchor(dataButton, 25.0);
        dataButton.setFont(Font.font("American Typewriter", 16.0));
        //endregion

        //region Most Frequent
        Text averageText = new Text("You're mostly:");
        homeRoot.getChildren().add(averageText);
        AnchorPane.setLeftAnchor(averageText, 25.0);
        AnchorPane.setTopAnchor(averageText, 25.0);
        averageText.setFont(Font.font("American Typewriter", 16.0));
        ImageView prominentMood = getMood();
        prominentMood.setFitWidth(25);
        prominentMood.setPreserveRatio(true);
        homeRoot.getChildren().add(prominentMood);
        AnchorPane.setLeftAnchor(prominentMood, 140.0);
        AnchorPane.setTopAnchor(prominentMood, 20.0);
        //endregion

        //region Question format
        StackPane questionPane = new StackPane();
            // might make it a variation later, how are u doing today? how did today go? etc..
        Text question = new Text("How are you feeling today?");
        questionPane.getChildren().add(question);
        questionPane.setAlignment(Pos.CENTER);
        question.setFont(Font.font("American Typewriter", 30.0));
        //endregion

        //region Home page setup
        HBox imageBox = new HBox();
        imageBox.setSpacing(15);
        imageBox.setAlignment(Pos.CENTER);
        VBox vbox = new VBox();
        homeRoot.getChildren().add(vbox);
        vbox.setAlignment(Pos.CENTER);
        vbox.getChildren().addAll(questionPane, imageBox);
        vbox.setSpacing(30);
        vbox.prefWidthProperty().bind(homesScene.widthProperty());
        AnchorPane.setTopAnchor(vbox, 0.0);
        AnchorPane.setBottomAnchor(vbox, 0.0);
        AnchorPane.setLeftAnchor(vbox, 0.0);
        AnchorPane.setRightAnchor(vbox, 0.0);
        dataButton.toFront();
        //endregion

        //region Data Page
        AnchorPane dataRoot = new AnchorPane();
        Scene dataScene = new Scene(dataRoot, homesScene.getWidth(), homesScene.getHeight());
        dataRoot.getChildren().add( homeButton);
        //endregion

        // Home and Data buttons
        dataButton.setOnMouseClicked(event -> {
            stage.setWidth(stage.getWidth());
            stage.setHeight(stage.getHeight());
            stage.setScene(dataScene);
        });
        homeButton.setOnMouseClicked(event -> {
            stage.setWidth(stage.getWidth());
            stage.setHeight(stage.getHeight());
            stage.setScene(homesScene);
        });

        //region Graph buttons
        Text frequencyChartButton = new Text("Mood Frequency Chart");
        frequencyChartButton.setFont(Font.font("American Typewriter", 16.0));
        Text trendChartButton = new Text("Mood Trend Chart");
        trendChartButton.setFont(Font.font("American Typewriter", 16.0));
        HBox chartOptions = new HBox();
        chartOptions.getChildren().addAll(frequencyChartButton, trendChartButton);
        chartOptions.setAlignment(Pos.CENTER);
        chartOptions.setSpacing(40);
        //endregion

        //region Line chart
        CategoryAxis xAxis = new CategoryAxis();
        ValueAxis<Number> yAxis = new NumberAxis(6, -1, 1);
        LineChart<String, Number> lineChart = new LineChart<>(xAxis, yAxis);
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Moods");
        lineChart.getData().add(series);
        lineChart.setMinHeight(500);
        updateGraph(series,  lineChart);
        //endregion

        //region Bar chart
        CategoryAxis x = new CategoryAxis();
        NumberAxis y = new NumberAxis(0, Arrays.stream(MoodEntryManager.moodEntries).max().getAsInt() ,1);
        BarChart<String, Number> barChart = new BarChart<>(x, y);
        barChart.setMinHeight(500);
        XYChart.Series<String, Number> series2 = new XYChart.Series<>();
        series.setName("Moods");
        barChart.getData().add(series2);
        updateBarGraph(series2, barChart);
        controlImages(imageBox, series,series2, barChart,  lineChart);
        //endregion

        // region All charts
        VBox chartView = new VBox();
        chartView.getChildren().addAll(chartOptions,lineChart);
        dataRoot.getChildren().add(chartView);
        AnchorPane.setTopAnchor(chartView, 50.0);
        AnchorPane.setBottomAnchor(chartView, 0.0);
        AnchorPane.setLeftAnchor(chartView, 0.0);
        AnchorPane.setRightAnchor(chartView, 0.0);
        chartView.setPadding(new Insets(100));
        chartView.setAlignment(Pos.CENTER);
        chartView.setSpacing(10);
        lineChart.prefHeightProperty().bind(chartView.heightProperty());
        //endregion

        // Chart buttons
        homeButton.toFront();
        frequencyChartButton.setOnMouseClicked(event->{
            if(!chartView.getChildren().contains(barChart))
                chartView.getChildren().add(barChart);
            chartView.getChildren().remove(lineChart);
        });
        trendChartButton.setOnMouseClicked(event->{
            if(!chartView.getChildren().contains(lineChart))
                chartView.getChildren().add(lineChart);
            chartView.getChildren().remove(barChart);
        });

        stage.show();
    }
    public void controlImages(HBox imageBox, XYChart.Series<String, Number> series, XYChart.Series<String, Number> series2, BarChart<String, Number> barChart, LineChart<String, Number> lineChart){
        for (int i = 0; i <= 5; i++) {
            Image image = new Image("/com/myapp/mood images/" + i + ".png");
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(90);
            imageView.setPreserveRatio(true);
            imageBox.getChildren().add(imageView);

            final int imageIndex = i;
            //when mood is clicked
            imageView.setOnMouseClicked(event -> {
                try {
                    MoodEntryManager.writeCSV("src/main/resources/com/myapp/moodData.csv",imageIndex);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                updateGraph(series,  lineChart);
                updateBarGraph(series2, barChart);

            });

            int finalI = i;
            imageView.setOnMouseEntered(event ->{
                DropShadow glow = new DropShadow();
                String colour = switch (finalI) {
                    case 0 -> "RED";
                    case 1 -> "ORANGE";
                    case 2 -> "YELLOW";
                    case 3 -> "GREEN";
                    case 4 -> "BLUE";
                    case 5 -> "PURPLE";
                    default -> "BLACK";
                };
                glow.setColor(Color.valueOf(colour));
                glow.setWidth(30);
                glow.setHeight(30);
                glow.setRadius(10);

                imageView.setEffect(glow);

            });
            imageView.setOnMouseExited(event ->{
                imageView.setEffect(null);
            });

        }

    }
    public void updateGraph(XYChart.Series<String, Number> series, LineChart<String, Number> lineChart){
        series.getData().clear();
        for (Map.Entry<LocalDate, Integer> entry : MoodEntryManager.moodMap.entrySet()) {
            LocalDate date = entry.getKey();
            Integer value = entry.getValue();
            series.getData().add(new XYChart.Data<>(date.toString(), value));
        }

        for (XYChart.Series<String, Number> s : lineChart.getData()) {
            for (XYChart.Data<String, Number> data : s.getData()) {
                Tooltip dataPointTooltip = new Tooltip( translateToEmoji((int)data.getYValue()));
                Tooltip.install(data.getNode(), dataPointTooltip);
                data.getNode().setOnMouseEntered(event -> {
                });
            }
        }

    }
    public String translateToEmoji(int value){
        return switch (value) {
            case 0 -> ":D";
            case 1 -> ":)";
            case 2 -> ":]";
            case 3 -> ":/";
            case 4 -> ":(";
            case 5 -> ":c";
            default -> "";
        };
    }
    public ImageView getMood() {
        int indexOfMax = -1;
        int max = -1;
        for (int i = 0; i < 5; i++){
            if (MoodEntryManager.moodEntries[i] > max) {
                max = MoodEntryManager.moodEntries[i];
                indexOfMax = i;
            }
        }
        Image image = new Image("/com/myapp/mood images/" + indexOfMax  + ".png");
        return new ImageView(image);
    }
    public void updateBarGraph(XYChart.Series<String, Number> series, BarChart<String, Number> barChart){
        series.getData().clear();
        String[] moodLabels = {":D", ":)", ":]", ":/", ":(", ":c"};
        for (int i = 0; i < MoodEntryManager.moodEntries.length; i++) {
            series.getData().add(new XYChart.Data<>(moodLabels[i], MoodEntryManager.moodEntries[i]));
        }
    }
    public static void main(String[] args) {
        launch();
    }
}

