package com.eventosapp.plataforma.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;

@Data
@Entity
@Table(name = "usuario")
public class Miembro implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long idMiembro;

    @NotBlank
    @Size(max = 150)
    @Column(name = "nombre", length = 150)
    private String nombreCompleto;

    @Email
    @NotBlank
    @Size(max = 200)
    @Column(name = "email", unique = true, length = 200)
    private String correoElectronico;

    @Column(name = "password", length = 255)
    private String clave;

    @Column(name = "activo")
    private boolean habilitado = true;

    @Column(name = "fecha_creacion", updatable = false)
    private LocalDateTime fechaRegistro;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "rol_id")
    private Perfil perfil;

    @PrePersist
    protected void alInsertar() {
        this.fechaRegistro = LocalDateTime.now();
    }
}
