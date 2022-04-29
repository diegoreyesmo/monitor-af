package com.rrfinformatica.monitoraf;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity
public class Registro {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NotNull
    private String actividad;
    @NotNull
    private String timestamp;
    @NotNull
    private String usuario;
    @NotNull
    private Integer duracion;

}
