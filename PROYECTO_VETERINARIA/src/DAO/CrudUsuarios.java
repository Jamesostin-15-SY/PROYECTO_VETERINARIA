package DAO;
import MODELO.*;
public interface CrudUsuarios {
    // Retorna el objeto completo con su Rol si los datos son correctos, o null si no existe
    public UsuariosCredenciales ValidarAcceso(String user, String pass);
}
