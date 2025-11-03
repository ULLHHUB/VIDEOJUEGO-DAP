package com.ull.dap.juego.model.estrategia;

import com.ull.dap.juego.model.unidades.Arquero;
import com.ull.dap.juego.model.unidades.Lancero;
import com.ull.dap.juego.model.unidades.Soldado;
import com.ull.dap.juego.model.unidades.Unidad;

public class AtaqueCuerpoACuerpo implements EstrategiaAtaque {
    @Override
    public void atacar(Unidad atacante, Unidad objetivo) {
        System.out.println(atacante.getNombre() + " ataca cuerpo a cuerpo a " + objetivo.getNombre());
        int danio = atacante.getAtaque();
        boolean ventaja = false;

        // Ventaja: Soldado > Arquero
        if (atacante instanceof Soldado && objetivo instanceof Arquero) {
            danio = (int) (danio * 1.5);
            System.out.println("¡Ventaja! El soldado hace más daño al arquero.");
            ventaja = true;
        }
        // Ventaja: Lancero > Soldado
        else if (atacante instanceof Lancero && objetivo instanceof Soldado) {
            danio = (int) (danio * 1.5);
            System.out.println("¡Ventaja! El lancero hace más daño al soldado.");
            ventaja = true;
        }
        // Desventaja: Soldado < Lancero
        else if (atacante instanceof Soldado && objetivo instanceof Lancero) {
            danio = (int) (danio * 0.75);
            System.out.println("¡Desventaja! El soldado hace menos daño al lancero.");
        }
         // Desventaja: Lancero < Arquero
        else if (atacante instanceof Lancero && objetivo instanceof Arquero) {
            danio = (int) (danio * 0.75);
            System.out.println("¡Desventaja! El lancero hace menos daño al arquero.");
        }

        if (ventaja && atacante.getVisual() != null) {
            atacante.getVisual().mostrarEfectoEspecial("VENTAJA");
        }

        objetivo.recibirDanio(danio);
    }
}
