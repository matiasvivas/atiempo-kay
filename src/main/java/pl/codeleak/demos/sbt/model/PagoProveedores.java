package pl.codeleak.demos.sbt.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import pl.codeleak.demos.sbt.enumeradores.Estado;

@Entity
public class PagoProveedores {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String proveedor;

    private Float montoPedido;

    private Float montoSaldoPendiente;

    private Float montoEntregaEfectivo;

    private Float montoEntregaDigital;

    private Estado estado;

    private Date fecha;

    public PagoProveedores() {
    }

    public PagoProveedores(Integer id, String proveedor, Float montoPedido, Float montoSaldoPendiente,
                           Float montoEntregaEfectivo, Float montoEntregaDigital, Estado estado, Date fecha) {
        this.id = id;
        this.proveedor = proveedor;
        this.montoPedido = montoPedido;
        this.montoSaldoPendiente = montoSaldoPendiente;
        this.montoEntregaEfectivo = montoEntregaEfectivo;
        this.montoEntregaDigital = montoEntregaDigital;
        this.estado = estado;
        this.fecha = fecha;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getProveedor() {
        return proveedor;
    }

    public void setProveedor(String proveedor) {
        this.proveedor = proveedor;
    }

    public Float getMontoPedido() {
        return montoPedido;
    }

    public void setMontoPedido(Float montoPedido) {
        this.montoPedido = montoPedido;
    }

    public Float getMontoSaldoPendiente() {
        return montoSaldoPendiente;
    }

    public void setMontoSaldoPendiente(Float montoSaldoPendiente) {
        this.montoSaldoPendiente = montoSaldoPendiente;
    }

    public Float getMontoEntregaEfectivo() {
        return montoEntregaEfectivo;
    }

    public void setMontoEntregaEfectivo(Float montoEntregaEfectivo) {
        this.montoEntregaEfectivo = montoEntregaEfectivo;
    }

    public Float getMontoEntregaDigital() {
        return montoEntregaDigital;
    }
    public void setMontoEntregaDigital(Float montoEntregaDigital) {
        this.montoEntregaDigital = montoEntregaDigital;
    }

    public Estado getEstado() {
        return estado;
    }
    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
}
