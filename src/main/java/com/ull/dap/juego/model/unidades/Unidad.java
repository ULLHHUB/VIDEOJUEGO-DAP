package com.ull.dap.juego.model.unidades;

import com.ull.dap.juego.control.GestorSonido;
import com.ull.dap.juego.model.estrategia.EstrategiaAtaque;
import com.ull.dap.juego.view.UnidadVisual;

public abstract class Unidad {
    private String nombre;
    private int vida;
    private int vidaMaxima;
    private int ataque;
    private String imagePath;
    private EstrategiaAtaque estrategiaAtaque;
    private UnidadVisual visual; // Referencia a la parte visual (UnidadVisual)
    private double modificadorAtaque = 1.0;
    private double modificadorDefensa = 1.0;


    public Unidad(String nombre, int vida, int ataque) {
        this.nombre = nombre;
        this.vida = vida;
        this.vidaMaxima = vida;
        this.ataque = ataque;
        this.imagePath = ""; // Default empty path
    }

    public void atacar(Unidad objetivo) {
        if (estrategiaAtaque != null) {
            estrategiaAtaque.atacar(this, objetivo);
        }
    }

    public void setEstrategiaAtaque(EstrategiaAtaque e) {
        this.estrategiaAtaque = e;
    }

    public void setVisual(UnidadVisual visual) {
        this.visual = visual;
    }

    public UnidadVisual getVisual() {
        return visual;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getNombre() {
        return nombre;
    }


    public int getVida() {
        return vida;
    }

    public int getVidaMaxima() {
        return vidaMaxima;
    }

    public void setVida(int vida) {
        this.vida = vida;
    }

    public int getAtaque() {
        return (int) (ataque * modificadorAtaque);
    }

    public void recibirDanio(int danio) {
        int danioFinal = (int) (danio / modificadorDefensa);

        // Añadimos una probabilidad de golpe crítico (ej. 15% de probabilidad de hacer el doble de daño)
        if (new java.util.Random().nextInt(100) < 15) {
            danioFinal *= 2;
            System.out.println("¡GOLPE CRÍTICO!");
            if (visual != null) {
                visual.mostrarEfectoEspecial("CRITICO");
            }
        }

        this.vida -= danioFinal;
        if (this.vida < 0) {
            this.vida = 0;
        }
        GestorSonido.getInstancia().reproducirSonido("golpe");
        if (visual != null) {
            visual.update();
        }
    }

    public void setModificadorAtaque(double modificadorAtaque) {
        this.modificadorAtaque = modificadorAtaque;
    }

    public void setModificadorDefensa(double modificadorDefensa) {
        this.modificadorDefensa = modificadorDefensa;
    }

    public void resetearModificadores() {
        this.modificadorAtaque = 1.0;
        this.modificadorDefensa = 1.0;
        this.vida = this.vidaMaxima; // Resetea la vida al máximo
        if (visual != null) {
            visual.update();
        }
    }

    @Override
    public String toString() {
        return nombre + " (Vida: " + vida + ", Ataque: " + ataque + ")";
    }
}
