package co.vinni.messaging;

import co.vinni.model.Factura;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

@Service
public class FacturaConsumerService {

    private final Map<String, Factura> facturas = new ConcurrentHashMap<>();

    @RabbitListener(queues = "factura-queue")
    public void recibirFactura(Factura factura) {
        System.out.println("📥 Factura recibida: " + factura);
        facturas.put(factura.getId(), factura);
    }

    public List<Factura> getAllFacturas() {
        return new ArrayList<>(facturas.values());
    }

    public Factura getFacturaByOrderId(String orderId) {
        return facturas.values().stream()
                .filter(f -> f.getOrderId().equals(orderId))
                .findFirst()
                .orElse(null);
    }
}