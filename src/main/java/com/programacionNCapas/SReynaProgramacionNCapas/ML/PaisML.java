package com.programacionNCapas.SReynaProgramacionNCapas.ML;

public class PaisML {

    private int IdPais;
    private String Nombre;

    public PaisML() {
    }

    public PaisML(
            int IdPais, String Nombre) {
        this.IdPais = IdPais;
        this.Nombre = Nombre;
    }

    public void setIdPais(int IdPais) {
        this.IdPais = IdPais;
    }

    public void setNombre(String Nombre) {
        this.Nombre = Nombre;
    }

    public int getIdPais() {
        return IdPais;
    }

    public String getNombre() {
        return Nombre;
    }
}
