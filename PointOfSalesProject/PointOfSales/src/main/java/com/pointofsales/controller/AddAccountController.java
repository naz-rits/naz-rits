package com.pointofsales.controller;


import com.pointofsales.entity.Cashier;
import com.pointofsales.services.CashierService;
import com.pointofsales.services.CustomerService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Controller;

@Controller
public class AddAccountController {

    public ConfigurableApplicationContext applicationContext;
    public CustomerService customerService;
    public CashierService cashierService;

    @FXML public SplitMenuButton cashierAdminSplit;
    @FXML public TextField fullName;
    @FXML public TextField username;
    @FXML public PasswordField password;
    @FXML public PasswordField confirmPassword;

    @Autowired
    public AddAccountController(CustomerService customerService, CashierService cashierService,
                                ConfigurableApplicationContext applicationContext) {
        this.customerService = customerService;
        this.applicationContext = applicationContext;
        this.cashierService = cashierService;
    }
    public void showAlert(String alertMessage, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setHeaderText(null);
        alert.setContentText(alertMessage);
        alert.showAndWait();
    }
    public void initialize(){

        MenuItem cashier = new MenuItem("Cashier");
        MenuItem admin = new MenuItem("Admin");
        cashierAdminSplit.getItems().clear();
        cashierAdminSplit.getItems().addAll(cashier, admin);


        cashier.setOnAction(event -> {
            cashierAdminSplit.setText("Cashier");
        });

        admin.setOnAction(event -> {
            cashierAdminSplit.setText("Admin");
        });
    }

    @FXML
    public void addCashierAction(){
        Cashier cashier = new Cashier();
        cashier.setFullName(fullName.getText());
        cashier.setUsername(username.getText());
        cashier.setPassword(password.getText());
        if (cashier.getPassword().equals(confirmPassword.getText())) {
            cashierService.addCashier(cashier);
            showAlert("Successfully added cashier", Alert.AlertType.INFORMATION);
        }
        else if (fullName.getText().isBlank() || username.getText().isBlank() || password.getText().isBlank()
                || confirmPassword.getText().isBlank()){
            showAlert("Please input all fields", Alert.AlertType.ERROR);
        }
    }

    public void addImage(){}

    public void cashierShowPasswordAction(){}
}
