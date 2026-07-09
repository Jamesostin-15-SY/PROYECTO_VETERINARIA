package CONTROLADOR;

import DAO.*;
import MODELO.*;
import VISTA.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import javax.swing.JOptionPane;

public class ControladorDeGestionCitas implements ActionListener {

    private final frmGestionDeCitas vista;
    private final CrudGestionDeCitas CrudCitas;
    public static String nombreMascotaPreseleccionada = "";

    private List<Empleados> listaVeterinarios;
    private List<Object[]> listaEstados;
    private List<Object[]> listaServicios;

    public ControladorDeGestionCitas(frmGestionDeCitas vista) {
        this.vista = vista;
        this.CrudCitas = new CrudGestionDeCitas();

        cargarCombosDesdeBD();

        this.vista.cbxTipodeServicio.addActionListener(this);
        this.vista.btnGuardar.addActionListener(this);
        this.vista.btnNuevo.addActionListener(this);
        this.vista.btnSalir.addActionListener(this);

        bloquearYPreseleccionar();
    }

    private void cargarCombosDesdeBD() {
        vista.cbxNombreMascota.removeAllItems();
        if (nombreMascotaPreseleccionada != null && !nombreMascotaPreseleccionada.isEmpty()) {
            vista.cbxNombreMascota.addItem(nombreMascotaPreseleccionada);
        } else {
            vista.cbxNombreMascota.addItem("Sin Mascota");
        }

        vista.cbxTipodeServicio.removeAllItems();
        listaServicios = CrudCitas.listarServiciosCombo();
        if (listaServicios != null) {
            for (Object[] serv : listaServicios) {
                vista.cbxTipodeServicio.addItem(serv[1].toString());
            }
        }

        actualizarComboVeterinarios();
        vista.cbxEstado.removeAllItems();
        listaEstados = CrudCitas.listarEstadosCombo();
        if (listaEstados != null) {
            for (Object[] est : listaEstados) {
                vista.cbxEstado.addItem(est[1].toString());
            }
        }
    }

    private void actualizarComboVeterinarios() {
        int indexServicio = vista.cbxTipodeServicio.getSelectedIndex();
        if (indexServicio == -1) {
            return;
        }

        int idServicioSeleccionado = (int) listaServicios.get(indexServicio)[0];
        System.out.println("Servicio seleccionado: " + vista.cbxTipodeServicio.getSelectedItem() + " con ID: " + idServicioSeleccionado);

        vista.cbxVeterinario.removeAllItems();

        listaVeterinarios = CrudCitas.listarVeterinariosPorServicio(idServicioSeleccionado);

        if (listaVeterinarios != null && !listaVeterinarios.isEmpty()) {
            for (Empleados emp : listaVeterinarios) {

                String nombreCompleto = "Dr. "+ emp.getPrimer_nombre() + " " + emp.getApellido_paterno();
                vista.cbxVeterinario.addItem(nombreCompleto);
            }
        } else {
            vista.cbxVeterinario.addItem("No hay especialistas disponibles");
        }
    }

    private void bloquearYPreseleccionar() {
        if (vista.cbxNombreMascota.getItemCount() > 0) {
            vista.cbxNombreMascota.setSelectedIndex(0);
        }

        if (listaEstados != null) {
            for (Object[] est : listaEstados) {
                String nombreEstado = est[1].toString();
                if (nombreEstado.equalsIgnoreCase("Pendiente")) {
                    vista.cbxEstado.setSelectedItem(nombreEstado);
                    break;
                }
            }
        }

        vista.cbxNombreMascota.setEnabled(false);
        vista.cbxEstado.setEnabled(false);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vista.cbxTipodeServicio) {
            actualizarComboVeterinarios();
        }
        if (e.getSource() == vista.btnGuardar) {
            ejecutarGuardado();
        }
        if (e.getSource() == vista.btnNuevo) {
            limpiarCampos();
        }
        if (e.getSource() == vista.btnSalir) {
            vista.dispose();
        }
    }

    private void ejecutarGuardado() {
        String motivo = vista.txaMotivoDeCita.getText().trim();

        if (vista.cbxNombreMascota.getSelectedIndex() == -1
                || vista.cbxVeterinario.getSelectedIndex() == -1
                || vista.cbxEstado.getSelectedIndex() == -1
                || motivo.isEmpty()) {

            JOptionPane.showMessageDialog(vista, "Por favor, complete todos los campos obligatorios.", "Campos Vacíos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Date fechaSpinner = (Date) vista.spnFecha.getValue();
        LocalDateTime fechaHora = java.time.LocalDateTime.ofInstant(fechaSpinner.toInstant(), java.time.ZoneId.systemDefault());

        String nomMascota = vista.cbxNombreMascota.getSelectedItem().toString();
        int idMascota = CrudCitas.obtenerIdMascotaPorNombre(nomMascota);

        int indexVet = vista.cbxVeterinario.getSelectedIndex();
        String dniVeterinario = listaVeterinarios.get(indexVet).getDni_empleado();

        int indexEst = vista.cbxEstado.getSelectedIndex();
        int idEstado = (int) listaEstados.get(indexEst)[0];

        int indexServ = vista.cbxTipodeServicio.getSelectedIndex();
        int idServicio = (int) listaServicios.get(indexServ)[0];

        Citas nuevaCita = new Citas();
        nuevaCita.setFk_id_mascota(idMascota);
        nuevaCita.setveterinario(dniVeterinario);
        nuevaCita.setFecha_hora(fechaHora);
        nuevaCita.setFk_id_estado(idEstado);
        nuevaCita.setMotivo_cita(motivo);
        nuevaCita.setFk_id_servicio(1);
        nuevaCita.setFk_id_servicio(idServicio);

        if (CrudCitas.registrarCita(nuevaCita)) {
            JOptionPane.showMessageDialog(vista, "¡Cita registrada correctamente!", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            limpiarCampos();
        } else {
            JOptionPane.showMessageDialog(vista, "Error al guardar la cita en la base de datos.", "Error SQL", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limpiarCampos() {
        vista.txaMotivoDeCita.setText("");
        vista.spnFecha.setValue(new Date());

        if (vista.cbxVeterinario.getItemCount() > 0) {
            vista.cbxVeterinario.setSelectedIndex(0);
        }
        actualizarComboVeterinarios();
        bloquearYPreseleccionar();
    }
}
