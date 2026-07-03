package DAO;
import MODELO.*;
import PROCESOS.*;
import java.sql.*;
public class EmpleadoDAO {
    // Inserta el empleado en la tabla 'empleados'
    public boolean registrar(Empleados empleado) {
        String sql = "INSERT INTO empleados (dni_empleado, primer_nombre, segundo_nombre, " +
                     "apellido_paterno, apellido_materno, telefono, fk_id_puesto, fk_id_turno, fk_id_estado) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        Conexion conexionAux = new Conexion();
        Connection con = conexionAux.getCon();
        if (con == null) {
            return false;
        }
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, empleado.getDni_empleado());
            ps.setString(2, empleado.getPrimer_nombre());
            ps.setString(3, empleado.getSegundo_nombre());
            ps.setString(4, empleado.getApellido_paterno());
            ps.setString(5, empleado.getApellido_materno());
            ps.setString(6, empleado.getTelefono());
            ps.setInt(7, empleado.getFk_id_puesto());
            ps.setInt(8, empleado.getFk_id_turno());
            ps.setInt(9, empleado.getFk_id_estado());
            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            Mensajes.M1("Error al registrar empleado en la BD: " + e.getMessage());
            return false;
        } finally {
            try {
                if (con != null && !con.isClosed()) {
                    con.close();
                }
            } catch (SQLException ex) {
                System.out.println("Error al cerrar conexión: " + ex.getMessage());
            }
        }
    }
    // Inserta la credencial en 'usuarios_credenciales' (se llama DESPUÉS de registrar())
    public boolean registrarCredencial(UsuariosCredenciales credencial) {
        String sql = "INSERT INTO usuarios_credenciales (fk_dni_empleado, usuario, contrasena, fk_id_role) " +
                     "VALUES (?, ?, ?, ?)";
        Conexion conexionAux = new Conexion();
        Connection con = conexionAux.getCon();
        if (con == null) {
            return false;
        }
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, credencial.getFk_dni_empleado());
            ps.setString(2, credencial.getUsuario());
            ps.setString(3, credencial.getContrasena());
            ps.setInt(4, credencial.getFk_id_role());
            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            Mensajes.M1("Error al registrar credencial en la BD: " + e.getMessage());
            return false;
        } finally {
            try {
                if (con != null && !con.isClosed()) {
                    con.close();
                }
            } catch (SQLException ex) {
                System.out.println("Error al cerrar conexión: " + ex.getMessage());
            }
        }
    }

    // Verifica si un nombre de usuario ya existe en usuarios_credenciales
    public boolean existeUsuario(String usuario) {
        String sql = "SELECT COUNT(*) FROM usuarios_credenciales WHERE usuario = ?";
        Conexion conexionAux = new Conexion();
        Connection con = conexionAux.getCon();
        if (con == null) {
            return false;
        }
        try (PreparedStatement ps = con.prepareStatement(sql)) {
        ps.setString(1, usuario);
        try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
         return rs.getInt(1) > 0;
        }
     }
        } catch (SQLException e) {
            Mensajes.M1("Error al verificar usuario: " + e.getMessage());
        } finally {
          try {
          if (con != null && !con.isClosed()) {
              con.close();
                }
            } catch (SQLException ex) {
                System.out.println("Error al cerrar conexión: " + ex.getMessage());
            }
        }
        return false;
    }
}