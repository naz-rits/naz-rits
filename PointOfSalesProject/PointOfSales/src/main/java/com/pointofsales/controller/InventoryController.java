package com.pointofsales.controller;

import com.pointofsales.entity.Ingredient;
import com.pointofsales.listener.IngredientAddedListener;
import com.pointofsales.services.IngredientService;
import com.pointofsales.services.ProductService;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.util.List;

@Controller
public class InventoryController implements IngredientAddedListener {


    private final ProductService productService;
    public ConfigurableApplicationContext applicationContext;

    @Setter
    public AddIngredientController addIngredientController;

    public IngredientService ingredientService;

    @FXML public ScrollPane scrollPane;
    @FXML public GridPane gridPane;

    @Autowired
    public InventoryController(ConfigurableApplicationContext applicationContext, IngredientService ingredientService, ProductService productService) {
        this.applicationContext = applicationContext;
        this.ingredientService = ingredientService;
        this.productService = productService;
    }

    public void initialize() throws IOException {
        createGrid();
        refreshGrid();
    }
    // getter connected to the menu controller
    static ImageView getImageView(String path) {
        ImageView imageView = new ImageView(new Image("file:" + path, true));
        imageView.setFitHeight(120);
        imageView.setFitWidth(98);
        imageView.setLayoutX(21);
        imageView.setLayoutY(-7);
        imageView.setPickOnBounds(true);
        imageView.setPreserveRatio(true);
        return imageView;
    }
    static Label getIngredientName(String ingredientName1){
        Label label = new Label(ingredientName1);
        label.setAlignment(Pos.CENTER);
        label.setTextAlignment(TextAlignment.CENTER);
        label.setContentDisplay(ContentDisplay.CENTER);
        label.setLayoutX(28);
        label.setLayoutY(109);
        label.setPrefHeight(54);
        label.setPrefWidth(50);
        label.setWrapText(true);
        label.setText(ingredientName1);

        return label;
    }


    public ImageView ingredientImage(String path) {
        return getImageView(path);
    }
    public Label ingredientName(String ingredientName){
        return  getIngredientName(ingredientName);
    }
    public Label expirationDate(String expirationDate){
        Label label = new Label(expirationDate);
        label.setAlignment(Pos.CENTER);

        return label;
    }

    public StackPane createIngredientBox(Ingredient ingredient){
        StackPane container = new StackPane();
        container.setPrefSize(200, 200);
        container.setStyle(
                "-fx-background-color: #F2F2F2;" +
                        "-fx-background-radius: 30;" +
                        "-fx-border-radius: 30;" +
                        "-fx-border-color: grey;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 10, 0.5, 0, 4);"
        );

        VBox vBox = new VBox();
        vBox.setAlignment(Pos.CENTER);

        ImageView ingredientImage = ingredientImage(ingredient.getImageUrl());

        Label ingredientName = ingredientName(ingredient.getName());
        ingredientName.setWrapText(true);
        ingredientName.setMaxWidth(150);
        ingredientName.setAlignment(Pos.CENTER);

        vBox.getChildren().addAll(ingredientImage, ingredientName);
        container.getChildren().add(vBox);
        return container;
    }

    public void createGrid() {
        gridPane.getChildren().clear();

        List<Ingredient> ingredients = ingredientService.getAllIngredients();
        int columns = 6;
        int col = 0;
        int row = 0;

        for (Ingredient ingredient : ingredients) {
            StackPane productBox = createIngredientBox(ingredient);
            gridPane.add(productBox, col, row);

            col++;
            if (col == columns) {
                col = 0;
                row++; // automatically go to next row
            }
        }
    }

    public void refreshGrid() throws IOException {
        createGrid();
        scrollPane.setContent(gridPane);
    }

    @Override
    public void onIngredientAdded() {
        try {
            refreshGrid();
        } catch (IOException e) {
            e.getCause();
        }
    }
}
