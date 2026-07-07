package MODELO;
import java.time.LocalDateTime;
public class Citas {
    //Atributos//
    private int id_cita;
    private int fk_id_mascota;
    private String veterinario;
    private LocalDateTime fecha_hora;
    private int fk_id_servicio;
    private int fk_id_estado;
    private String motivo_cita;
    
    //Auxiliares//
    public String nombreMascotaAux;
    public String nombreVeterinarioAux;
    public String nombreServicioAux;
    public String nombreEstadoAux;
    public LocalDateTime fechaHora;
    public String motivoCita;
    
    public Citas (){}
    
    public int getId_cita() { return id_cita; }
    public void setId_cita(int id_cita) { this.id_cita = id_cita; }
    public int getFk_id_mascota() { return fk_id_mascota; }
    public void setFk_id_mascota(int fk_id_mascota) { this.fk_id_mascota = fk_id_mascota; }
    public String getveterinario() { return veterinario; }
    public void setveterinario(String veterinario) { this.veterinario = veterinario; }
    public LocalDateTime getFecha_hora() { return fecha_hora; }
    public void setFecha_hora(LocalDateTime fecha_hora) { this.fecha_hora = fecha_hora; }
    public int getFk_id_servicio() { return fk_id_servicio; }
    public void setFk_id_servicio(int fk_id_servicio) { this.fk_id_servicio = fk_id_servicio; }
    public int getFk_id_estado() { return fk_id_estado; }
    public void setFk_id_estado(int fk_id_estado) { this.fk_id_estado = fk_id_estado; }
    public String getMotivo_cita() { return motivo_cita; }
    public void setMotivo_cita(String motivo_cita) { this.motivo_cita = motivo_cita; }
    
}