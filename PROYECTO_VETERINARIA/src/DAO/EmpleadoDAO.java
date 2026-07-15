package DAO;
import MODELO.*;
import PROCESOS.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmpleadoDAO {
    
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

    public Empleados buscarPorDni(String dni) {
        Empleados emp = null;
        String sql = "SELECT * FROM empleados WHERE dni_empleado = ?";
        Conexion conexionAux = new Conexion();
        Connection con = conexionAux.getCon();
        
        if (con == null) return null;

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, dni);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    emp = new Empleados();
                    emp.setDni_empleado(rs.getString("dni_empleado"));
                    emp.setPrimer_nombre(rs.getString("primer_nombre"));
                    emp.setSegundo_nombre(rs.getString("segundo_nombre"));
                    emp.setApellido_paterno(rs.getString("apellido_paterno"));
                    emp.setApellido_materno(rs.getString("apellido_materno"));
                    emp.setTelefono(rs.getString("telefono"));
                    emp.setFk_id_puesto(rs.getInt("fk_id_puesto"));
                    emp.setFk_id_turno(rs.getInt("fk_id_turno"));
                    emp.setFk_id_estado(rs.getInt("fk_id_estado"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar empleado por DNI: " + e.getMessage());
        } finally {
            try {
                if (con != null && !con.isClosed()) con.close();
            } catch (SQLException ex) {
                System.out.println("Error al cerrar conexión: " + ex.getMessage());
            }
        }
        return emp;
    }

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

    // Verifica si un nombre de usuario ya existe
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

    public List<Servicios> listarTodosServicios() {
        List<Servicios> lista = new ArrayList<>();
        String sql = "SELECT id_servicio, nombre_servicio FROM servicios ORDER BY id_servicio";
        Conexion conexionAux = new Conexion();
        Connection con = conexionAux.getCon();
        if (con == null) return lista;

        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Servicios s = new Servicios();
                s.setId_servicio(rs.getInt("id_servicio"));
                s.setNombre_servicio(rs.getString("nombre_servicio"));
                lista.add(s);
            }
        } catch (SQLException e) {
            Mensajes.M1("Error al listar servicios: " + e.getMessage());
        } finally {
            try {
                if (con != null && !con.isClosed()) con.close();
            } catch (SQLException ex) {
                System.out.println("Error al cerrar conexión: " + ex.getMessage());
            }
        }
        return lista;
    }

    public boolean asignarServicio(String dniEmpleado, int idServicio) {
        String sql = "INSERT INTO empleados_servicios (fk_dni_empleado, fk_id_servicio) VALUES (?, ?)";
        Conexion conexionAux = new Conexion();
        Connection con = conexionAux.getCon();
        if (con == null) return false;

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, dniEmpleado);
            ps.setInt(2, idServicio);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            Mensajes.M1("Error al asignar servicio: " + e.getMessage());
            return false;
        } finally {
            try {
                if (con != null && !con.isClosed()) con.close();
            } catch (SQLException ex) {
                System.out.println("Error al cerrar conexión: " + ex.getMessage());
            }
        }
    }
}