package com.programacionNCapas.SReynaProgramacionNCapas.ML;

public class RolML {

    private String Nombre;
    private int IdRol;

    public RolML() {
    }

    public RolML(
            String Nombre,
            int IdRol) {
        this.Nombre = Nombre;
        this.IdRol = IdRol;
    }

    public void setNombre(String Nombre) {
        this.Nombre = Nombre;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setIdRol(int IdRol) {
        this.IdRol = IdRol;
    }

    public int getIdRol() {
        return IdRol;
    }

}
