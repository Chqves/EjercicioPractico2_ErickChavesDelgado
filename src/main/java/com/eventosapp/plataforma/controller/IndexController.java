package com.eventosapp.plataforma.controller;

import com.eventosapp.plataforma.service.ServicioEvento;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

    private final ServicioEvento servicioEvento;

    public IndexController(ServicioEvento servicioEvento) {
        this.servicioEvento = servicioEvento;
    }

    @GetMapping("/")
    public String inicio(Authentication autenticacion, Model model) {
        var autoridades = autenticacion.getAuthorities();
        boolean esAdmin = autoridades.stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        boolean esOrganizador = autoridades.stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ORGANIZADOR"));

        if (esAdmin) {
            return "redirect:/miembro/listado";
        } else if (esOrganizador) {
            return "redirect:/evento/listado";
        }
        model.addAttribute("eventosDisponibles", servicioEvento.obtenerActivos());
        return "index";
    }
}
