package com.eventosapp.plataforma.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import lombok.Data;

@Data
@Entity
@Table(name = "rol")
public class Perfil implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long idPerfil;

    @NotBlank
    @Size(max = 100)
    @Column(name = "nombre", unique = true, nullable = false, length = 100)
    private String titulo;
}
