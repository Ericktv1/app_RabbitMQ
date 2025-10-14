package co.vinni.messaging;

import co.vinni.model.Promotion;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class PromotionPublisherService {
    private final RabbitTemplate rabbitTemplate;
    public PromotionPublisherService(RabbitTemplate rabbitTemplate) { this.rabbitTemplate = rabbitTemplate; }
    public void publish(Promotion promo) {
        rabbitTemplate.convertAndSend("promo-fanout", "", promo);
        System.out.println("📢 Publicada promo: " + promo);
    }
}
