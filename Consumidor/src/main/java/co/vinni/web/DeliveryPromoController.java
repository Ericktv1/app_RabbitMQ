package co.vinni.web;

import co.vinni.messaging.PromotionInboxRepaService;
import co.vinni.model.Promotion;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/delivery/promotions")
public class DeliveryPromoController {

    private final PromotionInboxRepaService inbox;

    public DeliveryPromoController(PromotionInboxRepaService inbox) {
        this.inbox = inbox;
    }

    // GET /delivery/promotions/status?cuisine=ITALIANA|ASIATICA
    @GetMapping("/status")
    public Object status(@RequestParam("cuisine") String cuisine) {
        return inbox.getActive(cuisine)
                .<Object>map(p -> p)
                .orElse(Map.of("active", false, "cuisine", cuisine.toUpperCase()));
    }
}
