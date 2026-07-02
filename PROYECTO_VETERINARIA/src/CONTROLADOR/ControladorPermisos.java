package CONTROLADOR;
import VISTA.*;
public class ControladorPermisos {
    
    public static void AplicarRestricciones(frmMenusVeterinaria vista, int idRol) {
        vista.MenuClientes.setVisible(false);
        vista.MenuCitas.setVisible(false);
        vista.MenuAgendas.setVisible(false);
        vista.Modificar.setVisible(false);
        vista.MenuEmpleados.setVisible(false);
        //ITEMS
        vista.itemRegistrarCli.setVisible(true);
        vista.itemCancelar.setVisible(true);
        vista.itemAgenda.setVisible(true);
        vista.itemCitas.setVisible(true);
        vista.itemEstado.setVisible(true);
        vista.itemAgregar.setVisible(true);
        vista.itemEliminar.setVisible(true);
        vista.itemTabla.setVisible(true);
        //HOLA

        switch (idRol) {
            case 1: // ADMINISTRADOR (Todo)
                vista.MenuClientes.setVisible(true);
                vista.MenuCitas.setVisible(true);
                vista.MenuAgendas.setVisible(true);
                vista.Modificar.setVisible(true);
                vista.MenuEmpleados.setVisible(true);
                break;

            case 2: // RECEPCIONISTA (Clientes y Citas)
                vista.MenuClientes.setVisible(true);
                vista.MenuCitas.setVisible(true);
                break;

            case 3: // VETERINARIO (Agenda y Procesos)
                vista.MenuAgendas.setVisible(true);
                vista.Modificar.setVisible(true);
                break;

            default:
                System.out.println("Error: Rol no reconocido.");
                break;
        }
        if (vista.getJMenuBar() != null) {
            vista.getJMenuBar().revalidate();
            vista.getJMenuBar().repaint();
        }
    }
}
