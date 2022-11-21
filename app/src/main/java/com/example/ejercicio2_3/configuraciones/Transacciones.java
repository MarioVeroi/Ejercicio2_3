package com.example.ejercicio2_3.configuraciones;

public class Transacciones {

    public static final String NameDatabase = "Photograh";
    public static final String tablaPhotograh = "Photograh";
    public static final String id = "id";
    public static final String descripcion = "descripcion";
    public static final String imagen = "imagen";
    public static final String CreateTablePhotograh = "CREATE TABLE "+tablaPhotograh + "(id INTEGER PRIMARY KEY AUTOINCREMENT,"+
            "descripcion TEXT, imagen BLOB)";

    public static final String DropTablePhotograh = "DROP TABLE IF EXISTS "+ tablaPhotograh;
}