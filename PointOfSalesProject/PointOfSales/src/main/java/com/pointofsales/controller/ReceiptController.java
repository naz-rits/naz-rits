package com.pointofsales.controller;

import com.pointofsales.entity.Cashier;
import com.pointofsales.entity.Product;
import com.pointofsales.entity.Receipt;
import com.pointofsales.entity.Total;
import com.pointofsales.entity.pendingorders_completed.Order;
import com.pointofsales.services.CashierService;
import com.pointofsales.services.OrderService;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.fxml.FXML;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Controller;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;

@Controller
public class ReceiptController {

    @Setter
    public ConfigurableApplicationContext applicationContext;

    @Setter
    public MenuController menuController;

    public OrderService orderService;
    public CashierService cashierService;

    @Autowired
    public ReceiptController(ConfigurableApplicationContext applicationContext,  OrderService orderService, CashierService cashierService) {
        this.applicationContext = applicationContext;
        this.orderService = orderService;
        this.cashierService = cashierService;
    }

    @FXML public Label customerNameLabel;
    @FXML public TextField customerNameTextfield;

    @FXML public GridPane gridPane;
    @FXML public ScrollPane scrollPane;
    @FXML public Label totalPrice;
    @FXML public Button proceedButton;
    @FXML public TextField paymentTextfield;
    @FXML public Button clearButton;

    public TableView<Receipt> receiptTableView = new TableView<>();
    public TableView<Total> totalTableView = new TableView<>();
    public TableColumn<Total, String> totalItemsColumn = new TableColumn<>();
    public TableColumn<Total, String> totalAmountColumn = new TableColumn<>();

    public DoubleProperty currentTotal = new SimpleDoubleProperty(0.0);
    public int totalQuantity;
    public double changeLabel;

    public Optional<Product> product = Optional.empty();

    public void setProduct(Optional<Product> product) {
        this.product = product;
        setGridPane();
    }
    public void showAlert(String alertMessage, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setHeaderText(null);
        alert.setContentText(alertMessage);
        alert.showAndWait();
    }
    public Long generateRandomTransactionNumber() {
        Random random = new Random();
        long number = random.nextInt(900000) + 100000; // ensures 6-digit number (100000–999999)
        return number;
    }


    public Label getShopDescription(){
        Label label = new Label("Healthy Shakes and Protein Snacks");
        label.setFont(Font.font("Courier", FontWeight.NORMAL, 16));

        return label;
    }
    public Label getShopAddress() {
        Label label = new Label("Chief E. Martin St. Caridad\n            Cavite City\n     Cavite, Philippines");
        label.setFont(Font.font("Courier", FontWeight.NORMAL, 12));

        return label;
    }
    public ImageView getLogo() throws FileNotFoundException {
        Image image = new Image(new FileInputStream("src/main/resources/images/ui-images/logo.png"));
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(100); // optional
        imageView.setPreserveRatio(true);
        return imageView;
    }

    public void initialize() {
        totalPrice.textProperty().bind(currentTotal.asString("₱%.2f"));
        proceedButton.setOnAction(event -> {
        receiptTableView.setPrefHeight(150);
        Total total = new Total();
        total.setPaymentAmount(paymentTextfield.getText());

        totalReceipt(total);
            try {
                proceedButtonAction();
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public Button removeButton(HBox hBox, double price) {
        Button button = new Button("-");
        button.setStyle("-fx-background-color: red; -fx-border-color: black;");
        button.setScaleY(0.7);
        button.setCursor(Cursor.HAND);

        button.setOnAction(event -> {
            totalTableView.refresh();
            // Remove from main display
            gridPane.getChildren().remove(hBox);


            // Get quantity from TextField
            TextField quantityField = (TextField) hBox.getChildren().stream()
                    .filter(n -> n instanceof HBox) // the quantity HBox
                    .flatMap(n -> ((HBox) n).getChildren().stream())
                    .filter(n -> n instanceof TextField)
                    .findFirst()
                    .orElse(null);

            int removedQty = 1;
            if (quantityField != null) {
                removedQty = Integer.parseInt(quantityField.getText());
                totalQuantity -= removedQty;
            }
            totalItemsColumn.setText("Qty: " + totalQuantity);
            // Get product name
            Label nameLabel = (Label) hBox.getChildren().get(1);
            String productName = nameLabel.getText();
            menuController.enableProductBox(productName);
            menuController.productsInReceipt.remove(productName);

            // Remove matching Receipt from table
            receiptTableView.getItems().removeIf(receipt ->
                    receipt.getName().startsWith(productName)
            );

            // Update total
            currentTotal.set(currentTotal.get() - price * removedQty);
            changeLabel = Double.parseDouble(paymentTextfield.getText()) - currentTotal.get();
            totalAmountColumn.setText(Double.toString(currentTotal.get()));

            try {
                menuController.createGrid(menuController.getCurrentCategory());
            } catch (IOException e) {
                e.printStackTrace();
            }


        });

        return button;
    }


    public Label productName () {
        Label label = new Label();
        label.setFont(Font.font("Arial", FontWeight.NORMAL, 15));
        product.ifPresent(value -> label.setText(value.getName()));
        label.setTranslateY(2.3);
        return label;
    }
    public HBox productHBox() {
        HBox hBox = new HBox(15);
        hBox.setAlignment(Pos.CENTER_LEFT);

        Label nameLabel = productName();


        AtomicReference<Double> basePrice = new AtomicReference<>(product.map(Product::getPrice).orElse(0.0));
        int initialQty = 1;
        double unitPrice = basePrice.get();


        Label productPriceLabel = new Label("₱" + unitPrice * initialQty);
        productPriceLabel.setFont(Font.font("Arial", FontWeight.BOLD, 15));
        productPriceLabel.setStyle("-fx-text-fill: black;");


        TextField quantityField = new TextField(String.valueOf(initialQty));
        quantityField.setPrefWidth(40);

        Button increaseButton = new Button("+");
        increaseButton.setStyle("-fx-background-color: green; -fx-text-fill: white; -fx-border-color: black;");
        increaseButton.setCursor(Cursor.HAND);

        Button decreaseButton = new Button("-");
        decreaseButton.setStyle("-fx-background-color: red; -fx-text-fill: white; -fx-border-color: black;");
        decreaseButton.setCursor(Cursor.HAND);


        Receipt receipt = new Receipt();
        receipt.setName(nameLabel.getText() + " (" + product.get().getCategory() + ")");
        receipt.setQuantity(initialQty);
        receipt.setAmount(unitPrice);


        increaseButton.setOnAction(event -> {
            int newQty = Integer.parseInt(quantityField.getText()) + 1;
            quantityField.setText(String.valueOf(newQty));
            currentTotal.set(currentTotal.get() + unitPrice);
            totalQuantity += 1;


            increaseOrDecrease(unitPrice, productPriceLabel, receipt, newQty);
        });


        decreaseButton.setOnAction(event -> {
            int currentQty = Integer.parseInt(quantityField.getText());
            if (currentQty > 1) {
                int newQty = currentQty - 1;
                quantityField.setText(String.valueOf(newQty));
                currentTotal.set(currentTotal.get() - unitPrice);
                totalQuantity -= 1;


                increaseOrDecrease(unitPrice, productPriceLabel, receipt, newQty);
            }
        });


        Button removeBtn = removeButton(hBox, unitPrice);


        HBox quantityBox = new HBox(5, increaseButton, quantityField, decreaseButton);
        quantityBox.setAlignment(Pos.CENTER_LEFT);

        hBox.getChildren().addAll(removeBtn, nameLabel, productPriceLabel, quantityBox);

        currentTotal.set(currentTotal.get() + (unitPrice * initialQty));
        totalQuantity += initialQty;



        receiptTable(receipt);
        return hBox;
    }

    private void increaseOrDecrease(double unitPrice, Label productPriceLabel, Receipt receipt, int newQty) {
        receipt.setQuantity(newQty);
        receipt.setAmount(unitPrice * newQty);
        productPriceLabel.setText("₱" + unitPrice * newQty);
        receiptTableView.refresh();
        totalTableView.refresh();

        totalItemsColumn.setText("Qty: " + totalQuantity);
        totalAmountColumn.setText(Double.toString(currentTotal.get()));
        changeLabel = Double.parseDouble(paymentTextfield.getText()) - currentTotal.get();
    }

    public void clearButtonAction() throws IOException {
        gridPane.getChildren().clear();

        menuController.productsInReceipt.clear();
        menuController.createGrid(menuController.getCurrentCategory());

        currentTotal.set(0);
        totalQuantity = 0;
        receiptTableView.getItems().clear();
        paymentTextfield.clear();
        customerNameTextfield.clear();
    }
    public void setGridPane(){
        gridPane.setVgap(10);
        int nextRow = gridPane.getRowCount(); // auto-increment row index
        gridPane.add(productHBox(), 0, nextRow);
        new Separator(Orientation.HORIZONTAL);

    }

    public Label customerName() {
        Label nameLabel = new Label();
        nameLabel.setText(customerNameTextfield.getText());
        nameLabel.setFont(Font.font("Courier", FontWeight.BOLD, 15));
        nameLabel.setAlignment(Pos.CENTER_RIGHT);
        return nameLabel;
    }
    public Label customerNamePlaceHolder() {
        Label nameLabel = new Label("Customer Name :");
        nameLabel.setFont(Font.font("Courier", FontWeight.NORMAL, 15));
        nameLabel.setAlignment(Pos.CENTER_LEFT);
        return nameLabel;
    }
    public void receiptTable(Receipt receipts) {

        if (receiptTableView.getColumns().isEmpty()) {
            TableColumn<Receipt, String> productColumn = new TableColumn<>("Product");
            productColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
            productColumn.setPrefWidth(150);
            productColumn.setStyle("-fx-alignment: CENTER;");

            TableColumn<Receipt, Integer> quantityColumn = new TableColumn<>("Qty");
            quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
            quantityColumn.setPrefWidth(30);
            quantityColumn.setStyle("-fx-alignment: CENTER;");

            TableColumn<Receipt, Double> amountColumn = new TableColumn<>("Amount");
            amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
            amountColumn.setPrefWidth(100);
            amountColumn.setStyle("-fx-alignment: CENTER;");

            receiptTableView.getColumns().add(productColumn);
            receiptTableView.getColumns().add(quantityColumn);
            receiptTableView.getColumns().add(amountColumn);
        }


        receiptTableView.setEditable(false);
        receiptTableView.setSelectionModel(null);
        receiptTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);


        receiptTableView.getItems().add(receipts);
        receiptTableView.refresh();
    }

    public void totalReceipt(Total total) {

        totalTableView.refresh();
        if (totalTableView.getColumns().isEmpty()) {

            TableColumn<Total, String> totalColumn = new TableColumn<>("TOTAL: ");
            totalColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper("Payment Amount:\nChange:"));
            totalColumn.setPrefWidth(150);
            totalColumn.setStyle("-fx-alignment: CENTER;");

            int quantity = totalQuantity;
            totalItemsColumn = new TableColumn<>("Qty: " + String.valueOf(quantity));
            totalItemsColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper());
            totalItemsColumn.setPrefWidth(30);
            totalItemsColumn.setStyle("-fx-alignment: CENTER;");

            changeLabel = Double.parseDouble(paymentTextfield.getText()) - currentTotal.get();
            totalAmountColumn = new TableColumn<>(String.valueOf(currentTotal.get()));
            totalAmountColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(paymentTextfield.getText() + "\n" + Double.toString(changeLabel)));

            totalAmountColumn.setPrefWidth(100);
            totalAmountColumn.setStyle("-fx-alignment: CENTER;");

            totalTableView.getColumns().add(totalColumn);
            totalTableView.getColumns().add(totalItemsColumn);
            totalTableView.getColumns().add(totalAmountColumn);
        }

        totalTableView.setEditable(false);
        totalTableView.setSelectionModel(null);
        totalTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
        totalTableView.setFixedCellSize(50);
        totalTableView.setPrefHeight(50 + 28);
        totalTableView.getItems().add(total);
        totalTableView.setPlaceholder(new Label(""));
        totalTableView.refresh();
    }
    public void proceedButtonAction() throws FileNotFoundException {
        Stage stage = new Stage();


        VBox fixedReceiptText = new VBox(2, getLogo(), getShopDescription(), getShopAddress());
        fixedReceiptText.setAlignment(Pos.CENTER);

        totalItemsColumn.setText("Qty: " + totalQuantity);
        totalAmountColumn.setText(Double.toString(currentTotal.get()));
        changeLabel = Double.parseDouble(paymentTextfield.getText()) - currentTotal.get();
        
        System.out.println(totalQuantity);
        System.out.println(totalPrice);
        LocalDate currentDate = LocalDate.now();
        LocalTime currentTime = LocalTime.now();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        String currentTimeString = currentTime.format(formatter);

        Button placeOrder = new Button("Place Order");
        placeOrder.setFont(Font.font("Courier", FontWeight.NORMAL, 15));
        placeOrder.setStyle("-fx-alignment: CENTER; -fx-background-color: green; -fx-text-fill: white;");
        placeOrder.setCursor(Cursor.HAND);
        placeOrder.setAlignment(Pos.CENTER);
        placeOrder.setOnAction(event -> {

            Long id = 1L;
            Cashier cashier = cashierService.findCashierById(id);

            Order order = new Order();
            order.setDate(currentDate);
            order.setTime(currentTime);
            order.setTotal(currentTotal.get());
            order.setTransactionNumber(generateRandomTransactionNumber());
            order.setCashier(cashier);

            System.out.println("TXN: " + order.getTransactionNumber());

            orderService.save(order);
            showAlert("Order Placed", Alert.AlertType.INFORMATION);
            Stage stage1 = (Stage) placeOrder.getScene().getWindow();
            stage1.close();
        });

        Button cancelOrder = new Button("Cancel");
        cancelOrder.setFont(Font.font("Courier", FontWeight.NORMAL, 15));
        cancelOrder.setStyle("-fx-alignment: CENTER; -fx-text-fill: white; -fx-background-color: red; -fx-scale-x: 1.3;");
        cancelOrder.setCursor(Cursor.HAND);
        cancelOrder.setAlignment(Pos.CENTER);
        cancelOrder.setOnAction(event -> {
            Stage stage2 = (Stage) cancelOrder.getScene().getWindow();
            totalTableView.getItems().clear();
            stage2.close();
        });

        HBox hBoxButtons = new HBox(30, placeOrder, cancelOrder);
        hBoxButtons.setAlignment(Pos.CENTER);

        HBox hBox = new HBox(20, new Label(currentDate.toString()), new Label(currentTimeString));
        hBox.setAlignment(Pos.CENTER);

        VBox vBox = new VBox(10, fixedReceiptText, new HBox(15, customerNamePlaceHolder(), customerName()),
                receiptTableView, totalTableView, new Separator(Orientation.HORIZONTAL),
               hBox, hBoxButtons);

        vBox.setStyle("-fx-border-color: black; -fx-border-width: 2; -fx-border-radius: 5;");
        Scene scene = new Scene(vBox, 400, 600);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);
        stage.show();
    }


}
