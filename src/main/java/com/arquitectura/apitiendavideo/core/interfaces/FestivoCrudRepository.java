package com.arquitectura.apitiendavideo.core.interfaces;

import com.arquitectura.apitiendavideo.core.dominio.Festivo;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface FestivoCrudRepository  extends CrudRepository<Festivo, Integer> {
}
