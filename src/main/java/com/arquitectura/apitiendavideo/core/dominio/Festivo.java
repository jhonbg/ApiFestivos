package com.arquitectura.apitiendavideo.core.dominio;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "festivo")
@Data
public class Festivo {
    @Id
    private Integer id;
    private Integer dia;
    private Integer mes;
    private String nombre;
    private Integer idtipo;
    private Integer diaspascua;
}
