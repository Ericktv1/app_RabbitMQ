package co.vinni.web;

import co.vinni.messaging.NotificacionConsumerService;
import co.vinni.model.Notificacion;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notifications")
public class NotificacionController {

    private final NotificacionConsumerService notificacionConsumerService;

    public NotificacionController(NotificacionConsumerService notificacionConsumerService) {
        this.notificacionConsumerService = notificacionConsumerService;
    }

    @GetMapping
    public List<Notificacion> getAllNotificaciones() {
        return notificacionConsumerService.getAllNotificaciones();
    }

    @GetMapping("/order/{orderId}")
    public Notificacion getNotificacionByOrderId(@PathVariable String orderId) {
        return notificacionConsumerService.getNotificacionByOrderId(orderId);
    }
}