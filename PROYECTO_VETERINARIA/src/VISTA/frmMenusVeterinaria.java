
package VISTA;
import COMPONENTES.DesktopPaneConFondo;

public class frmMenusVeterinaria extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(frmMenusVeterinaria.class.getName());

    
    public frmMenusVeterinaria() {
        initComponents();
        
    }

    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        spnContenedor = spnContenedor = new COMPONENTES.DesktopPaneConFondo();
        jMenuBar2 = new javax.swing.JMenuBar();
        MenuClientes = new javax.swing.JMenu();
        itemRegistrarCli = new javax.swing.JMenuItem();
        MenuCitas = new javax.swing.JMenu();
        itemAgenda = new javax.swing.JMenuItem();
        MenuServicios = new javax.swing.JMenu();
        itemMantenimiento = new javax.swing.JMenuItem();
        MenuAgendas = new javax.swing.JMenu();
        itemCitas = new javax.swing.JMenuItem();
        MenuEmpleados = new javax.swing.JMenu();
        itemAgregar = new javax.swing.JMenuItem();
        itemTabla = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        spnContenedor.setBackground(new java.awt.Color(0, 51, 0));

        javax.swing.GroupLayout spnContenedorLayout = new javax.swing.GroupLayout(spnContenedor);
        spnContenedor.setLayout(spnContenedorLayout);
        spnContenedorLayout.setHorizontalGroup(
            spnContenedorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 922, Short.MAX_VALUE)
        );
        spnContenedorLayout.setVerticalGroup(
            spnContenedorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 572, Short.MAX_VALUE)
        );

        MenuClientes.setText("Clientes");

        itemRegistrarCli.setText("Registrar Clientes");
        MenuClientes.add(itemRegistrarCli);

        jMenuBar2.add(MenuClientes);

        MenuCitas.setText("Citas");

        itemAgenda.setText("Agenda De Citas");
        itemAgenda.addActionListener(this::itemAgendaActionPerformed);
        MenuCitas.add(itemAgenda);

        jMenuBar2.add(MenuCitas);

        MenuServicios.setText("Servicios");

        itemMantenimiento.setText("Mantenimiento Servicios");
        MenuServicios.add(itemMantenimiento);

        jMenuBar2.add(MenuServicios);

        MenuAgendas.setText("Agenda");

        itemCitas.setText("Citas Asignadas");
        MenuAgendas.add(itemCitas);

        jMenuBar2.add(MenuAgendas);

        MenuEmpleados.setText("Empleados");

        itemAgregar.setText("Agregar empleados");
        MenuEmpleados.add(itemAgregar);

        itemTabla.setText("Tabla De Empleados");
        MenuEmpleados.add(itemTabla);

        jMenuBar2.add(MenuEmpleados);

        setJMenuBar(jMenuBar2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(spnContenedor)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(spnContenedor)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void itemAgendaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemAgendaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_itemAgendaActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new frmMenusVeterinaria().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JMenu MenuAgendas;
    public javax.swing.JMenu MenuCitas;
    public javax.swing.JMenu MenuClientes;
    public javax.swing.JMenu MenuEmpleados;
    public javax.swing.JMenu MenuServicios;
    public javax.swing.JMenuItem itemAgenda;
    public javax.swing.JMenuItem itemAgregar;
    public javax.swing.JMenuItem itemCitas;
    public javax.swing.JMenuItem itemMantenimiento;
    public javax.swing.JMenuItem itemRegistrarCli;
    public javax.swing.JMenuItem itemTabla;
    private javax.swing.JMenuBar jMenuBar2;
    public javax.swing.JDesktopPane spnContenedor;
    // End of variables declaration//GEN-END:variables
}
