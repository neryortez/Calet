package com.altechonduras.calet.objects;

import android.support.annotation.Keep;

/**
 * Created by Nery Ortez on 27-Sep-17.
 */
@Keep
public class MGP {
        private String RDA;
        private String idSitio;
        private String nombreSitio;
        private String combustible;
        private String horaInicio;
        private String horaFinal;
        private String fecha;
        private String id;
    private String gastoAcarreo;
    private String comentarios;

        public MGP() {}


        public String getId() {
            return id;
        }

    public void setId(String id) {
        this.id = id;
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

        public String getCombustible() {
            return combustible;
        }

        public void setCombustible(String combustible) {
            this.combustible = combustible;
        }

        public String getHoraInicio() {
            return horaInicio;
        }

        public void setHoraInicio(String horaInicio) {
            this.horaInicio = horaInicio;
        }

        public String getHoraFinal() {
            return horaFinal;
        }

        public void setHoraFinal(String horaFinal) {
            this.horaFinal = horaFinal;
        }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getGastoAcarreo() {
        return gastoAcarreo;
    }

    public void setGastoAcarreo(String gastoAcarreo) {
        this.gastoAcarreo = gastoAcarreo;
    }

    public String getComentarios() {
        return comentarios;
    }

    public void setComentarios(String comentarios) {
        this.comentarios = comentarios;
    }
}
