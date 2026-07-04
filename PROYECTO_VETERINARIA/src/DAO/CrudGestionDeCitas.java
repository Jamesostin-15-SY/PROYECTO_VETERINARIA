package DAO;
import java.util.*;
import java.sql.*;
import MODELO.*;
public class CrudGestionDeCitas {
    private final Conexion conectar = new Conexion(); 
    private Connection con;
    private PreparedStatement ps;
    private ResultSet rs;

    public List<Object[]> listarVeterinariosCombo() {
        List<Object[]> lista = new ArrayList<>();
        String sql = "SELECT dni_empleado, nombre FROM empleados WHERE fk_id_puesto = (SELECT id_puesto FROM puestos WHERE nombre_puesto = 'Veterinario')"; 
        
        try {
            con = conectar.getCon(); 
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                Object[] veterinario = new Object[2];
                veterinario[0] = rs.getString("dni_empleado");
                veterinario[1] = rs.getString("nombre");
                lista.add(veterinario);
            }
        } catch (SQLException e) {
            System.out.println("Error al listar veterinarios: " + e.toString());
        } finally {
            cerrarRecursos();
        }
        return lista;
    }

    public List<Object[]> listarEstadosCombo() {
        List<Object[]> lista = new ArrayList<>();
        String sql = "SELECT id_estado, nombre_estado FROM estados_cita"; 
        
        try {
            con = conectar.getCon();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                Object[] estado = new Object[2];
                estado[0] = rs.getInt("id_estado");
                estado[1] = rs.getString("nombre_estado");
                lista.add(estado);
            }
        } catch (SQLException e) {
            System.out.println("Error al listar estados: " + e.toString());
        } finally {
            cerrarRecursos();
        }
        return lista;
    }

    public int obtenerIdMascotaPorNombre(String nombre) {
        int idMascota = 0;
        String sql = "SELECT id_mascota FROM mascotas WHERE nombre_mascota = ?"; // Ajusta a tu columna
        
        try {
            con = conectar.getCon();
            ps = con.prepareStatement(sql);
            ps.setString(1, nombre);
            rs = ps.executeQuery();
            
            if (rs.next()) {
                idMascota = rs.getInt("id_mascota");
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener ID de mascota: " + e.toString());
        } finally {
            cerrarRecursos();
        }
        return idMascota;
    }

    public boolean registrarCita(Citas cita) {
        String sql = "INSERT INTO citas (fk_id_mascota, fk_dni_veterinario, fecha_hora, fk_id_estado, motivo_cita, fk_id_servicio) VALUES (?, ?, ?, ?, ?, ?)";
        
        try {
            con = conectar.getCon();
            ps = con.prepareStatement(sql);
            
            ps.setInt(1, cita.getFk_id_mascota());
            ps.setString(2, cita.getveterinario());
            
            ps.setTimestamp(3, Timestamp.valueOf(cita.getFecha_hora()));
            
            ps.setInt(4, cita.getFk_id_estado());
            ps.setString(5, cita.getMotivo_cita());
            ps.setInt(6, cita.getFk_id_servicio());
            
            int resultado = ps.executeUpdate();
            return resultado > 0;
            
        } catch (SQLException e) {
            System.out.println("Error al registrar la cita: " + e.toString());
            return false;
        } finally {
            cerrarRecursos();
        }
    }
    
    private void cerrarRecursos() {
        try {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
        } catch (SQLException e) {
            System.out.println("Error al cerrar recursos: " + e.toString());
        }
    }
}
