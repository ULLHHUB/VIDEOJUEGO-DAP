package com.ull.dap.juego.control;

import com.ull.dap.juego.model.batalla.BatallaTemplate;
import com.ull.dap.juego.model.factoria.CivilizacionFactory;
import com.ull.dap.juego.model.factoria.EgipciaFactory;
import com.ull.dap.juego.model.factoria.RomanaFactory;
import com.ull.dap.juego.model.factoria.VikingaFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase Singleton que gestiona el estado y la lógica principal del juego.
 */
public class GestorJuego {

    private static GestorJuego instancia;
    private List<CivilizacionFactory> civilizaciones;

    /**
     * Constructor privado para el patrón Singleton.
     */
    private GestorJuego() {
        civilizaciones = new ArrayList<>();
        // Por defecto, añadimos las civilizaciones disponibles
        civilizaciones.add(new RomanaFactory());
        civilizaciones.add(new EgipciaFactory());
        civilizaciones.add(new VikingaFactory());
    }

    /**
     * Devuelve la única instancia de GestorJuego.
     * @return La instancia de GestorJuego.
     */
    public static synchronized GestorJuego getInstancia() {
        if (instancia == null) {
            instancia = new GestorJuego();
        }
        return instancia;
    }

    /**
     * Inicia una partida o simulación.
     * Este método se expandirá para controlar la lógica del juego.
     */
    public void iniciarPartida() {
        System.out.println("¡El juego ha comenzado!");
        // Lógica para configurar la partida, seleccionar civilizaciones, etc.
    }

    /**
     * Controla una batalla entre dos ejércitos.
     * @param batalla El tipo de batalla a ejecutar.
     * @param ejercito1 El primer ejército.
     * @param ejercito2 El segundo ejército.
     */
    public void controlarBatalla(BatallaTemplate batalla, List<com.ull.dap.juego.model.unidades.Unidad> ejercito1, List<com.ull.dap.juego.model.unidades.Unidad> ejercito2) {
        batalla.iniciarBatalla(ejercito1, ejercito2);
    }

    public List<CivilizacionFactory> getCivilizaciones() {
        return civilizaciones;
    }
}
