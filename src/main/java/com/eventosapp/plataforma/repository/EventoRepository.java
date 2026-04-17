package com.eventosapp.plataforma.repository;

import com.eventosapp.plataforma.domain.Evento;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EventoRepository extends JpaRepository<Evento, Long> {

    // Consulta derivada 1: por estado activo/inactivo
    List<Evento> findByHabilitado(boolean estado);

    // Consulta derivada 2: por rango de fechas
    List<Evento> findByFechaEventoBetweenOrderByFechaEventoAsc(LocalDate inicio, LocalDate fin);

    // Consulta derivada 3: busqueda por nombre parcial
    List<Evento> findByTituloContainingIgnoreCaseOrderByFechaEventoAsc(String texto);

    // Consulta JPQL: contar eventos activos
    @Query("SELECT COUNT(e) FROM Evento e WHERE e.habilitado = true")
    long contarActivos();

    // Consulta SQL nativa: eventos activos ordenados por fecha
    @Query(nativeQuery = true,
           value = "SELECT * FROM evento WHERE activo = true ORDER BY fecha ASC")
    List<Evento> listarActivosOrdenadosPorFecha();
}
