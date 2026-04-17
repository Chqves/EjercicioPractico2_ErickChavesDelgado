package com.eventosapp.plataforma.service;

import com.eventosapp.plataforma.domain.Miembro;
import com.eventosapp.plataforma.repository.MiembroRepository;
import java.util.Set;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("userDetailsService")
public class ServicioAutenticacion implements UserDetailsService {

    private final MiembroRepository repositorioMiembros;

    public ServicioAutenticacion(MiembroRepository repositorioMiembros) {
        this.repositorioMiembros = repositorioMiembros;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String correo) throws UsernameNotFoundException {
        Miembro miembro = repositorioMiembros.findByCorreoElectronicoAndHabilitadoTrue(correo)
                .orElseThrow(() -> new UsernameNotFoundException("Miembro no encontrado: " + correo));

        var autoridad = new SimpleGrantedAuthority("ROLE_" + miembro.getPerfil().getTitulo());

        return new User(miembro.getCorreoElectronico(), miembro.getClave(), Set.of(autoridad));
    }
}
