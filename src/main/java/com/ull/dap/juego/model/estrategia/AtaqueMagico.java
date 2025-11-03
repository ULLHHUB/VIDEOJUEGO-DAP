package com.ull.dap.juego.model.estrategia;

import com.ull.dap.juego.model.unidades.Unidad;

public class AtaqueMagico implements EstrategiaAtaque {
    @Override
    public void atacar(Unidad atacante, Unidad objetivo) {
        System.out.println(atacante.getNombre() + " lanza un ataque mágico a " + objetivo.getNombre());
        // El ataque mágico podría tener una lógica diferente, por ejemplo, un daño base + un extra.
        int danioMagico = atacante.getAtaque() + 5; 
        objetivo.recibirDanio(danioMagico);
    }
}
