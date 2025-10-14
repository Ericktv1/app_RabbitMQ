package co.vinni.messaging;

import co.vinni.model.Pedido;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import static co.vinni.config.RabbitConfig.*;

@Service
public class ProducerService {
    private final RabbitTemplate rabbitTemplate;
    public ProducerService(RabbitTemplate rabbitTemplate) { this.rabbitTemplate = rabbitTemplate; }

    public void enviarPedido(Pedido pedido) {
        System.out.println("📤 Enviando pedido: " + pedido);
        // routing key según cocina
        String rk = "ASIATICA".equalsIgnoreCase(pedido.getCuisine()) ? RK_ASIATICA : RK_ITALIANA;
        rabbitTemplate.convertAndSend(EXCHANGE_KITCHEN, rk, pedido);
    }
}
