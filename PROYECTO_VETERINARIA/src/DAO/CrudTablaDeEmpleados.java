package DAO;

import java.sql.*;
import java.util.*;

public class CrudTablaDeEmpleados extends Conexion {

    public List<Object[]> listarEmpleadosConServicios() {
        List<Object[]> lista = new ArrayList<>();
        Connection cn = getCon();
        if (cn == null) {
            return lista;
        }

        String sql = "SELECT e.dni_empleado, "
                + "CONCAT(e.primer_nombre, ' ', COALESCE(e.segundo_nombre, '')) AS nombres, "
                + "CONCAT(e.apellido_paterno, ' ', e.apellido_materno) AS apellidos, "
                + "e.telefono, p.nombre_puesto, t.nombre_turno, est.nombre_estado, "
                + "COALESCE(GROUP_CONCAT(s.nombre_servicio SEPARATOR ', '), 'Sin Servicio Asignado') AS servicios "
                + "FROM empleados e "
                + "INNER JOIN puestos p ON e.fk_id_puesto = p.id_puesto "
                + "INNER JOIN turnos t ON e.fk_id_turno = t.id_turno "
                + "INNER JOIN estados_empleados est ON e.fk_id_estado = est.id_estado_emp "
                + "LEFT JOIN empleados_servicios es ON e.dni_empleado = es.fk_dni_empleado "
                + "LEFT JOIN servicios s ON es.fk_id_servicio = s.id_servicio "
                + "GROUP BY e.dni_empleado, e.primer_nombre, e.segundo_nombre, e.apellido_paterno, e.apellido_materno, e.telefono, p.nombre_puesto, t.nombre_turno, est.nombre_estado";

        try (PreparedStatement ps = cn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Object[] fila = new Object[8];
                fila[0] = rs.getString("dni_empleado");
                fila[1] = rs.getString("nombres").trim().replaceAll("\\s+", " ");
                fila[2] = rs.getString("apellidos");
                fila[3] = rs.getString("telefono");
                fila[4] = rs.getString("nombre_puesto");
                fila[5] = rs.getString("nombre_turno");
                fila[6] = rs.getString("nombre_estado");
                fila[7] = rs.getString("servicios");

                lista.add(fila);
            }

        } catch (SQLException e) {
            System.out.println("Error al listar empleados en el DAO: " + e.getMessage());
        } finally {
            try {
                if (cn != null && !cn.isClosed()) {
                    cn.close();
                }
            } catch (SQLException ex) {
                System.out.println("Error al cerrar conexión: " + ex.getMessage());
            }
        }
        return lista;
    }

    public List<Object[]> listarEstadosEmpleadosCombo() {
        List<Object[]> lista = new ArrayList<>();
        Connection cn = getCon();
        if (cn == null) {
            return lista;
        }

        String sql = "SELECT id_estado_emp, nombre_estado FROM estados_empleados";
        try (PreparedStatement ps = cn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new Object[]{rs.getInt("id_estado_emp"), rs.getString("nombre_estado")});
            }
        } catch (SQLException e) {
            System.out.println("Error al listar estados de empleados: " + e.getMessage());
        }
        return lista;
    }

    public boolean actualizarEstadoEmpleado(String dni, int idEstado) {
        Connection cn = getCon();
        if (cn == null) {
            return false;
        }

        String sql = "UPDATE empleados SET fk_id_estado = ? WHERE dni_empleado = ?"; //
        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, idEstado);
            ps.setString(2, dni);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al actualizar estado del empleado: " + e.getMessage());
            return false;
        } finally {
            try {
                if (cn != null && !cn.isClosed()) {
                    cn.close();
                }
            } catch (SQLException ex) {
                System.out.println("Error al cerrar conexión: " + ex.getMessage());
            }
        }

    }
}
