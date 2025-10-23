package co.vinni.messaging;

import co.vinni.model.Promotion;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class PromotionInboxRepaService {

    // Estado actual por cocina (ITALIANA / ASIATICA)
    private final Map<String, Promotion> active = new ConcurrentHashMap<>();

    @RabbitListener(queues = "promo-repa-queue")
    public void onPromo(Promotion promo) {
        if (promo == null || promo.getCuisine() == null) return;
        String key = promo.getCuisine().toUpperCase();
        System.out.println("🛵🎁 Promo (repartidor) recibida: " + promo);
        if (promo.isActive()) active.put(key, promo);
        else active.remove(key);
    }

    public Optional<Promotion> getActive(String cuisine) {
        String key = cuisine == null ? "" : cuisine.toUpperCase();
        return Optional.ofNullable(active.get(key));
    }
}
