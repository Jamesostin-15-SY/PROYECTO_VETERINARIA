package DAO;

import MODELO.*;
import PROCESOS.*;
import java.util.*;
import java.sql.*;

public class CrudMascotas {

    public List<String> listarEspecies() {
        List<String> lista = new ArrayList<>();
        // Buscamos el nombre de la especie para mostrárselo al usuario
        String sql = "SELECT nombre_especie FROM especies ORDER BY nombre_especie ASC";

        Conexion conAux = new Conexion();
        Connection con = conAux.getCon();

        if (con == null) {
            return lista;
        }

        try (PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(rs.getString("nombre_especie"));
            }

        } catch (SQLException e) {
            Mensajes.M1("Error al listar especies: " + e.getMessage());
        } finally {
            try {
                if (con != null && !con.isClosed()) {
                    con.close();
                }
            } catch (SQLException ex) {
                System.out.println("Error al cerrar conexión: " + ex.getMessage());
            }
        }
        return lista;
    }

    public boolean RegistrarMascota(Mascotas mascota) {

        String sql = "INSERT INTO mascotas (fk_dni_cliente, nombre_mascota, fecha_nacimiento, fk_id_especie, raza) "
                + "VALUES (?, ?, ?, ?, ?)";

        Conexion ConexionAux = new Conexion();
        Connection con = ConexionAux.getCon();

        if (con == null) {
            return false;
        }

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, mascota.getFk_dni_cliente());
            ps.setString(2, mascota.getNombre_mascota());
            ps.setDate(3, java.sql.Date.valueOf(mascota.getFecha_nacimiento()));
            ps.setInt(4, mascota.getFk_id_especie());
            ps.setString(5, mascota.getRaza());

            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;

        } catch (Exception e) {
            Mensajes.M1("Error al registrar mascota en la BD: " + e.getMessage());
            return false;
        } finally {
            try {
                if (con != null && !con.isClosed()) {
                    con.close();
                }
            } catch (Exception ex) {
                System.out.println("Error al cerrar conexión: " + ex.getMessage());
            }
        }
    }

    public int obtenerIdEspeciePorNombre(String nombreEspecie) {
        String sql = "SELECT id_especie FROM especies WHERE nombre_especie = ?"; //
        Conexion conAux = new Conexion();
        Connection con = conAux.getCon();
        if (con == null) {
            return 0;
        }

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nombreEspecie);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id_especie"); //
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener ID de especie: " + e.getMessage());
        }
        return 0;
    }
}
