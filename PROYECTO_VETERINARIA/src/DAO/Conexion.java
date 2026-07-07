package DAO;

import java.sql.*;
import PROCESOS.*;

public class Conexion implements Parametros {

    private Connection con;

    public Conexion() {
        conectar(); // Trasladamos la lógica a un método propio para poder reutilizarlo
    }
    
    // Método privado para abrir o reabrir la conexión
    private void conectar() {
        try {
            Class.forName(DRIVER);
            con = DriverManager.getConnection(URL, USUARIO, CLAVE);
        } catch (Exception ex) {
            Mensajes.M1("ERROR NO SE PUEDE CONECTAR A LA BASE DE DATOS VETERINARIA..." + ex);
        }
    }

    // Modificamos el getCon para que verifique si la conexión sigue viva
    public Connection getCon() {
        try {
            // Si la conexión es nula o el método close() fue ejecutado previamente sobre ella, la volvemos a abrir
            if (con == null || con.isClosed()) {
                conectar();
            }
        } catch (SQLException ex) {
            System.out.println("Error al verificar el estado de la conexión: " + ex.getMessage());
        }
        return con;
    }

    public void setCon(Connection con) { this.con = con; }
}