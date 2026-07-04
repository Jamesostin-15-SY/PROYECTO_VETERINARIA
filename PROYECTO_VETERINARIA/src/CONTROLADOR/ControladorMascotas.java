package CONTROLADOR;
import VISTA.*;
import DAO.*;
import PROCESOS.*;
import MODELO.*;
import FACTORY.*;
import java.util.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.ZoneId;
import javax.swing.JDesktopPane;
import javax.swing.SwingUtilities;

public class ControladorMascotas implements ActionListener{
    private final frmAgregarMascotas vista;
    private final CrudMascotas CrudEspecies;

    public ControladorMascotas (frmAgregarMascotas vista) {
        this.vista = vista;
        this.CrudEspecies = new CrudMascotas();
        
        
        this.vista.btnRegistrarMas.addActionListener(this);
        this.vista.btnAgregarCita.addActionListener(this);
        
        cargarComboEspecies();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vista.btnRegistrarMas) {
            ejecutarRegistroMascota();
        }
        if (e.getSource() == vista.btnAgregarCita) {
            JDesktopPane contenedor = (JDesktopPane) SwingUtilities.getAncestorOfClass(JDesktopPane.class, vista);
            
            if (contenedor != null) {
                String mascotaRegistrada = vista.txtNombreMascota.getText().trim();
                ControladorDeGestionCitas.nombreMascotaPreseleccionada = mascotaRegistrada;
                vista.dispose(); 
                VistasFactory.CrearVista("GestionDeCitas", "Gestion De Citas", contenedor);
            }
        }
    }
    
    private void cargarComboEspecies() {
        vista.cbxEspecieMascota.removeAllItems();
        vista.cbxEspecieMascota.addItem("- Seleccione Especie -");
        
        List<String> especies = CrudEspecies.listarEspecies();
        for (String especie : especies) {
            vista.cbxEspecieMascota.addItem(especie);
        }
    }
    
    private void ejecutarRegistroMascota() {
        
        if (vista.txtNombreMascota.getText().trim().isEmpty() || 
            vista.cbxEspecieMascota.getSelectedIndex() == 0||
            vista.txtRazaMascota.getText().trim().isEmpty()){
            
            Mensajes.M1("Por favor, ingrese el nombre de la mascota y seleccione una especie.");
            return;
        }
                
        Mascotas nuevaMascota = new Mascotas();
        nuevaMascota.setFk_dni_cliente(vista.txtDniClie2.getText().trim());
        nuevaMascota.setNombre_mascota(vista.txtNombreMascota.getText().trim());
        Date dateJCalendar = vista.jdcFechaNaci.getDate();
        LocalDate fechaNacimientoLocalDate = dateJCalendar.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        nuevaMascota.setFecha_nacimiento(fechaNacimientoLocalDate); 
        String especieSeleccionada = vista.cbxEspecieMascota.getSelectedItem().toString().trim();
        int idEspecie = CrudEspecies.obtenerIdEspeciePorNombre(especieSeleccionada);
        nuevaMascota.setFk_id_especie(idEspecie); 
        
        nuevaMascota.setRaza(vista.txtRazaMascota.getText().trim());

        if (CrudEspecies.RegistrarMascota(nuevaMascota)) {
            Mensajes.M1("¡Mascota registrada exitosamente!");

            vista.btnAgregarCita.setEnabled(true); 
            
            bloquearCamposMascota();
        } else {
            Mensajes.M1("Error al intentar registrar la mascota. Inténtelo de nuevo.");
        }
    }
 
    private void bloquearCamposMascota() {
        vista.txtNombreMascota.setEnabled(false);
        vista.cbxEspecieMascota.setEnabled(false);
        vista.txtRazaMascota.setEnabled(false);
        vista.jdcFechaNaci.setEnabled(false);
        vista.btnRegistrarMas.setEnabled(false);
    }
    
    
}

