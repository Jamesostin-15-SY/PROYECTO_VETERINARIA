package MODELO;
import java.time.LocalDate;
public class Mascotas {
    private int id_mascota;
    private String fk_dni_clinte;
    private String nombre_mascota;
    private LocalDate fecha_nacimiento;
    private int fk_id_especie;
    private String raza;
    
    public Mascotas (){}

    public int getId_mascota() { return id_mascota; }
    public void setId_mascota(int id_mascota) { this.id_mascota = id_mascota; }
    public String getFk_dni_clinte() { return fk_dni_clinte; }
    public void setFk_dni_clinte(String fk_dni_clinte) { this.fk_dni_clinte = fk_dni_clinte; }
    public String getNombre_mascota() { return nombre_mascota; }
    public void setNombre_mascota(String nombre_mascota) { this.nombre_mascota = nombre_mascota; }
    public LocalDate getFecha_nacimiento() { return fecha_nacimiento; }
    public void setFecha_nacimiento(LocalDate fecha_nacimiento) { this.fecha_nacimiento = fecha_nacimiento; }
    public int getFk_id_especie() { return fk_id_especie; }
    public void setFk_id_especie(int fk_id_especie) { this.fk_id_especie = fk_id_especie; }
    public String getRaza() { return raza;  }
    public void setRaza(String raza) { this.raza = raza; }
}
