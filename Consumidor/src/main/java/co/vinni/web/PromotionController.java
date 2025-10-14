package co.vinni.web;

import co.vinni.messaging.PromotionPublisherService;
import co.vinni.messaging.PromotionStateService;
import co.vinni.model.Promotion;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/promotions")
public class PromotionController {

    private final PromotionPublisherService publisher;
    private final PromotionStateService state;

    public PromotionController(PromotionPublisherService publisher, PromotionStateService state) {
        this.publisher = publisher;
        this.state = state;
    }

    // --- ESTADO ---
    @GetMapping("/status")
    public Object status(@RequestParam("cuisine") String cuisine) {
        Promotion p = state.get(cuisine);
        return p != null ? p : Map.of("active", false, "cuisine", cuisine.toUpperCase());
    }

    // --- ACTIVAR: 20% DESCUENTO ---
    @PostMapping("/{cuisine}/activate/discount20")
    public Map<String,String> activateDiscount(@PathVariable("cuisine") String cuisine) {
        Promotion p = new Promotion(UUID.randomUUID().toString(),
                cuisine.toUpperCase(), "DESCUENTO", 0.20, null, true);
        publisher.publish(p);
        state.set(p);
        return Map.of("message", "Promoción 20% ACTIVADA en " + cuisine);
    }

    // --- ACTIVAR: 2x1 EN PRODUCTO ---
    @PostMapping("/{cuisine}/activate/twoforone")
    public Map<String,String> activateTwoForOne(@PathVariable("cuisine") String cuisine, @RequestParam("product") String product) {
        Promotion p = new Promotion(UUID.randomUUID().toString(),
                cuisine.toUpperCase(), "2X1", 0.0, product, true);
        publisher.publish(p);
        state.set(p);
        return Map.of("message", "Promoción 2x1 ACTIVADA en " + product + " (" + cuisine + ")");
    }

    // --- DESACTIVAR (apaga cualquier promo de esa cocina) ---
    @PostMapping("/{cuisine}/deactivate")
    public Map<String,String> deactivate(@PathVariable("cuisine") String cuisine) {
        Promotion off = new Promotion(UUID.randomUUID().toString(),
                cuisine.toUpperCase(), "DESCUENTO", 0.0, null, false);
        publisher.publish(off);  // fanout con active=false → el Productor la apaga
        state.clear(cuisine);
        return Map.of("message", "Promociones DESACTIVADAS en " + cuisine);
    }
}
