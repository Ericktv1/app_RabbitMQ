package co.vinni.web;

import co.vinni.messaging.PromotionInboxRepaService;
import co.vinni.model.Promotion;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/delivery")
public class DeliveryPromoController {

    private final PromotionInboxRepaService inbox;

    public DeliveryPromoController(PromotionInboxRepaService inbox) {
        this.inbox = inbox;
    }

    // ✅ el repartidor obtiene TODAS las promos activas
    @GetMapping("/promotions/active")
    public Collection<Promotion> listActive() {
        return inbox.getAllActive();
    }
}
