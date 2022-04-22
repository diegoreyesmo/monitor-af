package com.rrfinformatica.monitoraf;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity
public class ActividadFisica {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NotNull
    private String actividad;
    private String categoria;
    @NotNull
    private String creadoPor;

    protected ActividadFisica() {
    }

    public ActividadFisica(String actividad, String categoria, String creadoPor) {
        this.actividad = actividad;
        this.categoria = categoria;
        this.creadoPor = creadoPor;
    }

    @Override
    public String toString() {
        return "ActividadFisica{" +
                "id=" + id +
                ", actividad='" + actividad + '\'' +
                ", categoria='" + categoria + '\'' +
                ", creadoPor='" + creadoPor + '\'' +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getActividad() {
        return actividad;
    }

    public void setActividad(String actividad) {
        this.actividad = actividad;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getCreadoPor() {
        return creadoPor;
    }

    public void setCreadoPor(String creadoPor) {
        this.creadoPor = creadoPor;
    }
}
