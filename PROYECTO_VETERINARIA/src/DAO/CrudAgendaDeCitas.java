package DAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import MODELO.*;

public class CrudAgendaDeCitas extends Conexion {

    public List<Object[]> listarCitas() {
        List<Object[]> lista = new ArrayList<>();
        Connection cn = getCon();

        String sql = "SELECT c.id_cita, m.nombre_mascota, concat(cl.primer_nombre, ' ', cl.apellido_paterno) AS cliente, "
                + "concat(e.primer_nombre, ' ', e.apellido_paterno) AS veterinario, c.fecha_hora, s.nombre_servicio, ec.nombre_estado, c.motivo_cita "
                + "FROM citas c "
                + "INNER JOIN mascotas m ON c.fk_id_mascota = m.id_mascota "
                + "INNER JOIN clientes cl ON m.fk_dni_cliente = cl.dni_cliente "
                + "INNER JOIN empleados e ON c.fk_dni_veterinario = e.dni_empleado "
                + "INNER JOIN servicios s ON c.fk_id_servicio = s.id_servicio "
                + "INNER JOIN estados_cita ec ON c.fk_id_estado = ec.id_estado";

        try {
            PreparedStatement ps = cn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                Object[] fila = new Object[8];
                fila[0] = rs.getInt("id_cita");
                fila[1] = rs.getString("nombre_mascota");
                fila[2] = rs.getString("cliente");
                fila[3] = rs.getString("veterinario");
                fila[4] = rs.getTimestamp("fecha_hora");
                fila[5] = rs.getString("nombre_servicio");
                fila[6] = rs.getString("nombre_estado");
                fila[7] = rs.getString("motivo_cita");

                lista.add(fila);
            }

            ps.close();
            rs.close();
        } catch (Exception e) {
            System.out.println("Error al listar las citas en el DAO: " + e);
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
}
