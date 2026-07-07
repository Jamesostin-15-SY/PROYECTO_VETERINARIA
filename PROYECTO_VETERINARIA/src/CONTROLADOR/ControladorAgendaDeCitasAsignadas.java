package CONTROLADOR;
import DAO.*;
import MODELO.*;
import VISTA.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.table.DefaultTableModel;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JOptionPane;

public class ControladorAgendaDeCitasAsignadas implements ActionListener{
    private final frmAgendadeCitasAsignadas vista;
    private final CrudAgendaDeCitasAsignadas dao;
    private List<Object[]> listaEstadosFiltrados;
    public ControladorAgendaDeCitasAsignadas(frmAgendadeCitasAsignadas vista) {
        this.vista = vista;
        this.dao = new CrudAgendaDeCitasAsignadas();

        cargarTabla();
        cargarComboEstadosCita();

        this.vista.btnConfirmar.addActionListener(this);
        
        this.vista.tblAgendaDeCitas.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                seleccionarFilaCita();
            }
        });
    }

    public void cargarTabla() {
        DefaultTableModel model = new DefaultTableModel(
            new Object[]{"ID", "Mascota", "Veterinario", "Servicio", "Fecha/Hora", "Estado", "Motivo"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        List<Citas> lista = dao.listarCitasAsignadas();
        
        for (Citas c : lista) {
            model.addRow(new Object[]{
                c.getId_cita(), c.nombreMascotaAux, c.nombreVeterinarioAux, 
                c.nombreServicioAux, c.getFecha_hora().format(formato), 
                c.nombreEstadoAux, c.getMotivo_cita()
            });
        }
        this.vista.tblAgendaDeCitas.setModel(model); 
    }
    private void cargarComboEstadosCita() {
        this.vista.cbxEstadoCita.removeAllItems(); 
        listaEstadosFiltrados = dao.listarEstadosCitaPermitidosCombo();

        if (listaEstadosFiltrados == null) return;

        for (Object[] estado : listaEstadosFiltrados) {
            this.vista.cbxEstadoCita.addItem(estado[1].toString());
        }
    }
    private void seleccionarFilaCita() {
        int filaSeleccionada = this.vista.tblAgendaDeCitas.getSelectedRow(); 
        if (filaSeleccionada != -1) {
            String idCita = this.vista.tblAgendaDeCitas.getValueAt(filaSeleccionada, 0).toString();
            this.vista.txtIdCita.setText(idCita); 
        }
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.vista.btnConfirmar) { 
            ejecutarCambioEstadoCita();
        }
    }
    private void ejecutarCambioEstadoCita() {
        String idCitaStr = this.vista.txtIdCita.getText().trim(); 
        int indexEstado = this.vista.cbxEstadoCita.getSelectedIndex(); 

        if (idCitaStr.isEmpty()) {
            JOptionPane.showMessageDialog(vista, "Por favor, seleccione una cita de la tabla primero.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (indexEstado == -1 || listaEstadosFiltrados == null) return;

        int idCita = Integer.parseInt(idCitaStr);
        int idEstado = (int) listaEstadosFiltrados.get(indexEstado)[0];
        String nombreEstado = listaEstadosFiltrados.get(indexEstado)[1].toString();

        int respuesta = JOptionPane.showConfirmDialog(
                vista, 
                "¿Está seguro de que desea cambiar el estado de la cita N° " + idCita + " a [" + nombreEstado + "]?", 
                "Confirmar Solicitud", 
                JOptionPane.YES_NO_OPTION, 
                JOptionPane.QUESTION_MESSAGE
        );

        if (respuesta == JOptionPane.YES_OPTION) {
            boolean exito = dao.actualizarEstadoCita(idCita, idEstado);
            if (exito) {
                JOptionPane.showMessageDialog(vista, "El estado de la cita se ha actualizado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                this.vista.txtIdCita.setText("");
                cargarTabla();
            } else {
                JOptionPane.showMessageDialog(vista, "No se pudo actualizar el estado de la cita.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
