package pl.codeleak.demos.sbt.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
public class Producto {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @NotNull(message = "Debes especificar el nombre")
    @Size(min = 1, max = 50, message = "El nombre debe medir entre 1 y 50")
    private String nombre;

    @NotNull(message = "Debes especificar el código")
    @Size(min = 1, max = 50, message = "El código debe medir entre 1 y 50")
    private String codigo;

    @NotNull(message = "Debes especificar el precio")
    @Min(value = 0, message = "El precio mínimo es 0")
    private Float precio;

    @NotNull(message = "Debes especificar la existencia")
    @Min(value = 0, message = "La existencia mínima es 0")
    private Float existencia;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date fechaVencimiento;

    private Integer prioridad;

    //Datos internos de control
    private Date fechaCreacion;
    private Date fechaActualizacionStock;
    private Date fechaActualizacionPrecio;
    private String usuarioActualizacionStock;

    private String categoria;

    public Producto(String nombre, String codigo, Float precio, Float existencia, Integer id) {
        this.nombre = nombre;
        this.codigo = codigo;
        this.precio = precio;
        this.existencia = existencia;
        this.id = id;
    }

    public Producto(String nombre, String codigo, Float precio, Float existencia) {
        this.nombre = nombre;
        this.codigo = codigo;
        this.precio = precio;
        this.existencia = existencia;
    }

    public Producto(String nombre, String codigo, Float precio, Float existencia, Date fechaVencimiento,
                    Integer prioridad) {
        this.nombre = nombre;
        this.codigo = codigo;
        this.precio = precio;
        this.existencia = existencia;
        this.fechaVencimiento = fechaVencimiento;
        this.prioridad = prioridad;
    }

    public Producto(@NotNull(message = "Debes especificar el código") @Size(min = 1, max = 50, message = "El código debe medir entre 1 y 50") String codigo) {
        this.codigo = codigo;
    }

    public Producto() {
    }

    public boolean sinExistencia() {
        return this.existencia <= 0;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Float getPrecio() {
        return precio;
    }

    public void setPrecio(Float precio) {
        this.precio = precio;
    }

    public Float getExistencia() {
        return existencia;
    }

    public void setExistencia(Float existencia) {
        this.existencia = existencia;
    }

    public void restarExistencia(Float existencia) {
        this.existencia -= existencia;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getFechaVencimiento() { return fechaVencimiento; }

    public void setFechaVencimiento(Date fechaVencimiento) { this.fechaVencimiento = fechaVencimiento; }

    public Integer getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(Integer prioridad) {
        this.prioridad = prioridad;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Date getFechaActualizacionStock() {
        return fechaActualizacionStock;
    }

    public void setFechaActualizacionStock(Date fechaActualizacionStock) {
        this.fechaActualizacionStock = fechaActualizacionStock;
    }

    public Date getFechaActualizacionPrecio() {
        return fechaActualizacionPrecio;
    }

    public void setFechaActualizacionPrecio(Date fechaActualizacionPrecio) {
        this.fechaActualizacionPrecio = fechaActualizacionPrecio;
    }

    public String getUsuarioActualizacionStock() {
        return usuarioActualizacionStock;
    }

    public void setUsuarioActualizacionStock(String usuarioActualizacionStock) {
        this.usuarioActualizacionStock = usuarioActualizacionStock;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
}
