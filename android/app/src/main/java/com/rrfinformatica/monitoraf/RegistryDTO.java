package com.rrfinformatica.monitoraf;

public class RegistryDTO {
    public String actividad;
    public String usuario;
    public String timestamp;
    public String duracion;

    public RegistryDTO(String actividad, String usuario, String timestamp, String duracion) {
        this.actividad = actividad;
        this.usuario = usuario;
        this.timestamp = timestamp;
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

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getDuracion() {
        return duracion;
    }

    public void setDuracion(String duracion) {
        this.duracion = duracion;
    }
}
