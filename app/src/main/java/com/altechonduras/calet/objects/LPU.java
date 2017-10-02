package com.altechonduras.calet.objects;

/**
 * Created by Nery Ortez on 27-Sep-17.
 */
public class LPU {
    private String RDA;
    private String idSitio;
    private String nombreSitio;
    private String id;
    private String name;
    private String falla;
    private String descripcion;
    private String time;

    public LPU() {}

    public String getName() {
        return name;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getRDA() {
        return RDA;
    }

    public void setRDA(String RDA) {
        this.RDA = RDA;
    }

    public String getIdSitio() {
        return idSitio;
    }

    public void setIdSitio(String idSitio) {
        this.idSitio = idSitio;
    }

    public String getNombreSitio() {
        return nombreSitio;
    }

    public void setNombreSitio(String nombreSitio) {
        this.nombreSitio = nombreSitio;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getFalla() {
        return falla;
    }

    public void setFalla(String falla) {
        this.falla = falla;
    }
}
