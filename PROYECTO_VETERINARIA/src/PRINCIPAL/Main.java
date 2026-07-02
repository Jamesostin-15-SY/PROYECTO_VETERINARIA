package PRINCIPAL;
import VISTA.*;
import CONTROLADOR.*;

public class Main {
    public static void main(String[] args) {
        // 1. Crear la vista
        frmLogin vista = new frmLogin();
        
        // 2. Crear el controlador asignándole la vista
        ControladorLogin loginControlador = new ControladorLogin(vista);
        
        // 3. Lanzar la aplicación
        loginControlador.iniciar();
    }
    
}
