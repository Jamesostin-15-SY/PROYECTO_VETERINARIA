package MODELO;

public class Clientes {
    private String dni_cliente;
    private String primer_nombre;
    private String segundo_nombre;
    private String apellido_paterno;
    private String apellido_materno;
    private String direccion;
    private String telefono;
    
    public Clientes (){}

    public String getDni_cliente() { return dni_cliente; }
    public void setDni_cliente(String dni_cliente) { this.dni_cliente = dni_cliente; }
    public String getPrimer_nombre() { return primer_nombre; }
    public void setPrimer_nombre(String primer_nombre) { this.primer_nombre = primer_nombre; }
    public String getSegundo_nombre() { return segundo_nombre; }
    public void setSegundo_nombre(String segundo_nombre) { this.segundo_nombre = segundo_nombre; }
    public String getApellido_paterno() { return apellido_paterno; }
    public void setApellido_paterno(String apellido_paterno) { this.apellido_paterno = apellido_paterno; }
    public String getApellido_materno() { return apellido_materno; }
    public void setApellido_materno(String apellido_materno) { this.apellido_materno = apellido_materno; }
    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }   
}