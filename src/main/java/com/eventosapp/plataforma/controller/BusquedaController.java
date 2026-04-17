package com.eventosapp.plataforma.controller;

import com.eventosapp.plataforma.service.ServicioEvento;
import com.eventosapp.plataforma.service.ServicioMiembro;
import com.eventosapp.plataforma.service.ServicioPerfil;
import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/busqueda")
public class BusquedaController {

    private final ServicioEvento servicioEvento;
    private final ServicioMiembro servicioMiembro;
    private final ServicioPerfil servicioPerfil;

    public BusquedaController(ServicioEvento servicioEvento,
            ServicioMiembro servicioMiembro,
            ServicioPerfil servicioPerfil) {
        this.servicioEvento = servicioEvento;
        this.servicioMiembro = servicioMiembro;
        this.servicioPerfil = servicioPerfil;
    }

    @GetMapping("/listado")
    public String listado(Model model) {
        model.addAttribute("perfiles", servicioPerfil.obtenerTodos());
        model.addAttribute("eventos", servicioEvento.obtenerTodos());
        return "/busqueda/listado";
    }

    // Consulta 1: eventos por estado activo/inactivo
    @PostMapping("/porEstado")
    public String porEstado(@RequestParam boolean estado, Model model) {
        var resultados = servicioEvento.buscarPorEstado(estado);
        model.addAttribute("eventos", resultados);
        model.addAttribute("perfiles", servicioPerfil.obtenerTodos());
        model.addAttribute("estadoBuscado", estado ? "Activos" : "Inactivos");
        return "/busqueda/listado";
    }

    // Consulta 2: eventos por rango de fechas
    @PostMapping("/porFechas")
    public String porFechas(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin,
            Model model) {
        var resultados = servicioEvento.buscarPorRangoFechas(fechaInicio, fechaFin);
        model.addAttribute("eventos", resultados);
        model.addAttribute("perfiles", servicioPerfil.obtenerTodos());
        model.addAttribute("fechaInicio", fechaInicio);
        model.addAttribute("fechaFin", fechaFin);
        return "/busqueda/listado";
    }

    // Consulta 3: eventos por nombre parcial
    @PostMapping("/porNombre")
    public String porNombre(@RequestParam String texto, Model model) {
        var resultados = servicioEvento.buscarPorNombre(texto);
        model.addAttribute("eventos", resultados);
        model.addAttribute("perfiles", servicioPerfil.obtenerTodos());
        model.addAttribute("textoBuscado", texto);
        return "/busqueda/listado";
    }

    // Consulta 4: miembros por perfil
    @PostMapping("/miembrosPorPerfil")
    public String miembrosPorPerfil(@RequestParam Long idPerfil, Model model) {
        var resultados = servicioMiembro.obtenerTodos().stream()
                .filter(m -> m.getPerfil() != null && m.getPerfil().getIdPerfil().equals(idPerfil))
                .toList();
        model.addAttribute("miembrosEncontrados", resultados);
        model.addAttribute("perfiles", servicioPerfil.obtenerTodos());
        model.addAttribute("eventos", servicioEvento.obtenerTodos());
        return "/busqueda/listado";
    }
}
