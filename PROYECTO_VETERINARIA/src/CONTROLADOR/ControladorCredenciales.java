package CONTROLADOR;
import DAO.*;
import MODELO.*;
import VISTA.*;
import PROCESOS.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class ControladorCredenciales implements ActionListener {
    private final frmCredenciales vista;
    private final RolDAO rolDAO;
    private final EmpleadoDAO empleadoDAO;
    private List<Roles> listaRoles;
    // Datos capturados SOLO SI el usuario confirma (aún no se guardan en BD)
    private String usuarioIngresado;
    private String contrasenaIngresada;
    private int idRolSeleccionado;
    private boolean confirmado = false;
    public ControladorCredenciales(frmCredenciales vista) {
        this.vista = vista;
        this.rolDAO = new RolDAO();
        this.empleadoDAO = new EmpleadoDAO();
        cargarRoles();
        this.vista.btnComfirmar.addActionListener(this);
        this.vista.btnCancelar.addActionListener(this);
    }
    private void cargarRoles() {
        listaRoles = rolDAO.listarTodos();
        vista.cbxRol.removeAllItems();
        for (Roles r : listaRoles) {
            vista.cbxRol.addItem(r.getNombre_role());
        }
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vista.btnComfirmar) {
            confirmarCredencial();
        }
        if (e.getSource() == vista.btnCancelar) {
            confirmado = false;
            vista.dispose();
        }
    }
    private void confirmarCredencial() {
        String usuario = vista.txtUsuario.getText().trim();
        String contrasena = new String(vista.jpdContraseña.getPassword()).trim();
        int indiceSeleccionado = vista.cbxRol.getSelectedIndex();
        if (usuario.isEmpty() || contrasena.isEmpty()) {
            Mensajes.M1("Por favor, complete usuario y contraseña.");
            return;
        }
        if (indiceSeleccionado < 0) {
            Mensajes.M1("Seleccione un rol.");
            return;
        }
        if (empleadoDAO.existeUsuario(usuario)) {
            Mensajes.M1("Ese nombre de usuario ya existe. Elija otro.");
            return;
        }
        this.usuarioIngresado = usuario;
        this.contrasenaIngresada = contrasena;
        this.idRolSeleccionado = listaRoles.get(indiceSeleccionado).getId_role();
        this.confirmado = true;
        vista.dispose(); // cierra el diálogo modal, el flujo regresa a quien lo abrió
    }
    // Getters para que ControladorEmpleado recupere los datos después
    public boolean isConfirmado() { return confirmado; }
    public String getUsuarioIngresado() { return usuarioIngresado; }
    public String getContrasenaIngresada() { return contrasenaIngresada; }
    public int getIdRolSeleccionado() { return idRolSeleccionado; }
}