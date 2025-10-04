package co.vinni.web;

import co.vinni.messaging.ProducerService;
import co.vinni.model.Pedido;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/orders")
public class OrderController {
    private final ProducerService producer;
    private final Map<String, Pedido> pedidos = new HashMap<>();
    private final Map<String, Integer> catalog = Map.of(
            "Pizza", 25000,
            "Pasta", 18000,
            "Ensalada", 12000,
            "Jugo", 5000
    );

    public OrderController(ProducerService producer) {
        this.producer = producer;
    }

    @GetMapping("/catalog")
    public Map<String, Integer> getCatalog() {
        return catalog;
    }

    @PostMapping
    public Map<String, String> createOrder(@RequestBody Map<String, Object> body) {
        String customerName = (String) body.get("customerName");
        List<String> items = (List<String>) body.get("items");
        double total = items.stream().mapToInt(i -> catalog.getOrDefault(i, 0)).sum();

        Pedido pedido = new Pedido(customerName, items, total);
        pedidos.put(pedido.getId(), pedido);
        producer.enviarPedido(pedido);

        return Map.of("orderId", pedido.getId());
    }

    @GetMapping("/{id}/status")
    public Map<String, String> getStatus(@PathVariable String id) {
        Pedido p = pedidos.get(id);
        if (p == null) return Map.of("status", "NOT_FOUND");
        return Map.of("status", p.getStatus());
    }
}