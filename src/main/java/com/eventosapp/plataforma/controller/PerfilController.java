package com.eventosapp.plataforma.controller;

import com.eventosapp.plataforma.domain.Perfil;
import com.eventosapp.plataforma.service.ServicioPerfil;
import jakarta.validation.Valid;
import java.util.Optional;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/perfil")
public class PerfilController {

    private final ServicioPerfil servicioPerfil;

    public PerfilController(ServicioPerfil servicioPerfil) {
        this.servicioPerfil = servicioPerfil;
    }

    @GetMapping("/listado")
    public String listado(Model model) {
        var perfiles = servicioPerfil.obtenerTodos();
        model.addAttribute("perfiles", perfiles);
        model.addAttribute("totalPerfiles", perfiles.size());
        model.addAttribute("perfil", new Perfil());
        return "/perfil/listado";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid Perfil perfil,
            RedirectAttributes redirectAttributes) {
        servicioPerfil.guardar(perfil);
        redirectAttributes.addFlashAttribute("todoOk", "Perfil guardado correctamente.");
        return "redirect:/perfil/listado";
    }

    @PostMapping("/eliminar")
    public String eliminar(@RequestParam Long idPerfil,
            RedirectAttributes redirectAttributes) {
        try {
            servicioPerfil.eliminar(idPerfil);
            redirectAttributes.addFlashAttribute("todoOk", "Perfil eliminado correctamente.");
        } catch (IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("error", "El perfil no fue encontrado.");
        } catch (IllegalStateException ex) {
            redirectAttributes.addFlashAttribute("error", "No se puede eliminar. Tiene miembros asociados.");
        }
        return "redirect:/perfil/listado";
    }

    @GetMapping("/modificar/{idPerfil}")
    public String modificar(@PathVariable Long idPerfil,
            Model model, RedirectAttributes redirectAttributes) {
        Optional<Perfil> encontrado = servicioPerfil.buscarPorId(idPerfil);
        if (encontrado.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "El perfil no fue encontrado.");
            return "redirect:/perfil/listado";
        }
        model.addAttribute("perfil", encontrado.get());
        return "/perfil/modifica";
    }
}
