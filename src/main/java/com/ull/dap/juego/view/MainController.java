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
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import java.io.OutputStream;
import java.io.PrintStream;
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

    @FXML
    public void initialize() {
        // Redirigir System.out al TextArea
        OutputStream out = new OutputStream() {
            @Override
            public void write(int b) {
                logTextArea.appendText(String.valueOf((char) b));
            }
        };
        System.setOut(new PrintStream(out, true));

        // Configurar ComboBox de Civilizaciones
        List<String> nombresCivs = gestor.getCivilizaciones().stream()
                .map(c -> c.getClass().getSimpleName().replace("Factory", ""))
                .collect(Collectors.toList());
        comboCiv1.setItems(FXCollections.observableArrayList(nombresCivs));
        comboCiv2.setItems(FXCollections.observableArrayList(nombresCivs));
        comboCiv1.getSelectionModel().selectFirst();
        comboCiv2.getSelectionModel().select(1);

        // Configurar ComboBox de Batalla
        comboBatalla.setItems(FXCollections.observableArrayList("Batalla Masiva", "Batalla 1v1"));
        comboBatalla.getSelectionModel().selectFirst();

        // Configurar ComboBox de Mapa
        comboMapa.setItems(FXCollections.observableArrayList("Pradera", "Desierto", "Nieve"));
        comboMapa.getSelectionModel().selectFirst();
        comboMapa.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            if (newValue != null) {
                String mapaSeleccionado = newValue.toLowerCase();
                String imagePath = "images/maps/" + mapaSeleccionado + ".png";
                campoBatalla.setStyle("-fx-background-image: url('" + getClass().getResource("/" + imagePath) + "'); " +
                                      "-fx-background-size: cover; -fx-border-color: black;");
            }
        });
        // Aplicar el mapa por defecto al inicio
        String mapaInicial = comboMapa.getSelectionModel().getSelectedItem().toLowerCase();
        String imagePathInicial = "images/maps/" + mapaInicial + ".png";
        campoBatalla.setStyle("-fx-background-image: url('" + getClass().getResource("/" + imagePathInicial) + "'); " +
                              "-fx-background-size: cover; -fx-border-color: black;");


        // Enlazar listas observables a los ListView
        listEjercito1.setItems(ejercito1);
        listEjercito2.setItems(ejercito2);
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
            case "Bosque":
                for (Unidad u : todasLasUnidades) {
                    if (u instanceof com.ull.dap.juego.model.unidades.Arquero) {
                        u.setModificadorDefensa(1.25); // 25% más de defensa
                        logTextArea.appendText(u.getNombre() + " obtiene un bonus de defensa en el bosque.\n");
                    }
                }
                break;
            case "Montaña":
                for (Unidad u : todasLasUnidades) {
                    if (u instanceof com.ull.dap.juego.model.unidades.Soldado || u instanceof com.ull.dap.juego.model.unidades.Lancero) {
                        u.setModificadorAtaque(1.20); // 20% más de ataque
                        logTextArea.appendText(u.getNombre() + " obtiene un bonus de ataque en la montaña.\n");
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
