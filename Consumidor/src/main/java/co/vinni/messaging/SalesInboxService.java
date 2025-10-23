package co.vinni.messaging;

import co.vinni.model.Venta;
import co.vinni.repository.SalesRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class SalesInboxService {

    private final SalesRepository repo;

    public SalesInboxService(SalesRepository repo) {
        this.repo = repo;
    }

    @RabbitListener(queues = "ventas-admin")
    public void onVenta(Venta venta) {
        System.out.println("💰 Venta recibida: " + venta.getOrderId() + " pagado=" + venta.isPagado());
        repo.add(venta);
    }
}
