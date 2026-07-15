package FACTORY;
import VISTA.*;
import CONTROLADOR.*;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;

public class VistasFactory {

    private static void PresentarVista(JInternalFrame iframe, JDesktopPane contenedor) {
        contenedor.removeAll();
        contenedor.add(iframe);
        contenedor.repaint();
        int x = (contenedor.getWidth() - iframe.getWidth()) / 2;
        int y = (contenedor.getHeight() - iframe.getHeight()) / 2;
        iframe.setLocation(x, y);
        iframe.setVisible(true);
    }

    public static JInternalFrame CrearVista(String nomvista, String titulo, JDesktopPane contenedor) {
        return CrearVista(nomvista, titulo, contenedor, null);
    }

    public static JInternalFrame CrearVista(String nomvista, String titulo, JDesktopPane contenedor, MODELO.Empleados emp) {
        JInternalFrame vistaRetornada = null;

        if (nomvista.equals("RegistrarClie")) {
            frmRegistrarClientes FRC = new frmRegistrarClientes();
            new ControladorCliente(FRC);
            FRC.setTitle(titulo);
            PresentarVista(FRC, contenedor);
            vistaRetornada = FRC;
        } else if (nomvista.equals("AgregarMascota")) {
            frmAgregarMascotas FAM = new frmAgregarMascotas();
            new ControladorMascotas(FAM);
            FAM.setTitle(titulo);
            PresentarVista(FAM, contenedor);
            vistaRetornada = FAM;
        } else if (nomvista.equals("AgendaCitas")) {
            frmAgendaDeCita FA = new frmAgendaDeCita();
            new ControladorAgendaDeCitas(FA);
            FA.setTitle(titulo);
            PresentarVista(FA, contenedor);
            vistaRetornada = FA;
        } else if (nomvista.equals("Tabladeempleados")) {
            frmTablaDeEmpleados FTE = new frmTablaDeEmpleados();
            new ControladorTablaDeEmpleados(FTE);
            FTE.setTitle(titulo);
            PresentarVista(FTE, contenedor);
            vistaRetornada = FTE;
        } else if (nomvista.equals("AgregarEmpleado")) {
            frmAgregarEmpleado FE = new frmAgregarEmpleado();
            new ControladorEmpleado(FE);
            FE.setTitle(titulo);
            PresentarVista(FE, contenedor);
            vistaRetornada = FE;
        } else if (nomvista.equals("AgendasAsignadas")) {
            // Se usa el constructor con el empleado que ya tenías
            frmAgendadeCitasAsignadas FACA = new frmAgendadeCitasAsignadas(emp);
            FACA.setTitle(titulo);
            PresentarVista(FACA, contenedor);
            vistaRetornada = FACA;
        } else if (nomvista.equals("GestionDeCitas")) {
            frmGestionDeCitas FGC = new frmGestionDeCitas();
            new ControladorDeGestionCitas(FGC);
            FGC.setTitle(titulo);
            PresentarVista(FGC, contenedor);
            vistaRetornada = FGC;
        } else if (nomvista.equals("Servicios")) {
            frmAgregarServicio FAS = new frmAgregarServicio();
            FAS.setTitle(titulo);
            PresentarVista(FAS, contenedor);
            vistaRetornada = FAS;
        }

        return vistaRetornada;
    }
}