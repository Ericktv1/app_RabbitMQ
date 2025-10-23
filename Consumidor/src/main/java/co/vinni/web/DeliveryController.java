package co.vinni.web;

import co.vinni.messaging.DeliveryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/delivery")
public class DeliveryController {

    private final DeliveryService delivery;

    public DeliveryController(DeliveryService delivery) {
        this.delivery = delivery;
    }

    /** Pedidos listos para entregar (aceptados). */
    @GetMapping("/ready")
    public ResponseEntity<?> getReadyOrders(
            @RequestParam(name = "cuisine", required = false) String cuisine
    ) {
        return ResponseEntity.ok(delivery.getReadyToDeliver(cuisine));
    }

    /** Repartidor marca si fue pagado o no pagado → publica venta al admin. */
    @PostMapping("/{orderId}/pay")
    public ResponseEntity<Map<String, Object>> pay(
            @PathVariable("orderId") String orderId,
            @RequestParam(name = "paid") boolean paid,
            @RequestParam(name = "who", required = false) String who
    ) {
        delivery.registrarEntrega(orderId, paid, who);
        return ResponseEntity.ok(Map.of(
                "ok", true,
                "orderId", orderId,
                "paid", paid,
                "who", who == null ? "repartidor" : who
        ));
    }
}
