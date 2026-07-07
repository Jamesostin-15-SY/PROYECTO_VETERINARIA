package CONTROLADOR;

import DAO.*;
import VISTA.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class ControladorAgendaDeCitas {

    private final frmAgendaDeCita vista;
    private DefaultTableModel modeloTabla;
    private final CrudAgendaDeCitas crudAgenda;
    private final DateTimeFormatter formateador = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private final int ID_ESTADO_CANCELADA = 3;

    public ControladorAgendaDeCitas(frmAgendaDeCita vista) {
        this.vista = vista;
        this.crudAgenda = new CrudAgendaDeCitas();

        configurarTabla();
        cargarDatosCitas();
        configurarComponentesCancelacion();
        initEventos();

    }

    private void configurarTabla() {
        String[] columnas = {"ID Cita", "Mascota", "Dueño / Cliente", "Veterinario", "Fecha y Hora", "Servicio", "Estado", "Motivo"};
        modeloTabla = new DefaultTableModel(null, columnas) {

            @Override
            public boolean isCellEditable(int registro, int columna) {
                return false;
            }
        };

        this.vista.tblCitas.setModel(modeloTabla);
    }

    public void cargarDatosCitas() {

        modeloTabla.setRowCount(0);

        List<Object[]> lista = crudAgenda.listarCitas();

        for (Object[] fila : lista) {
            if (fila[4] instanceof java.sql.Timestamp) {
                java.sql.Timestamp timestamp = (java.sql.Timestamp) fila[4];
                fila[4] = timestamp.toLocalDateTime().format(formateador);
            }

            modeloTabla.addRow(fila);
        }
    }

    private void configurarComponentesCancelacion() {
        vista.cbxEstado.removeAllItems();
        vista.cbxEstado.addItem("CANCELADA");
        vista.cbxEstado.setSelectedIndex(0);
        vista.cbxEstado.setEnabled(false);
        vista.txtCita.setEditable(false);
    }

    private void initEventos() {
        this.vista.tblCitas.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int filaSeleccionada = vista.tblCitas.getSelectedRow();
                if (filaSeleccionada != -1) {
                    String idCita = vista.tblCitas.getValueAt(filaSeleccionada, 0).toString();
                    vista.txtCita.setText(idCita);
                }
            }
        });

        this.vista.btnCancelarCita.addActionListener(e -> accionCancelarCita());
    }

    private void accionCancelarCita() {
        String idTxt = vista.txtCita.getText().trim();

        if (idTxt.isEmpty()) {
            JOptionPane.showMessageDialog(vista, "Por favor, seleccione una cita de la tabla o ingrese un ID.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int idCita = Integer.parseInt(idTxt);
            System.out.println("Buscando en la BD la cita con ID exacto: '" + idCita + "'");
            if (!crudAgenda.existeCita(idCita)) {
                JOptionPane.showMessageDialog(vista, "ID no encontrado en el sistema.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int respuesta = JOptionPane.showConfirmDialog(vista, "¿Está seguro de que desea cambiar el estado de la cita a CANCELADA?", "Confirmar Acción", JOptionPane.YES_NO_OPTION);

            if (respuesta == JOptionPane.YES_OPTION) {

                if (crudAgenda.actualizarEstadoCita(idCita, ID_ESTADO_CANCELADA)) {
                    JOptionPane.showMessageDialog(vista, "La cita ha sido cancelada exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    vista.txtCita.setText("");
                    cargarDatosCitas();
                } else {
                    JOptionPane.showMessageDialog(vista, "No se pudo actualizar el estado de la cita.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(vista, "El ID de la cita debe ser un valor numérico.", "Error de formato", JOptionPane.ERROR_MESSAGE);
        }
    }

}
