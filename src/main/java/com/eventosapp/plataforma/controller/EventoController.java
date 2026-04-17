package com.eventosapp.plataforma.controller;

import com.eventosapp.plataforma.domain.Evento;
import com.eventosapp.plataforma.service.ServicioEvento;
import jakarta.validation.Valid;
import java.util.Optional;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/evento")
public class EventoController {

    private final ServicioEvento servicioEvento;

    public EventoController(ServicioEvento servicioEvento) {
        this.servicioEvento = servicioEvento;
    }

    @GetMapping("/listado")
    public String listado(Model model) {
        var eventos = servicioEvento.obtenerTodos();
        model.addAttribute("eventos", eventos);
        model.addAttribute("totalEventos", eventos.size());
        model.addAttribute("totalActivos", servicioEvento.contarActivos());
        model.addAttribute("evento", new Evento());
        return "/evento/listado";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid Evento evento,
            BindingResult resultado,
            RedirectAttributes redirectAttributes) {
        if (resultado.hasErrors()) {
            redirectAttributes.addFlashAttribute("error", "Datos inválidos. Revisá el formulario.");
            if (evento.getIdEvento() == null) {
                return "redirect:/evento/listado";
            }
            return "redirect:/evento/modificar/" + evento.getIdEvento();
        }
        servicioEvento.guardar(evento);
        redirectAttributes.addFlashAttribute("todoOk", "Evento guardado correctamente.");
        return "redirect:/evento/listado";
    }

    @PostMapping("/eliminar")
    public String eliminar(@RequestParam Long idEvento,
            RedirectAttributes redirectAttributes) {
        try {
            servicioEvento.eliminar(idEvento);
            redirectAttributes.addFlashAttribute("todoOk", "Evento eliminado correctamente.");
        } catch (IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("error", "El evento no fue encontrado.");
        } catch (IllegalStateException ex) {
            redirectAttributes.addFlashAttribute("error", "No se puede eliminar. Tiene datos asociados.");
        }
        return "redirect:/evento/listado";
    }

    @GetMapping("/modificar/{idEvento}")
    public String modificar(@PathVariable Long idEvento,
            Model model, RedirectAttributes redirectAttributes) {
        Optional<Evento> encontrado = servicioEvento.buscarPorId(idEvento);
        if (encontrado.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "El evento no fue encontrado.");
            return "redirect:/evento/listado";
        }
        model.addAttribute("evento", encontrado.get());
        return "/evento/modifica";
    }
}
