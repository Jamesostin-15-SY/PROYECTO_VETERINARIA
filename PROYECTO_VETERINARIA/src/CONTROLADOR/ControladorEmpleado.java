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

    // Listas fijas: qué labor corresponde a cada puesto (filtro en Java, sin tocar la BD)
    private static final List<String> LABORES_VETERINARIO = Arrays.asList(
            "Médico Veterinario General", "Cirujano Veterinario", "Fisioterapeuta Veterinario");
    private static final List<String> LABORES_AUXILIAR = Arrays.asList(
            "Bañista", "Peluquero Canino", "Limpieza", "Técnica Veterinaria");

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
        this.vista.txtPuesto.addActionListener(this); // NUEVO: escucha cambios de puesto

        actualizarListaServicios(); // deja el JList correcto desde que se abre la ventana
    }

    private void cargarCombos() {
        // La tabla 'puestos' en la BD ya solo tiene los 4 correctos, no hace falta filtrar aquí
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

    // NUEVO: trae UNA sola vez las 7 labores completas desde la BD
    private void cargarServiciosDisponibles() {
        listaServiciosTodos = empleadoDAO.listarTodosServicios();
    }

    // NUEVO: según el puesto seleccionado, decide qué labores mostrar en el JList
    private void actualizarListaServicios() {
        int idxPuesto = vista.txtPuesto.getSelectedIndex();
        if (idxPuesto < 0 || listaPuestos.isEmpty()) {
            return;
        }

        String puesto = listaPuestos.get(idxPuesto).getNombre_puesto().trim().toUpperCase();

        List<String> permitidas;
        if (puesto.equals("VETERINARIO")) {
            permitidas = LABORES_VETERINARIO;
        } else if (puesto.equals("AUXILIAR")) {
            permitidas = LABORES_AUXILIAR;
        } else {
            // ADMINISTRADOR o RECEPCIONISTA no requieren labores/servicios
            permitidas = new ArrayList<>();
        }

        listaServiciosFiltrados = new ArrayList<>();
        DefaultListModel<String> modelo = new DefaultListModel<>();
        for (Servicios s : listaServiciosTodos) {
            if (permitidas.contains(s.getNombre_servicio())) {
                modelo.addElement(s.getNombre_servicio());
                listaServiciosFiltrados.add(s);
            }
        }
        vista.lstServicios.setModel(modelo);
        vista.lstServicios.setEnabled(!permitidas.isEmpty());
        vista.lstServicios.clearSelection();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vista.txtPuesto) {
            actualizarListaServicios();
        }
        if (e.getSource() == vista.btnAgregarCredencial) {
            abrirDialogoCredencial();
        }
        if (e.getSource() == vista.btnRegistrarEmpleado) {
            ejecutarRegistro();
        }
    }

    // Valida los datos del empleado ANTES de dejar pasar al diálogo de credencial
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

        // NUEVO: solo exige seleccionar labor si el puesto es Veterinario o Auxiliar
        String puesto = listaPuestos.get(vista.txtPuesto.getSelectedIndex()).getNombre_puesto().trim().toUpperCase();
        boolean requiereLabor = puesto.equals("VETERINARIO") || puesto.equals("AUXILIAR");
        if (requiereLabor && vista.lstServicios.isSelectionEmpty()) {
            Mensajes.M1("Seleccione al menos una labor/servicio para este puesto.");
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

        // NUEVO: registrar las labores/servicios seleccionadas (relación N:N) usando la lista FILTRADA
        int[] indicesSeleccionados = vista.lstServicios.getSelectedIndices();
        String dniEmpleadoNuevo = vista.txtDni.getText().trim();
        for (int idx : indicesSeleccionados) {
            int idServicio = listaServiciosFiltrados.get(idx).getId_servicio();
            empleadoDAO.asignarServicio(dniEmpleadoNuevo, idServicio);
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
        vista.lstServicios.setEnabled(false);
        vista.btnAgregarCredencial.setEnabled(false);
        vista.btnRegistrarEmpleado.setEnabled(false);
    }
}