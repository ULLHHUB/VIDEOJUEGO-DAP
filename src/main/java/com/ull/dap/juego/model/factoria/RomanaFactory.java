package com.ull.dap.juego.model.factoria;

import com.ull.dap.juego.model.unidades.Arquero;
import com.ull.dap.juego.model.unidades.Lancero;
import com.ull.dap.juego.model.unidades.Soldado;
import com.ull.dap.juego.model.unidades.Unidad;

public class RomanaFactory implements CivilizacionFactory {
    @Override
    public Unidad crearUnidad(String tipo) {
        Unidad unidad;
        switch (tipo.toLowerCase()) {
            case "soldado":
                unidad = new Soldado("Legionario Romano", 100, 15);
                unidad.setImagePath("images/legionario_romano.png");
                break;
            case "arquero":
                unidad = new Arquero("Arquero Romano", 70, 10);
                unidad.setImagePath("images/arquero_romano.png");
                break;
            case "lancero":
                unidad = new Lancero("Lancero Romano", 85, 13);
                unidad.setImagePath("images/lancero_romano.png");
                break;
            default:
                throw new IllegalArgumentException("Tipo de unidad no válido para Romanos: " + tipo);
        }
        return unidad;
    }

}
