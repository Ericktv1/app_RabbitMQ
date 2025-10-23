package co.vinni.messaging;

import co.vinni.config.RabbitConfig;
import co.vinni.model.Factura;
import co.vinni.model.Notificacion;
import co.vinni.model.Pedido;
import co.vinni.repository.PedidoRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.Locale;
import java.util.UUID;
import java.util.Map;
import java.util.HashMap;
import java.util.List;

/**
 * Servicio del consumidor (Restaurante).
 * - Recibe pedidos por cocina (topic) y los deja en estado PENDIENTE.
 * - Desde el panel, permite ACEPTAR/DENEGAR.
 * - Publica Notificaciones y Facturas por fanout.
 */
@Service
public class ConsumerService {

    private final PedidoRepository pedidoRepository;
    private final RabbitTemplate rabbitTemplate;

    public ConsumerService(PedidoRepository pedidoRepository, RabbitTemplate rabbitTemplate) {
        this.pedidoRepository = pedidoRepository;
        this.rabbitTemplate = rabbitTemplate;
    }

    // ===================== LISTENERS (dejan PENDIENTE) =====================

    @RabbitListener(queues = RabbitConfig.Q_PEDIDOS_ITALIANA)
    public void procesarPedidoItaliana(Pedido pedido) {
        recibirPedido(pedido, "ITALIANA");
    }

    @RabbitListener(queues = RabbitConfig.Q_PEDIDOS_ASIATICA)
    public void procesarPedidoAsiatica(Pedido pedido) {
        recibirPedido(pedido, "ASIATICA");
    }

    /** Registra el pedido en estado PENDIENTE para ser gestionado desde el panel. */
    private void recibirPedido(Pedido pedido, String cocina) {
        if (pedido == null) return;

        // Normaliza campos clave
        pedido.setStatus(Pedido.ESTADO_PENDIENTE);
        pedido.setCuisine(cocina);

        // Guarda/actualiza en el repositorio en memoria
        pedidoRepository.addPedido(pedido);
        pedidoRepository.update(pedido);

        System.out.printf("📥 Pedido recibido (%s): %s · Cliente: %s · Total: %.0f · ID: %s%n",
                cocina,
                String.join(", ", Optional.ofNullable(pedido.getItems()).orElse(List.of())),
                pedido.getCustomerName(),
                pedido.getTotal(),
                pedido.getId());
    }

    // ===================== PUBLICACIÓN POR FANOUT =====================

    private void enviarNotificacionFanout(Pedido pedido, String estado) {
        Notificacion notif = new Notificacion();
        notif.setOrderId(pedido.getId());
        notif.setCustomerName(pedido.getCustomerName());
        notif.setTipo(estado.toUpperCase(Locale.ROOT));
        if ("ACEPTADO".equalsIgnoreCase(estado)) {
            notif.setMensaje("✅ Tu pedido fue aceptado. Pronto recibirás tu factura.");
        } else {
            notif.setMensaje("❌ Tu pedido fue denegado. Intenta nuevamente más tarde.");
        }

        rabbitTemplate.convertAndSend("notificaciones.fanout", "", notif);
        System.out.println("📣 Notificación enviada (fanout): " + notif.getOrderId() + " → " + notif.getTipo());
    }

    private void enviarFacturaFanout(Pedido pedido) {
        double subtotal = pedido.getTotal();
        double tax = Math.round(subtotal * 0.19);
        double total = subtotal + tax;

        Factura factura = new Factura();
        factura.setId(UUID.randomUUID().toString());         // <-- IMPORTANTE
        factura.setOrderId(pedido.getId());
        factura.setCustomerName(pedido.getCustomerName());
        factura.setSubtotal(subtotal);
        factura.setTax(tax);
        factura.setTotal(total);
        factura.setCuisine(pedido.getCuisine());

        // Guarda también en el repositorio local para la tabla de "Facturas" del panel
        Map<String, Object> facturaMap = new HashMap<>();
        facturaMap.put("id", factura.getId());
        facturaMap.put("orderId", factura.getOrderId());
        facturaMap.put("customerName", factura.getCustomerName());
        facturaMap.put("subtotal", factura.getSubtotal());
        facturaMap.put("tax", factura.getTax());
        facturaMap.put("total", factura.getTotal());
        facturaMap.put("cuisine", factura.getCuisine());
        pedidoRepository.addFactura(facturaMap);

        // Publica por fanout para que el cliente la reciba
        rabbitTemplate.convertAndSend("facturas.fanout", "", factura);
        System.out.println("🧾 Factura enviada (fanout): " + factura.getOrderId());
    }

    // ===================== CONSULTAS PARA LA UI DEL RESTAURANTE =====================

    public List<Pedido> getPedidos() {
        return pedidoRepository.getPendientes();
    }

    public List<Pedido> getPedidos(String cuisine) {
        if (cuisine == null || cuisine.isBlank()) {
            return pedidoRepository.getPendientes();
        }
        return pedidoRepository.getPendientesByCuisine(cuisine);
    }

    public List<Map<String, Object>> getFacturasByCuisine(String cuisine) {
        return pedidoRepository.getFacturasByCuisine(cuisine);
    }

    public List<Map<String, Object>> getFacturas() {
        return pedidoRepository.getFacturas();
    }

    // ===================== ACEPTAR / DENEGAR DESDE EL PANEL =====================

    /** Acepta el pedido, publica notificación y factura (fanout). */
    public void aceptarPedido(String id) {
        Pedido pedido = pedidoRepository.findById(id);
        if (pedido == null) return;

        pedido.setStatus(Pedido.ESTADO_ACEPTADO);
        pedidoRepository.update(pedido);

        // Genera y guarda factura local + publica por fanout
        enviarFacturaFanout(pedido);

        // Notificación de aceptado
        enviarNotificacionFanout(pedido, "ACEPTADO");

        System.out.println("✅ Pedido ACEPTADO (manual): " + id);
    }

    /** Deniega el pedido y publica notificación (fanout). */
    public void denegarPedido(String id) {
        Pedido pedido = pedidoRepository.findById(id);
        if (pedido == null) return;

        pedido.setStatus(Pedido.ESTADO_DENEGADO);
        pedidoRepository.update(pedido);

        // Notificación de denegado
        enviarNotificacionFanout(pedido, "DENEGADO");

        System.out.println("❌ Pedido DENEGADO (manual): " + id);
    }
}
