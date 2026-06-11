package com.example.quickpass8.entidades;

public class Alumno {
    private Integer id_alumno;
    private String tipoUsuario;
    private String nombrecompleto;
    private String boleta;
    private String contrasena;
    private String grupo;
    private String turno;
    private String semestre;
    private String carrera;

    public Integer getId_alumno() {
        return id_alumno;
    }

    public void setId_alumno(Integer id_alumno) {
        this.id_alumno = id_alumno;
    }

    public String getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(String tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    public String getNombre() {
        return nombrecompleto;
    }

    public void setNombre(String nombre) {
        this.nombrecompleto = nombre; // ← corregido
    }

    public Alumno(Integer id_alumno, String tipoUsuario, String nombrecompleto,
                  String boleta, String contrasena, String grupo, String turno,
                  String semestre, String carrera) { // ← quitado "apellidos", agregado "carrera"
        this.id_alumno = id_alumno;
        this.tipoUsuario = tipoUsuario;
        this.nombrecompleto = nombrecompleto;
        this.boleta = boleta;
        this.contrasena = contrasena;
        this.grupo = grupo;
        this.turno = turno;
        this.semestre = semestre;
        this.carrera = carrera;
    }

    public String getBoleta() {
        return boleta;
    }

    public void setBoleta(String boleta) {
        this.boleta = boleta;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getGrupo() {
        return grupo;
    }

    public void setGrupo(String grupo) {
        this.grupo = grupo;
    }

    public String getTurno() {
        return turno;
    }

    public void setTurno(String turno) {
        this.turno = turno;
    }

    public String getSemestre() {
        return semestre;
    }

    public void setSemestre(String semestre) {
        this.semestre = semestre;
    }

    public String getCarrera() {
        return carrera;
    }

    public void setCarrera(String carrera) {
        this.carrera = carrera;
    }

}
