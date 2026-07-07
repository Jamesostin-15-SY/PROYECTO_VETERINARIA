package CONTROLADOR;

import DAO.*;
import VISTA.*;
import java.util.List;
import java.awt.*;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionEvent;

public class ControladorTablaDeEmpleados implements ActionListener{
    
    private final frmTablaDeEmpleados vista;
    private final CrudTablaDeEmpleados crudEmpleado;
    private DefaultTableModel modeloTabla;
    private List<Object[]> listaEstados;
    public ControladorTablaDeEmpleados(frmTablaDeEmpleados vista) {
        this.vista = vista;
        this.crudEmpleado = new CrudTablaDeEmpleados();

        configurarTabla();
        cargarDatosEmpleados();
        cargarComboEstados();
        this.vista.btnConfirmar.addActionListener(this);
        this.vista.tblRegistrosEmpleados.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                seleccionarFilaTabla();
            }
        
        });
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
    private void cargarComboEstados() {
        this.vista.cbxEstadoEmpleado.removeAllItems(); 
        listaEstados = crudEmpleado.listarEstadosEmpleadosCombo();
        
        int indexDeshabilitado = -1;
        for (int i = 0; i < listaEstados.size(); i++) {
            String nombreEstado = listaEstados.get(i)[1].toString();
            this.vista.cbxEstadoEmpleado.addItem(nombreEstado); 
            
            if (nombreEstado.equalsIgnoreCase("DESHABILITADO")) {
                indexDeshabilitado = i;
            }
        }

        if (indexDeshabilitado != -1) {
            this.vista.cbxEstadoEmpleado.setSelectedIndex(indexDeshabilitado); 
        }
    }

    private void seleccionarFilaTabla() {
        int filaSeleccionada = this.vista.tblRegistrosEmpleados.getSelectedRow();
        if (filaSeleccionada != -1) {

            String dni = this.vista.tblRegistrosEmpleados.getValueAt(filaSeleccionada, 0).toString();
            this.vista.txtDniEmpleado.setText(dni);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.vista.btnConfirmar) { 
            ejecutarCambioEstado();
        }
    }

    private void ejecutarCambioEstado() {
        String dni = this.vista.txtDniEmpleado.getText().trim();
        int indexEstado = this.vista.cbxEstadoEmpleado.getSelectedIndex(); 

        if (dni.isEmpty()) {
            JOptionPane.showMessageDialog(vista, "Por favor, seleccione un empleado de la tabla primero.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (indexEstado == -1) return;

        int idEstado = (int) listaEstados.get(indexEstado)[0];
        String nombreEstado = listaEstados.get(indexEstado)[1].toString();

        int respuesta = JOptionPane.showConfirmDialog(
                vista, 
                "¿Está seguro de que desea cambiar el estado del empleado con DNI " + dni + " a [" + nombreEstado + "]?", 
                "Confirmar Solicitud", 
                JOptionPane.YES_NO_OPTION, 
                JOptionPane.QUESTION_MESSAGE
        );

        if (respuesta == JOptionPane.YES_OPTION) {
            boolean exito = crudEmpleado.actualizarEstadoEmpleado(dni, idEstado);
            if (exito) {
                JOptionPane.showMessageDialog(vista, "El estado del empleado se ha modificado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                this.vista.txtDniEmpleado.setText(""); 
                cargarDatosEmpleados();
            } else {
                JOptionPane.showMessageDialog(vista, "No se pudo actualizar el estado en la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
