package com.mycompany.hcorevisualizer;

import graph.Graph;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.StackPane;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import data.JsonLoader;
import java.io.IOException;
import javafx.scene.layout.Region;
import javafx.beans.value.ChangeListener;

public class Javafx extends Application {
    private Graph graf;
    private TextField isimAlani;
    private TextField kDegeriAlani;
    private gorsellestirme.Cizim cizici;
    
    @Override
    public void start(Stage stage) {
        try {
            this.graf = JsonLoader.grafOlustur();
            
            Canvas canvas = new Canvas();
            this.cizici = new gorsellestirme.Cizim(graf, canvas);
            
            isimAlani = new TextField();
            isimAlani.setPromptText("Başlangıç düğüm ID...");
            isimAlani.setMaxWidth(200);
            isimAlani.setStyle("-fx-font-size: 13px;");
            
            Button baslatButton = new Button("Başlat");
            baslatButton.setStyle(
                "-fx-background-color: #27ae60; " +
                "-fx-text-fill: white; " +
                "-fx-font-weight: bold; " +
                "-fx-font-size: 13px; " +
                "-fx-padding: 8 20;"
            );
            baslatButton.setOnAction(e -> {
                cizici.grafBaslat(isimAlani.getText());
            });

            kDegeriAlani = new TextField();
            kDegeriAlani.setPromptText("K değeri (örn: 3)");
            kDegeriAlani.setMaxWidth(120);
            kDegeriAlani.setStyle("-fx-font-size: 13px;");
            
            Button kCoreButton = new Button("K-Core Hesapla");
            kCoreButton.setStyle(
                "-fx-background-color: #e67e22; " +
                "-fx-text-fill: white; " +
                "-fx-font-weight: bold; " +
                "-fx-font-size: 13px; " +
                "-fx-padding: 8 20;"
            );
            kCoreButton.setOnAction(e -> {
                try {
                    int k = Integer.parseInt(kDegeriAlani.getText().trim());
                    cizici.kCoreHesapla(k);
                } catch (NumberFormatException ex) {
                    System.out.println("❌ Geçerli bir K değeri girin!");
                }
            });
            
            Button kCoreSifirlaButton = new Button("Temizle");
            kCoreSifirlaButton.setStyle(
                "-fx-background-color: #c0392b; " +
                "-fx-text-fill: white; " +
                "-fx-font-weight: bold; " +
                "-fx-font-size: 13px; " +
                "-fx-padding: 8 20;"
            );
            kCoreSifirlaButton.setOnAction(e -> {
                cizici.kCoreSifirla();
                kDegeriAlani.clear();
            });

            Button bcButton = new Button("BC Hesapla");
            bcButton.setStyle(
                "-fx-background-color: #9b59b6; " +
                "-fx-text-fill: white; " +
                "-fx-font-weight: bold; " +
                "-fx-font-size: 13px; " +
                "-fx-padding: 8 20;"
            );
            bcButton.setOnAction(e -> {
                cizici.betweennessCentralityHesapla();
            });

            Label yardimLabel = new Label("💡 Normal Tık:Tablo | Shift+Tık:Genişlet | Ctrl+Tık:H-core");
            yardimLabel.setStyle(
                "-fx-text-fill: #ecf0f1; " +
                "-fx-font-size: 11px; " +
                "-fx-font-style: italic;"
            );

            HBox baslatPanel = new HBox(10);
            baslatPanel.setAlignment(Pos.CENTER);
            baslatPanel.getChildren().addAll(isimAlani, baslatButton);
            
            HBox kCorePanel = new HBox(10);
            kCorePanel.setAlignment(Pos.CENTER);
            kCorePanel.getChildren().addAll(kDegeriAlani, kCoreButton, kCoreSifirlaButton);
            
            HBox bcPanel = new HBox(10);
            bcPanel.setAlignment(Pos.CENTER);
            bcPanel.getChildren().addAll(bcButton);

            VBox ustKontroller = new VBox(8);
            ustKontroller.setPadding(new Insets(15));
            ustKontroller.setAlignment(Pos.TOP_RIGHT);
            ustKontroller.setPickOnBounds(false);
            ustKontroller.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
            ustKontroller.setStyle(
                "-fx-background-color: rgba(44, 62, 80, 0.85); " +
                "-fx-background-radius: 10; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.4), 10, 0, 0, 2);"
            );
            ustKontroller.getChildren().addAll(baslatPanel, kCorePanel, bcPanel, yardimLabel);
            
            StackPane root = new StackPane();
            
            canvas.widthProperty().bind(root.widthProperty());
            canvas.heightProperty().bind(root.heightProperty());
            
            root.getChildren().addAll(canvas, ustKontroller);
            StackPane.setAlignment(ustKontroller, Pos.TOP_RIGHT);
            StackPane.setMargin(ustKontroller, new Insets(10));
            
            Scene scene = new Scene(root, 1200, 800);
            
            ChangeListener<Number> sizeListener = (obs, oldVal, newVal) -> cizici.ciz();
            
            scene.widthProperty().addListener(sizeListener);
            scene.heightProperty().addListener(sizeListener);
            
            stage.setScene(scene);
            stage.setTitle("🎯 H-Core Visualizer");
            stage.show();
            
            cizici.yenidenciz();
            
        } catch (IOException e) {
            System.err.println("❌ Grafik verisi yüklenemedi: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("❌ Uygulama hatası: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}