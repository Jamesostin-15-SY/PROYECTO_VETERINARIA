package MODELO;

public class Empleados {
    private String dni_empleado;
    private String primer_nombre;
    private String segundo_nombre;
    private String apellido_paterno;
    private String apellido_materno;
    private String telefono;
    private int fk_id_puesto;
    private int fk_id_turno;
    private int fk_id_estado;
// Constructor
    public Empleados(){}
    
    public String getDni_empleado() {return dni_empleado; }
    public void setDni_empleado(String dni_empleado) {this.dni_empleado = dni_empleado;}
    public String getPrimer_nombre() {return primer_nombre;}
    public void setPrimer_nombre(String primer_nombre) {this.primer_nombre = primer_nombre; }
    public String getSegundo_nombre() {return segundo_nombre;}
    public void setSegundo_nombre(String segundo_nombre) {this.segundo_nombre = segundo_nombre; }
    public String getApellido_paterno() {return apellido_paterno;}
    public void setApellido_paterno(String apellido_paterno) {this.apellido_paterno = apellido_paterno;}
    public String getApellido_materno() {return apellido_materno;}
    public void setApellido_materno(String apellido_materno) {this.apellido_materno = apellido_materno;}
    public String getTelefono() {return telefono;}
    public void setTelefono(String telefono) {this.telefono = telefono;}
    public int getFk_id_puesto() {return fk_id_puesto;}
    public void setFk_id_puesto(int fk_id_puesto) {this.fk_id_puesto = fk_id_puesto;}
    public int getFk_id_turno() {return fk_id_turno;}
    public void setFk_id_turno(int fk_id_turno) {this.fk_id_turno = fk_id_turno;}
    public int getFk_id_estado(){return fk_id_estado;  }
    public void setFk_id_estado(int fk_id_estado) {this.fk_id_estado = fk_id_estado;  }
    
}