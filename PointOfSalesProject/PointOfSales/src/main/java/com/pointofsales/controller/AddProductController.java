package com.pointofsales.controller;

import com.pointofsales.entity.Product;
import com.pointofsales.fxmlLoader.SpringFXMLLoader;
import com.pointofsales.listener.ProductAddedListener;
import com.pointofsales.services.ProductService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;


@Controller
public class AddProductController {

    private final ProductService productService;
    public ConfigurableApplicationContext applicationContext;
    private Product optionalProduct;

    @Setter
    public boolean isEdit = false;

    @FXML public StackPane addProductImagePane;
    @FXML public ImageView addProductImage;
    @FXML public TextField addProductName;
    @FXML public ComboBox<String> addProductCategory;
    @FXML public TextField addProductPrice;
    @FXML public TextArea addProductDescription;
    @FXML public Button addProductButton;
    @FXML public Button saveButton;

    @Setter
    private ProductAddedListener productAddedListener;

    @Getter
    @Setter
    public File selectedFile;

    public AddProductController(ProductService productService, ConfigurableApplicationContext applicationContext) {
        this.productService = productService;
        this.applicationContext = applicationContext;
    }


    @FXML public void initialize(){
        addProductCategory.getItems().addAll("Blend", "Refresher", "Coffee", "Waffle", "Smoothie", "Chia Pudding");
        if (isEdit) {
            addProductButton.setVisible(false);
            saveButton.setVisible(true);
        }
        else {
            addProductButton.setVisible(true);
            saveButton.setVisible(false);
        }
    }
    public void loadProductForEdit(Product product) {
        isEdit = true;
        optionalProduct = product;
        addProductButton.setVisible(false);
        saveButton.setVisible(true);

        addProductImage.setImage(new Image("file:" + product.getImageUrl()));
        addProductName.setText(product.getName());
        addProductDescription.setText(product.getDescription());
        addProductPrice.setText(String.valueOf(Math.round(product.getPrice())));
        addProductCategory.setValue(product.getCategory());
        setSelectedFile(new File(product.getImageUrl()));
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
                addProductImage.setImage(image);
                selectedFile = file;
            } catch (FileNotFoundException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    @FXML public void saveButtonAction() {

            optionalProduct.setImageUrl(optionalProduct.getImageUrl());
            optionalProduct.setName(addProductName.getText());
            optionalProduct.setDescription(addProductDescription.getText());
            optionalProduct.setPrice(Double.parseDouble(addProductPrice.getText()));
            optionalProduct.setCategory(addProductCategory.getValue());
            optionalProduct.setUpdatedAt(LocalDateTime.now());
            
            productService.updateProduct(optionalProduct);

            showAlert("Product updated successfully", Alert.AlertType.INFORMATION);

            productAddedListener.onProductAdded();

    }
    @FXML public void addButtonAction() throws IOException {
        Product product = new Product();

        if (addProductImage.getImage() == null || addProductName.getText().isBlank() ||
            addProductCategory.getValue().isBlank() || addProductPrice.getText().isBlank() ||
            addProductDescription.getText().isBlank()) {
            showAlert("Please fill all the fields", Alert.AlertType.ERROR);
            return;
        }

        if (!addProductPrice.getText().matches("^\\d+(\\.\\d{2})?$")) {
            showAlert("Invalid price", Alert.AlertType.ERROR);
            return;
        }

        product.setImageUrl(selectedFile.getAbsolutePath());
        product.setName(addProductName.getText());
        product.setCategory(addProductCategory.getSelectionModel().getSelectedItem());
        product.setPrice(Double.parseDouble(addProductPrice.getText()));
        product.setDescription(addProductDescription.getText());
        product.setStatus("Continued");
        product.setCreatedAt(LocalDateTime.now());
        product.setUpdatedAt(LocalDateTime.now());

        productService.addProduct(product);
        showAlert("Product added successfully", Alert.AlertType.INFORMATION);
        addProductName.clear();
        addProductDescription.clear();
        addProductImage.setImage(new Image(new FileInputStream("src/main/resources/images/ui-images/add_product.png")));
        addProductCategory.setValue(null);
        addProductPrice.clear();

        if (productAddedListener != null) {
            productAddedListener.onProductAdded();
        }

    }

    public void showAlert(String alertMessage, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setHeaderText(null);
        alert.setContentText(alertMessage);
        alert.showAndWait();
    }
}
