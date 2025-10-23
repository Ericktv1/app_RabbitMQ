package co.vinni.web;

import co.vinni.messaging.ConsumerService;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@Profile("consumidor")
@RestController
@RequestMapping("/invoices")
public class InvoiceController {

    private final ConsumerService consumerService;

    public InvoiceController(ConsumerService consumerService) {
        this.consumerService = consumerService;
    }

    @GetMapping
    public List<Map<String, Object>> getFacturas(
            @RequestParam(name = "cuisine", required = false) String cuisine) {
        if (cuisine == null || cuisine.isBlank()) {
            return consumerService.getFacturas();
        }
        return consumerService.getFacturasByCuisine(cuisine);
    }
}
