package CONTROLADOR;
import DAO.CrudAgendaDeCitasAsignadas;
import MODELO.Citas;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ControladorAgendaDeCitasAsignadas {
    private CrudAgendaDeCitasAsignadas dao = new CrudAgendaDeCitasAsignadas();

    public void cargarTabla(JTable tabla) {
        DefaultTableModel model = new DefaultTableModel(new Object[]{"ID", "Mascota", "Veterinario", "Servicio", "Fecha/Hora", "Estado", "Motivo"}, 0);
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        List<Citas> lista = dao.listarCitasAsignadas();
        
        for (Citas c : lista) {
            model.addRow(new Object[]{
                c.getId_cita(), c.nombreMascotaAux, c.nombreVeterinarioAux, 
                c.nombreServicioAux, c.getFecha_hora().format(formato), 
                c.nombreEstadoAux, c.getMotivo_cita()
            });
        }
        tabla.setModel(model);
    }
}
