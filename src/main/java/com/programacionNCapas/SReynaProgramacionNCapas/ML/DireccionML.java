package com.programacionNCapas.SReynaProgramacionNCapas.ML;

public class DireccionML {

    private int IdDireccion;
    private String Calle;
    private String NumeroInterior;
    private String NumeroExterior;
    public ColoniaML Colonia;

    public DireccionML() {
    }

    public DireccionML(
            int IdDireccion,
            String Calle,
            String NumeroInterior,
            String NumeroExterior,
            ColoniaML Colonia) {
        this.IdDireccion = IdDireccion;
        this.Calle = Calle;
        this.NumeroExterior = NumeroExterior;
        this.NumeroInterior = NumeroInterior;
        this.Colonia = Colonia;
    }

    public DireccionML(int IdDireccion) {
        this.IdDireccion = IdDireccion;
    }

    public void setIdDireccion(int IdDireccion) {
        this.IdDireccion = IdDireccion;
    }

    public void setCalle(String Calle) {
        this.Calle = Calle;
    }

    public void setNumeroExterior(String NumeroExterior) {
        this.NumeroExterior = NumeroExterior;
    }

    public void setNumeroInterior(String NumeroInterior) {
        this.NumeroInterior = NumeroInterior;
    }

    public int getIdDireccion() {
        return IdDireccion;
    }

    public String getCalle() {
        return Calle;
    }

    public String getNumeroExterior() {
        return NumeroExterior;
    }

    public String getNumeroInterior() {
        return NumeroInterior;
    }

    public ColoniaML getColonia() {
        return Colonia;
    }

    public void setColonia(ColoniaML Colonia) {
        this.Colonia = Colonia;
    }

}
