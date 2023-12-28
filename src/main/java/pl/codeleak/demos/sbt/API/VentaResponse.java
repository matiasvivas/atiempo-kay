package pl.codeleak.demos.sbt.API;

import java.util.Date;

public class VentaResponse {
    private Date fechaHora;
    private String detalle;
    private Float montoPendiente;

    public VentaResponse(){}
    public VentaResponse(Date fechaHora, String detalle, Float montoPendiente) {
        this.fechaHora = fechaHora;
        this.detalle = detalle;
        this.montoPendiente = montoPendiente;
    }

    public Date getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(Date fechaHora) {
        this.fechaHora = fechaHora;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    public Float getMontoPendiente() {
        return montoPendiente;
    }

    public void setMontoPendiente(Float montoPendiente) {
        this.montoPendiente = montoPendiente;
    }
}
