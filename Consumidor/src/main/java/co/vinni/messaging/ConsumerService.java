package co.vinni.messaging;

import co.vinni.model.Factura;
import co.vinni.model.Notificacion;
import co.vinni.model.Pedido;
import co.vinni.repository.PedidoRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ConsumerService {

    private final PedidoRepository pedidoRepository;
    private final RabbitTemplate rabbitTemplate;

    public ConsumerService(PedidoRepository pedidoRepository, RabbitTemplate rabbitTemplate) {
        this.pedidoRepository = pedidoRepository;
        this.rabbitTemplate = rabbitTemplate;
    }

    @RabbitListener(queues = "pedido-queue")
    public void recibirPedido(Pedido pedido) {
        System.out.println("📥 Pedido recibido: " + pedido);
        pedidoRepository.addPedido(pedido);
    }

    public List<Pedido> getPedidos() {
        return pedidoRepository.getPendientes();
    }

    public List<Map<String, Object>> getFacturas() {
        return pedidoRepository.getFacturas();
    }

    public void aceptarPedido(String id) {
        Pedido pedido = pedidoRepository.findById(id);
        if (pedido != null) {
            pedido.setStatus("ACEPTADO");
            pedidoRepository.update(pedido);

            // Crear factura
            double subtotal = pedido.getTotal();
            double tax = subtotal * 0.19;
            double total = subtotal + tax;

            Factura factura = new Factura(
                    UUID.randomUUID().toString(),
                    pedido.getId(),
                    pedido.getCustomerName(),
                    subtotal,
                    tax,
                    total
            );

            // Guardar en repositorio local
            Map<String, Object> facturaMap = new HashMap<>();
            facturaMap.put("id", factura.getId());
            facturaMap.put("orderId", factura.getOrderId());
            facturaMap.put("customerName", factura.getCustomerName());
            facturaMap.put("subtotal", factura.getSubtotal());
            facturaMap.put("tax", factura.getTax());
            facturaMap.put("total", factura.getTotal());
            pedidoRepository.addFactura(facturaMap);

            // Enviar factura al productor
            System.out.println("📤 Enviando factura al cliente: " + factura);
            rabbitTemplate.convertAndSend("factura-queue", factura);
        }
    }

    public void denegarPedido(String id) {
        Pedido pedido = pedidoRepository.findById(id);
        if (pedido != null) {
            pedido.setStatus("DENEGADO");
            pedidoRepository.update(pedido);

            // Enviar notificación de denegación al cliente
            Notificacion notificacion = new Notificacion(
                    pedido.getId(),
                    pedido.getCustomerName(),
                    "Lo sentimos, tu pedido ha sido rechazado por el restaurante.",
                    "DENEGADO"
            );

            System.out.println("📤 Enviando notificación de rechazo al cliente: " + notificacion);
            rabbitTemplate.convertAndSend("notificacion-queue", notificacion);
        }
    }
}