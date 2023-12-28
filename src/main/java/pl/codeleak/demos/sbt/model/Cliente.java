package pl.codeleak.demos.sbt.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.format.annotation.DateTimeFormat;
import pl.codeleak.demos.sbt.enumeradores.Estado;

@Entity
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String nombre;
    private String apellido;
    private String numeroDocumento;
    private String telefono;
    private Boolean cuentaCorriente;
    private Estado estado;
    private Boolean activo;
    private Date fechaCarga;
    private String username;

    private String passwordMobile;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date fechaCumpleanos;
    private String email;
    public Cliente(){
        this.fechaCarga=Utiles.obtenerFechaYHoraActualDate();
    }

    public Cliente(Integer id, String nombre, String apellido, String numeroDocumento, String telefono,
                   Boolean cuentaCorriente, Estado estado, Boolean activo, Date fechaCarga, String username,
                   Date fechaCumpleanos, String email, String passwordMobile) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.numeroDocumento = numeroDocumento;
        this.telefono = telefono;
        this.cuentaCorriente = cuentaCorriente;
        this.estado = estado;
        this.activo = activo;
        this.fechaCarga = fechaCarga;
        this.username = username;
        this.fechaCumpleanos = fechaCumpleanos;
        this.email = email;
        this.passwordMobile = passwordMobile;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getNumeroDocumento() {
        return numeroDocumento;
    }

    public void setNumeroDocumento(String numeroDocumento) {
        this.numeroDocumento = numeroDocumento;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public Boolean getCuentaCorriente() {
        return cuentaCorriente;
    }

    public void setCuentaCorriente(Boolean cuentaCorriente) {
        this.cuentaCorriente = cuentaCorriente;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public Date getFechaCarga() {
        return fechaCarga;
    }

    public void setFechaCarga(Date fechaCarga) {
        this.fechaCarga = fechaCarga;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Date getFechaCumpleanos() {
        return fechaCumpleanos;
    }

    public void setFechaCumpleanos(Date fechaCumpleanos) {
        this.fechaCumpleanos = fechaCumpleanos;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordMobile() {
        return passwordMobile;
    }

    public void setPasswordMobile(String passwordMobile) {
        this.passwordMobile = passwordMobile;
    }
}
