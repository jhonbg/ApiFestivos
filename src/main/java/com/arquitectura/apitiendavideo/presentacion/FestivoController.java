package com.arquitectura.apitiendavideo.presentacion;

import com.arquitectura.apitiendavideo.aplicacion.FestivoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.DateTimeException;
import java.time.LocalDate;

@RestController
@RequestMapping("/festivos")
public class FestivoController {
    private static final Logger LOGGER = LoggerFactory.getLogger(FestivoController.class);

    @Autowired
    private FestivoService festivoService;

    @GetMapping("/verificar/{año}/{mes}/{dia}")
    public ResponseEntity<String> verificarFestividad(@PathVariable int año, @PathVariable int mes, @PathVariable int dia) {
        LOGGER.info("Verificando festividad para {}-{}-{}", año, mes, dia);

        try {
            LocalDate.of(año, mes, dia); // Verificar si la fecha es válida
        } catch (DateTimeException e) {
            LOGGER.error("Fecha no válida: {}-{}-{}", año, mes, dia);
            return ResponseEntity.badRequest().body("Fecha no válida");
        }

        boolean esFestivo = festivoService.esFestivo(año, mes, dia);
        String respuesta = esFestivo ? "Es Festivo" : "No es festivo";

        if (!esFestivo && !fechaValida(año, mes, dia)) {
            respuesta = "No es una fecha válida";
        }

        return ResponseEntity.ok(respuesta);
    }

    private boolean fechaValida(int año, int mes, int dia) {
        try {
            LocalDate.of(año, mes, dia);
            return true;
        } catch (DateTimeException e) {
            return false;
        }
    }
}
