package com.ull.dap.juego.model.estrategia;

import com.ull.dap.juego.model.unidades.Arquero;
import com.ull.dap.juego.model.unidades.Lancero;
import com.ull.dap.juego.model.unidades.Soldado;
import com.ull.dap.juego.model.unidades.Unidad;

public class AtaqueADistancia implements EstrategiaAtaque {
    @Override
    public void atacar(Unidad atacante, Unidad objetivo) {
        System.out.println(atacante.getNombre() + " ataca a distancia a " + objetivo.getNombre());
        int danio = atacante.getAtaque();
        boolean ventaja = false;

        // Ventaja: Arquero > Lancero
        if (atacante instanceof Arquero && objetivo instanceof Lancero) {
            danio = (int) (danio * 1.5);
            System.out.println("¡Ventaja! El arquero hace más daño al lancero.");
            ventaja = true;
        }
        // Desventaja: Arquero < Soldado
        else if (atacante instanceof Arquero && objetivo instanceof Soldado) {
            danio = (int) (danio * 0.75);
            System.out.println("¡Desventaja! El arquero hace menos daño al soldado.");
        }

        if (ventaja && atacante.getVisual() != null) {
            atacante.getVisual().mostrarEfectoEspecial("VENTAJA");
        }

        objetivo.recibirDanio(danio);
    }
}
