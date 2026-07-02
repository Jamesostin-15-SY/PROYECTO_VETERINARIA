package PROCESOS;
import javax.swing.JOptionPane;


public class Mensajes {
    public static void M1(String mensaje) {
        JOptionPane.showMessageDialog(null, mensaje);
    }
    
    public static String M2(String mensaje) {
        return JOptionPane.showInputDialog(mensaje);
    }
    
    public static int M3(String titulo, String mensaje) {
        return JOptionPane.showConfirmDialog(null, mensaje, titulo, JOptionPane.YES_NO_OPTION);
    }
}
