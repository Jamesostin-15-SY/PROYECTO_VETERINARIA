package CONTROLADOR;
import DAO.*;
import FACTORY.*;
import VISTA.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ControladorGestionDeCitas implements ActionListener{
    private final frmGestionDeCitas vista;
    private final CrudGestionCitas CrudGestion;
    public ControladorGestionDeCitas(frmGestionDeCitas vista){
        this.vista = vista;
        this. CrudGestion = new CrudGestionCitas();
        
        this.vista.btnGuardar.addActionListener(this);
        this.vista.btnNuevo.addActionListener(this);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
    }
    
    
}
