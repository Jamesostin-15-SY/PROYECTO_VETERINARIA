package CONTROLADOR;

import javax.swing.JFrame;
import javax.swing.JOptionPane; 
import DAO.*;
import FACTORY.*;
import MODELO.*;
import VISTA.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Component;

public class ControladorMenu implements ActionListener {

    private final frmMenusVeterinaria vistaMenu;
    private final UsuariosCredenciales usuarioLogueado;

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

    public void iniciar() {
        vistaMenu.setTitle("Sistema de Gestión Veterinaria - Panel Principal");
        vistaMenu.setLocationRelativeTo(null);
        vistaMenu.setExtendedState(JFrame.MAXIMIZED_BOTH);
        vistaMenu.setVisible(true);
        ControladorPermisos.AplicarRestricciones(vistaMenu, usuarioLogueado.getFk_id_role());
        
        if (usuarioLogueado.getFk_id_role() != 3) {
            vistaMenu.itemCitas.setVisible(false);
        }
    }

    private Empleados obtenerEmpleadoPorDni(String dni) {
        return new EmpleadoDAO().buscarPorDni(dni);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vistaMenu.itemRegistrarCli) {
            VistasFactory.CrearVista("RegistrarClie", "RegistrarClientes", vistaMenu.spnContenedor);
        } else if (e.getSource() == vistaMenu.itemTabla) {
            VistasFactory.CrearVista("Tabladeempleados", "Tabla De Empleados", vistaMenu.spnContenedor);
        } else if (e.getSource() == vistaMenu.itemAgregar) {
            VistasFactory.CrearVista("AgregarEmpleado", "Agregar Nuevo Empleado", vistaMenu.spnContenedor);
        } else if (e.getSource() == vistaMenu.itemAgenda) {
            VistasFactory.CrearVista("AgendaCitas", "Agenda de Citas", vistaMenu.spnContenedor);
        } else if (e.getSource() == vistaMenu.itemCitas) {
            
            if (usuarioLogueado.getFk_id_role() != 3) {
                JOptionPane.showMessageDialog(vistaMenu, 
                    "Esta sección es exclusiva para el personal médico (Veterinarios).", 
                    "Acceso Denegado", 
                    JOptionPane.ERROR_MESSAGE);
                return; 
            }
            
            String dni = usuarioLogueado.getFk_dni_empleado();
            Empleados emp = obtenerEmpleadoPorDni(dni);
            
            Object vista = VistasFactory.CrearVista("AgendasAsignadas", "Agenda de Citas Asignadas", vistaMenu.spnContenedor);
            
            if (vista instanceof frmAgendadeCitasAsignadas) {
                frmAgendadeCitasAsignadas vistaAgenda = (frmAgendadeCitasAsignadas) vista;
                vistaAgenda.veterinarioLogueado = emp;
                new ControladorAgendaDeCitasAsignadas(vistaAgenda);
            }
        } else if (e.getSource() == vistaMenu.itemMantenimiento) {
            VistasFactory.CrearVista("Servicios", "Mantenimiendo de Servicios", vistaMenu.spnContenedor);
        }
    }
}