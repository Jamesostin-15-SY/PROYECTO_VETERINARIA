package DAO;
import MODELO.Puestos;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import PROCESOS.*;

public class PuestoDAO {
    public List<Puestos> listarTodos() {
        List<Puestos> lista = new ArrayList<>();
        String sql = "SELECT id_puesto, nombre_puesto FROM puestos ORDER BY nombre_puesto";

        Conexion conexionAux = new Conexion();
        Connection con = conexionAux.getCon();

        if (con == null) {
            return lista;
        }

        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Puestos p = new Puestos();
                p.setId_puesto(rs.getInt("id_puesto"));
                p.setNombre_puesto(rs.getString("nombre_puesto"));
                lista.add(p);
            }

        } catch (SQLException e) {
            Mensajes.M1("Error al listar puestos: " + e.getMessage());
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
}