package com.ull.dap.juego.model.batalla;

import com.ull.dap.juego.model.unidades.Unidad;
import java.util.List;

/**
 * Clase abstracta para el patrón Template Method.
 * Define el esqueleto de una batalla.
 */
public abstract class BatallaTemplate {

    protected List<Unidad> ejercito1;
    protected List<Unidad> ejercito2;

    /**
     * El método template, define el flujo de la batalla.
     * Es final para que las subclases no puedan alterarlo.
     */
    public final void iniciarBatalla(List<Unidad> ejercito1, List<Unidad> ejercito2) {
        prepararEjercitos(ejercito1, ejercito2);
        resolverCombate();
        mostrarResultado();
    }

    /**
     * Prepara los ejércitos para la batalla.
     */
    protected void prepararEjercitos(List<Unidad> ejercito1, List<Unidad> ejercito2) {
        this.ejercito1 = ejercito1;
        this.ejercito2 = ejercito2;
        System.out.println("Preparando ejércitos para la batalla...");
    }

    /**
     * Lógica del combate. Este es un paso que las subclases deben implementar.
     */
    protected abstract void resolverCombate();

    /**
     * Muestra el resultado de la batalla.
     */
    protected abstract void mostrarResultado();
}
