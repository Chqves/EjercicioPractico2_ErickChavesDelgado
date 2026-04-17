package com.eventosapp.plataforma.repository;

import com.eventosapp.plataforma.domain.Perfil;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PerfilRepository extends JpaRepository<Perfil, Long> {

    Optional<Perfil> findByTitulo(String titulo);

    boolean existsByTitulo(String titulo);
}
