package com.arquitectura.apitiendavideo.aplicacion;

import com.arquitectura.apitiendavideo.core.dominio.Festivo;
import com.arquitectura.apitiendavideo.core.interfaces.repositorios.IFestivoRepositorio;
import com.arquitectura.apitiendavideo.core.interfaces.servicios.IFestivoServicio;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FestivoService implements IFestivoServicio {
    private static final Logger LOGGER = LoggerFactory.getLogger(FestivoService.class);

    @Autowired
    private IFestivoRepositorio repositorio;

    public FestivoService(IFestivoRepositorio repositorio) {
        this.repositorio = repositorio;
    }


    @Override
    public List<Map<String, Object>> obtenerFestivosPorAno(int año) {
        List<Map<String, Object>> festivosDelAno = new ArrayList<>();
        for (Festivo festivo : repositorio.findAll()) {
            LocalDate fechaFestivo = calcularFechaFestivo(festivo, año);
            if (fechaFestivo != null && fechaFestivo.getYear() == año) {
                Map<String, Object> festivoMap = new HashMap<>();
                festivoMap.put("nombre", festivo.getNombre());
                festivoMap.put("año", año);
                festivoMap.put("mes", fechaFestivo.getMonthValue());
                festivoMap.put("dia", fechaFestivo.getDayOfMonth());
                festivosDelAno.add(festivoMap);
            }
        }
        return festivosDelAno;
    }

    @Override
    public boolean esFestivo(int año, int mes, int dia) {
            LocalDate fecha = LocalDate.of(año, mes, dia);
            for (Festivo festivo : repositorio.findAll()) {
                LocalDate fechaFestivo = calcularFechaFestivo(festivo, año);
                if (fechaFestivo != null && fechaFestivo.isEqual(fecha)) {
                    LOGGER.info("Festivo encontrado: {}", festivo.getNombre());
                    return true;
                }
            }
            return false;
        }

    @Override
    public LocalDate calcularFechaFestivo(Festivo festivo, int año) {
        LocalDate fechaFestivo = null;
        if (festivo.getIdtipo() == 1) { // Festivo fijo
            fechaFestivo = LocalDate.of(año, festivo.getMes(), festivo.getDia());
        } else if (festivo.getIdtipo() == 2) { // Ley de "Puente festivo"
            fechaFestivo = LocalDate.of(año, festivo.getMes(), festivo.getDia());
            if (fechaFestivo.getDayOfWeek() != DayOfWeek.MONDAY) {
                fechaFestivo = fechaFestivo.with(TemporalAdjusters.next(DayOfWeek.MONDAY));
            }
        } else if (festivo.getIdtipo() == 3) { // Festivo basado en el domingo de Pascua
            fechaFestivo = calcularDomingoPascua(año).plusDays(festivo.getDiaspascua());
        } else if (festivo.getIdtipo() == 4) { // Festivo basado en el domingo de Pascua y Ley de "Puente festivo"
            fechaFestivo = calcularDomingoPascua(año).plusDays(festivo.getDiaspascua());
            if (fechaFestivo.getDayOfMonth() != festivo.getDia()) {
                fechaFestivo = fechaFestivo.with(TemporalAdjusters.next(DayOfWeek.MONDAY));
            }
        }
        return fechaFestivo;
    }

    private LocalDate calcularDomingoPascua(int año) {
        int a = año % 19;
        int b = año % 4;
        int c = año % 7;
        int d = (19 * a + 24) % 30;
        int dias = d + (2 * b + 4 * c + 6 * d + 5) % 7;
        int diaDomingoPascua = 15 + dias + 7;
        if (diaDomingoPascua > 31) {
            diaDomingoPascua -= 31;
            return LocalDate.of(año, 4, diaDomingoPascua);
        } else {
            return LocalDate.of(año, 3, diaDomingoPascua);
        }
    }
}
