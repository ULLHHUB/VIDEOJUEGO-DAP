package com.ull.dap.juego.model.factoria;

import com.ull.dap.juego.model.unidades.Arquero;
import com.ull.dap.juego.model.unidades.Lancero;
import com.ull.dap.juego.model.unidades.Soldado;
import com.ull.dap.juego.model.unidades.Unidad;

public class EgipciaFactory implements CivilizacionFactory {
    @Override
    public Unidad crearUnidad(String tipo) {
        Unidad unidad;
        switch (tipo.toLowerCase()) {
            case "soldado":
                unidad = new Soldado("Guerrero Egipcio", 90, 12);
                unidad.setImagePath("images/guerrero_egipcio.png");
                break;
            case "arquero":
                unidad = new Arquero("Arquero Egipcio", 80, 12);
                unidad.setImagePath("images/arquero_egipcio.png");
                break;
            case "lancero":
                unidad = new Lancero("Lancero Egipcio", 75, 11);
                unidad.setImagePath("images/lancero_egipcio.png");
                break;
            default:
                throw new IllegalArgumentException("Tipo de unidad no válido para Egipcios: " + tipo);
        }
        return unidad;
    }

}
