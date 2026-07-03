package DAO;
import MODELO.Roles;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import PROCESOS.*;
public class RolDAO {
    public List<Roles> listarTodos() {
        List<Roles> lista = new ArrayList<>();
        String sql = "SELECT id_role, nombre_role FROM roles ORDER BY id_role";
        Conexion conexionAux = new Conexion();
        Connection con = conexionAux.getCon();
        if (con == null) {
            return lista;
        }
        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Roles r = new Roles();
                r.setId_role(rs.getInt("id_role"));
                r.setNombre_role(rs.getString("nombre_role"));
                lista.add(r);
            }
        } catch (SQLException e) {
            Mensajes.M1("Error al listar roles: " + e.getMessage());
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