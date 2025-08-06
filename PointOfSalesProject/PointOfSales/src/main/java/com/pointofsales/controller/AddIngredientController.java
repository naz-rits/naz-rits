package com.pointofsales.controller;

import com.pointofsales.entity.Ingredient;
import com.pointofsales.listener.IngredientAddedListener;
import com.pointofsales.services.IngredientService;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

@Controller
public class AddIngredientController {

    @Setter
    public IngredientAddedListener ingredientAddedListener;

    public ConfigurableApplicationContext applicationContext;
    public IngredientService ingredientService;

    @FXML public ImageView addIngredientImage;
    @FXML public TextField ingredientNameField;
    @FXML public TextField quantityField;
    @FXML public TextField unitField;
    @FXML public DatePicker expirationDate;

    @Getter
    @Setter
    public File selectedFile;

    @Autowired
    public AddIngredientController(ConfigurableApplicationContext applicationContext, IngredientService ingredientService) {
        this.applicationContext = applicationContext;
        this.ingredientService = ingredientService;
    }

    public void showAlert(String alertMessage, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setHeaderText(null);
        alert.setContentText(alertMessage);
        alert.showAndWait();
    }

    @FXML public void addImage(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Product Image");

        FileChooser.ExtensionFilter imageFilter =
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg");
        fileChooser.getExtensionFilters().add(imageFilter);

        File file = fileChooser.showOpenDialog(new Stage());

        if (file != null) {
            try {
                Image image = new Image(new FileInputStream(file));
                addIngredientImage.setImage(image);
                selectedFile = file;
            } catch (FileNotFoundException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    @FXML public void addButtonAction() throws IOException {
        Ingredient ingredient = new Ingredient();
        if (ingredientNameField.getText().isEmpty() ||  quantityField.getText().isEmpty()
                || unitField.getText().isEmpty() || expirationDate.getValue() == null) {
            showAlert("Please fill all the fields", Alert.AlertType.INFORMATION);
        }
        else {
            ingredient.setImageUrl(selectedFile.getAbsolutePath());
            ingredient.setName(ingredientNameField.getText());
            ingredient.setQuantity(Integer.parseInt(quantityField.getText()));
            ingredient.setUnit(unitField.getText());
            ingredient.setExpirationDate(expirationDate.getValue());

            ingredientService.save(ingredient);

            ingredientNameField.clear();
            quantityField.clear();
            unitField.clear();
            expirationDate.setValue(null);
            addIngredientImage.setImage(new Image(new FileInputStream("src/main/resources/images/ui-images/add_product.png")));

            if (ingredientAddedListener != null) {
                ingredientAddedListener.onIngredientAdded();
            }
            showAlert("Added Successfully", Alert.AlertType.INFORMATION);
        }
    }
}
