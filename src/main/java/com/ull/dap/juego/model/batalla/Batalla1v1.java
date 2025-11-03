package com.ull.dap.juego.model.batalla;

import com.ull.dap.juego.control.GestorSonido;
import com.ull.dap.juego.model.unidades.Unidad;

public class Batalla1v1 extends BatallaTemplate {

    @Override
    protected void resolverCombate() {
        if (ejercito1.size() != 1 || ejercito2.size() != 1) {
            System.out.println("La Batalla 1v1 requiere exactamente una unidad por ejército.");
            return;
        }

        Unidad u1 = ejercito1.get(0);
        Unidad u2 = ejercito2.get(0);

        System.out.println("¡Comienza la batalla 1v1 entre " + u1.getNombre() + " y " + u2.getNombre() + "!");

        while (u1.getVida() > 0 && u2.getVida() > 0) {
            // Turno de u1
            u1.atacar(u2);
            System.out.println(u1);
            System.out.println(u2);
            if (u2.getVida() <= 0) break;

            // Turno de u2
            u2.atacar(u1);
            System.out.println(u1);
            System.out.println(u2);
        }
    }

    @Override
    protected void mostrarResultado() {
        Unidad u1 = ejercito1.get(0);
        Unidad u2 = ejercito2.get(0);

        System.out.println("\n--- Resultado de la Batalla 1v1 ---");
        if (u1.getVida() <= 0) {
            System.out.println(u2.getNombre() + " ha ganado la batalla!");
            GestorSonido.getInstancia().reproducirSonido("victoria");
        } else if (u2.getVida() <= 0) {
            System.out.println(u1.getNombre() + " ha ganado la batalla!");
            GestorSonido.getInstancia().reproducirSonido("victoria");
        } else {
            System.out.println("La batalla terminó en empate (esto no debería ocurrir en una batalla 1v1 normal).");
        }
        System.out.println("Estado final:");
        System.out.println(u1);
        System.out.println(u2);
    }
}
