package PROCESOS;

public class ProcesosLogin {
    public static UsuariosCredenciales Leer(frmLogin fl) {
        UsuariosCredenciales uc = new UsuariosCredenciales();
        uc.setUsuario(fl.txtUsuario.getText());
        // Convertimos el password array a String de forma segura
        uc.setContrasena(new String(fl.jpfContrasenia.getPassword())); 
        return uc;
    }
     public static void LimpiarEntradas(frmLogin fl) {
         
        fl.txtUsuario.setText("");
        fl.jpfContrasenia.setText("");
        fl.txtUsuario.requestFocus();
    }

}
