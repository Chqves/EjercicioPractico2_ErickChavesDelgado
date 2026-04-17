package com.eventosapp.plataforma.service;

import com.eventosapp.plataforma.domain.Miembro;
import com.eventosapp.plataforma.repository.MiembroRepository;
import jakarta.mail.MessagingException;
import java.util.List;
import java.util.Optional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ServicioMiembro {

    private final MiembroRepository repositorioMiembros;
    private final PasswordEncoder codificadorClave;
    private final MensajeriaService mensajeria;

    public ServicioMiembro(MiembroRepository repositorioMiembros,
            PasswordEncoder codificadorClave,
            MensajeriaService mensajeria) {
        this.repositorioMiembros = repositorioMiembros;
        this.codificadorClave = codificadorClave;
        this.mensajeria = mensajeria;
    }

    @Transactional(readOnly = true)
    public List<Miembro> obtenerTodos() {
        return repositorioMiembros.findAll();
    }

    @Transactional(readOnly = true)
    public List<Miembro> obtenerHabilitados() {
        return repositorioMiembros.findByHabilitadoTrue();
    }

    @Transactional(readOnly = true)
    public Optional<Miembro> buscarPorId(Long idMiembro) {
        return repositorioMiembros.findById(idMiembro);
    }

    @Transactional
    public void guardar(Miembro miembro) {
        boolean esNuevo = miembro.getIdMiembro() == null;

        if (esNuevo) {
            miembro.setClave(codificadorClave.encode(miembro.getClave()));
        } else {
            if (miembro.getClave() == null || miembro.getClave().isBlank()) {
                Miembro existente = repositorioMiembros.findById(miembro.getIdMiembro())
                        .orElseThrow(() -> new IllegalArgumentException("Miembro no encontrado."));
                miembro.setClave(existente.getClave());
            } else {
                miembro.setClave(codificadorClave.encode(miembro.getClave()));
            }
        }

        repositorioMiembros.save(miembro);

        if (esNuevo) {
            try {
                mensajeria.enviarBienvenida(miembro.getCorreoElectronico(), miembro.getNombreCompleto());
            } catch (MessagingException ex) {
                // El correo falla silenciosamente para no interrumpir el flujo
            }
        }
    }

    @Transactional
    public void eliminar(Long idMiembro) {
        if (!repositorioMiembros.existsById(idMiembro)) {
            throw new IllegalArgumentException("El miembro con ID " + idMiembro + " no existe.");
        }
        try {
            repositorioMiembros.deleteById(idMiembro);
        } catch (DataIntegrityViolationException ex) {
            throw new IllegalStateException("No se puede eliminar el miembro. Tiene datos asociados.", ex);
        }
    }
}
