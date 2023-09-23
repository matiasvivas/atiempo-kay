package pl.codeleak.demos.sbt.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import pl.codeleak.demos.sbt.enumeradores.TipoCodigo;

@Entity
public class Codigo {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String serial;
    private TipoCodigo tipo;
    private String concepto;
    private Float monto;

    private Date fechaActivacion;

    private Integer diasRestantes;

    private Integer userId;

    public Codigo() {
    }

    public Codigo(Integer id, String serial, TipoCodigo tipo, String concepto, Float monto, Date fechaActivacion,
                  Integer diasRestantes, Integer userId) {
        this.id = id;
        this.serial = serial;
        this.tipo = tipo;
        this.concepto = concepto;
        this.monto = monto;
        this.fechaActivacion = fechaActivacion;
        this.diasRestantes = diasRestantes;
        this.userId = userId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public TipoCodigo getTipo() {
        return tipo;
    }

    public void setTipo(TipoCodigo tipo) {
        this.tipo = tipo;
    }

    public String getConcepto() {
        return concepto;
    }

    public void setConcepto(String concepto) {
        this.concepto = concepto;
    }

    public Float getMonto() {
        return monto;
    }

    public void setMonto(Float monto) {
        this.monto = monto;
    }

    public Date getFechaActivacion() {
        return fechaActivacion;
    }

    public void setFechaActivacion(Date fechaActivacion) {
        this.fechaActivacion = fechaActivacion;
    }

    public Integer getDiasRestantes() {
        return diasRestantes;
    }

    public void setDiasRestantes(Integer diasRestantes) {
        this.diasRestantes = diasRestantes;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
