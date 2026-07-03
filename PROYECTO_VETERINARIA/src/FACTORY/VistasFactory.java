package FACTORY;
import VISTA.*;
import CONTROLADOR.*;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
public class VistasFactory {
    private static void PresentarVista (JInternalFrame iframe, JDesktopPane contenedor) {
        contenedor.removeAll();
        contenedor.add(iframe);
        contenedor.repaint();
        int x = (contenedor.getWidth() - iframe.getWidth())/2;
        int y = (contenedor.getHeight() - iframe.getHeight())/2;
        iframe.setLocation(x, y);
        iframe.setVisible(true);
    }
    public static void CrearVista (String nomvista, String titulo, JDesktopPane contenedor) {
        if (nomvista.equals("RegistrarClie")){
            frmRegistrarClientes FRC = new frmRegistrarClientes();
            
            ControladorCliente CC = new ControladorCliente(FRC);
            FRC.setTitle(titulo);
            PresentarVista(FRC, contenedor);
        }
        if (nomvista.equals("Tabladeempleados")){
            frmTablaDeEmpleados FTE = new frmTablaDeEmpleados();
            FTE.setTitle(titulo);
            PresentarVista(FTE, contenedor);
        }
        if (nomvista.equals("EliminarEmpleado")){
            frmEliminarEmpleado FEE = new frmEliminarEmpleado();
            FEE.setTitle(titulo);
            PresentarVista(FEE, contenedor);
        }
        if (nomvista.equals("Modificar")){
            frmModificar FMC = new frmModificar();
            FMC.setTitle(titulo);
            PresentarVista(FMC, contenedor);
        }
        if (nomvista.equals("")){}
        
        if (nomvista.equals("AgregarEmpleado")) { 
           frmAgregarEmpleado FE = new frmAgregarEmpleado(); 
           FE.setTitle(titulo);
           PresentarVista(FE, contenedor); 
        }
       if (nomvista.equals("AgendaCitas")) {
           frmAgendaDeCita FA = new frmAgendaDeCita();
           FA.setTitle(titulo);
           PresentarVista(FA, contenedor);
        }
       if (nomvista.equals("CancelarCita")){
           frmCancelarCita FCC = new frmCancelarCita();
           FCC.setTitle(titulo);
           PresentarVista(FCC, contenedor);
        }

        if (nomvista.equals("AgendasAsignadas")){
            frmAgendadeCitasAsignadas FACA = new frmAgendadeCitasAsignadas();
            FACA.setTitle(titulo);
            PresentarVista(FACA, contenedor);
        }
        if (nomvista.equals("AgregarEmpleado")) { 
            frmAgregarEmpleado FE = new frmAgregarEmpleado(); 
            ControladorEmpleado CE = new ControladorEmpleado(FE);   
            PresentarVista(FE, contenedor); 
       }    
    }   
   
}
