package com.rrfinformatica.monitoraf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping(path = "/registro")
public class RegistroController {
    @Autowired
    private RegistroRepository repository;

    @PostMapping(path = "/add") // Map ONLY POST Requests
    public @ResponseBody
    String addNewRegistro(@Valid @RequestBody Registro registro) {
        repository.save(registro);
        return "saved";
    }

    @GetMapping(path = "/get")
    public @ResponseBody
    Iterable<Registro> getRegistroPorUsuario(@RequestParam String usuario) {
        return repository.findByUsuario(usuario);
    }

    @GetMapping(path = "/all")
    public @ResponseBody
    Iterable<Registro> getAll() {
        // This returns a JSON or XML with the users
        return repository.findAll();
    }
}
