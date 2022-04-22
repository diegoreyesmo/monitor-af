package com.rrfinformatica.monitoraf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(path = "/af")
public class ActividadFisicaController {
    @Autowired
    private ActividadFisicaRepository repository;

    @PostMapping(path = "/add") // Map ONLY POST Requests
    public @ResponseBody
    String addNewActividadFisica(@RequestBody ActividadFisica actividadFisica) {
        repository.save(actividadFisica);
        return "saved";
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
