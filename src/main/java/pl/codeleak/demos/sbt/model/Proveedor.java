package pl.codeleak.demos.sbt.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import pl.codeleak.demos.sbt.enumeradores.Categoria;

@Entity
public class Proveedor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nombre;
    private String telefono;
    private Categoria categoria;
    private Boolean activo;
    private Date fechaCarga;
    private String username;

    public Proveedor(){
        this.fechaCarga = Utiles.obtenerFechaYHoraActualDate();
    }
    public Proveedor(Integer id, String nombre, String telefono, Categoria categoria, Boolean activo, Date fechaCarga,
                     String username) {
        this.id = id;
        this.nombre = nombre;
        this.telefono = telefono;
        this.categoria = categoria;
        this.activo = activo;
        this.fechaCarga = fechaCarga;
        this.username = username;
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

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
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
}
