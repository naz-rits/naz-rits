package com.pointofsales.controller;

import javafx.fxml.FXML;
import javafx.geometry.Side;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Controller;

import java.util.Date;

@Controller
public class DashboardController {

    public ConfigurableApplicationContext applicationContext;

    public CategoryAxis xAxis = new CategoryAxis();
    public NumberAxis yAxis = new NumberAxis();

    @FXML public LineChart<String, Number> totalSalesChart = new LineChart<>(xAxis, yAxis);
    @FXML public Label totalSalesAmount;
    @FXML public Label totalSalesCount;

    @FXML public Label bestSeller;
    @FXML public ImageView bestSellerImage;

    @FXML public Label lowStock;
    @FXML public ImageView lowStockImage;

    @FXML public Label blendsLegend;
    @FXML public Label wafflesLegend;

    @Autowired
    public DashboardController(ConfigurableApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public void initialize() {
        blendsLegend.setTextFill(Color.ORANGERED);
        wafflesLegend.setTextFill(Color.GOLD);
        xAxis.setLabel("MONTH");
        yAxis.setLabel("AMOUNT");
        int nigga = 100;
        int nigga2 = 200;
        String[] month = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        XYChart.Series<String, Number> totalSalesBlends = new XYChart.Series<>();
        XYChart.Series<String, Number> totalSalesWaffles = new XYChart.Series<>();
        totalSalesChart.setLegendVisible(false);

        for (int i = 0; i < 11; i++) {
            totalSalesBlends.getData().add(new XYChart.Data<>(month[i], nigga));
            if (month[i].equals("June")) {
                nigga -= 200;
            }
            else if (month[i].equals("September")) {
                nigga += 400;
            }
            else {
                nigga += 100;
            }
        }

        for (int i = 0; i < 11; i++) {
            totalSalesWaffles.getData().add(new XYChart.Data<>(month[i], nigga2));
            nigga2 += 300;
        }

        totalSalesChart.getData().add(totalSalesBlends);
        totalSalesChart.getData().add(totalSalesWaffles);
    }
}
