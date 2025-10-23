package co.vinni.messaging;

import co.vinni.model.Pedido;
import co.vinni.model.Venta;
import co.vinni.repository.PedidoRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class DeliveryService {

    private final PedidoRepository pedidos;
    private final RabbitTemplate rabbit;

    public DeliveryService(PedidoRepository pedidos, RabbitTemplate rabbit) {
        this.pedidos = pedidos;
        this.rabbit = rabbit;
    }

    /** Lista de pedidos aceptados, listos para entregar */
    public List<Pedido> getReadyToDeliver(String cuisine) {
        if (cuisine == null || cuisine.isBlank()) {
            return pedidos.getAceptados();
        }
        return pedidos.getAceptadosByCuisine(cuisine);
    }

    /** Marca el pedido como entregado (pagado o no) y lo elimina de la lista */
    public void registrarEntrega(String orderId, boolean pagado, String repartidor) {
        Pedido p = pedidos.findById(orderId);
        if (p == null) return;

        Venta v = new Venta();
        v.setId(UUID.randomUUID().toString());
        v.setOrderId(p.getId());
        v.setCustomerName(p.getCustomerName());
        v.setTotal(p.getTotal());
        v.setPagado(pagado);
        v.setRepartidor(repartidor == null ? "repartidor" : repartidor);
        v.setTs(Instant.now());

        // Publica venta al exchange directo
        rabbit.convertAndSend("ventas.direct", "ventas.registro", v);
        System.out.println("➡️ Venta publicada a admin: orderId=" + v.getOrderId() + " pagado=" + pagado);

        // Elimina el pedido de la lista de "pendientes"
        pedidos.remove(orderId);
        System.out.println("🗑️ Pedido removido de la lista de entregas: " + orderId);
    }
}
