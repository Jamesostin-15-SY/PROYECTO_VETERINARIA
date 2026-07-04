package CONTROLADOR;

import DAO.*;
import MODELO.*;
import VISTA.*;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;

public class ControladorLogin implements ActionListener {

    private final frmLogin vista;
    private final CrudUsuarioslmp crudUsuarios;

    // Constructor: Enlaza la vista y activa los listeners de los botones
    public ControladorLogin(frmLogin vistaLogin) {
        this.vista = vistaLogin;
        this.crudUsuarios = new CrudUsuarioslmp();
        
        // Registrar los botones en el ActionListener
        this.vista.btnIngresar.addActionListener(this);
        this.vista.btnSalir.addActionListener(this);
    }

    // Configura e inicia la ventana visual del login
    public void iniciar() {
        vista.setTitle("Iniciar Sesión - Veterinaria");
        vista.setLocationRelativeTo(null);
        vista.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Evento al presionar el botón Ingresar
        if (e.getSource() == vista.btnIngresar) {
            ejecutarLogueo();
        }

        // Evento al presionar el botón Salir
        if (e.getSource() == vista.btnSalir) {
            System.exit(0);
        }
    }

    private void ejecutarLogueo() {
        String usuario = vista.txtUsuario.getText().trim();
        // Cambia vistaLogin.txtContrasena por vistaLogin.jpfContraseña
        String contrasena = new String(vista.jpfContrasenia.getPassword()).trim();

        //jpfContraseña.getPassword()).trim();
        // Validación de campos vacíos
        if (usuario.isEmpty() || contrasena.isEmpty()) {
            JOptionPane.showMessageDialog(vista, "Por favor, complete ambos campos.", "Campos Vacíos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Consulta mediante tu método en CrudUsuariosImp
        UsuariosCredenciales usuarioValido = crudUsuarios.ValidarAcceso(usuario, contrasena);

        if (usuarioValido != null) {
            JOptionPane.showMessageDialog(vista, "¡Bienvenido " + usuarioValido.getUsuario() + "!", "Acceso Autorizado", JOptionPane.INFORMATION_MESSAGE);
            
            // Instanciar el menú principal y transferir el control a su controlador dedicado
            frmMenusVeterinaria vistaMenu = new frmMenusVeterinaria();
            ControladorMenu controladorMenu = new ControladorMenu(vistaMenu, usuarioValido);
            controladorMenu.iniciar();
            
            // Cerrar de forma limpia la ventana de login
            vista.dispose();
        } else {
            JOptionPane.showMessageDialog(vista, "Usuario o contraseña incorrectos.", "Error de Login", JOptionPane.ERROR_MESSAGE);
            limpiarCajas();
        }
    }

    private void limpiarCajas() {
        vista.txtUsuario.setText("");
        vista.jpfContrasenia.setText("");
        vista.txtUsuario.requestFocus();
    }
}