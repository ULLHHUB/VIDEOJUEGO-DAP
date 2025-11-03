package com.ull.dap.juego.model.unidades;

import com.ull.dap.juego.model.estrategia.AtaqueCuerpoACuerpo;

public class Soldado extends Unidad {
    public Soldado(String nombre, int vida, int ataque) {
        super(nombre, vida, ataque);
        setEstrategiaAtaque(new AtaqueCuerpoACuerpo());
    }
}
