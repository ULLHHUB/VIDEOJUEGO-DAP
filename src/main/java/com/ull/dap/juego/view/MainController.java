package com.ull.dap.juego.view;

import com.ull.dap.juego.control.GestorJuego;
import com.ull.dap.juego.control.GestorSonido;
import com.ull.dap.juego.model.batalla.Batalla1v1;
import com.ull.dap.juego.model.batalla.BatallaMasiva;
import com.ull.dap.juego.model.batalla.BatallaTemplate;
import com.ull.dap.juego.model.factoria.CivilizacionFactory;
import com.ull.dap.juego.model.unidades.Unidad;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class MainController {

    @FXML
    private ComboBox<String> comboCiv1;
    @FXML
    private ComboBox<String> comboCiv2;
    @FXML
    private ComboBox<String> comboBatalla;
    @FXML
    private ComboBox<String> comboMapa;
    @FXML
    private ListView<Unidad> listEjercito1;
    @FXML
    private ListView<Unidad> listEjercito2;
    @FXML
    private TextArea logTextArea;
    @FXML
    private Pane campoBatalla;

    private GestorJuego gestor = GestorJuego.getInstancia();
    private ObservableList<Unidad> ejercito1 = FXCollections.observableArrayList();
    private ObservableList<Unidad> ejercito2 = FXCollections.observableArrayList();

    private List<UnidadVisual> unidadesVisuales1 = new ArrayList<>();
    private List<UnidadVisual> unidadesVisuales2 = new ArrayList<>();

    private Timeline timeline;
    private Random random = new Random();

    /**
     * Un OutputStream personalizado que redirige la salida a un TextArea de JavaFX.
     * Utiliza un buffer para manejar correctamente los caracteres multibyte (como UTF-8).
     * Vuelca el contenido del búfer cuando encuentra un salto de línea o se llama a flush().
     */
    private static class TextAreaOutputStream extends OutputStream {

        private final TextArea textArea;
        private final ByteArrayOutputStream buffer = new ByteArrayOutputStream();

        public TextAreaOutputStream(TextArea textArea) {
            this.textArea = textArea;
        }

        @Override
        public void write(int b) {
            if (b == '\n') {
                // Al encontrar un salto de línea, vuelca el buffer al TextArea.
                // Esto asegura que las líneas completas se procesen de una vez.
                buffer.write(b); // Añade el salto de línea al buffer también
                flush();
            } else {
                buffer.write(b);
            }
        }

        @Override
        public void flush() {
            // Se ejecuta en el hilo de la aplicación de JavaFX para actualizar la UI de forma segura.
            javafx.application.Platform.runLater(() -> {
                // Convierte los bytes del buffer a String usando UTF-8 y los añade al TextArea.
                textArea.appendText(buffer.toString(StandardCharsets.UTF_8));
                // Limpia el buffer para la siguiente escritura.
                buffer.reset();
            });
        }

        @Override
        public void close() {
            flush(); // Asegurarse de que cualquier contenido restante en el buffer se escriba al cerrar.
        }
    }


    @FXML
    public void initialize() {
        // Redirigir System.out al TextArea usando nuestro OutputStream personalizado y seguro para UTF-8.
        System.setOut(new PrintStream(new TextAreaOutputStream(logTextArea), true, StandardCharsets.UTF_8));

        // Configurar ComboBoxes
        comboCiv1.setItems(FXCollections.observableArrayList("Romana", "Vikinga", "Egipcia"));
        comboCiv2.setItems(FXCollections.observableArrayList("Romana", "Vikinga", "Egipcia"));
        comboBatalla.setItems(FXCollections.observableArrayList("Batalla 1v1", "Batalla Masiva"));
        comboMapa.setItems(FXCollections.observableArrayList("Pradera", "Nieve", "Desierto"));

        // Seleccionar valores por defecto
        comboCiv1.getSelectionModel().selectFirst();
        comboCiv2.getSelectionModel().selectFirst();
        comboBatalla.getSelectionModel().selectFirst();
        comboMapa.getSelectionModel().selectFirst();

        // Enlazar listas observables a los ListView
        listEjercito1.setItems(ejercito1);
        listEjercito2.setItems(ejercito2);

        // Listener para cambiar el fondo del mapa
        comboMapa.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            actualizarFondoMapa(newValue);
        });
        // Cargar fondo inicial
        actualizarFondoMapa(comboMapa.getSelectionModel().getSelectedItem());


        // Reproducir música de fondo al iniciar
        GestorSonido.getInstancia().reproducirMusicaFondo();
    }

    private void actualizarFondoMapa(String nombreMapa) {
        if (nombreMapa == null) return;
        String imagePath = "/images/maps/" + nombreMapa.toLowerCase() + ".png";
        try {
            Image image = new Image(getClass().getResourceAsStream(imagePath));
            BackgroundImage backgroundImage = new BackgroundImage(image,
                    BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                    BackgroundPosition.CENTER, new BackgroundSize(100, 100, true, true, true, true));
            campoBatalla.setBackground(new Background(backgroundImage));
        } catch (Exception e) {
            System.err.println("No se pudo cargar la imagen de fondo: " + imagePath);
            // Opcional: poner un fondo por defecto si falla la carga
            campoBatalla.setStyle("-fx-background-color: #a3d9a5;");
        }
    }

    private CivilizacionFactory getFactory(String nombre) {
        return gestor.getCivilizaciones().stream()
                .filter(f -> f.getClass().getSimpleName().contains(nombre))
                .findFirst().orElse(null);
    }

    @FXML
    private void limpiarBatalla() {
        if (timeline != null) {
            timeline.stop();
        }
        ejercito1.forEach(Unidad::resetearModificadores);
        ejercito2.forEach(Unidad::resetearModificadores);

        ejercito1.clear();
        ejercito2.clear();
        unidadesVisuales1.clear();
        unidadesVisuales2.clear();
        campoBatalla.getChildren().clear();
        actualizarFondoMapa(comboMapa.getSelectionModel().getSelectedItem()); // Restaurar fondo al limpiar
        logTextArea.clear();
        System.out.println("Campo de batalla limpiado. Listo para un nuevo combate.");
        GestorSonido.getInstancia().reproducirMusicaFondo();
    }

    @FXML
    private void crearSoldado1() {
        crearUnidad("soldado", 1, ejercito1, unidadesVisuales1, true);
    }

    @FXML
    private void crearArquero1() {
        crearUnidad("arquero", 1, ejercito1, unidadesVisuales1, true);
    }

    @FXML
    private void crearSoldado2() {
        crearUnidad("soldado", 2, ejercito2, unidadesVisuales2, false);
    }

    @FXML
    private void crearArquero2() {
        crearUnidad("arquero", 2, ejercito2, unidadesVisuales2, false);
    }

    @FXML
    private void crearLancero1() {
        crearUnidad("lancero", 1, ejercito1, unidadesVisuales1, true);
    }

    @FXML
    private void crearLancero2() {
        crearUnidad("lancero", 2, ejercito2, unidadesVisuales2, false);
    }

    private void crearUnidad(String tipo, int equipo, ObservableList<Unidad> ejercito, List<UnidadVisual> unidadesVisuales, boolean esEquipo1) {
        String civSeleccionada = (esEquipo1 ? comboCiv1 : comboCiv2).getSelectionModel().getSelectedItem();
        if (civSeleccionada != null) {
            CivilizacionFactory factory = getFactory(civSeleccionada);
            if (factory != null) {
                Unidad nueva = factory.crearUnidad(tipo);
                ejercito.add(nueva);
                System.out.println("Creado: " + nueva + " para " + civSeleccionada);

                // Crear y posicionar la unidad visual
                UnidadVisual uv = new UnidadVisual(nueva);
                unidadesVisuales.add(uv);
                campoBatalla.getChildren().add(uv);
                posicionarUnidad(uv, unidadesVisuales.size(), esEquipo1);

                // Reproducir sonido de despliegue
                GestorSonido.getInstancia().reproducirSonido("despliegue");
            }
        }
    }

    private void posicionarUnidad(UnidadVisual uv, int indice, boolean esEquipo1) {
        int unidadesPorFila = 4; // Unidades máximas por fila antes de saltar a la siguiente
        int fila = (indice - 1) / unidadesPorFila;
        int columna = (indice - 1) % unidadesPorFila;

        double xOffset = columna * 50; // Espacio horizontal entre unidades
        double yOffset = fila * 60;    // Espacio vertical entre filas

        if (esEquipo1) {
            uv.setTranslateX(50 + xOffset);
            uv.setTranslateY(20 + yOffset);
        } else {
            uv.setTranslateX(campoBatalla.getPrefWidth() - 100 - xOffset);
            uv.setTranslateY(20 + yOffset);
            uv.setScaleX(-1); // Voltear la imagen para que mire a la izquierda
        }
    }

    private void posicionarUnidades() {
        // Posicionar ejército 1 a la izquierda
        for (int i = 0; i < unidadesVisuales1.size(); i++) {
            UnidadVisual uv = unidadesVisuales1.get(i);
            uv.setTranslateX(50 + (i % 5) * 50);
            uv.setTranslateY(50 + (i / 5) * 50);
            campoBatalla.getChildren().add(uv);
        }

        // Posicionar ejército 2 a la derecha
        for (int i = 0; i < unidadesVisuales2.size(); i++) {
            UnidadVisual uv = unidadesVisuales2.get(i);
            uv.setTranslateX(campoBatalla.getPrefWidth() - 100 - (i % 5) * 50);
            uv.setTranslateY(50 + (i / 5) * 50);
            campoBatalla.getChildren().add(uv);
        }
    }

    private void aplicarEfectosDeMapa() {
        String mapaSeleccionado = comboMapa.getValue();
        if (mapaSeleccionado == null) return;

        logTextArea.appendText("Aplicando efectos del mapa: " + mapaSeleccionado + "\n");

        List<Unidad> todasLasUnidades = new ArrayList<>();
        todasLasUnidades.addAll(ejercito1);
        todasLasUnidades.addAll(ejercito2);

        for (Unidad u : todasLasUnidades) {
            u.resetearModificadores(); // Primero reseteamos por si acaso
        }

        switch (mapaSeleccionado) {
            case "Pradera":
                for (Unidad u : todasLasUnidades) {
                    if (u instanceof com.ull.dap.juego.model.unidades.Arquero) {
                        u.setModificadorDefensa(1.25); // 25% más de defensa
                        logTextArea.appendText(u.getNombre() + " obtiene un bonus de defensa en la pradera.\n");
                    }
                }
                break;
            case "Nieve":
                for (Unidad u : todasLasUnidades) {
                    if (u instanceof com.ull.dap.juego.model.unidades.Soldado || u instanceof com.ull.dap.juego.model.unidades.Lancero) {
                        u.setModificadorAtaque(1.20); // 20% más de ataque
                        logTextArea.appendText(u.getNombre() + " obtiene un bonus de ataque en la nieve.\n");
                    }
                }
                break;
            case "Desierto":
                for (Unidad u : todasLasUnidades) {
                    int danioDesierto = (int) (u.getVidaMaxima() * 0.10); // Pierden 10% de vida
                    u.setVida(u.getVida() - danioDesierto);
                    logTextArea.appendText(u.getNombre() + " sufre por el calor del desierto.\n");
                    if (u.getVisual() != null) {
                        u.getVisual().update();
                    }
                }
                break;
        }
    }

    @FXML
    private void iniciarBatalla() {
        if (ejercito1.isEmpty() || ejercito2.isEmpty()) {
            System.out.println("Ambos ejércitos deben tener unidades para luchar.");
            return;
        }
        if (timeline != null && timeline.getStatus() == Timeline.Status.RUNNING) {
            System.out.println("La batalla ya está en curso.");
            return;
        }

        logTextArea.clear();
        logTextArea.appendText("¡La batalla comienza!\n");

        aplicarEfectosDeMapa(); // Aplicar bonus/penalizaciones del mapa

        GestorSonido.getInstancia().reproducirMusicaBatalla();

        // Iniciar la animación de la batalla en tiempo real
        timeline = new Timeline(new KeyFrame(Duration.seconds(1.5), e -> {
            realizarTurnoDeCombateSimultaneo();
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private void realizarTurnoDeCombateSimultaneo() {
        List<UnidadVisual> atacantes1 = getUnidadesVivas(unidadesVisuales1);
        List<UnidadVisual> objetivos1 = getUnidadesVivas(unidadesVisuales2);
        List<UnidadVisual> atacantes2 = getUnidadesVivas(unidadesVisuales2);
        List<UnidadVisual> objetivos2 = getUnidadesVivas(unidadesVisuales1);

        if (atacantes1.isEmpty() || objetivos1.isEmpty()) {
            finalizarBatalla();
            return;
        }

        // Determinar cuántos ataques simultáneos ocurrirán (ej. hasta 3 por bando)
        int numAtaques = Math.min(3, Math.min(atacantes1.size(), objetivos1.size()));

        // Turno del ejército 1
        for (int i = 0; i < numAtaques; i++) {
            UnidadVisual atacante = atacantes1.get(random.nextInt(atacantes1.size()));
            UnidadVisual objetivo = objetivos1.get(random.nextInt(objetivos1.size()));
            realizarAtaque(atacante, objetivo);
        }

        // Turno del ejército 2
        numAtaques = Math.min(3, Math.min(atacantes2.size(), objetivos2.size()));
         for (int i = 0; i < numAtaques; i++) {
            UnidadVisual atacante = atacantes2.get(random.nextInt(atacantes2.size()));
            UnidadVisual objetivo = objetivos2.get(random.nextInt(objetivos2.size()));
            realizarAtaque(atacante, objetivo);
        }
    }

    private void finalizarBatalla() {
        timeline.stop();
        System.out.println("¡La batalla ha concluido!");

        boolean ejercito1Vivo = !getUnidadesVivas(unidadesVisuales1).isEmpty();
        boolean ejercito2Vivo = !getUnidadesVivas(unidadesVisuales2).isEmpty();

        if (!ejercito1Vivo) {
            System.out.println("¡El Ejército 2 ha ganado la batalla!");
            GestorSonido.getInstancia().reproducirSonido("victoria");
        } else if (!ejercito2Vivo) {
            System.out.println("¡El Ejército 1 ha ganado la batalla!");
            GestorSonido.getInstancia().reproducirSonido("victoria");
        }

        GestorSonido.getInstancia().reproducirMusicaFondo();
    }


    private void realizarAtaque(UnidadVisual atacanteVisual, UnidadVisual objetivoVisual) {
        if (atacanteVisual.getUnidad().getVida() > 0 && objetivoVisual.getUnidad().getVida() > 0) {
            atacanteVisual.animarAtaque(objetivoVisual);
            atacanteVisual.getUnidad().atacar(objetivoVisual.getUnidad()); // Lógica de daño real
            objetivoVisual.update(); // Actualiza la barra de vida
        }
    }

    private void actualizarVisualizadores() {
        campoBatalla.getChildren().clear();
        unidadesVisuales1.forEach(uv -> campoBatalla.getChildren().add(uv));
        unidadesVisuales2.forEach(uv -> campoBatalla.getChildren().add(uv));
    }

    private List<UnidadVisual> getUnidadesVivas(List<UnidadVisual> unidades) {
        return unidades.stream()
                .filter(uv -> uv.getUnidad().getVida() > 0)
                .collect(Collectors.toList());
    }

    private UnidadVisual getUnidadVisual(Unidad unidad, List<UnidadVisual> listaVisual) {
        return listaVisual.stream()
                .filter(uv -> uv.getUnidad() == unidad)
                .findFirst()
                .orElse(null);
    }
}
