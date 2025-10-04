package co.vinni.web;

import co.vinni.messaging.ConsumerService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/invoices")
public class InvoiceController {

    private final ConsumerService consumerService;

    public InvoiceController(ConsumerService consumerService) {
        this.consumerService = consumerService;
    }

    @GetMapping
    public List<Map<String, Object>> getFacturas() {
        return consumerService.getFacturas();
    }
}