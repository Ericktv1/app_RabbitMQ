package co.vinni.messaging;

import co.vinni.model.Promotion;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class PromotionInboxRepaService {

    // Estado actual por cocina (ITALIANA / ASIATICA), clave SIEMPRE mayúscula
    private final Map<String, Promotion> active = new ConcurrentHashMap<>();

    @RabbitListener(queues = "promo-repa-queue")
    public void onPromo(Promotion promo) {
        if (promo == null || promo.getCuisine() == null) return;

        // 🔧 Normaliza por si llegan espacios/minúsculas
        String key = promo.getCuisine().trim().toUpperCase(Locale.ROOT);

        System.out.println("🛵🎁 Promo (repartidor) recibida: " + promo);
        if (promo.isActive()) {
            active.put(key, promo);
        } else {
            active.remove(key);
        }
    }

    // Sigue disponible si quieres consultar por cocina concreta
    public Optional<Promotion> getActive(String cuisine) {
        String key = cuisine == null ? "" : cuisine.trim().toUpperCase(Locale.ROOT);
        return Optional.ofNullable(active.get(key));
    }

    // ✅ NUEVO: devuelve TODAS las promociones activas (ITALIANA y/o ASIATICA)
    public Collection<Promotion> getAllActive() {
        return active.values();
    }
}
