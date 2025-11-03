package com.ull.dap.juego.control;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class GestorSonido {

    private static GestorSonido instancia;
    private Map<String, MediaPlayer> sonidos;
    private MediaPlayer musicaFondoPlayer;
    private MediaPlayer musicaBatallaPlayer;

    private GestorSonido() {
        sonidos = new HashMap<>();
        // Precargar los sonidos que usaremos
        cargarSonido("golpe", "/sounds/golpe.wav");
        cargarSonido("victoria", "/sounds/victoria.wav");
        cargarSonido("despliegue", "/sounds/despliegue.wav");

        // Cargar la música de fondo
        cargarMusicaDeFondo("/sounds/musica_fondo.wav");
        cargarMusicaDeBatalla("/sounds/musica_batalla.wav");
    }

    public static synchronized GestorSonido getInstancia() {
        if (instancia == null) {
            instancia = new GestorSonido();
        }
        return instancia;
    }

    private void cargarMusicaDeFondo(String rutaArchivo) {
        try {
            URL resource = getClass().getResource(rutaArchivo);
            if (resource == null) {
                System.err.println("No se pudo encontrar el archivo de música de fondo: " + rutaArchivo);
                return;
            }
            Media media = new Media(resource.toString());
            musicaFondoPlayer = new MediaPlayer(media);
            musicaFondoPlayer.setCycleCount(MediaPlayer.INDEFINITE); // Para que se repita en bucle
        } catch (Exception e) {
            System.err.println("Error al cargar la música de fondo.");
            e.printStackTrace();
        }
    }

    private void cargarMusicaDeBatalla(String rutaArchivo) {
        try {
            URL resource = getClass().getResource(rutaArchivo);
            if (resource == null) {
                System.err.println("No se pudo encontrar el archivo de música de batalla: " + rutaArchivo);
                return;
            }
            Media media = new Media(resource.toString());
            musicaBatallaPlayer = new MediaPlayer(media);
            musicaBatallaPlayer.setCycleCount(MediaPlayer.INDEFINITE); // Para que se repita en bucle
        } catch (Exception e) {
            System.err.println("Error al cargar la música de batalla.");
            e.printStackTrace();
        }
    }

    private void cargarSonido(String nombre, String rutaArchivo) {
        try {
            URL resource = getClass().getResource(rutaArchivo);
            if (resource == null) {
                System.err.println("No se pudo encontrar el archivo de sonido: " + rutaArchivo);
                return;
            }
            Media media = new Media(resource.toString());
            MediaPlayer mediaPlayer = new MediaPlayer(media);
            sonidos.put(nombre, mediaPlayer);
        } catch (Exception e) {
            System.err.println("Error al cargar el sonido: " + nombre);
            e.printStackTrace();
        }
    }

    public void reproducirMusicaFondo() {
        if (musicaBatallaPlayer != null) musicaBatallaPlayer.stop();
        if (musicaFondoPlayer != null) {
            musicaFondoPlayer.play();
        }
    }

    public void detenerMusicaFondo() {
        if (musicaFondoPlayer != null) {
            musicaFondoPlayer.stop();
        }
    }

    public void reproducirMusicaBatalla() {
        if (musicaFondoPlayer != null) musicaFondoPlayer.stop();
        if (musicaBatallaPlayer != null) {
            musicaBatallaPlayer.play();
        }
    }

    public void reproducirSonido(String nombre) {
        MediaPlayer mediaPlayer = sonidos.get(nombre);
        if (mediaPlayer != null) {
            // Detenemos el sonido si ya se está reproduciendo y lo reiniciamos
            mediaPlayer.stop();
            mediaPlayer.play();
        } else {
            System.err.println("El sonido '" + nombre + "' no está cargado.");
        }
    }
}
