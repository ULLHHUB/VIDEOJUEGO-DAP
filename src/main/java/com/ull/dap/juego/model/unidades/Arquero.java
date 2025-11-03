package com.ull.dap.juego.model.unidades;

import com.ull.dap.juego.model.estrategia.AtaqueADistancia;

public class Arquero extends Unidad {
    public Arquero(String nombre, int vida, int ataque) {
        super(nombre, vida, ataque);
        setEstrategiaAtaque(new AtaqueADistancia());
    }
}
