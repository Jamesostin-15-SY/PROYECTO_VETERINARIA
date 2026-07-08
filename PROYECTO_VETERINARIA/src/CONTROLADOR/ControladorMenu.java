package CONTROLADOR;
import javax.swing.JFrame;
import DAO.*;
import FACTORY.*;
import MODELO.*;
import VISTA.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
public class ControladorMenu implements ActionListener {
    
    
    private final frmMenusVeterinaria vistaMenu;
    private final UsuariosCredenciales usuarioLogueado;

    // Recibe la ventana del menú y el objeto del usuario que inició sesión
    public ControladorMenu(frmMenusVeterinaria vistaMenu, UsuariosCredenciales usuarioLogueado) {
        this.vistaMenu = vistaMenu;
        this.usuarioLogueado = usuarioLogueado;
        vistaMenu.itemRegistrarCli.addActionListener(this);

        vistaMenu.itemTabla.addActionListener(this);
        

        vistaMenu.itemAgregar.addActionListener(this);
        vistaMenu.itemAgenda.addActionListener(this);
        vistaMenu.itemCitas.addActionListener(this);
        vistaMenu.itemMantenimiento.addActionListener(this);

    }
        

    // Muestra el panel principal configurando títulos y permisos de rol
    public void iniciar() {
        vistaMenu.setTitle("Sistema de Gestión Veterinaria - Panel Principal");
        vistaMenu.setLocationRelativeTo(null);
        vistaMenu.setExtendedState(JFrame.MAXIMIZED_BOTH);
        
        vistaMenu.setVisible(true);
        // Llama a tu clase ProcesosPermisos para habilitar/deshabilitar los componentes correspondientes
        ControladorPermisos.AplicarRestricciones(vistaMenu, usuarioLogueado.getFk_id_role());
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vistaMenu.itemRegistrarCli){
            VistasFactory.CrearVista("RegistrarClie", "RegistrarClientes", vistaMenu.spnContenedor);
        }
        if (e.getSource() == vistaMenu.itemTabla){
            VistasFactory.CrearVista("Tabladeempleados", "Tabla De Empleados", vistaMenu.spnContenedor);
        }        
        if (e.getSource() == vistaMenu.itemAgregar) {
            VistasFactory.CrearVista("AgregarEmpleado", "Agregar Nuevo Empleado", vistaMenu.spnContenedor);
        }
        if (e.getSource() == vistaMenu.itemAgenda) {
            VistasFactory.CrearVista("AgendaCitas", "Agenda de Citas", vistaMenu.spnContenedor);
        }
        if (e.getSource() == vistaMenu.itemCitas) {
        VistasFactory.CrearVista("AgendasAsignadas", "Agenda de Citas Asignadas", vistaMenu.spnContenedor);
        }
        if (e.getSource() == vistaMenu.itemMantenimiento) {
            VistasFactory.CrearVista("Servicios", "Mantenimiendo de Servicios", vistaMenu.spnContenedor);
        }
    }
}
