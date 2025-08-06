package com.pointofsales.controller;

import com.pointofsales.entity.Cashier;
import com.pointofsales.fxmlLoader.SpringFXMLLoader;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import lombok.Setter;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Optional;

@Controller
public class HomeController {

    public void showAlert(String alertMessage, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setHeaderText(null);
        alert.setContentText(alertMessage);
        alert.showAndWait();
    }

    // border pane
    @FXML public AnchorPane centerArea;
    @FXML public AnchorPane rightArea;
    @FXML public Rectangle bottomArea;
    @FXML public AnchorPane leftArea;
    // sidebar
    @FXML public Button menuBar;
    @FXML public Button inventoryProductBar;
    @FXML public Button inventoryMaterialsBar;
    @FXML public Button dashboardBar;
    @FXML public Button orderHistoryBar;
    @FXML public Button cashierBar;
    @FXML public Button logoutButton;

    public ConfigurableApplicationContext applicationContext;

    @Setter
    public Optional<Cashier> cashier = Optional.empty();
    

    public void helperSidebarCSS(){
        if (dashboardBar != null) {
            dashboardBar.setStyle("-fx-background-color: white;" + "-fx-border-color: grey;");
        }
        if (menuBar != null) {
            menuBar.setStyle("-fx-background-color: white;" + "-fx-border-color: grey;");
        }
        if (inventoryProductBar != null) {
            inventoryProductBar.setStyle("-fx-background-color: white;" + "-fx-border-color: grey;");
        }
        if (inventoryMaterialsBar != null) {
            inventoryMaterialsBar.setStyle("-fx-background-color: white;" + "-fx-border-color: grey;");
        }
        if (orderHistoryBar != null) {
            orderHistoryBar.setStyle("-fx-background-color: white;" + "-fx-border-color: grey;");
        }
        if (cashierBar != null) {
            cashierBar.setStyle("-fx-background-color: white;" + "-fx-border-color: grey;");
        }
    }
    public void buttonControl() {
        if (dashboardBar != null) {
            dashboardBar.setDisable(false);
        }
        if (menuBar != null) {
            menuBar.setDisable(false);
        }
        if (inventoryProductBar != null) {
            inventoryProductBar.setDisable(false);
        }
        if (inventoryMaterialsBar != null) {
            inventoryMaterialsBar.setDisable(false);
        }
        if (orderHistoryBar != null) {
            orderHistoryBar.setDisable(false);
        }
        if (cashierBar != null) {
            cashierBar.setDisable(false);
        }

    }

    public HomeController(ConfigurableApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public void initialize() throws IOException {
        loadDashboard();
        if (cashier.isPresent()) {
            System.out.println(cashier.get().getFullName());
            SpringFXMLLoader springFXMLLoader = new SpringFXMLLoader(applicationContext);
            Parent sideBar = springFXMLLoader.load("/view/CashierSidebar.fxml");
            CashierSidebarController cashierSidebarController = springFXMLLoader.getLoader().getController();
            cashierSidebarController.setHomeController(this);


            leftArea.getChildren().setAll(sideBar);
        }

    }


    // sidebar
    public void loadDashboard() throws IOException {
        helperSidebarCSS();
        dashboardBar.setStyle("-fx-background-color: grey;" + "-fx-border-color: grey;" + "-fx-text-fill: white;");

        SpringFXMLLoader loader = new SpringFXMLLoader(applicationContext);
        Parent dashboardRoot = loader.load("/view/Dashboard.fxml");

        Parent placeholderRoot = loader.load("/view/Placeholder.fxml");
        centerArea.getChildren().setAll(dashboardRoot);
        rightArea.getChildren().setAll(placeholderRoot);

        buttonControl();
        dashboardBar.setDisable(true);

    }
    public void loadMenu() throws IOException {
        helperSidebarCSS();
        menuBar.setStyle("-fx-background-color: grey;" + "-fx-border-color: grey;" + "-fx-text-fill: white;");


        SpringFXMLLoader menuLoader = new SpringFXMLLoader(applicationContext);
        Parent menuRoot = menuLoader.load("/view/Menu.fxml");
        MenuController menuController = menuLoader.getLoader().getController();
        menuController.setInventoryMode(false);

        SpringFXMLLoader inventoryLoader = new SpringFXMLLoader(applicationContext);
        Parent addProductRoot = inventoryLoader.load("/view/AddProduct.fxml");
        AddProductController addProductController = inventoryLoader.getLoader().getController();

        SpringFXMLLoader receiptLoader = new SpringFXMLLoader(applicationContext);
        Parent receiptRoot = receiptLoader.load("/view/Receipt.fxml");
        ReceiptController receiptController = receiptLoader.getLoader().getController();
        receiptController.setMenuController(menuController);

        menuController.setReceiptController(receiptController);
        menuController.setAddProductController(addProductController);
        addProductController.setProductAddedListener(menuController);

        menuController.onEditStage = false;
        menuController.addProductController.isEdit = false;
        receiptController.currentTotal.set(0.0);


        centerArea.getChildren().setAll(menuRoot);
        rightArea.getChildren().setAll(receiptRoot);

        buttonControl();
        menuBar.setDisable(true);

    }
    public void loadProductInventory() throws IOException {
        helperSidebarCSS();
        inventoryProductBar.setStyle("-fx-background-color: grey;" + "fx-border-color: grey;" + "-fx-text-fill: white;");


        SpringFXMLLoader loader = new SpringFXMLLoader(applicationContext);

        Parent addProductRoot = loader.load("/view/AddProduct.fxml");
        AddProductController addProductController = loader.getLoader().getController();

        Parent inventoryRoot = loader.load("/view/Menu.fxml");
        MenuController menuController = loader.getLoader().getController();
        menuController.setInventoryMode(true);

        addProductController.setProductAddedListener(menuController);
        menuController.setAddProductController(addProductController);

        centerArea.getChildren().setAll(inventoryRoot);
        rightArea.getChildren().setAll(addProductRoot);

        buttonControl();
        inventoryProductBar.setDisable(true);

    }
    public void loadMaterialsInventory() throws IOException {
        helperSidebarCSS();
        inventoryMaterialsBar.setStyle("-fx-background-color: grey;" + "-fx-border-color: grey;" + "-fx-text-fill: white;");

        SpringFXMLLoader fxmlLoader = new SpringFXMLLoader(applicationContext);
        Parent inventoryRoot = fxmlLoader.load("/view/Inventory.fxml");

        SpringFXMLLoader springFXMLLoader = new SpringFXMLLoader(applicationContext);
        Parent addIngredientRoot = springFXMLLoader.load("/view/AddIngredient.fxml");

        AddIngredientController addIngredientController = springFXMLLoader.getLoader().getController();
        InventoryController inventoryController = fxmlLoader.getLoader().getController();

        inventoryController.setAddIngredientController(addIngredientController);
        addIngredientController.setIngredientAddedListener(inventoryController);


        centerArea.getChildren().setAll(inventoryRoot);
        rightArea.getChildren().setAll(addIngredientRoot);

        buttonControl();
        inventoryMaterialsBar.setDisable(true);


        showAlert("", Alert.AlertType.WARNING);
    }
    public void loadOrderHistory() throws IOException {
        helperSidebarCSS();
        orderHistoryBar.setStyle("-fx-background-color: grey;" + "-fx-border-color: grey;" + "-fx-text-fill: white;");

        SpringFXMLLoader fxmlLoader = new SpringFXMLLoader(applicationContext);
        Parent orderHistoryRoot = fxmlLoader.load("/view/OrderHistory.fxml");

//        OrderHistoryController orderHistoryController = fxmlLoader.getLoader().getController();

        buttonControl();
        orderHistoryBar.setDisable(true);
        centerArea.getChildren().setAll(orderHistoryRoot);
    }
    public void loadCashier() throws IOException {
        helperSidebarCSS();
        cashierBar.setStyle("-fx-background-color: grey;" + "-fx-border-color: grey;" + "-fx-text-fill: white;");

        SpringFXMLLoader fxmlLoader = new SpringFXMLLoader(applicationContext);
        Parent cashierRoot = fxmlLoader.load("/view/CashierAccount.fxml");

        Parent addAccountRoot = fxmlLoader.load("/view/AddAccount.fxml");

        buttonControl();
        cashierBar.setDisable(true);
        
        centerArea.getChildren().setAll(cashierRoot);
        rightArea.getChildren().setAll(addAccountRoot);
    }
    public void logoutButtonAction(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/Login.fxml"));
        fxmlLoader.setControllerFactory(applicationContext::getBean);

        Parent root = fxmlLoader.load();

        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.centerOnScreen();
        stage.show();
    }


}
