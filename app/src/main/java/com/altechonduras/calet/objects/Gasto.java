package com.altechonduras.calet.objects;

import android.support.annotation.Keep;

/**
 * Created by Nery Ortez on 27-Sep-17.
 */
@Keep
public class Gasto {
    private String time;
    private String RDA;
    private String nombreSitio;
    private String gasto;
    private String autorizado;
    private String pagoA;
    private String cedula;
    private String descripcion;

    private String telefono;

    public Gasto() {
    }

    public String getPagoA() {
        return pagoA;
    }

    public void setPagoA(String pagoA) {
        this.pagoA = pagoA;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getRDA() {
        return RDA;
    }

    public void setRDA(String RDA) {
        this.RDA = RDA;
    }

    public String getGasto() {
        return gasto;
    }

    public void setGasto(String gasto) {
        this.gasto = gasto;
    }

    public String getNombreSitio() {
        return nombreSitio;
    }

    public void setNombreSitio(String nombreSitio) {
        this.nombreSitio = nombreSitio;
    }

    public String getAutorizado() {
        return autorizado;
    }

    public void setAutorizado(String autorizado) {
        this.autorizado = autorizado;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
}
