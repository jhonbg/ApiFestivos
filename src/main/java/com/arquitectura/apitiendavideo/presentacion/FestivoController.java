package com.arquitectura.apitiendavideo.presentacion;

import com.arquitectura.apitiendavideo.aplicacion.FestivoService;
import com.arquitectura.apitiendavideo.core.dominio.Festivo;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
            return ResponseEntity.ok("No es una fecha válida");
        }

        boolean esFestivo = festivoService.esFestivo(año, mes, dia);
        String respuesta = esFestivo ? "Es Festivo" : "No es festivo";

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

    @GetMapping("/listado/{año}")
    public ResponseEntity<List<Map<String, Object>>> listarFestivosPorAno(@PathVariable int año) {
        List<Map<String, Object>> festivos = festivoService.obtenerFestivosPorAno(año);
        return ResponseEntity.ok(festivos);
    }
}
