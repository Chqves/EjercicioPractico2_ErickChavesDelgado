package com.eventosapp.plataforma.service;

import com.eventosapp.plataforma.domain.Evento;
import com.eventosapp.plataforma.repository.EventoRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ServicioEvento {

    private final EventoRepository repositorioEventos;

    public ServicioEvento(EventoRepository repositorioEventos) {
        this.repositorioEventos = repositorioEventos;
    }

    @Transactional(readOnly = true)
    public List<Evento> obtenerTodos() {
        return repositorioEventos.findAll();
    }

    @Transactional(readOnly = true)
    public List<Evento> obtenerActivos() {
        return repositorioEventos.findByHabilitado(true);
    }

    @Transactional(readOnly = true)
    public Optional<Evento> buscarPorId(Long idEvento) {
        return repositorioEventos.findById(idEvento);
    }

    @Transactional(readOnly = true)
    public List<Evento> buscarPorRangoFechas(LocalDate inicio, LocalDate fin) {
        return repositorioEventos.findByFechaEventoBetweenOrderByFechaEventoAsc(inicio, fin);
    }

    @Transactional(readOnly = true)
    public List<Evento> buscarPorNombre(String texto) {
        return repositorioEventos.findByTituloContainingIgnoreCaseOrderByFechaEventoAsc(texto);
    }

    @Transactional(readOnly = true)
    public List<Evento> buscarPorEstado(boolean estado) {
        return repositorioEventos.findByHabilitado(estado);
    }

    @Transactional(readOnly = true)
    public long contarActivos() {
        return repositorioEventos.contarActivos();
    }

    @Transactional
    public void guardar(Evento evento) {
        repositorioEventos.save(evento);
    }

    @Transactional
    public void eliminar(Long idEvento) {
        if (!repositorioEventos.existsById(idEvento)) {
            throw new IllegalArgumentException("El evento con ID " + idEvento + " no existe.");
        }
        try {
            repositorioEventos.deleteById(idEvento);
        } catch (DataIntegrityViolationException ex) {
            throw new IllegalStateException("No se puede eliminar el evento. Tiene datos asociados.", ex);
        }
    }
}
