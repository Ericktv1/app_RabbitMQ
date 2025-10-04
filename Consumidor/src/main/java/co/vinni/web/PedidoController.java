package co.vinni.web;

import co.vinni.messaging.ConsumerService;
import co.vinni.model.Pedido;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/orders")
public class PedidoController {

    private final ConsumerService consumerService;

    public PedidoController(ConsumerService consumerService) {
        this.consumerService = consumerService;
    }

    @GetMapping("/pending")
    public List<Pedido> getPedidosPendientes() {
        return consumerService.getPedidos();
    }

    @PostMapping("/{id}/accept")
    public Map<String, String> acceptOrder(@PathVariable("id") String orderId) {
        consumerService.aceptarPedido(orderId);
        return Map.of("message", "Pedido " + orderId + " aceptado");
    }

    @PostMapping("/{id}/deny")
    public Map<String, String> denyOrder(@PathVariable("id") String orderId) {
        consumerService.denegarPedido(orderId);
        return Map.of("message", "Pedido " + orderId + " denegado");
    }
}