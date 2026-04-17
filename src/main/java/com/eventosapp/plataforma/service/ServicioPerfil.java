package com.eventosapp.plataforma.service;

import com.eventosapp.plataforma.domain.Perfil;
import com.eventosapp.plataforma.repository.PerfilRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ServicioPerfil {

    private final PerfilRepository repositorioPerfiles;

    public ServicioPerfil(PerfilRepository repositorioPerfiles) {
        this.repositorioPerfiles = repositorioPerfiles;
    }

    @Transactional(readOnly = true)
    public List<Perfil> obtenerTodos() {
        return repositorioPerfiles.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Perfil> buscarPorId(Long idPerfil) {
        return repositorioPerfiles.findById(idPerfil);
    }

    @Transactional
    public void guardar(Perfil perfil) {
        repositorioPerfiles.save(perfil);
    }

    @Transactional
    public void eliminar(Long idPerfil) {
        if (!repositorioPerfiles.existsById(idPerfil)) {
            throw new IllegalArgumentException("El perfil con ID " + idPerfil + " no existe.");
        }
        try {
            repositorioPerfiles.deleteById(idPerfil);
        } catch (DataIntegrityViolationException ex) {
            throw new IllegalStateException("No se puede eliminar el perfil. Tiene miembros asociados.", ex);
        }
    }
}
