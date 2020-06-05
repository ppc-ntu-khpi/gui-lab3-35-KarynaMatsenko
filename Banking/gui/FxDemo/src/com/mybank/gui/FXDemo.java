package com.mybank.gui;

import com.mybank.data.DataSource;
import com.mybank.domain.*;
import java.io.IOException;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 *
 * @author Alexander 'Taurus' Babich
 */
public class FXDemo extends Application {

    private Text title;
    private Text details;
    private ComboBox clients;

    @Override
    public void start(Stage primaryStage) {

        BorderPane border = new BorderPane();
        HBox hbox = addHBox();
        border.setTop(hbox);
        border.setLeft(addVBox());
        addStackPane(hbox);

        Scene scene = new Scene(border, 500, 900);

        primaryStage.setTitle("MyBank Clients");
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    public VBox addVBox() {
        VBox vbox = new VBox();
        vbox.setPadding(new Insets(10));
        vbox.setSpacing(8);

        title = new Text("Client Name");
        title.setFont(Font.font("Arial",18));
        vbox.getChildren().add(title);

        return vbox;
    }

    public HBox addHBox() {
        HBox hbox = new HBox();
        hbox.setPadding(new Insets(15, 12, 15, 12));
        hbox.setSpacing(10);
        hbox.setStyle("-fx-background-color: #336699;");

        ObservableList<String> items = FXCollections.observableArrayList();
        for (int i = 0; i < Bank.getNumberOfCustomers(); i++) {
            items.add(Bank.getCustomer(i).getLastName() + ", " + Bank.getCustomer(i).getFirstName());
        }

        clients = new ComboBox(items);
        clients.setPrefSize(150, 20);
        clients.setPromptText("Click to choose...");

        Button buttonShow = new Button("Show");
        buttonShow.setPrefSize(100, 20);

        buttonShow.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                try {
                   
                   StringBuilder repcust = new StringBuilder();
                
                   int custVal = clients.getItems().indexOf(clients.getValue());
                   
                   Customer current = Bank.getCustomer(custVal);
                   
                   repcust.append(current.getFirstName()).append(", ").append(current.getLastName()).append("\n___________________________\n\n");                                                  

                        for (int k = 0; k < current.getNumberOfAccounts(); k++) {
                        
                            Account account = current.getAccount(k);
            
                            repcust.append("Account:   ").append("#").append(k).append("\n").append("Acc Type:  ").append(account instanceof CheckingAccount ? "Checking " : "Savings ").append("\n").append("Balance:  $").append(account.getBalance()).append("\n\n");
                    
                    }title.setText(repcust.toString());
                 
                } catch (Exception e) {
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error getting client...");
                    // Header Text: null
                    alert.setHeaderText(null);
                    String details = e.getMessage() != null ? e.getMessage() : "You need to choose a client first!";
                    alert.setContentText("Choose the correct client");
                    alert.showAndWait();
                }
            }
        });
        
        Button buttonReport = new Button("Report");
        buttonReport.setPrefSize(100, 20);
        
        buttonReport.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                
               StringBuilder repcust = new StringBuilder();

                for(int j = 0; j <Bank.getNumberOfCustomers(); j++ ){
                    
                    Customer current = Bank.getCustomer(j);
                    
                    repcust.append(current.getFirstName()).append(", ").append(current.getLastName()).append("\n___________________________\n\n");
      
                 for (int i = 0; i < current.getNumberOfAccounts(); i++) {
                        
                        Account account = current.getAccount(i);
                        
                        repcust.append("Account:   ").append("#").append(i).append("\n").append("Acc Type:  ").append(account instanceof CheckingAccount ? "Checking " : "Savings ").append("\n").append("Balance:  $").append(account.getBalance()).append("\n\n");
                        
                    }repcust.append("\n");
                    
                }title.setText(repcust.toString());
            }
        });

        hbox.getChildren().addAll(clients, buttonShow, buttonReport);

        return hbox;
    }

    public void addStackPane(HBox hb) {
        StackPane stack = new StackPane();
        Rectangle helpIcon = new Rectangle(30.0, 25.0);
        helpIcon.setFill(new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE,
                new Stop[]{
                    new Stop(0, Color.web("#4977A3")),
                    new Stop(0.5, Color.web("#B0C6DA")),
                    new Stop(1, Color.web("#9CB6CF")),}));
        helpIcon.setStroke(Color.web("#D0E6FA"));
        helpIcon.setArcHeight(3.5);
        helpIcon.setArcWidth(3.5);

        Text helpText = new Text("?");
        helpText.setFont(Font.font("Verdana", FontWeight.BOLD, 18));
        helpText.setFill(Color.WHITE);
        helpText.setStroke(Color.web("#7080A0"));

        helpIcon.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                ShowAboutInfo();
            }
        });

        helpText.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                ShowAboutInfo();
            }
        });
        
        stack.getChildren().addAll(helpIcon, helpText);
        stack.setAlignment(Pos.CENTER_RIGHT);     
        StackPane.setMargin(helpText, new Insets(0, 10, 0, 0)); 

        hb.getChildren().add(stack);            
        HBox.setHgrow(stack, Priority.ALWAYS);    
    }

    private void ShowAboutInfo() {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("About");
        // Header Text: null
        alert.setHeaderText(null);
        alert.setContentText("Just a simple JavaFX demo.\nCopyright \u00A9 2019 Alexander \'Taurus\' Babich");
        alert.showAndWait();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        
        DataSource dataSource = new DataSource("F:/Java/Banking/FxDemo/src/com/mybank/data/test.dat");
        
        dataSource.loadData();

        launch(args);
    }

}