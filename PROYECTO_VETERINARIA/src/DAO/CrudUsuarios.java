package DAO;
import MODELO.*;
public interface CrudUsuarios {
    public UsuariosCredenciales ValidarAcceso(String user, String pass);
}
