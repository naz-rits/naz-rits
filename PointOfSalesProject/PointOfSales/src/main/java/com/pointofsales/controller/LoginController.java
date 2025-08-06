package com.pointofsales.controller;

import com.pointofsales.entity.Cashier;
import com.pointofsales.services.CashierService;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import lombok.Setter;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Controller;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

@Controller
public class LoginController {

    public ConfigurableApplicationContext applicationContext;
    public CashierService cashierService;

    public LoginController(ConfigurableApplicationContext applicationContext, CashierService cashierService) {
        this.applicationContext = applicationContext;
        this.cashierService = cashierService;
    }


    @FXML public Label adminPinLabel;
    @FXML public PasswordField adminPinPasswordfield;
    @FXML public TextField adminVisiblePasswordField;

    @FXML public Label cashierUsernameLabel;
    @FXML public TextField cashierUsernameTextfield;

    @FXML public Label cashierPinLabel;
    @FXML public PasswordField cashierPinPasswordfield;
    @FXML public TextField cashierVisiblePasswordField;

    @FXML public Button loginButton;
    @FXML public SplitMenuButton adminOrCashier;

    @FXML public ImageView adminShowPassword;
    @FXML public ImageView cashierShowPassword;

    private boolean isVisibleAdmin = false;
    private boolean isVisibleCashier = false;

    public void initialize() {
        adminVisiblePasswordField.managedProperty().bind(adminVisiblePasswordField.visibleProperty());
        adminPinPasswordfield.managedProperty().bind(adminPinPasswordfield.visibleProperty());

        cashierPinPasswordfield.managedProperty().bind(cashierPinPasswordfield.visibleProperty());
        cashierVisiblePasswordField.managedProperty().bind(cashierVisiblePasswordField.visibleProperty());

        adminVisiblePasswordField.setVisible(false);
        cashierVisiblePasswordField.setVisible(false);

        cashierShowPassword.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/ui-images/eye.png"))));
        adminShowPassword.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/ui-images/eye.png"))));
        cashierShowPassword.setVisible(true);
        adminShowPassword.setVisible(false);

        MenuItem cashier = new MenuItem("Cashier");
        MenuItem admin = new MenuItem("Admin");
        adminOrCashier.getItems().clear();
        adminOrCashier.getItems().addAll(cashier, admin);
        if (adminOrCashier.getText().equals("Cashier")) {
            adminPinPasswordfield.setVisible(false);

            adminPinLabel.setVisible(false);

            adminShowPassword.setVisible(false);

            cashierUsernameLabel.setVisible(true);
            cashierUsernameTextfield.setVisible(true);

            cashierPinLabel.setVisible(true);
            cashierPinPasswordfield.setVisible(true);
        }
        cashier.setOnAction(event -> {
            adminOrCashier.setText("Cashier");

            adminPinPasswordfield.setVisible(false);
            adminPinPasswordfield.clear();
            adminPinLabel.setVisible(false);
            adminShowPassword.setVisible(false);

            cashierUsernameLabel.setVisible(true);
            cashierUsernameTextfield.setVisible(true);

            cashierPinLabel.setVisible(true);
            cashierPinPasswordfield.setVisible(true);
            cashierShowPassword.setVisible(true);
        });

        admin.setOnAction(event -> {
            adminOrCashier.setText("Admin");

            adminPinLabel.setVisible(true);
            adminPinPasswordfield.setVisible(true);
            adminShowPassword.setVisible(true);

            cashierUsernameLabel.setVisible(false);
            cashierUsernameTextfield.setVisible(false);
            cashierUsernameTextfield.clear();

            cashierPinLabel.setVisible(false);
            cashierPinPasswordfield.setVisible(false);
            cashierShowPassword.setVisible(false);
            cashierPinPasswordfield.clear();
        });
    }

    @FXML public void loginButtonAction(ActionEvent event) throws IOException {
        Optional<Cashier> cashier = cashierService.findCashierByUsernameAndPassword(cashierUsernameTextfield.getText(), cashierPinPasswordfield.getText());
        Optional<Cashier> cashier1 = cashierService.findCashierByUsernameAndPassword(cashierUsernameTextfield.getText(), cashierVisiblePasswordField.getText());
        if (cashier.isPresent() || cashier1.isPresent()) {
            alert("Logged In Successfully", Alert.AlertType.INFORMATION);

            HomeController homeController = applicationContext.getBean(HomeController.class);
            homeController.setCashier(cashier);
            homeController.helperSidebarCSS();
            homeController.buttonControl();

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/Home.fxml"));
            fxmlLoader.setControllerFactory(applicationContext::getBean);

            Parent root = fxmlLoader.load();


            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.centerOnScreen();
            stage.show();
        }
        else if (adminPinPasswordfield.getText().equals("1234") || adminVisiblePasswordField.getText().equals("1234")) {
            alert("Logged In Successfully", Alert.AlertType.INFORMATION);

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/Home.fxml"));
            fxmlLoader.setControllerFactory(applicationContext::getBean);

            Parent root = fxmlLoader.load();

            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.centerOnScreen();
            stage.show();
        }
        else if (cashierUsernameTextfield.getText().isBlank() || cashierPinPasswordfield.getText().isBlank() ||
                 adminPinPasswordfield.getText().isBlank()) {
            alert("Please fill all the fields", Alert.AlertType.ERROR);
        }
        else {
            alert("Invalid credentials", Alert.AlertType.ERROR);
        }
    }

    @FXML public void alert(String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML public void adminShowPasswordAction() throws FileNotFoundException {
        isVisibleAdmin = !isVisibleAdmin;

        if (isVisibleAdmin) {
            adminVisiblePasswordField.setVisible(true);
            adminPinPasswordfield.setVisible(false);
            adminShowPassword.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/ui-images/view.png"))));
            adminVisiblePasswordField.setText(adminPinPasswordfield.getText());
        }
        else {
            adminVisiblePasswordField.setVisible(false);
            adminPinPasswordfield.setVisible(true);
            adminShowPassword.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/ui-images/eye.png"))));
            adminPinPasswordfield.setText(adminVisiblePasswordField.getText());
        }
    }

    @FXML public void cashierShowPasswordAction() {
        isVisibleCashier = !isVisibleCashier;

        if (isVisibleCashier) {
            cashierVisiblePasswordField.setVisible(true);
            cashierPinPasswordfield.setVisible(false);
            cashierShowPassword.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/ui-images/view.png"))));
            cashierVisiblePasswordField.setText(cashierPinPasswordfield.getText());
        }
        else {
            cashierVisiblePasswordField.setVisible(false);
            cashierPinPasswordfield.setVisible(true);
            cashierShowPassword.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/ui-images/eye.png"))));
            cashierPinPasswordfield.setText(cashierVisiblePasswordField.getText());
        }
    }
}
