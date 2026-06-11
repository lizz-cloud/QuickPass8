package com.example.quickpass8.utilidades;

public class Utilidades {

    public static final String TABLA_ALUMNO = "Alumno";

    public static final String CAMPO_ID = "id_alumno";
    public static final String CAMPO_TIPO_USUARIO = "tipo_usuario";
    public static final String CAMPO_NOMBRECOMPLETO = "nombrecompleto";
    public static final String CAMPO_BOLETA = "boleta";
    public static final String CAMPO_CONTRASENA = "contrasena";
    public static final String CAMPO_GRUPO = "grupo";
    public static final String CAMPO_TURNO = "turno";
    public static final String CAMPO_SEMESTRE = "semestre";
    public static final String CAMPO_CARRERA = "carrera";

    // Tabla Tareas
    public static final String TABLA_TAREAS = "Tareas";

    public static final String CAMPO_TAREA_ID       = "id_tarea";
    public static final String CAMPO_TAREA_BOLETA   = "boleta";
    public static final String CAMPO_TAREA_TEXTO    = "texto";
    public static final String CAMPO_TAREA_IMAGEN   = "imagen_index";

    public static final String CREAR_TABLA_TAREAS =
            "CREATE TABLE " + TABLA_TAREAS + " (" +
                    CAMPO_TAREA_ID     + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    CAMPO_TAREA_BOLETA + " TEXT, " +
                    CAMPO_TAREA_TEXTO  + " TEXT, " +
                    CAMPO_TAREA_IMAGEN + " INTEGER)";

    public static final String CREAR_TABLA_ALUMNO =
            "CREATE TABLE " + TABLA_ALUMNO + " (" +
                    CAMPO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    CAMPO_TIPO_USUARIO + " TEXT, " +
                    CAMPO_NOMBRECOMPLETO + " TEXT, " +
                    CAMPO_BOLETA + " TEXT UNIQUE, " +
                    CAMPO_CONTRASENA + " TEXT, " +
                    CAMPO_GRUPO + " TEXT, " +
                    CAMPO_TURNO + " TEXT, " +
                    CAMPO_SEMESTRE + " TEXT, " +
                    CAMPO_CARRERA + " TEXT)";
}

