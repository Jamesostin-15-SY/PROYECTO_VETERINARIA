package CONTROLADOR;
import DAO.*;
import VISTA.*;
import PROCESOS.*;
import MODELO.*;
import FACTORY.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JDesktopPane;
import javax.swing.SwingUtilities;
public class ControladorCliente implements ActionListener{

    private frmRegistrarClientes vista;
    private CrudCliente dao;

    public ControladorCliente(frmRegistrarClientes vista) {
        this.vista = vista;
        this.dao = new CrudCliente();
        
        // Escuchamos el botón de la interfaz
        this.vista.btnRegistrarCliente.addActionListener(this);
        this.vista.btnAgregarMascota.addActionListener(this);
        
        // Nos aseguramos de que el botón de mascota empiece deshabilitado al abrir la ventana
        this.vista.btnAgregarMascota.setEnabled(false);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vista.btnRegistrarCliente) {
            ejecutarRegistro();
        }
        
        if (e.getSource() == vista.btnAgregarMascota) {
            JDesktopPane contenedor = (JDesktopPane) SwingUtilities.getAncestorOfClass(JDesktopPane.class, vista);

            if (contenedor != null) {
                String dniPasar = vista.txtDniClie.getText().trim();
                vista.dispose(); 
            
                VistasFactory.CrearVista("AgregarMascota", "Registrar Nueva Mascota", contenedor);
                for (javax.swing.JInternalFrame iframe : contenedor.getAllFrames()) {
                    if (iframe instanceof frmAgregarMascotas) {
                        frmAgregarMascotas vistaMascota = (frmAgregarMascotas) iframe;
                        vistaMascota.txtDniClie2.setText(dniPasar);
                        vistaMascota.txtDniClie2.setEnabled(false);
                        break;
                    }
                }
            }
        }
    }
    private void ejecutarRegistro() {
        // 1. Validaciones básicas de campos vacíos
        if (vista.txtDniClie.getText().trim().isEmpty() || 
            vista.txtNombreClie.getText().trim().isEmpty() ||
            vista.txtApellidoPCli.getText().trim().isEmpty() ||
            vista.txtApellidoMCli.getText().trim().isEmpty()||
            vista.txtDireccionClie.getText().trim().isEmpty()||
            vista.txtTelefonoClie.getText().trim().isEmpty())
            {
            
            Mensajes.M1("Por favor, complete los campos obligatorios (DNI, Primer Nombre y Apellidos).");
            return;
        }
        
        // Validación de longitud del DNI
        if (vista.txtDniClie.getText().trim().length() != 8) {
            Mensajes.M1("El DNI debe tener exactamente 8 dígitos.");
            return;
        }
        if (vista.txtTelefonoClie.getText().trim().length() != 9){
            Mensajes.M1("El numero de telefono deb ser de 9 digitos");
            return;
        }

        // 2. Pasamos los datos de la interfaz al Modelo (en plural)
        Clientes nuevoCliente = new Clientes();
        nuevoCliente.setDni_cliente(vista.txtDniClie.getText().trim());
        nuevoCliente.setPrimer_nombre(vista.txtNombreClie.getText().trim());
        nuevoCliente.setSegundo_nombre(vista.txtSegundoNCli.getText().trim());
        nuevoCliente.setApellido_paterno(vista.txtApellidoPCli.getText().trim());
        nuevoCliente.setApellido_materno(vista.txtApellidoMCli.getText().trim());
        nuevoCliente.setDireccion(vista.txtDireccionClie.getText().trim());
        nuevoCliente.setTelefono(vista.txtTelefonoClie.getText().trim());

        // 3. Llamamos al DAO para que lo inserte en MySQL
        if (dao.registrar(nuevoCliente)) {
            Mensajes.M1("¡Cliente registrado exitosamente!");
            vista.btnAgregarMascota.setEnabled(true);
            
            bloquearCamposCliente();
        } else {
            Mensajes.M1("Error al registrar el cliente. Verifique si el DNI ya existe.");
        }
    }
    
    
    private void bloquearCamposCliente() {
        vista.txtDniClie.setEnabled(false);
        vista.txtNombreClie.setEnabled(false);
        vista.txtSegundoNCli.setEnabled(false);
        vista.txtApellidoPCli.setEnabled(false);
        vista.txtApellidoMCli.setEnabled(false);
        vista.txtDireccionClie.setEnabled(false);
        vista.txtTelefonoClie.setEnabled(false);
        vista.btnRegistrarCliente.setEnabled(false);
    }
    
    private void limpiarFormulario() {
        vista.txtDniClie.setText("");
        vista.txtNombreClie.setText("");
        vista.txtSegundoNCli.setText("");
        vista.txtApellidoPCli.setText("");
        vista.txtApellidoMCli.setText("");
        vista.txtDireccionClie.setText("");
        vista.txtTelefonoClie.setText("");
        vista.txtDniClie.requestFocus();
    }  
}
    
