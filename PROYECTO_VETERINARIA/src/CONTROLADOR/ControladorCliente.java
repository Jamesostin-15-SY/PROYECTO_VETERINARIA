
package CONTROLADOR;
import DAO.*;
import VISTA.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import PROCESOS.*;
import MODELO.*;
public class ControladorCliente implements ActionListener{

    private frmRegistrarClientes vista;
    private ClienteDAO dao;

    public ControladorCliente(frmRegistrarClientes vista) {
        this.vista = vista;
        this.dao = new ClienteDAO();
        
        // Escuchamos el botón de la interfaz
        this.vista.btnRegistrarCliente.addActionListener(this);
        
        // Nos aseguramos de que el botón de mascota empiece deshabilitado al abrir la ventana
        this.vista.btnAgregarMascota.setEnabled(false);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vista.btnRegistrarCliente) {
            ejecutarRegistro();
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
    
