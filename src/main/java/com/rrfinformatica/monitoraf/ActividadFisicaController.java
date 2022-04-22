package com.rrfinformatica.monitoraf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(path = "/af")
public class ActividadFisicaController {
    @Autowired
    private ActividadFisicaRepository repository;

    @PostMapping(path = "/add") // Map ONLY POST Requests
    public @ResponseBody
    String addNewActividadFisica(@RequestBody String tipo, @RequestBody String categoria, @RequestBody String creadoPor) {
        ActividadFisica actividadFisica = new ActividadFisica(tipo, categoria, creadoPor);
        repository.save(actividadFisica);
        return "Saved";
    }

    @PostMapping(path = "/get") // Map ONLY POST Requests
    public @ResponseBody
    Iterable<ActividadFisica> getActividadFisicaCreadoPo(@RequestBody String creadoPor) {
        return repository.findByCreadoPor(creadoPor);
    }

    @GetMapping(path = "/all")
    public @ResponseBody
    Iterable<ActividadFisica> getAll() {
        // This returns a JSON or XML with the users
        return repository.findAll();
    }
}