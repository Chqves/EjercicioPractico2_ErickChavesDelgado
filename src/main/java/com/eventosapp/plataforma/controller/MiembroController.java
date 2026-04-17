package com.eventosapp.plataforma.controller;

import com.eventosapp.plataforma.domain.Miembro;
import com.eventosapp.plataforma.service.ServicioMiembro;
import com.eventosapp.plataforma.service.ServicioPerfil;
import jakarta.validation.Valid;
import java.util.Optional;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/miembro")
public class MiembroController {

    private final ServicioMiembro servicioMiembro;
    private final ServicioPerfil servicioPerfil;

    public MiembroController(ServicioMiembro servicioMiembro, ServicioPerfil servicioPerfil) {
        this.servicioMiembro = servicioMiembro;
        this.servicioPerfil = servicioPerfil;
    }

    @GetMapping("/listado")
    public String listado(Model model) {
        var miembros = servicioMiembro.obtenerTodos();
        model.addAttribute("miembros", miembros);
        model.addAttribute("totalMiembros", miembros.size());
        model.addAttribute("miembro", new Miembro());
        model.addAttribute("perfiles", servicioPerfil.obtenerTodos());
        return "/miembro/listado";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid Miembro miembro,
            BindingResult resultado,
            RedirectAttributes redirectAttributes) {
        if (resultado.hasErrors()) {
            redirectAttributes.addFlashAttribute("error", "Datos inválidos. Revisá el formulario.");
            if (miembro.getIdMiembro() == null) {
                return "redirect:/miembro/listado";
            }
            return "redirect:/miembro/modificar/" + miembro.getIdMiembro();
        }
        servicioMiembro.guardar(miembro);
        redirectAttributes.addFlashAttribute("todoOk", "Miembro guardado correctamente.");
        return "redirect:/miembro/listado";
    }

    @PostMapping("/eliminar")
    public String eliminar(@RequestParam Long idMiembro,
            RedirectAttributes redirectAttributes) {
        try {
            servicioMiembro.eliminar(idMiembro);
            redirectAttributes.addFlashAttribute("todoOk", "Miembro eliminado correctamente.");
        } catch (IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("error", "El miembro no fue encontrado.");
        } catch (IllegalStateException ex) {
            redirectAttributes.addFlashAttribute("error", "No se puede eliminar. Tiene datos asociados.");
        }
        return "redirect:/miembro/listado";
    }

    @GetMapping("/modificar/{idMiembro}")
    public String modificar(@PathVariable Long idMiembro,
            Model model, RedirectAttributes redirectAttributes) {
        Optional<Miembro> encontrado = servicioMiembro.buscarPorId(idMiembro);
        if (encontrado.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "El miembro no fue encontrado.");
            return "redirect:/miembro/listado";
        }
        Miembro miembro = encontrado.get();
        miembro.setClave("");
        model.addAttribute("miembro", miembro);
        model.addAttribute("perfiles", servicioPerfil.obtenerTodos());
        return "/miembro/modifica";
    }
}
