package MODELO;

public class Servicios {
    int id_servicio;
    String nombre_servicio;
    double precio;
    //Constructor 
    public Servicios (){}

    public int getId_servicio() {return id_servicio; }
    public void setId_servicio(int id_servicio) {this.id_servicio = id_servicio; }
    public String getNombre_servicio() {return nombre_servicio; }
    public void setNombre_servicio(String nombre_servicio) {this.nombre_servicio = nombre_servicio; }
    public double getPrecio() {return precio;  }
    public void setPrecio(double precio) {this.precio = precio;}
}