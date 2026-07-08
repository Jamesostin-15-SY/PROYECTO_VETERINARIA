package PROCESOS;
import VISTA.*;
public class ProcesosPermisos {
     public static void AplicarRestricciones(frmMenusVeterinaria mv, int idRol) {

        mv.MenuClientes.setEnabled(false);
        mv.MenuCitas.setEnabled(false);
        mv.MenuAgendas.setEnabled(false);
        mv.MenuEmpleados.setVisible(false); 
        
        
        if (idRol == 1) { 
            
            mv.MenuClientes.setEnabled(true);
            mv.MenuCitas.setEnabled(true);
            mv.MenuAgendas.setEnabled(true);
            mv.MenuEmpleados.setVisible(true); 
            mv.MenuServicios.setVisible(true);
            
        } else if (idRol == 2) { 
            
            mv.MenuClientes.setEnabled(true);
            mv.MenuCitas.setEnabled(true);
            mv.MenuServicios.setEnabled(true);
            
        } else if (idRol == 3) { 
            
            mv.MenuAgendas.setEnabled(true);
        }
    }
}
