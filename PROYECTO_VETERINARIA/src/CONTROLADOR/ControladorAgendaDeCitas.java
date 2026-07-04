package CONTROLADOR;
import DAO.*;
import VISTA.*;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.swing.table.DefaultTableModel;


public class ControladorAgendaDeCitas {
    private final frmAgendaDeCita vista;
    private DefaultTableModel modeloTabla;
    private final CrudAgendaDeCitas crudAgenda;
    private final DateTimeFormatter formateador = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    
    public ControladorAgendaDeCitas(frmAgendaDeCita vista) {
        this.vista = vista;
        this.crudAgenda = new CrudAgendaDeCitas();
        
        
        configurarTabla();
        cargarDatosCitas();
        
        
    }
    private void configurarTabla() {
        String[] columnas = {"ID Cita", "Mascota", "Dueño / Cliente", "Veterinario", "Fecha y Hora", "Servicio", "Estado", "Motivo"};
        modeloTabla = new DefaultTableModel(null, columnas) {
            
            @Override
            public boolean isCellEditable(int registro, int columna) {
                return false;
            }
        };
        
        this.vista.tblCitas.setModel(modeloTabla);
    }
    public void cargarDatosCitas() {
        
        modeloTabla.setRowCount(0); 
        
        // 4. Llamamos al método usando la instancia "crudAgenda" y recibimos List<Object[]>
        List<Object[]> lista = crudAgenda.listarCitas();
        
        for (Object[] fila : lista) {
            // Formateamos la fecha si es necesario antes de agregarla a la tabla
            if (fila[4] instanceof java.sql.Timestamp) {
                java.sql.Timestamp timestamp = (java.sql.Timestamp) fila[4];
                fila[4] = timestamp.toLocalDateTime().format(formateador);
            }
            
            modeloTabla.addRow(fila);
        }
    }
    
}
