package com.programacionNCapas.SReynaProgramacionNCapas.ML;

public class EstadoML {

    private int IdEstado;
    private String Nombre;
    public PaisML Pais;

    public EstadoML() {
    }

    public EstadoML(int IdEstado, String Nombre) {
        this.IdEstado = IdEstado;
        this.Nombre = Nombre;
    }

    public void setPais(PaisML Pais) {
        this.Pais = Pais;
    }

    public PaisML getPais() {
        return Pais;
    }

    public void setIdEstado(int IdEstado) {
        this.IdEstado = IdEstado;
    }

    public int getIdEstado() {
        return IdEstado;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String Nombre) {
        this.Nombre = Nombre;
    }

}
