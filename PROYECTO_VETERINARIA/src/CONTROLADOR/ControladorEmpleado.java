package CONTROLADOR;
import DAO.*;
import MODELO.*;
import VISTA.*;
import PROCESOS.*;
import javax.swing.JOptionPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
public class ControladorEmpleado implements ActionListener {
    private final frmAgregarEmpleado vista;
    private final EmpleadoDAO empleadoDAO;
    private final PuestoDAO puestoDAO;
    private final TurnoDAO turnoDAO;
    private List<Puestos> listaPuestos;
    private List<Turnos> listaTurnos;
    private boolean credencialLista = false;
    private String usuarioCredencial;
    private String contrasenaCredencial;
    private int idRolCredencial;
    private static final int ESTADO_ACTIVO = 1;

    public ControladorEmpleado(frmAgregarEmpleado vista) {
        this.vista = vista;
        this.empleadoDAO = new EmpleadoDAO();
        this.puestoDAO = new PuestoDAO();
        this.turnoDAO = new TurnoDAO();

        cargarCombos();

        this.vista.btnRegistrarEmpleado.setEnabled(false);
        this.vista.btnAgregarCredencial.addActionListener(this);
        this.vista.btnRegistrarEmpleado.addActionListener(this);
    }
    private void cargarCombos() {
        listaPuestos = puestoDAO.listarTodos();
        vista.txtPuesto.removeAllItems();
        for (Puestos p : listaPuestos) {
            vista.txtPuesto.addItem(p.getNombre_puesto());
        }
        listaTurnos = turnoDAO.listarTodos();
        vista.cbxTurno.removeAllItems();
        for (Turnos t : listaTurnos) {
            vista.cbxTurno.addItem(t.getNombre_turno());
        }
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vista.btnAgregarCredencial) {
            abrirDialogoCredencial();
        }
        if (e.getSource() == vista.btnRegistrarEmpleado) {
            ejecutarRegistro();
        }
    }
    // NUEVO: valida los datos del empleado ANTES de dejar pasar al diálogo de credencial
    private boolean validarDatosEmpleado() {
        if (vista.txtDni.getText().trim().isEmpty() ||
            vista.txtPrimerNombre.getText().trim().isEmpty() ||
            vista.txtApellidoPaterno.getText().trim().isEmpty() ||
            vista.txtApellidoMaterno.getText().trim().isEmpty() ||
            vista.txtNumeroDeTelefono.getText().trim().isEmpty()) {

            Mensajes.M1("Por favor, complete los campos obligatorios antes de agregar la credencial.");
            return false;
        }
        if (vista.txtDni.getText().trim().length() != 8) {
            Mensajes.M1("El DNI debe tener exactamente 8 dígitos.");
            return false;
        }
          if (vista.txtNumeroDeTelefono.getText().trim().length() != 9) {
        Mensajes.M1("El número de teléfono debe tener exactamente 9 dígitos.");
        return false;
        }
        if (vista.txtPuesto.getSelectedIndex() < 0 || vista.cbxTurno.getSelectedIndex() < 0) {
            Mensajes.M1("Seleccione puesto y turno antes de agregar la credencial.");
            return false;
        }
        return true;
    }
    private void abrirDialogoCredencial() {
        // Validamos primero, antes de gastar tiempo llenando el diálogo
        if (!validarDatosEmpleado()) {
            return;
         }
        
       java.awt.Frame parentFrame = JOptionPane.getFrameForComponent(vista);
        frmCredenciales dialogo = new frmCredenciales(parentFrame, true);
        ControladorCredenciales controladorCred = new ControladorCredenciales(dialogo);

        dialogo.setLocationRelativeTo(null);
        dialogo.setVisible(true);
        
        if (controladorCred.isConfirmado()) {
            this.usuarioCredencial = controladorCred.getUsuarioIngresado();
            this.contrasenaCredencial = controladorCred.getContrasenaIngresada();
            this.idRolCredencial = controladorCred.getIdRolSeleccionado();
            this.credencialLista = true;
            vista.btnRegistrarEmpleado.setEnabled(true);
            Mensajes.M1("Credencial lista. Ahora puede registrar al empleado.");
        } else {
            Mensajes.M1("No se agregó ninguna credencial.");
        }
    }
    private void ejecutarRegistro() {
        // Estas validaciones se mantienen como respaldo (doble seguro)
        if (!validarDatosEmpleado()) {
            return;
        }
        if (!credencialLista) {
            Mensajes.M1("Debe agregar la credencial antes de registrar.");
            return;
        }
        int indicePuesto = vista.txtPuesto.getSelectedIndex();
        int indiceTurno = vista.cbxTurno.getSelectedIndex();
        Empleados nuevoEmpleado = new Empleados();
        nuevoEmpleado.setDni_empleado(vista.txtDni.getText().trim());
        nuevoEmpleado.setPrimer_nombre(vista.txtPrimerNombre.getText().trim());
        nuevoEmpleado.setSegundo_nombre(vista.txtSegundoNombre.getText().trim());
        nuevoEmpleado.setApellido_paterno(vista.txtApellidoPaterno.getText().trim());
        nuevoEmpleado.setApellido_materno(vista.txtApellidoMaterno.getText().trim());
        nuevoEmpleado.setTelefono(vista.txtNumeroDeTelefono.getText().trim());
        nuevoEmpleado.setFk_id_puesto(listaPuestos.get(indicePuesto).getId_puesto());
        nuevoEmpleado.setFk_id_turno(listaTurnos.get(indiceTurno).getId_turno());
        nuevoEmpleado.setFk_id_estado(ESTADO_ACTIVO);
        if (!empleadoDAO.registrar(nuevoEmpleado)) {
            Mensajes.M1("Error al registrar el empleado. Verifique si el DNI ya existe.");
            return;
        }
        UsuariosCredenciales nuevaCredencial = new UsuariosCredenciales();
        nuevaCredencial.setFk_dni_empleado(vista.txtDni.getText().trim());
        nuevaCredencial.setUsuario(usuarioCredencial);
        nuevaCredencial.setContrasena(contrasenaCredencial);
        nuevaCredencial.setFk_id_role(idRolCredencial);

        if (empleadoDAO.registrarCredencial(nuevaCredencial)) {
            Mensajes.M1("¡Empleado y credencial registrados exitosamente!");
            bloquearCampos();
        } else {
            Mensajes.M1("El empleado se registró, pero hubo un error al guardar la credencial.");
        }
    }
    private void bloquearCampos() {
        vista.txtDni.setEnabled(false);
        vista.txtPrimerNombre.setEnabled(false);
        vista.txtSegundoNombre.setEnabled(false);
        vista.txtApellidoPaterno.setEnabled(false);
        vista.txtApellidoMaterno.setEnabled(false);
        vista.txtNumeroDeTelefono.setEnabled(false);
        vista.cbxTurno.setEnabled(false);
        vista.txtPuesto.setEnabled(false);
        vista.btnAgregarCredencial.setEnabled(false);
        vista.btnRegistrarEmpleado.setEnabled(false);
    }
}