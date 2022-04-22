package com.rrfinformatica.monitoraf;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class MonitorAfApplication {
    private static final Logger log = LoggerFactory.getLogger(MonitorAfApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(MonitorAfApplication.class, args);
    }

    @GetMapping("/hello")
    public String hello(@RequestParam(value = "name", defaultValue = "World") String name) {
        return String.format("Hello %s!", name);
    }

    @Bean
    public CommandLineRunner demo(ActividadFisicaRepository repository) {
        return (args) -> {
            // save a few customers
            repository.save(new ActividadFisica("tipo1", "categoria1", "usuario1"));
            repository.save(new ActividadFisica("tipo2", "categoria2", "usuario2"));
            repository.save(new ActividadFisica("tipo3", "categoria1", "usuario2"));
            repository.save(new ActividadFisica("tipo4", "categoria2", "usuario2"));
            repository.save(new ActividadFisica("tipo5", "categoria1", "usuario1"));


            // fetch all customers
            log.info("ActividadFisicas found with findAll():");
            log.info("-------------------------------");
            for (ActividadFisica actividadFisica : repository.findAll()) {
                log.info(actividadFisica.toString());
            }
            log.info("");

            // fetch an individual actividadFisica by ID
            ActividadFisica actividadFisica = repository.findById(1L);
            log.info("ActividadFisica found with findById(1L):");
            log.info("--------------------------------");
            log.info(actividadFisica.toString());
            log.info("");

            // fetch customers by last name
            log.info("ActividadFisica found with findByCreadoPor('usuario2'):");
            log.info("--------------------------------------------");
            repository.findByCreadoPor("usuario2").forEach(bauer -> {
                log.info(bauer.toString());
            });

            log.info("");
        };
    }
}
