package com.altechonduras.calet.objects;

/**
 * Created by Nery Ortez on 27-Sep-17.
 */

public class MGP {
        private String RDA;
        private String idSitio;
        private String nombreSitio;
        private String combustible;
        private String horaInicio;
        private String horaFinal;
        private String fecha;
        private String id;

        public MGP() {}


        public String getId() {
            return id;
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

        public void setId(String id) {
            this.id = id;
        }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
}
