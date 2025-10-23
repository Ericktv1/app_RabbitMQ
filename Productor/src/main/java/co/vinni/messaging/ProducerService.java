package co.vinni.messaging;

import co.vinni.config.RabbitConfig;
import co.vinni.model.Pedido;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Locale;

import static co.vinni.config.RabbitConfig.*;

@Service
public class ProducerService {

    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public ProducerService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
        this.rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
    }

    public void enviarPedido(Pedido pedido) {
        // pedido.getCuisine() debe ser ITALIANA o ASIATICA
        String cocina = (pedido.getCuisine() == null ? "" : pedido.getCuisine().toLowerCase(Locale.ROOT));
        String routingKey = "cocina." + cocina; // ej: cocina.italiana
        rabbitTemplate.convertAndSend(
                RabbitConfig.EXCHANGE_KITCHEN_TOPIC,
                routingKey,
                pedido
        );
    }
}