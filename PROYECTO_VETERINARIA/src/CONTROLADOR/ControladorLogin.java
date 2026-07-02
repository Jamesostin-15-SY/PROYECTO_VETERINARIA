package CONTROLADOR;

import DAO.*;
import MODELO.UsuariosCredenciales;
import VISTA.frmLogin;
import VISTA.frmMenusVeterinaria;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;

public class ControladorLogin implements ActionListener {

    private final frmLogin vistaLogin;
    private final CrudUsuarioslmp crudUsuarios;

    // Constructor: Enlaza la vista y activa los listeners de los botones
    public ControladorLogin(frmLogin vistaLogin) {
        this.vistaLogin = vistaLogin;
        this.crudUsuarios = new CrudUsuarioslmp();
        
        // Registrar los botones en el ActionListener
        this.vistaLogin.btnIngresar.addActionListener(this);
        this.vistaLogin.btnSalir.addActionListener(this);
    }

    // Configura e inicia la ventana visual del login
    public void iniciar() {
        vistaLogin.setTitle("Iniciar Sesión - Veterinaria");
        vistaLogin.setLocationRelativeTo(null);
        vistaLogin.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Evento al presionar el botón Ingresar
        if (e.getSource() == vistaLogin.btnIngresar) {
            ejecutarLogueo();
        }

        // Evento al presionar el botón Salir
        if (e.getSource() == vistaLogin.btnSalir) {
            System.exit(0);
        }
    }

    private void ejecutarLogueo() {
        String usuario = vistaLogin.txtUsuario.getText().trim();
        
      
   // Cambia vistaLogin.txtContrasena por vistaLogin.jpfContraseña
String contrasena = new String(vistaLogin.jpfContrasenia.getPassword()).trim();

//jpfContraseña.getPassword()).trim();
        // Validación de campos vacíos
        if (usuario.isEmpty() || contrasena.isEmpty()) {
            JOptionPane.showMessageDialog(vistaLogin, "Por favor, complete ambos campos.", "Campos Vacíos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Consulta mediante tu método en CrudUsuariosImp
        UsuariosCredenciales usuarioValido = crudUsuarios.ValidarAcceso(usuario, contrasena);

        if (usuarioValido != null) {
            JOptionPane.showMessageDialog(vistaLogin, "¡Bienvenido " + usuarioValido.getUsuario() + "!", "Acceso Autorizado", JOptionPane.INFORMATION_MESSAGE);
            
            // Instanciar el menú principal y transferir el control a su controlador dedicado
            frmMenusVeterinaria vistaMenu = new frmMenusVeterinaria();
            ControladorMenu controladorMenu = new ControladorMenu(vistaMenu, usuarioValido);
            controladorMenu.iniciar();
            
            // Cerrar de forma limpia la ventana de login
            vistaLogin.dispose();
        } else {
            JOptionPane.showMessageDialog(vistaLogin, "Usuario o contraseña incorrectos.", "Error de Login", JOptionPane.ERROR_MESSAGE);
            limpiarCajas();
        }
    }

    private void limpiarCajas() {
        vistaLogin.txtUsuario.setText("");
        vistaLogin.jpfContrasenia.setText("");
        vistaLogin.txtUsuario.requestFocus();
    }
}