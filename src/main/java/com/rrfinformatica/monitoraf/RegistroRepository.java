package com.rrfinformatica.monitoraf;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RegistroRepository extends CrudRepository<Registro, Long> {
    List<Registro> findAll(String creadoPor);
    List<Registro> findByUsuario(String usuario);
    ActividadFisica findById(long id);
}
