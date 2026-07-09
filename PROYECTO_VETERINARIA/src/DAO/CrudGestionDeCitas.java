package DAO;

import java.util.*;
import java.sql.*;
import MODELO.*;

public class CrudGestionDeCitas {

    private final Conexion conectar = new Conexion();
    private Connection con;
    private PreparedStatement ps;
    private ResultSet rs;

    public List<Object[]> listarServiciosCombo() {
        List<Object[]> lista = new ArrayList<>();

        String sql = "SELECT id_servicio, nombre_servicio FROM servicios ORDER BY nombre_servicio ASC";

        Conexion conAux = new Conexion();
        Connection con = conAux.getCon();
        if (con == null) {
            return lista;
        }

        try (PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new Object[]{rs.getInt("id_servicio"), rs.getString("nombre_servicio")});
            }
        } catch (SQLException e) {
            System.out.println("Error al listar servicios: " + e.getMessage());
        } finally {

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

    public List<Empleados> listarVeterinariosPorServicio(int idServicio) {
        List<Empleados> lista = new ArrayList<>();
        String sql = "SELECT e.dni_empleado, e.primer_nombre, e.apellido_paterno "+
                     "FROM empleados_servicios es "+
                     "INNER JOIN empleados e ON es.fk_dni_empleado = e.dni_empleado "+
                     "WHERE es.fk_id_servicio = ?";

        Conexion conAux = new Conexion();
        Connection con = conAux.getCon();
        if (con == null) {
            return lista;
        }

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idServicio);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Empleados emp = new Empleados();
                    emp.setDni_empleado(rs.getString("dni_empleado"));
                    emp.setPrimer_nombre(rs.getString("primer_nombre"));
                    emp.setApellido_paterno(rs.getString("apellido_paterno"));

                    lista.add(emp);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al filtrar empleados: " + e.getMessage());
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

    public int obtenerIdMascotaPorNombre(String nombre) {
        int idMascota = 0;
        String sql = "SELECT id_mascota FROM mascotas WHERE nombre_mascota = ?";

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
            if (rs != null) {
                rs.close();
            }
            if (ps != null) {
                ps.close();
            }
        } catch (SQLException e) {
            System.out.println("Error al cerrar recursos: " + e.toString());
        }
    }
    
    public boolean registrarServicio(Servicios servicio, String descripcion) {
    // Forzamos el uso de las columnas correctas
    String sql = "INSERT INTO servicios (nombre_servicio, precio, descripcion, fk_id_estado_serv) VALUES (?, ?, ?, ?)";

    try {
        con = conectar.getCon();
        ps = con.prepareStatement(sql);
        ps.setString(1, servicio.getNombre_servicio());
        ps.setDouble(2, servicio.getPrecio());
        ps.setString(3, descripcion);
        ps.setInt(4, 1); // Registra por defecto con el ID de estado 1 ('DISPONIBLE')

        int resultado = ps.executeUpdate();
        return resultado > 0;
    } catch (SQLException e) {
        System.out.println("Error crítico al registrar servicio: " + e.getMessage());
        return false;
    } finally {
        cerrarRecursos();
    }
}

   public List<Object[]> listarServiciosTabla() {
        List<Object[]> lista = new ArrayList<>();
        // Unimos las tablas para traer el nombre del estado real de la BD
        String sql = "SELECT s.id_servicio, s.nombre_servicio, s.precio, s.descripcion, e.nombre_estado_serv " +
                     "FROM servicios s " +
                     "INNER JOIN estados_servicios e ON s.fk_id_estado_serv = e.id_estado_serv";

        try {
            con = conectar.getCon();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                lista.add(new Object[]{
                    rs.getInt("id_servicio"),            // [0] ID
                    rs.getString("nombre_servicio"),     // [1] Nombre
                    rs.getDouble("precio"),              // [2] Precio
                    rs.getString("descripcion"),         // [3] Descripción
                    rs.getString("nombre_estado_serv")   // [4] Estado (¡Nueva columna!)
                });
            }
        } catch (SQLException e) {
            System.out.println("Error al listar servicios en la tabla: " + e.toString());
        } finally {
            cerrarRecursos();
        }
        return lista;
    }

    // 2. NUEVO MÉTODO PARA LISTAR LOS ESTADOS DISPONIBLES EN EL COMBOBOX INFERIOR
    public List<Object[]> listarEstadosServiciosCombo() {
        List<Object[]> lista = new ArrayList<>();
        String sql = "SELECT id_estado_serv, nombre_estado_serv FROM estados_servicios";

        try {
            con = conectar.getCon();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                lista.add(new Object[]{rs.getInt("id_estado_serv"), rs.getString("nombre_estado_serv")});
            }
        } catch (SQLException e) {
            System.out.println("Error al listar estados de servicios: " + e.toString());
        } finally {
            cerrarRecursos();
        }
        return lista;
    }

    // 3. NUEVO MÉTODO PARA ACTUALIZAR EL ESTADO
    public boolean modificarEstadoServicio(int idServicio, int idEstadoNuevo) {
        String sql = "UPDATE servicios SET fk_id_estado_serv = ? WHERE id_servicio = ?";

        try {
            con = conectar.getCon();
            ps = con.prepareStatement(sql);
            ps.setInt(1, idEstadoNuevo);
            ps.setInt(2, idServicio);

            int resultado = ps.executeUpdate();
            return resultado > 0;
        } catch (SQLException e) {
            System.out.println("Error al modificar el estado del servicio: " + e.toString());
            return false;
        } finally {
            cerrarRecursos();
        }
    }
}
