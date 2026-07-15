package DAO;

import MODELO.Citas;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CrudAgendaDeCitasAsignadas {

    public List<Citas> listarCitasAsignadas(String dniVeterinario) {
        System.out.println("DEBUG: Consultando citas para el DNI: " + dniVeterinario);
        
        List<Citas> lista = new ArrayList<>();
        
        
        
        String sql = "SELECT c.id_cita, m.nombre_mascota, e.primer_nombre AS nombre_vet, "
                + "s.nombre_servicio, est.nombre_estado, c.motivo_cita, c.fecha_hora "
                + "FROM citas c "
                + "INNER JOIN mascotas m ON c.fk_id_mascota = m.id_mascota "
                + "INNER JOIN empleados e ON c.fk_dni_veterinario = e.dni_empleado "
                + "INNER JOIN servicios s ON c.fk_id_servicio = s.id_servicio "
                + "INNER JOIN estados_cita est ON c.fk_id_estado = est.id_estado "
                + "WHERE c.fk_dni_veterinario = ?"; 

        Conexion conObj = new Conexion();
        
        try (Connection con = conObj.getCon(); PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, dniVeterinario);
            
            try (ResultSet rs = ps.executeQuery()) {
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
            }
        } catch (SQLException e) {
            System.err.println("Error en listarCitasAsignadas: " + e.getMessage());
        }
        return lista;
    }

    public List<Object[]> listarEstadosCitaPermitidosCombo() {
        List<Object[]> lista = new ArrayList<>();
        Conexion conObj = new Conexion();

        String sql = "SELECT id_estado, nombre_estado FROM estados_cita WHERE nombre_estado IN ('ATENDIDA', 'FINALIZADA')";

        try (Connection con = conObj.getCon(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new Object[]{rs.getInt("id_estado"), rs.getString("nombre_estado")});
            }
        } catch (SQLException e) {
            System.err.println("Error al listar estados de cita: " + e.getMessage());
        }
        return lista;
    }

    public boolean actualizarEstadoCita(int idCita, int idEstado) {
        Conexion conObj = new Conexion();
        String sql = "UPDATE citas SET fk_id_estado = ? WHERE id_cita = ?"; 

        try (Connection con = conObj.getCon(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idEstado);
            ps.setInt(2, idCita);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar estado de la cita: " + e.getMessage());
            return false;
        }
    }
}