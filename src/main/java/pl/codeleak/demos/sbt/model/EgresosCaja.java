package pl.codeleak.demos.sbt.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Entity
public class EgresosCaja {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private Float monto;
    private String motivo;
    private boolean esCajaFuerte;
    private boolean revisado;
    private Date fechaHoraEgreso;
    private String username;

    public EgresosCaja (){}

    public EgresosCaja(Integer id, Float monto, String motivo, boolean esCajaFuerte, boolean revisado,
                       Date fechaHoraEgreso,
                       String username) {
        this.id = id;
        this.monto = monto;
        this.motivo = motivo;
        this.esCajaFuerte = esCajaFuerte;
        this.revisado = revisado;
        this.fechaHoraEgreso = fechaHoraEgreso;
        this.username = username;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Float getMonto() {
        return monto;
    }

    public void setMonto(Float monto) {
        this.monto = monto;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public boolean isEsCajaFuerte() {
        return esCajaFuerte;
    }

    public void setEsCajaFuerte(boolean esCajaFuerte) {
        this.esCajaFuerte = esCajaFuerte;
    }

    public boolean isRevisado() {
        return revisado;
    }

    public void setRevisado(boolean revisado) {
        this.revisado = revisado;
    }

    public Date getFechaHoraEgreso() {
        return fechaHoraEgreso;
    }

    public void setFechaHoraEgreso(Date fechaHoraEgreso) {
        this.fechaHoraEgreso = fechaHoraEgreso;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
