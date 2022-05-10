package com.rrfinformatica.monitoraf;

public class RegistryDTO {
    public String actividad;
    public String usuario;
    public String incio;
    public String termino;
    public String duracion;

    public RegistryDTO(String actividad, String usuario, String incio, String termino, String duracion) {
        this.actividad = actividad;
        this.usuario = usuario;
        this.incio = incio;
        this.termino = termino;
        this.duracion = duracion;
    }

    public String getActividad() {
        return actividad;
    }

    public void setActividad(String actividad) {
        this.actividad = actividad;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getIncio() {
        return incio;
    }

    public void setIncio(String incio) {
        this.incio = incio;
    }

    public String getTermino() {
        return termino;
    }

    public void setTermino(String termino) {
        this.termino = termino;
    }

    public String getDuracion() {
        return duracion;
    }

    public void setDuracion(String duracion) {
        this.duracion = duracion;
    }
}
