package CONTROLADOR;
import DAO.*;
import MODELO.*;
import VISTA.*;
import PROCESOS.*;
import javax.swing.JOptionPane;
import javax.swing.DefaultListModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ControladorEmpleado implements ActionListener {
    private final frmAgregarEmpleado vista;
    private final EmpleadoDAO empleadoDAO;
    private final PuestoDAO puestoDAO;
    private final TurnoDAO turnoDAO;

    private List<Puestos> listaPuestos;
    private List<Turnos> listaTurnos;
    private List<Servicios> listaServiciosTodos;
    private List<Servicios> listaServiciosFiltrados;

    private boolean credencialLista = false;
    private String usuarioCredencial;
    private String contrasenaCredencial;
    private int idRolCredencial;
    private static final int ESTADO_ACTIVO = 1;

    // Puestos que SI tienen acceso al sistema (requieren credencial de usuario/contraseña)
    private static final List<String> PUESTOS_CON_ACCESO = Arrays.asList(
            "ADMINISTRADOR", "RECEPCIONISTA", "VETERINARIO", "VETERINARIO ESPECIALISTA");

    // Labores/servicios que se muestran en el JList según el puesto elegido
    private static final List<String> LABORES_VETERINARIO = Arrays.asList(
            "Consulta Veterinaria General", "Intervención Quirúrgica");

    private static final List<String> LABORES_VETERINARIO_ESPECIALISTA = Arrays.asList(
            "Cardiología Veterinaria", "Dermatología Veterinaria", "Fisioterapia y Rehabilitación",
            "Oftalmología Veterinaria", "Oncología Veterinaria", "Odontología Veterinaria");

    private static final List<String> LABORES_OTROS_PUESTOS = Arrays.asList(
            "Logística y Traslados", "Mantenimiento", "Saneamiento Canino", "Paseos Caninos");

    private static final List<String> LABORES_AUXILIAR = Arrays.asList(
            "Asistente Practicante");

    public ControladorEmpleado(frmAgregarEmpleado vista) {
        this.vista = vista;
        this.empleadoDAO = new EmpleadoDAO();
        this.puestoDAO = new PuestoDAO();
        this.turnoDAO = new TurnoDAO();

        cargarCombos();
        cargarServiciosDisponibles();

        this.vista.btnRegistrarEmpleado.setEnabled(false);
        this.vista.btnAgregarCredencial.addActionListener(this);
        this.vista.btnRegistrarEmpleado.addActionListener(this);
        this.vista.txtPuesto.addActionListener(this);

        actualizarListaServicios();
        actualizarBotonesSegunPuesto();
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

    private void cargarServiciosDisponibles() {
        listaServiciosTodos = empleadoDAO.listarTodosServicios();
    }

    private String getPuestoSeleccionado() {
        int idx = vista.txtPuesto.getSelectedIndex();
        if (idx < 0 || listaPuestos.isEmpty()) {
            return "";
        }
        return listaPuestos.get(idx).getNombre_puesto().trim().toUpperCase();
    }

    // Devuelve la lista de labores permitidas según el puesto (o vacía si no aplica)
    private List<String> obtenerLaboresPermitidas(String puesto) {
        switch (puesto) {
            case "VETERINARIO":
                return LABORES_VETERINARIO;
            case "VETERINARIO ESPECIALISTA":
                return LABORES_VETERINARIO_ESPECIALISTA;
            case "OTROS PUESTOS":
                return LABORES_OTROS_PUESTOS;
            case "AUXILIAR":
                return LABORES_AUXILIAR;
            default:
                // ADMINISTRADOR y RECEPCIONISTA no requieren labores
                return new ArrayList<>();
        }
    }

    // Filtra el JList de labores según el puesto elegido
    private void actualizarListaServicios() {
        String puesto = getPuestoSeleccionado();
        if (puesto.isEmpty()) {
            return;
        }

        List<String> permitidas = obtenerLaboresPermitidas(puesto);

        listaServiciosFiltrados = new ArrayList<>();
        DefaultListModel<String> modelo = new DefaultListModel<>();
        for (Servicios s : listaServiciosTodos) {
            if (permitidas.contains(s.getNombre_servicio())) {
                modelo.addElement(s.getNombre_servicio());
                listaServiciosFiltrados.add(s);
            }
        }
        vista.lstServicios.setModel(modelo);

        boolean habilitado = !permitidas.isEmpty();
        vista.lstServicios.setEnabled(habilitado);
        vista.lstServicios.setBackground(habilitado ? java.awt.Color.WHITE : java.awt.Color.LIGHT_GRAY);
        vista.lstServicios.clearSelection();
    }

    // Habilita/deshabilita "Agregar Credencial" y "Registrar Empleado" según el puesto
    private void actualizarBotonesSegunPuesto() {
        String puesto = getPuestoSeleccionado();
        if (puesto.isEmpty()) {
            return;
        }

        boolean requiereAcceso = PUESTOS_CON_ACCESO.contains(puesto);
        vista.btnAgregarCredencial.setEnabled(requiereAcceso);

        if (!requiereAcceso) {
            // Puesto sin acceso al sistema: se puede registrar directo, sin credencial
            credencialLista = false;
            vista.btnRegistrarEmpleado.setEnabled(true);
        } else {
            // Puesto con acceso: solo se habilita Registrar cuando ya se agregó la credencial
            vista.btnRegistrarEmpleado.setEnabled(credencialLista);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vista.txtPuesto) {
            credencialLista = false; // al cambiar de puesto, se reinicia el estado de credencial
            actualizarListaServicios();
            actualizarBotonesSegunPuesto();
        }
        if (e.getSource() == vista.btnAgregarCredencial) {
            abrirDialogoCredencial();
        }
        if (e.getSource() == vista.btnRegistrarEmpleado) {
            ejecutarRegistro();
        }
    }

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

        String puesto = getPuestoSeleccionado();
        boolean requiereLabor = !obtenerLaboresPermitidas(puesto).isEmpty();
        if (requiereLabor && vista.lstServicios.isSelectionEmpty()) {
            Mensajes.M1("Seleccione al menos una labor/servicio para este puesto.");
            return false;
        }
        return true;
    }

    private void abrirDialogoCredencial() {
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
        if (!validarDatosEmpleado()) {
            return;
        }

        String puesto = getPuestoSeleccionado();
        boolean requiereAcceso = PUESTOS_CON_ACCESO.contains(puesto);

        if (requiereAcceso && !credencialLista) {
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

        // Registrar las labores/servicios seleccionadas (relación N:N)
        int[] indicesSeleccionados = vista.lstServicios.getSelectedIndices();
        String dniEmpleadoNuevo = vista.txtDni.getText().trim();
        for (int idx : indicesSeleccionados) {
            int idServicio = listaServiciosFiltrados.get(idx).getId_servicio();
            empleadoDAO.asignarServicio(dniEmpleadoNuevo, idServicio);
        }

        // Solo se registra credencial si el puesto requiere acceso al sistema
        if (requiereAcceso) {
            UsuariosCredenciales nuevaCredencial = new UsuariosCredenciales();
            nuevaCredencial.setFk_dni_empleado(vista.txtDni.getText().trim());
            nuevaCredencial.setUsuario(usuarioCredencial);
            nuevaCredencial.setContrasena(contrasenaCredencial);
            nuevaCredencial.setFk_id_role(idRolCredencial);

            if (empleadoDAO.registrarCredencial(nuevaCredencial)) {
                Mensajes.M1("¡Empleado y credencial registrados exitosamente!");
            } else {
                Mensajes.M1("El empleado se registró, pero hubo un error al guardar la credencial.");
            }
        } else {
            Mensajes.M1("¡Empleado registrado exitosamente!");
        }

        bloquearCampos();
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
        vista.lstServicios.setEnabled(false);
        vista.btnAgregarCredencial.setEnabled(false);
        vista.btnRegistrarEmpleado.setEnabled(false);
    }
}