package com.arquitectura.apitiendavideo.core.interfaces.repositorios;

import com.arquitectura.apitiendavideo.core.dominio.Festivo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IFestivoRepositorio extends JpaRepository<Festivo, Integer> {
}
