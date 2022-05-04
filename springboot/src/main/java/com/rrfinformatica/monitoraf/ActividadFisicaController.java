package com.rrfinformatica.monitoraf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/af")
public class ActividadFisicaController {
    @Autowired
    private ActividadFisicaRepository repository;

    @PostMapping(path = "/add") // Map ONLY POST Requests
    public @ResponseBody
    ActividadFisica addNewActividadFisica(@Valid @RequestBody ActividadFisica actividadFisica) {
        return repository.save(actividadFisica);
    }

    @GetMapping(path = "/get")
    public @ResponseBody
    Iterable<ActividadFisica> getActividadFisicaCreadoPo(@RequestParam String creadoPor) {
        return repository.findByCreadoPor(creadoPor);
    }

    @GetMapping(path = "/all")
    public @ResponseBody
    Iterable<ActividadFisica> getAll() {
        // This returns a JSON or XML with the users
        return repository.findAll();
    }
}
