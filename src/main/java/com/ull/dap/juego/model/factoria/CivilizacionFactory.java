package com.ull.dap.juego.model.factoria;

import com.ull.dap.juego.model.unidades.Unidad;

/**
 * Interfaz para el patrón Abstract Factory.
 * Define los métodos para crear unidades y edificios.
 */
public interface CivilizacionFactory {
    Unidad crearUnidad(String tipo);
}
