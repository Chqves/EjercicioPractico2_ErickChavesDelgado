package com.eventosapp.plataforma.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDate;
import lombok.Data;

@Data
@Entity
@Table(name = "evento")
public class Evento implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long idEvento;

    @NotBlank
    @Size(max = 150)
    @Column(name = "nombre", length = 150)
    private String titulo;

    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String resumen;

    @NotNull
    @Column(name = "fecha")
    private LocalDate fechaEvento;

    @NotNull
    @Min(value = 1)
    @Column(name = "capacidad")
    private Integer aforo;

    @Column(name = "activo")
    private boolean habilitado = true;
}
