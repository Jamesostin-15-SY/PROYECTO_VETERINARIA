package PROCESOS;
import VISTA.*;
import javax.swing.table.DefaultTableModel;
public class ProcesosAgendaCitasAsignadas {
    public static DefaultTableModel FormatoTabla(frmAgendadeCitasAsignadas fc) {
        String titulos[] = {"ID Cita", "Mascota", "Veterinario", "Fecha y Hora", "Servicio", "Motivo", "Estado"};
        DefaultTableModel mt = new DefaultTableModel(null, titulos) {
            
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }    
        };        
        fc.tblAgendaDeCitas.setModel(mt);
        return mt;
    }
    
}