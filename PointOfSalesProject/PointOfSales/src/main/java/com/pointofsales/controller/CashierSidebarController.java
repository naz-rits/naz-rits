package com.pointofsales.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.util.Optional;

@Controller
public class CashierSidebarController {

    public ConfigurableApplicationContext applicationContext;

    @Autowired
    public CashierSidebarController(ConfigurableApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @FXML public Button dashboardBar;
    @FXML public Button menuBar;
    @FXML public Button orderHistoryBar;

    @Setter
    private HomeController homeController;

    public void helperSidebarCSS(){
        if (dashboardBar != null) {
            dashboardBar.setStyle("-fx-background-color: white;" + "-fx-border-color: grey;");
        }
        if (menuBar != null) {
            menuBar.setStyle("-fx-background-color: white;" + "-fx-border-color: grey;");
        }
        if (orderHistoryBar != null) {
            orderHistoryBar.setStyle("-fx-background-color: white;" + "-fx-border-color: grey;");
        }
    }
    public void buttonControl() {
        if (dashboardBar != null) {
            dashboardBar.setDisable(false);
        }
        if (menuBar != null) {
            menuBar.setDisable(false);
        }
        if (orderHistoryBar != null) {
            orderHistoryBar.setDisable(false);
        }

    }
    public void initialize() throws IOException {
        loadDashboard();

    }

    public void loadDashboard() throws IOException {
        helperSidebarCSS();
        dashboardBar.setStyle("-fx-background-color: grey;" + "-fx-border-color: grey;" + "-fx-text-fill: white;");

        buttonControl();
        dashboardBar.setDisable(true);
        if (homeController != null) {
            homeController.loadDashboard();
        }

    }
    public void loadMenu() throws IOException {
        if (homeController != null) {
            helperSidebarCSS();
            menuBar.setStyle("-fx-background-color: grey;" + "-fx-border-color: grey;" + "-fx-text-fill: white;");

            buttonControl();
            menuBar.setDisable(true);
            homeController.loadMenu();
        }

    }
    public void loadOrderHistory() throws IOException {
        if (homeController != null) {
            helperSidebarCSS();
            orderHistoryBar.setStyle("-fx-background-color: grey;" + "-fx-border-color: grey;" + "-fx-text-fill: white;");

            buttonControl();
            orderHistoryBar.setDisable(true);
            homeController.loadOrderHistory();
        }
    }
    public void logoutButtonAction(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/Login.fxml"));
        fxmlLoader.setControllerFactory(applicationContext::getBean);

        Parent root = fxmlLoader.load();
        homeController.setCashier(Optional.empty());

        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.centerOnScreen();
        stage.show();
    }
}
