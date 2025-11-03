package com.ull.dap.juego.model.batalla;

import com.ull.dap.juego.control.GestorSonido;
import com.ull.dap.juego.model.unidades.Unidad;
import java.util.Random;
import java.util.stream.Collectors;

public class BatallaMasiva extends BatallaTemplate {

    private Random random = new Random();

    @Override
    protected void resolverCombate() {
        System.out.println("¡Comienza la batalla masiva!");

        while (sigueLaBatalla()) {
            // Turno del ejército 1
            for (Unidad atacante : ejercito1) {
                if (atacante.getVida() > 0 && !ejercito2.stream().allMatch(u -> u.getVida() <= 0)) {
                    Unidad objetivo = seleccionarObjetivo(ejercito2);
                    if (objetivo != null) {
                        atacante.atacar(objetivo);
                    }
                }
            }
            
            // Turno del ejército 2
            for (Unidad atacante : ejercito2) {
                if (atacante.getVida() > 0 && !ejercito1.stream().allMatch(u -> u.getVida() <= 0)) {
                    Unidad objetivo = seleccionarObjetivo(ejercito1);
                    if (objetivo != null) {
                        atacante.atacar(objetivo);
                    }
                }
            }
        }
    }

    private boolean sigueLaBatalla() {
        boolean ejercito1Vivo = ejercito1.stream().anyMatch(u -> u.getVida() > 0);
        boolean ejercito2Vivo = ejercito2.stream().anyMatch(u -> u.getVida() > 0);
        return ejercito1Vivo && ejercito2Vivo;
    }

    private Unidad seleccionarObjetivo(java.util.List<Unidad> ejercito) {
        java.util.List<Unidad> vivos = ejercito.stream().filter(u -> u.getVida() > 0).collect(Collectors.toList());
        if (vivos.isEmpty()) {
            return null;
        }
        return vivos.get(random.nextInt(vivos.size()));
    }

    @Override
    protected void mostrarResultado() {
        System.out.println("\n--- Resultado de la Batalla Masiva ---");
        boolean ejercito1Vivo = ejercito1.stream().anyMatch(u -> u.getVida() > 0);
        boolean ejercito2Vivo = ejercito2.stream().anyMatch(u -> u.getVida() > 0);

        if (!ejercito1Vivo) {
            System.out.println("¡El Ejército 2 ha ganado la batalla!");
            GestorSonido.getInstancia().reproducirSonido("victoria");
        } else if (!ejercito2Vivo) {
            System.out.println("¡El Ejército 1 ha ganado la batalla!");
            GestorSonido.getInstancia().reproducirSonido("victoria");
        } else {
            System.out.println("La batalla terminó en empate.");
        }

        System.out.println("Unidades restantes del Ejército 1: " + ejercito1.stream().filter(u -> u.getVida() > 0).count());
        System.out.println("Unidades restantes del Ejército 2: " + ejercito2.stream().filter(u -> u.getVida() > 0).count());
    }
}
