package co.vinni.messaging;

import co.vinni.model.Promotion;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class PromotionInboxService {

    // Promoción activa por cocina (ITALIANA / ASIATICA)
    private final Map<String, Promotion> active = new ConcurrentHashMap<>();

    @RabbitListener(queues = "promo-queue")
    public void onPromo(Promotion promo) {
        System.out.println("🎁 Promo recibida: " + promo);
        if (promo.isActive()) {
            active.put(promo.getCuisine(), promo);
        } else {
            active.remove(promo.getCuisine());
        }
    }

    public Optional<Promotion> getActive(String cuisine) {
        return Optional.ofNullable(active.get(cuisine));
    }
}
