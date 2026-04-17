package com.eventosapp.plataforma.repository;

import com.eventosapp.plataforma.domain.Miembro;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MiembroRepository extends JpaRepository<Miembro, Long> {

    Optional<Miembro> findByCorreoElectronicoAndHabilitadoTrue(String correo);

    Optional<Miembro> findByCorreoElectronico(String correo);

    List<Miembro> findByHabilitadoTrue();

    List<Miembro> findByPerfil_IdPerfil(Long idPerfil);

    boolean existsByCorreoElectronico(String correo);
}
