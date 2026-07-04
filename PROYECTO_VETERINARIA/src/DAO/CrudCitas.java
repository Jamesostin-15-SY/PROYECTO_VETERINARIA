package DAO;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import MODELO.*;

public class CrudCitas extends Conexion{
    public boolean registrarCita(Citas cita) {
        Connection cn = getCon();
        String sql = "INSERT INTO citas (fk_id_mascota, fk_dni_veterinario, fecha_hora, fk_id_servicio, fk_id_estado, motivo_cita) "
                   + "VALUES (?, ?, ?, ?, ?, ?)";

        try {
            PreparedStatement ps = cn.prepareStatement(sql);
            ps.setInt(1, cita.getFk_id_mascota()); 
            ps.setString(2, cita.getveterinario()); 
            ps.setTimestamp(3, Timestamp.valueOf(cita.getFecha_hora())); 
            ps.setInt(4, cita.getFk_id_servicio()); 
            ps.setInt(5, cita.getFk_id_estado());
            ps.setString(6, cita.getMotivo_cita());

            int resultado = ps.executeUpdate();
            ps.close();
            return resultado > 0;
        } catch (Exception e) {
            System.out.println("Error al registrar la cita en DAO: " + e);
            return false;
        }
    }

    public List<Object[]> listarMascotasCombo() {
        List<Object[]> lista = new ArrayList<>();
        Connection cn = getCon();
        String sql = "SELECT id_mascota, nombre_mascota FROM mascotas";
        try {
            PreparedStatement ps = cn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(new Object[]{ rs.getInt("id_mascota"), rs.getString("nombre_mascota") });
            }
            ps.close();
            rs.close();
        } catch (Exception e) {
            System.out.println("Error al cargar mascotas en el combo: " + e);
        }
        return lista;
    }

    public List<Object[]> listarVeterinariosCombo() {
        List<Object[]> lista = new ArrayList<>();
        Connection cn = getCon();
        String sql = "SELECT dni_empleado, concat(primer_nombre, ' ', apellido_paterno) AS nombre FROM empleados";
        try {
            PreparedStatement ps = cn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(new Object[]{ rs.getString("dni_empleado"), rs.getString("nombre") });
            }
            ps.close();
            rs.close();
        } catch (Exception e) {
            System.out.println("Error al cargar veterinarios: " + e);
        }
        return lista;
    }

    public List<Object[]> listarEstadosCombo() {
        List<Object[]> lista = new ArrayList<>();
        Connection cn = getCon();
        String sql = "SELECT id_estado, nombre_estado FROM estados_cita";
        try {
            PreparedStatement ps = cn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(new Object[]{ rs.getInt("id_estado"), rs.getString("nombre_estado") });
            }
            ps.close();
            rs.close();
        } catch (Exception e) {
            System.out.println("Error al cargar estados: " + e);
        }
        return lista;
    }
    
}
