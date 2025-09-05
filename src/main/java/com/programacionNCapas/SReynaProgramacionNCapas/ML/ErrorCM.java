package com.programacionNCapas.SReynaProgramacionNCapas.ML;

public class ErrorCM {

    public int Linea;
    public String Dato;
    public String Mensaje;

    public ErrorCM() {
    }

    public ErrorCM(
            int Linea, String Dato, String Mensaje) {
        this.Linea = Linea;
        this.Dato = Dato;
        this.Mensaje = Mensaje;
    }
    
    public void setLinea(int Linea){
        this.Linea = Linea;
        
    }
    public void setDato(String Dato){
        this.Dato = Dato;
       
    }
    public void setMensaje(String Mensaje){
        this.Mensaje = Mensaje;
    }
    
    public int getLinea(){
        return Linea;
    }
    public String getDato(){
        return Dato;
    }
    public String getMensaje(){
        return Mensaje;
    }
}
