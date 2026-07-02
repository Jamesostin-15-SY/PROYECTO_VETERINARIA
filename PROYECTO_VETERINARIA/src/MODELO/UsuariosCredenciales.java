package MODELO; 

public class UsuariosCredenciales {
    
    // Atributos privados
    private int id_usuario;
    private String fk_dni_empleado;
    private String usuario;
    private String contrasena;
    private int fk_id_role;

    // Constructor vacío
    public UsuariosCredenciales() {}

    // Getters y Setters
    public int getId_usuario() { return id_usuario; }
    public void setId_usuario(int id_usuario) {this.id_usuario = id_usuario;   }
    public String getFk_dni_empleado() {return fk_dni_empleado; }
    public void setFk_dni_empleado(String fk_dni_empleado) {this.fk_dni_empleado = fk_dni_empleado;   }
    public String getUsuario() {return usuario; }
    public void setUsuario(String usuario) {this.usuario = usuario;    }
    public String getContrasena() {return contrasena; }
    public void setContrasena(String contrasena) { this.contrasena = contrasena;}
    public int getFk_id_role() {return fk_id_role;}
    public void setFk_id_role(int fk_id_role) { this.fk_id_role = fk_id_role; }
    // Método para devolver los datos en un Object para las tablas
    public Object[] registro(int num) {
        Object[] fila = {num, this.id_usuario, this.usuario, this.fk_id_role};
        return fila;
    }
}