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
        vistaMenu.itemEliminar.addActionListener(this);
        vistaMenu.itemEstado.addActionListener(this);
        

        vistaMenu.itemAgregar.addActionListener(this);
        vistaMenu.itemAgenda.addActionListener(this);
        vistaMenu.itemCancelar.addActionListener(this);
        vistaMenu.itemCitas.addActionListener(this);

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
