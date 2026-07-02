package PROCESOS;
import VISTA.*;
import javax.swing.table.DefaultTableModel;
public class ProcesosAgendaDeCita {
    public static DefaultTableModel FormatoTabla(frmAgendaDeCita fc) {
        String titulos[] = {"ID Cita", "Mascota", "Veterinario", "Fecha y Hora", "Servicio", "Estado"};
        DefaultTableModel mt = new DefaultTableModel(null, titulos) {
            
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }    
        };
        fc.tblCitas.setModel(mt);
        return mt;
    }
}