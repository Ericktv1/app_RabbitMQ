package co.vinni.messaging;

import co.vinni.model.Promotion;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class PromotionInboxService {

    // clave SIEMPRE en mayúsculas: ITALIANA / ASIATICA
    private final Map<String, Promotion> active = new ConcurrentHashMap<>();

    @RabbitListener(queues = "promo-queue")
    public void onPromo(Promotion promo) {
        if (promo == null) return;
        final String key = (promo.getCuisine() == null ? "" : promo.getCuisine().trim().toUpperCase(Locale.ROOT));

        if (promo.isActive()) {
            active.put(key, promo);
            System.out.println("🎁 [Producer] Promo ACTIVADA recibida: " + promo);
        } else {
            active.remove(key);
            System.out.println("🎁 [Producer] Promo DESACTIVADA recibida: " + promo);
        }
    }

    public Optional<Promotion> getActive(String cuisine) {
        final String key = cuisine == null ? "" : cuisine.trim().toUpperCase(Locale.ROOT);
        return Optional.ofNullable(active.get(key));
    }
}
