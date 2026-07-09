package CONTROLADOR;

import DAO.CrudGestionDeCitas;
import MODELO.Servicios;
import VISTA.frmAgregarServicio;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class ControladorDeServicios implements ActionListener {

    private final frmAgregarServicio vista;
    private final CrudGestionDeCitas dao;
    private List<Object[]> listaEstadosServicios;

    public ControladorDeServicios(frmAgregarServicio vista) {
        this.vista = vista;
        this.dao = new CrudGestionDeCitas();

        cargarTablaServicios();
        cargarComboEstados();

        // Escuchadores de tus botones (Nombres exactos)
        this.vista.btnGuardar.addActionListener(this);       
        this.vista.btnNuevo.addActionListener(this); 
        this.vista.btnModificar.addActionListener(this); // Escuchador para btnModifica

        // EVENTO DE CLIC EN LA TABLA PARA CAPTURAR EL ID
        this.vista.tblServicios.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int filaSeleccionada = vista.tblServicios.getSelectedRow();
                if (filaSeleccionada >= 0) {
                    // El ID está guardado en la columna 0 de la tabla
                    String id = vista.tblServicios.getValueAt(filaSeleccionada, 0).toString();
                    vista.txtIdServicio.setText(id);
                }
            }
        });
    }

    private void cargarComboEstados() {
        this.vista.cbxEstadoServicio.removeAllItems();
        listaEstadosServicios = dao.listarEstadosServiciosCombo();
        if (listaEstadosServicios != null) {
            for (Object[] est : listaEstadosServicios) {
                this.vista.cbxEstadoServicio.addItem(est[1].toString());
            }
        }
    }

    public void cargarTablaServicios() {
        DefaultTableModel model = new DefaultTableModel(
            new Object[]{"ID", "Nombre del Servicio", "Precio del Servicio", "Descripción", "Estado"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; 
            }
        };

        // 🚨 ESTA LÍNEA ES LA MAGIA: Asegura que el modelo empiece completamente limpio
        model.setRowCount(0); 

        List<Object[]> lista = dao.listarServiciosTabla();
        for (Object[] fila : lista) {
            model.addRow(fila);
        }
        this.vista.tblServicios.setModel(model); 
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.vista.btnGuardar) {
            ejecutarRegistroServicio();
        }
        if (e.getSource() == this.vista.btnNuevo) {
            limpiarCampos();
        }
        if (e.getSource() == this.vista.btnModificar) { // Modificado para btnModifica
            ejecutarModificacionEstado();
        }
    }

    private void limpiarCampos() {
        // 1. Limpiamos todas las cajas de texto y componentes de entrada
        this.vista.txtNombreServicio.setText("");
        this.vista.spPrecio.setValue(0); 
        this.vista.txaDescripcion.setText(""); 
        this.vista.txtIdServicio.setText("");
        
        // 2. Vaciamos las filas de la tabla sin romper su estructura de columnas
        DefaultTableModel modeloActual = (DefaultTableModel) this.vista.tblServicios.getModel();
        modeloActual.setRowCount(0); 
    }

    private void ejecutarRegistroServicio() {
        String nombre = this.vista.txtNombreServicio.getText().trim();
        String descripcion = this.vista.txaDescripcion.getText().trim(); 

        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(vista, "Por favor, ingrese el nombre del servicio.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        double precio = 0;
        try {
            precio = Double.parseDouble(this.vista.spPrecio.getValue().toString());
        } catch (Exception ex) {
            System.out.println("Error al leer el precio: " + ex);
        }

        Servicios nuevoServicio = new Servicios();
        nuevoServicio.setNombre_servicio(nombre);
        nuevoServicio.setPrecio(precio);

        // Enviamos al DAO
        boolean exito = dao.registrarServicio(nuevoServicio, descripcion);

        if (exito) {
            JOptionPane.showMessageDialog(vista, "Servicio guardado con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            
            // Reestablecemos toda la tabla con los datos actualizados de la base de datos
            cargarTablaServicios(); 
            
            // Limpiamos los campos de texto de la derecha
            this.vista.txtNombreServicio.setText("");
            this.vista.spPrecio.setValue(0); 
            this.vista.txaDescripcion.setText(""); 
            this.vista.txtIdServicio.setText("");
        } else {
            JOptionPane.showMessageDialog(vista, "Hubo un error al intentar guardar el servicio.\nVerifique la consola o el estado por defecto.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ACCIÓN DEL BOTÓN MODIFICA
    private void ejecutarModificacionEstado() {
        String idStr = this.vista.txtIdServicio.getText().trim();
        int indexEstado = this.vista.cbxEstadoServicio.getSelectedIndex();

        if (idStr.isEmpty()) {
            JOptionPane.showMessageDialog(vista, "Por favor, seleccione un servicio de la tabla primero.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (indexEstado == -1) {
            JOptionPane.showMessageDialog(vista, "No hay un estado seleccionado válido.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int idServicio = Integer.parseInt(idStr);
        int idEstadoNuevo = (int) listaEstadosServicios.get(indexEstado)[0];

        boolean exito = dao.modificarEstadoServicio(idServicio, idEstadoNuevo);

        if (exito) {
            JOptionPane.showMessageDialog(vista, "Estado del servicio modificado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            limpiarCampos();
            cargarTablaServicios(); 
        } else {
            JOptionPane.showMessageDialog(vista, "Error al intentar modificar el estado en la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}