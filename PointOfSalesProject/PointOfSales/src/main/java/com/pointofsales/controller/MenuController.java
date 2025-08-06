package com.pointofsales.controller;

import com.pointofsales.entity.Product;
import com.pointofsales.listener.ProductAddedListener;
import com.pointofsales.listener.ShareUIState;
import com.pointofsales.services.ProductService;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Controller;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

import static com.pointofsales.controller.InventoryController.getImageView;
import static com.pointofsales.controller.InventoryController.getIngredientName;

@Controller
public class MenuController implements ProductAddedListener {

    @Setter
    @Getter
    private String currentCategory = "all";

    public ProductService productService;
    public ConfigurableApplicationContext applicationContext;

    public boolean onEditStage = false;
    private Product currentlyEditingProduct = null;

    @Getter
    public final Set<String> productsInReceipt = new HashSet<>();

    @Setter
    public AddProductController addProductController;

    @Setter
    public ReceiptController receiptController;

    @Getter
    private final Map<String, StackPane> disabledProductBoxes = new HashMap<>();


    @Autowired
    public MenuController(ConfigurableApplicationContext applicationContext, ProductService productService) {
        this.applicationContext = applicationContext;
        this.productService = productService;
    }

    private boolean inventoryMode = false;



    @FXML public Button all;
    @FXML public Button blendsButton;
    @FXML public Button coffeesButton;
    @FXML public Button refresherButton;
    @FXML public Button wafflesButton;
    @FXML public Button smoothiesButton;
    @FXML public Button chiaPuddingsButton;
    @FXML public AnchorPane menu;
    @FXML public GridPane gridPane;
    @FXML public ScrollPane scrollPane;

    public void initialize() throws IOException {
        gridPane.setMaxWidth(Region.USE_PREF_SIZE);
        activeButton("all");
        refreshGrid();

    }

    public void setInventoryMode(boolean inventoryMode) throws IOException {
        this.inventoryMode = inventoryMode;
        refreshGrid();
    }

    // product view
    public ImageView productImage(String path) {
        return getImageView(path);
    }
    public Label productName(String productName){
        return getIngredientName(productName);
    }
    public Label productPrice(String productPrice){
        Label label = new Label(productPrice);
        label.setLayoutX(42);
        label.setLayoutY(163);
        label.setFont(Font.font("Arial", FontWeight.BOLD, 18));

        return label;
    }
    public Button removeProductButton(Product product, String status){


        Button removeButton = new Button("Discontinue");
        removeButton.setStyle("-fx-background-color: red;" + "-fx-background-radius: 50;"
                              + "-fx-border-radius: 50;" + "-fx-text-fill: white;");
        removeButton.setTranslateY(-4);
        removeButton.setCursor(Cursor.HAND);

        removeButton.setOnAction(event -> {
            product.setStatus("Discontinued");
            productService.addProduct(product);
            if (removeButton.getText().equals("Remove")){
                productService.removeProduct(product);

            }
            onProductAdded();
        });

        if (status.equals("Discontinued")){
            removeButton.setText("Remove");
        }

        return removeButton;
    }
    public Button continueProductButton(Product product) {
        Button continueButton = new Button("Continue");
        continueButton.setStyle("-fx-background-color: green;" + "-fx-background-radius: 50;"
                + "-fx-border-radius: 50;" + "-fx-text-fill: white;");
        continueButton.setTranslateY(-4);
        continueButton.setCursor(Cursor.HAND);

        continueButton.setOnAction(event -> {
            product.setStatus("Continued");
            productService.addProduct(product);

            onProductAdded();
        });
        return continueButton;
    }
    public Button editProductButton(Product product) throws IOException{
        Button editButton = new Button("Edit");
        editButton.setStyle("-fx-background-color: green;" + "-fx-background-radius: 50;"
                + "-fx-border-radius: 50;" + "-fx-text-fill: white;");
        editButton.setTranslateY(-4);
        editButton.setCursor(Cursor.HAND);
        editButton.setScaleX(1.1);

        editButton.setOnAction(event -> {
            onEditStage = true;
            currentlyEditingProduct = product;

            addProductController.addProductImage.setImage(new Image("file:" + product.getImageUrl()));
            addProductController.addProductName.setText(product.getName());
            addProductController.addProductDescription.setText(product.getDescription());
            addProductController.addProductPrice.setText(String.valueOf(Math.round(product.getPrice())));
            addProductController.addProductCategory.setValue(product.getCategory());

            addProductController.loadProductForEdit(product);

            onProductAdded();

        });

        return editButton;
    }
    public Button cancelButton(Product product){
        Button cancelButton = new Button("Cancel");
        cancelButton.setStyle("-fx-background-color: red;" + "-fx-background-radius: 50;"
                + "-fx-border-radius: 50;" + "-fx-text-fill: white;");
        cancelButton.setTranslateY(-4);
        cancelButton.setCursor(Cursor.HAND);
        cancelButton.setScaleX(1.1);
        cancelButton.setOnAction(event -> {
           currentlyEditingProduct = null;
           onEditStage = false;
           addProductController.isEdit = false;
           addProductController.saveButton.setVisible(false);
           addProductController.addProductButton.setVisible(true);

           onProductAdded();

            try {
                addProductController.addProductImage.setImage(new Image(new FileInputStream("src/main/resources/images/ui-images/add_product.png")));
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
           addProductController.addProductName.clear();
           addProductController.addProductDescription.clear();
           addProductController.addProductPrice.clear();
           addProductController.addProductCategory.setValue(null);


        });
        return cancelButton;
    }
    public StackPane createProductBox(Product product) throws IOException {
        StackPane container = new StackPane();
        container.setPrefSize(200, 200);
        container.setStyle(
                "-fx-background-color: #F2F2F2;" +
                        "-fx-background-radius: 30;" +
                        "-fx-border-radius: 30;" +
                        "-fx-border-color: grey;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 10, 0.5, 0, 4);"
        );

        // Store reference

        VBox vBox = new VBox();
        vBox.setAlignment(Pos.CENTER);

        ImageView productImage = productImage(product.getImageUrl());

        Label productName = productName(product.getName());
        productName.setWrapText(true);
        productName.setMaxWidth(150);
        productName.setAlignment(Pos.CENTER);

        Label productPrice = productPrice("₱" + product.getPrice().toString());
        productPrice.setFont(Font.font("Arial", FontWeight.BOLD, 15));
        HBox discontinuedHBox = new HBox(5, continueProductButton(product), removeProductButton(product, "Discontinued"));
        discontinuedHBox.setAlignment(Pos.CENTER);

        HBox continuedHBox = new HBox(5, editProductButton(product), removeProductButton(product, "Continued"));
        continuedHBox.setAlignment(Pos.CENTER);


        Label label = new Label("Discontinued");
        label.setTextFill(Paint.valueOf("red"));

        vBox.getChildren().addAll(productImage, productName, inventoryMode ?
                                                         product.getStatus().equals("Discontinued") ?
                                                                 discontinuedHBox : (onEditStage && currentlyEditingProduct != null && currentlyEditingProduct.equals(product)) ?
                                                                 cancelButton(product) : continuedHBox
                                                                    : productPrice);
        container.getChildren().addAll(vBox, product.getStatus().equals("Discontinued") ? label : new Label());
        if (product.getStatus().equals("Discontinued") && !inventoryMode){
            container.setDisable(true);
            container.setOpacity(0.8);
        }

        if (!inventoryMode) {
            container.setOnMouseClicked(event -> {
                receiptController.setProduct(Optional.of(product));
                container.setDisable(true);
                container.setOpacity(0.8);

                disabledProductBoxes.put(product.getName(), container);
                productsInReceipt.add(product.getName());

                if (!productsInReceipt.contains(product.getName())) {
                    productsInReceipt.add(product.getName());

                    container.setDisable(true);
                    container.setOpacity(0.8);
                }

            });
        }


        container.setCursor(inventoryMode ? Cursor.DEFAULT : Cursor.HAND);
        return container;
    }

    public void enableProductBox(String productName) {
        StackPane box = disabledProductBoxes.get(productName);
        if (box != null) {
            box.setDisable(false);
            box.setOpacity(1.0);
            disabledProductBoxes.remove(productName); // clean up
        }
    }


    // menu grid
    public void createGrid(String categories) throws IOException {
        this.currentCategory = categories;
        gridPane.getChildren().clear();

        productService.applicationContext = applicationContext;

        List<Product> products;

        if (categories.equals("all")) {
            products = productService.findAll();
        }
        else {
            products = switch (categories) {
                case "Blends" -> productService.findProductByCategory("Blend");
                case "Coffees" -> productService.findProductByCategory("Coffee");
                case "Refresher" -> productService.findProductByCategory("Refresher");
                case "Waffles" ->  productService.findProductByCategory("Waffle");
                case "Smoothies" -> productService.findProductByCategory("Smoothie");
                case "Chia Puddings" -> productService.findProductByCategory("Chia Pudding");
                default -> productService.findAll();
            };
        }


        int columns = 6;
        int col = 0;
        int row = 0;

        gridPane.getChildren().clear();

        for (Product product : products) {
            StackPane productBox = createProductBox(product);
            gridPane.add(productBox, col, row);

            if (productsInReceipt.contains(product.getName())) {
                productBox.setDisable(true);
                productBox.setOpacity(0.8);
            }
            else {
                productBox.setDisable(false);
                productBox.setOpacity(1.0);
            }
            col++;
            if (col == columns) {
                col = 0;
                row++; // automatically go to next row
            }
        }

    }
    public void refreshGrid() throws IOException {
        allButtonAction();
        scrollPane.setContent(gridPane);
    }

    // menu filter buttons
    public void activeButton(String button){
        switch (button){
            case "all":
                all.setStyle("-fx-background-color: grey;" + "-fx-border-color: grey;" + "-fx-text-fill: white;");
                blendsButton.setStyle("-fx-background-color: white;" + "-fx-border-color: grey;");
                coffeesButton.setStyle("-fx-background-color: white;" + "-fx-border-color: grey;");
                refresherButton.setStyle("-fx-background-color: white;" + "-fx-border-color: grey;");
                wafflesButton.setStyle("-fx-background-color: white;" + "-fx-border-color: grey;");
                smoothiesButton.setStyle("-fx-background-color: white;" + "-fx-border-color: grey;");
                chiaPuddingsButton.setStyle("-fx-background-color: white;" + "-fx-border-color: grey;");
                break;
            case "Blends":
                all.setStyle("-fx-background-color: white;" + "-fx-border-color: grey;");
                blendsButton.setStyle("-fx-background-color: grey;" + "-fx-border-color: grey;" + "-fx-text-fill: white;");
                coffeesButton.setStyle("-fx-background-color: white;" + "-fx-border-color: grey;");
                refresherButton.setStyle("-fx-background-color: white;" + "-fx-border-color: grey;");
                wafflesButton.setStyle("-fx-background-color: white;" + "-fx-border-color: grey;");
                smoothiesButton.setStyle("-fx-background-color: white;" + "-fx-border-color: grey;");
                chiaPuddingsButton.setStyle("-fx-background-color: white;" + "-fx-border-color: grey;");
                break;
            case "Coffees":
                all.setStyle("-fx-background-color: white;" + "-fx-border-color: grey;");
                blendsButton.setStyle("-fx-background-color: white;" + "-fx-border-color: grey;");
                coffeesButton.setStyle("-fx-background-color: grey;" + "-fx-border-color: grey;" + "-fx-text-fill: white;");
                refresherButton.setStyle("-fx-background-color: white;" + "-fx-border-color: grey;");
                wafflesButton.setStyle("-fx-background-color: white;" + "-fx-border-color: grey;");
                smoothiesButton.setStyle("-fx-background-color: white;" + "-fx-border-color: grey;");
                chiaPuddingsButton.setStyle("-fx-background-color: white;" + "-fx-border-color: grey;");
                break;
            case "Refresher":
                all.setStyle("-fx-background-color: white;" + "-fx-border-color: grey;");
                blendsButton.setStyle("-fx-background-color: white;" + "-fx-border-color: grey;");
                coffeesButton.setStyle("-fx-background-color: white;" + "-fx-border-color: grey;");
                refresherButton.setStyle("-fx-background-color: grey;" + "-fx-border-color: grey;" + "-fx-text-fill: white;");
                wafflesButton.setStyle("-fx-background-color: white;" + "-fx-border-color: grey;");
                smoothiesButton.setStyle("-fx-background-color: white;" + "-fx-border-color: grey;");
                chiaPuddingsButton.setStyle("-fx-background-color: white;" + "-fx-border-color: grey;");
                break;
            case "Waffles":
                all.setStyle("-fx-background-color: white;" + "-fx-border-color: grey;");
                blendsButton.setStyle("-fx-background-color: white;" + "-fx-border-color: grey;");
                coffeesButton.setStyle("-fx-background-color: white;" + "-fx-border-color: grey;");
                refresherButton.setStyle("-fx-background-color: white;" + "-fx-border-color: grey;");
                wafflesButton.setStyle("-fx-background-color: grey;" + "-fx-border-color: grey;" + "-fx-text-fill: white;");
                smoothiesButton.setStyle("-fx-background-color: white;" + "-fx-border-color: grey;");
                chiaPuddingsButton.setStyle("-fx-background-color: white;" + "-fx-border-color: grey;");
                break;
            case "Smoothies":
                all.setStyle("-fx-background-color: white;" + "-fx-border-color: grey;");
                blendsButton.setStyle("-fx-background-color: white;" + "-fx-border-color: grey;");
                coffeesButton.setStyle("-fx-background-color: white;" + "-fx-border-color: grey;");
                refresherButton.setStyle("-fx-background-color: white;" + "-fx-border-color: grey;");
                wafflesButton.setStyle("-fx-background-color: white;" + "-fx-border-color: grey;");
                smoothiesButton.setStyle("-fx-background-color: grey;" + "-fx-border-color: grey;" + "-fx-text-fill: white;");
                chiaPuddingsButton.setStyle("-fx-background-color: white;" + "-fx-border-color: grey;");
                break;
            case "Chia Puddings":
                all.setStyle("-fx-background-color: white;" + "-fx-border-color: grey;");
                blendsButton.setStyle("-fx-background-color: white;" + "-fx-border-color: grey;");
                coffeesButton.setStyle("-fx-background-color: white;" + "-fx-border-color: grey;");
                refresherButton.setStyle("-fx-background-color: white;" + "-fx-border-color: grey;");
                wafflesButton.setStyle("-fx-background-color: white;" + "-fx-border-color: grey;");
                smoothiesButton.setStyle("-fx-background-color: white;" + "-fx-border-color: grey;");
                chiaPuddingsButton.setStyle("-fx-background-color: grey;" + "-fx-border-color: grey;" + "-fx-text-fill: white;");
                break;

        }
    }

    public void allButtonAction() throws IOException {
        activeButton("all");
        createGrid("all");
    }
    public void blendsButtonAction() throws IOException {
        activeButton("Blends");
        createGrid("Blends");
    }
    public void coffeesButtonAction() throws IOException {
        activeButton("Coffees");
        createGrid("Coffees");
    }
    public void refresherButtonAction() throws IOException {
        activeButton("Refresher");
        createGrid("Refresher");
    }
    public void wafflesButtonAction() throws IOException {
        activeButton("Waffles");
        createGrid("Waffles");
    }
    public void smoothiesButtonAction() throws IOException {
        activeButton("Smoothies");
        createGrid("Smoothies");
    }
    public void chiaPuddingsButtonAction() throws IOException {
        activeButton("Chia Puddings");
        createGrid("Chia Puddings");
    }

    @Override
    public void onProductAdded() {
        try {
            refreshGrid();
        }
        catch (IOException e) {
            e.getCause();
        }
    }

}
