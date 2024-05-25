package com.arquitectura.apitiendavideo.core.interfaces.servicios;

import com.arquitectura.apitiendavideo.core.dominio.Festivo;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface IFestivoServicio {

    public List<Map<String, Object>> obtenerFestivosPorAno(int año);

    public boolean esFestivo(int año, int mes, int dia);

    public LocalDate calcularFechaFestivo(Festivo festivo, int año);
}
