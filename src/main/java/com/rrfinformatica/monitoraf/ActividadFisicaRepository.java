package com.rrfinformatica.monitoraf;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ActividadFisicaRepository extends CrudRepository<ActividadFisica, Long> {
    List<ActividadFisica> findByCreadoPor(String creadoPor);
    List<ActividadFisica> findByActividad(String actividad);
    ActividadFisica findById(long id);
}
