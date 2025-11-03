package com.ull.dap.juego.model.factoria;

import com.ull.dap.juego.model.unidades.Arquero;
import com.ull.dap.juego.model.unidades.Lancero;
import com.ull.dap.juego.model.unidades.Soldado;
import com.ull.dap.juego.model.unidades.Unidad;

public class VikingaFactory implements CivilizacionFactory {
    @Override
    public Unidad crearUnidad(String tipo) {
        Unidad unidad;
        switch (tipo.toLowerCase()) {
            case "soldado":
                unidad = new Soldado("Berserker Vikingo", 120, 18);
                unidad.setImagePath("images/berserker_vikingo.png");
                break;
            case "arquero":
                unidad = new Arquero("Arquero Vikingo", 60, 8);
                unidad.setImagePath("images/arquero_vikingo.png");
                break;
            case "lancero":
                unidad = new Lancero("Lancero Vikingo", 100, 14);
                unidad.setImagePath("images/lancero_vikingo.png");
                break;
            default:
                throw new IllegalArgumentException("Tipo de unidad no válido para Vikingos: " + tipo);
        }
        return unidad;
    }

}
