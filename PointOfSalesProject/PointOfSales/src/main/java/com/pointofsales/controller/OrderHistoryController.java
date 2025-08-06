package com.pointofsales.controller;


import com.pointofsales.entity.Receipt;
import com.pointofsales.entity.pendingorders_completed.Order;
import com.pointofsales.services.OrderService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Controller;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;

@Controller
public class OrderHistoryController {

    public ConfigurableApplicationContext applicationContext;

    public OrderService orderService;

    @Autowired
    public OrderHistoryController(ConfigurableApplicationContext applicationContext, OrderService orderService) {
        this.applicationContext = applicationContext;
        this.orderService = orderService;
    }

    // pending orders
    @FXML public TableView<Order> pendingOrderTable;
    @FXML public TableColumn<Order, Integer> transactionColumn;
    @FXML public TableColumn<Order, LocalDate> dateColumn;
    @FXML public TableColumn<Order, LocalTime> timeColumn;
    @FXML public TableColumn<Order, Double> totalColumn;

    // completed orders
    @FXML public TableView<Order> completedOrderTable;
    @FXML public TableColumn<Order, Integer> COTransactionColumn;
    @FXML public TableColumn<Order, LocalDate> CODateColumn;
    @FXML public TableColumn<Order, LocalTime> COTimeColumn;
    @FXML public TableColumn<Order, Double> COTotalColumn;

    @FXML public SplitPane splitPane;

    public void pendingOrderColumnSetUp(){
        pendingOrderTable.setEditable(false);
        pendingOrderTable.setSelectionModel(null);
        pendingOrderTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);

        transactionColumn.setCellValueFactory(new PropertyValueFactory<>("transactionNumber"));
        transactionColumn.setStyle("-fx-alignment: CENTER;");

        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        dateColumn.setStyle("-fx-alignment: CENTER;");

        timeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));
        timeColumn.setStyle("-fx-alignment: CENTER;");

        totalColumn.setCellValueFactory(new PropertyValueFactory<>("total"));
        totalColumn.setStyle("-fx-alignment: CENTER;");
    }
    public void initialize(){
        pendingOrderColumnSetUp();


        List<Order> order = orderService.findAllByDateAndTime();
        for (Order o : order){
            pendingOrderTable.getItems().add(o);
        }


    }

}
