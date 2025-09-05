package com.programacionNCapas.SReynaProgramacionNCapas.ML;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;

public class UsuarioML {

    private int IdUser;
    private String Username;
    private String NombreUsuario;
    private String ApellidoPaterno;
    private String ApellidoMaterno;
    private String Password;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate FechaNacimiento;
    private String Email;
    private String Telefono;
    private String Celular;
    private String Curp;
    private String Sexo;
    private String Img;
    private int Estatus;
    public RolML Rol = new RolML();
    public List<DireccionML> Direcciones = new ArrayList<>();

    public UsuarioML() {
    }

    public RolML getRol() {
        return Rol;
    }

    public void setRol(RolML Rol) {
        this.Rol = Rol;
    }

    public List<DireccionML> getDirecciones() {
        return Direcciones;
    }

    public void setDirecciones(List<DireccionML> Direcciones) {
        this.Direcciones = Direcciones;
    }

    public UsuarioML(
            int IdUser,
            String NombreUsuario,
            String Username,
            String ApellidoPaterno,
            String ApellidoMaterno,
            LocalDate FechaNacimiento,
            String Password,
            String Sexo,
            String Email,
            String Telefono,
            String Celular,
            String Curp,
            int Estatus
    ) {

        this.IdUser = IdUser;
        this.NombreUsuario = NombreUsuario;
        this.Username = Username;
        this.Telefono = Telefono;
        this.Celular = Celular;
        this.Curp = Curp;
        this.ApellidoPaterno = ApellidoPaterno;
        this.ApellidoMaterno = ApellidoMaterno;
        this.FechaNacimiento = FechaNacimiento;
        this.Password = Password;
        this.Sexo = Sexo;
        this.Email = Email;
        this.Estatus = Estatus;
    }

    public void setIdUser(int IdUser) {
        this.IdUser = IdUser;
    }

    public void setNombre(String NombreUsuario) {
        this.NombreUsuario = NombreUsuario;
    }

    public void setUsername(String Username) {
        this.Username = Username;
    }

    public String getUsername() {
        return Username;
    }

    public String getNombreUsuario() {
        return NombreUsuario;
    }

    public String getApellidoPaterno() {
        return ApellidoPaterno;
    }

    public String getApellidoMaterno() {
        return ApellidoMaterno;
    }

    public String getPassword() {
        return Password;
    }

    public String getEmail() {
        return Email;
    }

    public String getTelefono() {
        return Telefono;
    }

    public String getCelular() {
        return Celular;
    }

    public String getCurp() {
        return Curp;
    }

    public void setNombreUsuario(String NombreUsuario) {
        this.NombreUsuario = NombreUsuario;
    }

    public void setApellidoPaterno(String ApellidoPaterno) {
        this.ApellidoPaterno = ApellidoPaterno;
    }

    public void setApellidoMaterno(String ApellidoMaterno) {
        this.ApellidoMaterno = ApellidoMaterno;
    }

    public void setPassword(String Password) {
        this.Password = Password;
    }

    public void setEmail(String Email) {
        this.Email = Email;
    }

    public int getEstatus() {
        return Estatus;
    }

    public void setEstatus(int Estatus) {
        this.Estatus = Estatus;
    }

    public void setTelefono(String Telefono) {
        this.Telefono = Telefono;
    }

    public void setCelular(String Celular) {
        this.Celular = Celular;
    }

    public void setCurp(String Curp) {
        this.Curp = Curp;
    }

    public void setFechaNacimiento(LocalDate FechaNacimiento) {
        this.FechaNacimiento = FechaNacimiento;
    }

    public void setSexo(String Sexo) {
        this.Sexo = Sexo;
    }

    public int getIdUser() {
        return IdUser;
    }

    public LocalDate getFechaNacimiento() {
        return FechaNacimiento;
    }

    public String getSexo() {
        return Sexo;
    }

    public void setImg(String Img) {
        this.Img = Img;
    }

    public String getImg() {
        return Img;
    }
}

//    private int IdUser;
//    @Pattern(regexp = "^(?!.*[_.]{2})[a-zA-Z0-9](?!.*[_.]{2})[a-zA-Z0-9._]{1,14}[a-zA-Z0-9]$", message = "Invalid content type")
//    private String Username;
//    @Size(min = 2, max = 20, message = "Texto de entre 2 y 20 letras")
//    @NotEmpty(message = "Información requerida")
//    private String NombreUsuario;
//    @Size(min = 2, max = 20, message = "Texto de entre 2 y 20 letras")
//    @NotEmpty(message = "Información requerida")
//    private String ApellidoPaterno;
//    @Size(min = 2, max = 20, message = "Texto de entre 2 y 20 letras")
//    @NotEmpty(message = "Información requerida")
//    private String ApellidoMaterno;
//    @NotEmpty(message = "Información requerida")
//    @Size(min = 8)
//    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$", message = "Invalid content type")
//    private String Password;
//    @DateTimeFormat(pattern = "yyyy-MM-dd")
//    private LocalDate FechaNacimiento;
//    @Pattern(regexp = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])", message = "Invalid content type")
//    private String Email;
//    private String Telefono;
//    private String Celular;
//    private String Curp;
//    private String Sexo;
//    private String Img;
//    private int Estatus;
//    public RolML Rol = new RolML();
//    public List<DireccionML> direcciones = new ArrayList<>();
//    public DireccionML Direccion;
