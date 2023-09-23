package pl.codeleak.demos.sbt.model;

import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Venta {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private Date fechaYHora;

    private String username;

    private Float pagoEfectivo;

    private Float pagoDigital;

    private Float pagoCuentaCorriente;

    private Integer clienteCuentaCorriente;

    private Date fechaCancelacionCuentaCorriente;

    @OneToMany(mappedBy = "venta", cascade = CascadeType.ALL)
    private Set<ProductoVendido> productos;

    public Venta() {
        this.fechaYHora = Utiles.obtenerFechaYHoraActualDate();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Float getTotal() {
        Float total = 0f;
        for (ProductoVendido productoVendido : this.productos) {
            total += productoVendido.getTotal();
        }
        return total;
    }

    public Date getFechaYHora() {
        return fechaYHora;
    }

    public void setFechaYHora(Date fechaYHora) {
        this.fechaYHora = fechaYHora;
    }

    public Set<ProductoVendido> getProductos() {
        return productos;
    }

    public void setProductos(Set<ProductoVendido> productos) {
        this.productos = productos;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Float getPagoEfectivo() {
        return pagoEfectivo;
    }

    public void setPagoEfectivo(Float pagoEfectivo) {
        this.pagoEfectivo = pagoEfectivo;
    }

    public Float getPagoDigital() {
        return pagoDigital;
    }

    public void setPagoDigital(Float pagoDigital) {
        this.pagoDigital = pagoDigital;
    }

    public Float getPagoCuentaCorriente() {
        return pagoCuentaCorriente;
    }

    public void setPagoCuentaCorriente(Float pagoCuentaCorriente) {
        this.pagoCuentaCorriente = pagoCuentaCorriente;
    }

    public Integer getClienteCuentaCorriente() {
        return clienteCuentaCorriente;
    }

    public void setClienteCuentaCorriente(Integer clienteCuentaCorriente) {
        this.clienteCuentaCorriente = clienteCuentaCorriente;
    }

    public Date getFechaCancelacionCuentaCorriente() {
        return fechaCancelacionCuentaCorriente;
    }

    public void setFechaCancelacionCuentaCorriente(Date fechaCancelacionCuentaCorriente) {
        this.fechaCancelacionCuentaCorriente = fechaCancelacionCuentaCorriente;
    }
}
