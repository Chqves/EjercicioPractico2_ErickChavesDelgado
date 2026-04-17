package com.eventosapp.plataforma.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class ConfiguracionSeguridad {

    @Bean
    public SecurityFilterChain cadenaFiltros(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests(peticiones -> {
            peticiones.requestMatchers("/webjars/**", "/css/**", "/js/**", "/login").permitAll();
            peticiones.requestMatchers("/evento/listado").hasAnyRole("ADMIN", "ORGANIZADOR", "CLIENTE");
            peticiones.requestMatchers("/evento/**").hasAnyRole("ADMIN", "ORGANIZADOR");
            peticiones.requestMatchers("/miembro/**", "/perfil/**").hasRole("ADMIN");
            peticiones.requestMatchers("/busqueda/**").hasAnyRole("ADMIN", "ORGANIZADOR", "CLIENTE");
            peticiones.anyRequest().authenticated();
        });

        http.formLogin(formulario -> formulario
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/", true)
                .failureUrl("/login?error=true")
                .permitAll()
        ).logout(salida -> salida
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout=true")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll()
        ).exceptionHandling(excepciones -> excepciones
                .accessDeniedPage("/acceso_denegado")
        ).sessionManagement(sesion -> sesion
                .maximumSessions(1)
                .maxSessionsPreventsLogin(false)
        );

        return http.build();
    }

    @Bean
    public PasswordEncoder codificadorClave() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    public void enlazarAutenticacion(AuthenticationManagerBuilder builder,
            @Lazy PasswordEncoder codificadorClave,
            @Lazy UserDetailsService userDetailsService) throws Exception {
        builder.userDetailsService(userDetailsService).passwordEncoder(codificadorClave);
    }
}
