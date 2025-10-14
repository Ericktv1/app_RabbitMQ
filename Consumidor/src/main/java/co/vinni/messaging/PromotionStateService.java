package co.vinni.messaging;

import co.vinni.model.Promotion;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class PromotionStateService {
    private final Map<String, Promotion> current = new ConcurrentHashMap<>();

    public Promotion get(String cuisine) { return current.get(cuisine.toUpperCase()); }

    public void set(Promotion p) {
        if (p == null) return;
        if (p.isActive()) current.put(p.getCuisine().toUpperCase(), p);
        else current.remove(p.getCuisine().toUpperCase());
    }

    public void clear(String cuisine) { current.remove(cuisine.toUpperCase()); }
}
