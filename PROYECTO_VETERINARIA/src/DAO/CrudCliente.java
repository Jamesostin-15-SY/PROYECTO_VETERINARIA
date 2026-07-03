package DAO;
import MODELO.*;
import PROCESOS.*;
import java.sql.*;
public class CrudCliente {
    public boolean registrar(Clientes cliente) {
        String sql = "INSERT INTO clientes (dni_cliente, primer_nombre, segundo_nombre, " +
                     "apellido_paterno, apellido_materno, direccion, telefono) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        //Instanciamos tu clase Conexion para que ejecute su constructor
        Conexion conexionAux = new Conexion(); 
        
        //Extraemos el objeto Connection usando tu getter getCon()
        Connection con = conexionAux.getCon();
        
        //Validamos que la conexión no sea null antes de continuar
        if (con == null) {
            return false; 
        }

        //Ejecutamos el PreparedStatement
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, cliente.getDni_cliente());
            ps.setString(2, cliente.getPrimer_nombre());
            ps.setString(3, cliente.getSegundo_nombre()); 
            ps.setString(4, cliente.getApellido_paterno());
            ps.setString(5, cliente.getApellido_materno());
            ps.setString(6, cliente.getDireccion());
            ps.setString(7, cliente.getTelefono());
            
            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;
            
        } catch (Exception e) {
            Mensajes.M1("Error al registrar cliente en la BD: " + e.getMessage());
            return false;
        } finally {
            try {
                if (con != null && !con.isClosed()) {
                    con.close();
                }
            } catch (Exception ex) {
                System.out.println("Error al cerrar conexión: " + ex.getMessage());
            }
        }
    }
}
