package DAO;
import MODELO.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import PROCESOS.*;

public class TurnoDAO {
    public List<Turnos> listarTodos() {
        List<Turnos> lista = new ArrayList<>();
        String sql = "SELECT id_turno, nombre_turno FROM turnos ORDER BY id_turno";
        Conexion conexionAux = new Conexion();
        Connection con = conexionAux.getCon();
        if (con == null) {
            return lista;
        }
        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Turnos t = new Turnos();
                t.setId_turno(rs.getInt("id_turno"));
                t.setNombre_turno(rs.getString("nombre_turno"));
                lista.add(t);
            }
        } catch (SQLException e) {
            Mensajes.M1("Error al listar turnos: " + e.getMessage());
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
