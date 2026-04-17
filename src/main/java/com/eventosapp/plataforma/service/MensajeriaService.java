package com.eventosapp.plataforma.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class MensajeriaService {

    private final JavaMailSender remitente;

    public MensajeriaService(JavaMailSender remitente) {
        this.remitente = remitente;
    }

    public void enviarBienvenida(String destinatario, String nombreCompleto) throws MessagingException {
        String asunto = "Bienvenido a la Plataforma de Eventos";
        String cuerpo = "<h2>Hola, " + nombreCompleto + "!</h2>"
                + "<p>Tu cuenta ha sido creada exitosamente en la Plataforma de Reservas de Eventos.</p>"
                + "<p>Ya puedes iniciar sesión con tu correo electrónico.</p>"
                + "<br/><p>Saludos,<br/>Equipo de Eventos</p>";
        enviarCorreoHtml(destinatario, asunto, cuerpo);
    }

    public void enviarCorreoHtml(String destinatario, String asunto, String cuerpo) throws MessagingException {
        MimeMessage mensaje = remitente.createMimeMessage();
        MimeMessageHelper ayudante = new MimeMessageHelper(mensaje, true);
        ayudante.setTo(destinatario);
        ayudante.setSubject(asunto);
        ayudante.setText(cuerpo, true);
        remitente.send(mensaje);
    }
}
