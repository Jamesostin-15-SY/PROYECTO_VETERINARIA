package DAO;

import java.sql.*;
import Procesos.*;

public class Conexion implements Parametros {

    private Connection con;
    
    public Conexion() {
        try {
            Class.forName(DRIVER);
            con = DriverManager.getConnection(URL, USUARIO, CLAVE);            
        } catch (Exception ex) {
            Mensajes.M1("ERROR NO SE PUEDE CONECTAR A LA BASE DE DATOS VETERINARIA..." + ex);
        }  
    }
    public Connection getCon() {return con;}
    public void setCon(Connection con) {this.con = con;}  
}