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

    // /notifications/order/{orderId}
    @GetMapping("/order/{orderId}")
    public Notificacion getNotificacionByOrderId(@PathVariable("orderId") String orderId) {
        return notificacionConsumerService.getNotificacionByOrderId(orderId);
    }
}
