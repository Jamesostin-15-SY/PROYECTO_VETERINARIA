package DAO;
import MODELO.*;
import java.sql.*;

public class CrudUsuarioslmp extends Conexion implements CrudUsuarios {
    
    @Override
    public UsuariosCredenciales ValidarAcceso(String user, String pass) {
        UsuariosCredenciales uc = null;
        Connection cn = getCon();
    String sql = "SELECT * FROM usuarios_credenciales WHERE BINARY usuario = ? AND BINARY contrasena = ?";
        
        try {
            PreparedStatement ps = cn.prepareStatement(sql);
            ps.setString(1, user);
            ps.setString(2, pass);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                uc = new UsuariosCredenciales();
                uc.setUsuario(rs.getString("usuario"));
                uc.setContrasena(rs.getString("contrasena"));
                uc.setFk_id_role(rs.getInt("fk_id_role"));
                
                uc.setFk_dni_empleado(rs.getString("fk_dni_empleado"));
            }
            ps.close();
            rs.close();
        } catch (Exception e) {
            System.out.println("Error al validar credenciales: " + e);
        }
        return uc;
    }
}
