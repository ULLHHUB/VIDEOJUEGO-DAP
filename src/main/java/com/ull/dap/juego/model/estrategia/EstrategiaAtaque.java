package com.ull.dap.juego.model.estrategia;

import com.ull.dap.juego.model.unidades.Unidad;

/**
 * Interfaz para el patrón Strategy, define el método de ataque.
 */
public interface EstrategiaAtaque {
    void atacar(Unidad atacante, Unidad objetivo);
}
