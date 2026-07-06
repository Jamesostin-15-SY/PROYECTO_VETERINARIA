package CONTROLADOR;

import DAO.*;
import VISTA.*;
import java.util.*;
import javax.swing.table.DefaultTableModel;

public class ControladorTablaDeEmpleados {

    private final frmTablaDeEmpleados vista;
    private final CrudTablaDeEmpleados crudEmpleado;
    private DefaultTableModel modeloTabla;

    public ControladorTablaDeEmpleados(frmTablaDeEmpleados vista) {
        this.vista = vista;
        this.crudEmpleado = new CrudTablaDeEmpleados();

        configurarTabla();
        cargarDatosEmpleados();
    }

    private void configurarTabla() {

        String[] columnas = {"Dni", "Nombres", "Apellidos", "Teléfono", "Puesto", "Turno", "Estado", "Servicio"};

        modeloTabla = new DefaultTableModel(null, columnas) {
            @Override
            public boolean isCellEditable(int registro, int columna) {
                return false;
            }
        };

        this.vista.tblRegistrosEmpleados.setModel(modeloTabla);
    }

    public void cargarDatosEmpleados() {
        modeloTabla.setRowCount(0);

        List<Object[]> lista = crudEmpleado.listarEmpleadosConServicios();

        for (Object[] fila : lista) {
            modeloTabla.addRow(fila);
        }
    }

}
