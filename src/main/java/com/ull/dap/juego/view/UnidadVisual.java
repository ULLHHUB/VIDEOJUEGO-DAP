package com.ull.dap.juego.view;

import com.ull.dap.juego.model.unidades.Arquero;
import com.ull.dap.juego.model.unidades.Unidad;
import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.geometry.Point2D;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class UnidadVisual extends VBox {
    private Unidad unidad;
    private ImageView imageView;
    private ProgressBar healthBar;

    public UnidadVisual(Unidad unidad) {
        this.unidad = unidad;
        this.unidad.setVisual(this); // Enlazar el modelo con su vista

        String imagePath = "/" + unidad.getImagePath();
        try {
            Image image = new Image(getClass().getResourceAsStream(imagePath));
            imageView = new ImageView(image);
            imageView.setFitWidth(40);
            imageView.setFitHeight(40);
        } catch (Exception e) {
            System.err.println("No se pudo cargar la imagen: " + imagePath);
            // Usar un label como fallback si la imagen no carga
            this.getChildren().add(new Label(unidad.getNombre().substring(0, 1)));
        }

        healthBar = new ProgressBar(1.0);
        healthBar.setPrefWidth(40);

        this.setSpacing(2);
        if (imageView != null) {
            this.getChildren().add(imageView);
        }
        this.getChildren().add(healthBar);
    }

    public Unidad getUnidad() {
        return unidad;
    }

    public void update() {
        if (unidad.getVida() <= 0) {
            animarMuerte();
        } else {
            double healthPercentage = (double) unidad.getVida() / unidad.getVidaMaxima();
            healthBar.setProgress(healthPercentage);

            if (healthPercentage < 0.3) {
                healthBar.setStyle("-fx-accent: red;");
            } else if (healthPercentage < 0.6) {
                healthBar.setStyle("-fx-accent: orange;");
            } else {
                healthBar.setStyle("-fx-accent: #00b300;");
            }
        }
    }

    public void animarAtaque(UnidadVisual objetivo) {
        Point2D posInicial = new Point2D(this.getTranslateX(), this.getTranslateY());
        Point2D posObjetivo = new Point2D(objetivo.getTranslateX(), objetivo.getTranslateY());

        TranslateTransition tt = new TranslateTransition(Duration.millis(200), this);
        tt.setToX(posObjetivo.getX());
        tt.setToY(posObjetivo.getY());
        tt.setAutoReverse(true);
        tt.setCycleCount(2);
        tt.setOnFinished(e -> {
            this.setTranslateX(posInicial.getX());
            this.setTranslateY(posInicial.getY());
        });
        tt.play();
    }

    public void animarMuerte() {
        FadeTransition ft = new FadeTransition(Duration.millis(500), this);
        ft.setFromValue(1.0);
        ft.setToValue(0.0);
        ft.setOnFinished(e -> {
            this.setVisible(false);
        });
        ft.play();
    }

    public void mostrarEfectoEspecial(String tipo) {
        DropShadow glow = new DropShadow();
        glow.setRadius(20);
        glow.setSpread(0.6);

        if ("CRITICO".equals(tipo)) {
            glow.setColor(Color.RED);
        } else if ("VENTAJA".equals(tipo)) {
            glow.setColor(Color.YELLOW);
        }

        this.setEffect(glow);

        // Quitar el efecto después de un corto tiempo
        FadeTransition ft = new FadeTransition(Duration.millis(700), this);
        ft.setFromValue(1.0);
        ft.setToValue(0.99); // No queremos que se desvanezca, solo usar el timeline
        ft.setOnFinished(e -> this.setEffect(null));
        ft.play();
    }
}
