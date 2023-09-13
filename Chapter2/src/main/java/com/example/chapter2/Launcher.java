package com.example.chapter2;

import com.example.chapter2.controller.Initialize;
import com.example.chapter2.controller.RefreshTask;
import com.example.chapter2.model.Currency;
import com.example.chapter2.view.CurrencyPane;
import com.example.chapter2.view.CurrencyParentPane;
import com.example.chapter2.view.TopPane;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class Launcher extends Application {
 private static Stage primaryStage;
 private static Scene mainScene;
 private static FlowPane mainPane;
 private static TopPane topPane;
    private static CurrencyPane currencyPane;
    private static ArrayList<Currency> currency;
 private static CurrencyParentPane currencyParentPane;
 private static ArrayList<Currency> currencyList;
@Override
public void start(Stage primaryStage) throws ExecutionException,
        InterruptedException {
         this.primaryStage = primaryStage;
         this.primaryStage.setTitle("Currency Watcher");
         this.currency = Initialize.initialize_app();
         this.currencyList = Initialize.initialize_app();
         initMainPane();
         mainScene = new Scene(mainPane);
         this.primaryStage.setScene(mainScene);
         this.primaryStage.show();
         RefreshTask r = new RefreshTask();
         Thread th = new Thread(r);
         th.setDaemon(true);
         th.start();
         }
    public void initMainPane() throws ExecutionException, InterruptedException {
         mainPane = new FlowPane();
         topPane = new TopPane();
        currencyParentPane = new CurrencyParentPane(this.currencyList);
         mainPane.getChildren().add(topPane);
        mainPane.getChildren().add(currencyParentPane);
         }
    public static void refreshPane() throws InterruptedException, ExecutionException
    {
         topPane.refreshPane();
        currencyParentPane.refreshPane(currencyList);
         primaryStage.sizeToScene();
         }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void setPrimaryStage(Stage primaryStage) {
        Launcher.primaryStage = primaryStage;
    }

    public static Scene getMainScene() {
        return mainScene;
    }

    public static void setMainScene(Scene mainScene) {
        Launcher.mainScene = mainScene;
    }

    public static FlowPane getMainPane() {
        return mainPane;
    }

    public static void setMainPane(FlowPane mainPane) {
        Launcher.mainPane = mainPane;
    }

    public static TopPane getTopPane() {
        return topPane;
    }

    public static void setTopPane(TopPane topPane) {
        Launcher.topPane = topPane;
    }

    public static CurrencyPane getCurrencyPane() {
        return currencyPane;
    }

    public static void setCurrencyPane(CurrencyPane currencyPane) {
        Launcher.currencyPane = currencyPane;
    }

    public static ArrayList<Currency> getCurrency() {
        return currency;
    }

    public static void setCurrency(Currency currency) {
        Launcher.currency = currency;
    }

    public static CurrencyParentPane getCurrencyParentPane() {
        return currencyParentPane;
    }

    public static void setCurrencyParentPane(CurrencyParentPane currencyParentPane) {
        Launcher.currencyParentPane = currencyParentPane;
    }

    public static ArrayList<Currency> getCurrencyList() {
        return currencyList;
    }

    public static void setCurrencyList(ArrayList<Currency> currencyList) {
        Launcher.currencyList = currencyList;
    }
}
