package co.vinni.messaging;

import co.vinni.model.Pedido;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class ProducerService {

    private final RabbitTemplate rabbitTemplate;

    public ProducerService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void enviarPedido(Pedido pedido) {
        System.out.println("📤 Enviando pedido: " + pedido);
        rabbitTemplate.convertAndSend("pedido-queue", pedido);
    }
}
