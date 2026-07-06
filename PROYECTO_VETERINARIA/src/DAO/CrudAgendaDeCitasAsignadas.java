package DAO;
import MODELO.Citas;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CrudAgendaDeCitasAsignadas {
    public List<Citas> listarCitasAsignadas() {
        List<Citas> lista = new ArrayList<>();
        String sql = "SELECT c.*, m.nombre_mascota, e.primer_nombre AS nombre_vet, " +
                     "s.nombre_servicio, est.nombre_estado " +
                     "FROM citas c " +
                     "INNER JOIN mascotas m ON c.fk_id_mascota = m.id_mascota " +
                     "INNER JOIN empleados e ON c.fk_dni_veterinario = e.dni_empleado " +
                     "INNER JOIN servicios s ON c.fk_id_servicio = s.id_servicio " +
                     "INNER JOIN estados_cita est ON c.fk_id_estado = est.id_estado";
        
        Conexion conObj = new Conexion(); 
        try (Connection con = conObj.getCon();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Citas c = new Citas();
                c.setId_cita(rs.getInt("id_cita"));
                c.nombreMascotaAux = rs.getString("nombre_mascota");
                c.nombreVeterinarioAux = rs.getString("nombre_vet");
                c.nombreServicioAux = rs.getString("nombre_servicio");
                c.nombreEstadoAux = rs.getString("nombre_estado");
                c.setMotivo_cita(rs.getString("motivo_cita"));
                c.setFecha_hora(rs.getTimestamp("fecha_hora").toLocalDateTime());
                lista.add(c);
            }
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        }
        return lista;
    }
}
