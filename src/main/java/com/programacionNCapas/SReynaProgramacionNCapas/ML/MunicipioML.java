package com.programacionNCapas.SReynaProgramacionNCapas.ML;

public class MunicipioML {

    private int IdMunicipio;
    private String Nombre;
    public EstadoML Estado;

    public MunicipioML() {
    }

    public MunicipioML(int idMunicipio,
            String nombre) {
        this.IdMunicipio = idMunicipio;
        this.Nombre = nombre;
    }

    public void setEstado(EstadoML Estado) {
        this.Estado = Estado;
    }

    public EstadoML getEstado() {
        return Estado;
    }

    public void setIdMunicipio(int idMunicipio) {
        this.IdMunicipio = idMunicipio;
    }

    public int getIdMunicipio() {
        return IdMunicipio;
    }

    public void setNombre(String nombre) {
        this.Nombre = nombre;
    }

    public String getNombre() {
        return Nombre;
    }
}
