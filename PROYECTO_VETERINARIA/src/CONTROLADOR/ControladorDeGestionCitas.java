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

public class ControladorDeGestionCitas implements ActionListener{
    private final frmGestionDeCitas vista;
    private final CrudCitas crudCitas;
    private String nombreMascotaPreseleccionada = "";
    
    private List<Object[]> listaMascotas;
    private List<Object[]> listaVeterinarios;
    private List<Object[]> listaEstados;

    public ControladorDeGestionCitas(frmGestionDeCitas vista) {
        this.vista = vista;
        this.crudCitas = new CrudCitas();
        

        cargarCombos();
        bloquearYPreseleccionar();
        
        this.vista.btnGuardar.addActionListener(this);
        this.vista.btnNuevo.addActionListener(this);
        this.vista.btnSalir.addActionListener(this);
    }

    private void cargarCombos() {
    
        vista.cbxNombreMascota.removeAllItems();
        listaMascotas = crudCitas.listarMascotasCombo(); 
        for (Object[] masc : listaMascotas) {
            vista.cbxNombreMascota.addItem(masc[1].toString());
        }

    
        vista.cbxVeterinario.removeAllItems();
        listaVeterinarios = crudCitas.listarVeterinariosCombo();
        for (Object[] vet : listaVeterinarios) {
            vista.cbxVeterinario.addItem(vet[1].toString()); 
        }

    
        vista.cbxEstado.removeAllItems();
        listaEstados = crudCitas.listarEstadosCombo();
        for (Object[] est : listaEstados) {
            vista.cbxEstado.addItem(est[1].toString()); 
        }

    
        vista.cbxNombreMascota.setEnabled(false);
        vista.cbxEstado.setEnabled(false);
    }
    private void bloquearYPreseleccionar() {
        
        vista.cbxNombreMascota.setEnabled(false);
        vista.cbxEstado.setEnabled(false);

        
        if (nombreMascotaPreseleccionada != null && !nombreMascotaPreseleccionada.isEmpty()) {
            vista.cbxNombreMascota.setSelectedItem(nombreMascotaPreseleccionada);
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
    }

    @Override
    public void actionPerformed(ActionEvent e) {
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

        if (vista.cbxNombreMascota.getSelectedIndex() == -1 || 
            vista.cbxVeterinario.getSelectedIndex() == -1 || 
            vista.cbxEstado.getSelectedIndex() == -1 || 
            motivo.isEmpty()) {
            
            JOptionPane.showMessageDialog(vista, "Por favor, complete todos los campos obligatorios.", "Campos Vacíos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Date fechaSpinner = (Date) vista.spnFecha.getValue();
        LocalDateTime fechaHora = java.time.LocalDateTime.ofInstant(fechaSpinner.toInstant(), java.time.ZoneId.systemDefault());

        int indexMasc = vista.cbxNombreMascota.getSelectedIndex();
        int idMascota = (int) listaMascotas.get(indexMasc)[0];

        int indexVet = vista.cbxVeterinario.getSelectedIndex();
        String dniVeterinario = listaVeterinarios.get(indexVet)[0].toString();

        int indexEst = vista.cbxEstado.getSelectedIndex();
        int idEstado = (int) listaEstados.get(indexEst)[0];

        Citas nuevaCita = new Citas();
        nuevaCita.setFk_id_mascota(idMascota); 
        nuevaCita.setveterinario(dniVeterinario); 
        nuevaCita.setFecha_hora(fechaHora);
        nuevaCita.setFk_id_estado(idEstado);
        nuevaCita.setMotivo_cita(motivo);
        nuevaCita.setFk_id_servicio(1); 

        if (crudCitas.registrarCita(nuevaCita)) {
            JOptionPane.showMessageDialog(vista, "¡Cita registrada correctamente!", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            limpiarCampos();
        } else {
            JOptionPane.showMessageDialog(vista, "Error al guardar la cita en la base de datos.", "Error SQL", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limpiarCampos() {
        vista.txaMotivoDeCita.setText("");
        vista.spnFecha.setValue(new Date()); 
        
        if (vista.cbxNombreMascota.getItemCount() > 0) vista.cbxNombreMascota.setSelectedIndex(0);
        if (vista.cbxVeterinario.getItemCount() > 0) vista.cbxVeterinario.setSelectedIndex(0);
        if (vista.cbxEstado.getItemCount() > 0) vista.cbxEstado.setSelectedIndex(0);
        bloquearYPreseleccionar();
        if (vista.cbxVeterinario.getItemCount() > 0) vista.cbxVeterinario.setSelectedIndex(0);
    }
}
