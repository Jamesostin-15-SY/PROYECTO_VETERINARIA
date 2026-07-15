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

    public ControladorLogin(frmLogin vistaLogin) {
        this.vista = vistaLogin;
        this.crudUsuarios = new CrudUsuarioslmp();
        
        this.vista.btnIngresar.addActionListener(this);
        this.vista.btnSalir.addActionListener(this);
    }

    public void iniciar() {
        vista.setTitle("Iniciar Sesión - Veterinaria");
        vista.setLocationRelativeTo(null);
        vista.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vista.btnIngresar) {
            ejecutarLogueo();
        }

        if (e.getSource() == vista.btnSalir) {
            System.exit(0);
        }
    }

    private void ejecutarLogueo() {
        String usuario = vista.txtUsuario.getText().trim();
        String contrasena = new String(vista.jpfContrasenia.getPassword()).trim();

        if (usuario.isEmpty() || contrasena.isEmpty()) {
            JOptionPane.showMessageDialog(vista, "Por favor, complete ambos campos.", "Campos Vacíos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        UsuariosCredenciales usuarioValido = crudUsuarios.ValidarAcceso(usuario, contrasena);

        if (usuarioValido != null) {
            System.out.println("DEBUG: Usuario logueado: " + usuarioValido.getUsuario());
            System.out.println("DEBUG: DNI asociado: " + usuarioValido.getFk_dni_empleado());
            
            JOptionPane.showMessageDialog(vista, "¡Bienvenido " + usuarioValido.getUsuario() + "!", "Acceso Autorizado", JOptionPane.INFORMATION_MESSAGE);
            
            frmMenusVeterinaria vistaMenu = new frmMenusVeterinaria();
            ControladorMenu controladorMenu = new ControladorMenu(vistaMenu, usuarioValido);
            controladorMenu.iniciar();
            
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