package com.example.chapter2.view;

import com.example.chapter2.controller.AllEventHandlers;
import com.example.chapter2.controller.draw.DrawGraphTask;
import com.example.chapter2.model.Currency;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;

import java.util.concurrent.*;


public class CurrencyPane extends BorderPane {
    private Currency currency;
 private Button watch;
    private Button delete;
    private Button unwatch;
 public CurrencyPane(Currency currency) {
         this.watch = new Button("Watch");
     this.delete = new Button("Delete");
     this.unwatch = new Button("Unwatch");
     this.watch.setOnAction(new EventHandler<ActionEvent>() {
 @Override
 public void handle(ActionEvent event) {
              AllEventHandlers.onWatch(currency.getShortCode());
              }
 });
      this.delete.setOnAction(new EventHandler<ActionEvent>() {
 @Override
 public void handle(ActionEvent event) {
              AllEventHandlers.onDelete(currency.getShortCode());
              }
});
         this.setPadding(new Insets(0));
         this.setPrefSize(640,300);
         this.setStyle("-fx-border-color: black");
     this.unwatch.setOnAction(event -> {
         currency.setWatch(false);
         currency.setWatchRate(0.0);
         try {
             refreshPane(currency);
         } catch (ExecutionException e) {
             throw new RuntimeException(e);
         } catch (InterruptedException e) {
             throw new RuntimeException(e);
         }
     });
         try {
          this.refreshPane(currency);
          } catch (ExecutionException e) {
          System.out.println("Encountered an execution exception.");
          } catch (InterruptedException e) {
          System.out.println("Encountered an interupted exception.");
          }


         }

     public void refreshPane(Currency currency) throws ExecutionException,
             InterruptedException {
          this.currency = currency;
          //
         ExecutorService executorService = Executors.newFixedThreadPool(2);
         Future<Pane> infoPaneFuture = executorService.submit(new GenerateInfoPaneTask(currency));
         Future<Pane> graphPaneFuture = executorService.submit(new GenerateGraphPaneTask(currency));

         Pane currencyInfo = infoPaneFuture.get();
         Pane currencyGraph = graphPaneFuture.get();
         //
         // Pane currencyInfo = genInfoPane();
          FutureTask futureTask = new FutureTask<VBox>(new DrawGraphTask(currency));
          ExecutorService executor = Executors.newSingleThreadExecutor();
          executor.execute(futureTask);
          //VBox currencyGraph = (VBox) futureTask.get();
          Pane topArea = genTopArea();
          this.setTop(topArea);
          this.setLeft(currencyInfo);
          this.setCenter(currencyGraph);
          }

 private Pane genInfoPane() {
         VBox currencyInfoPane = new VBox(10);
         currencyInfoPane.setPadding(new Insets(5, 25, 5, 25));
         currencyInfoPane.setAlignment(Pos.CENTER);
         Label exchangeString = new Label("");
         Label watchString = new Label("");
         exchangeString.setStyle("-fx-font-size: 20;");
         watchString.setStyle("-fx-font-size: 14;");
         if (this.currency != null) {
             exchangeString.setText(String.format("%s: %.4f",this.currency.getShortCode(),this.currency.getCurrent().getRate()));
             if(this.currency.getWatch() == true) {
                 watchString.setText(String.format("(Watch @%.4f)",this.currency.getWatchRate()));
                 }
             }
         currencyInfoPane.getChildren().addAll(exchangeString,watchString);
         return currencyInfoPane;
         }
 private HBox genTopArea() {
         HBox topArea = new HBox(10);
         topArea.setPadding(new Insets(5));
     topArea.getChildren().addAll(watch, delete);
         ((HBox) topArea).setAlignment(Pos.CENTER_RIGHT);
         return topArea;
         }
         //
         private class GenerateInfoPaneTask implements Callable<Pane> {
             private Currency currency;

             public GenerateInfoPaneTask(Currency currency) {
                 this.currency = currency;
             }

             @Override
             public Pane call() {
                 return genInfoPane();
             }
         }

    private class GenerateGraphPaneTask implements Callable<Pane> {
        private Currency currency;

        public GenerateGraphPaneTask(Currency currency) {
            this.currency = currency;
        }

        @Override
        public Pane call() {
            try {
                FutureTask<VBox> futureTask = new FutureTask<>(new DrawGraphTask(currency));
                ExecutorService executor = Executors.newSingleThreadExecutor();
                executor.execute(futureTask);
                return futureTask.get();
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
            return new VBox(); // Return an empty VBox in case of error
        }
    }

    //
    }

