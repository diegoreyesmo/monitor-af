package com.rrfinformatica.monitoraf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/registro")
public class RegistroController {
    @Autowired
    private RegistroRepository repository;

    @PostMapping(path = "/add") // Map ONLY POST Requests
    public @ResponseBody
    Registro addNewRegistro(@Valid @RequestBody Registro registro) {
        return repository.save(registro);
    }

    @GetMapping(path = "/get")
    public @ResponseBody
    Iterable<Registro> getRegistroPorUsuario(@RequestParam String usuario) {
        return repository.findByUsuario(usuario);
    }

    @GetMapping(path = "/all")
    public @ResponseBody
    Iterable<Registro> getAll() {
        return repository.findAll();
    }
}
