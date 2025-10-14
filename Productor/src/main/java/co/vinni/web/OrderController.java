package co.vinni.web;

import co.vinni.messaging.ProducerService;
import co.vinni.messaging.PromotionInboxService;
import co.vinni.model.Pedido;
import co.vinni.model.Promotion;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * Controlador del cliente (Productor).
 * - Exponde el catálogo por cocina, aplicando la promoción activa recibida por fanout.
 * - Crea pedidos indicando la cocina.
 * - Consulta estado de pedidos locales.
 */
@RestController
@RequestMapping("/orders")
public class OrderController {

    private final ProducerService producer;
    private final PromotionInboxService promos;

    // Pedidos creados en el cliente para consultar estado localmente
    private final Map<String, Pedido> pedidos = new HashMap<>();

    // Catálogos base por cocina (precios sin promo)
    private static final Map<String, Integer> CAT_ITALIANA = Map.of(
            "Pasta", 18000,
            "Pizza", 25000,
            "Tiramisu", 12000
    );

    private static final Map<String, Integer> CAT_ASIATICA = Map.of(
            "Ramen", 22000,
            "Sushi", 26000,
            "Bao", 15000
    );

    public OrderController(ProducerService producer, PromotionInboxService promos) {
        this.producer = producer;
        this.promos = promos;
    }

    // ---------- Catálogo ----------

    /**
     * Devuelve el catálogo de UNA cocina con las promociones aplicadas (si existen).
     * Front: GET /orders/catalog?cuisine=ITALIANA|ASIATICA
     */
    @GetMapping("/catalog")
    public Map<String, Integer> getCatalog(@RequestParam("cuisine") String cuisine) {
        String c = normalizeCuisine(cuisine);
        Map<String, Integer> base = new LinkedHashMap<>(getBaseCatalog(c));
        applyPromotion(base, c);
        return base;
    }

    // ---------- Crear pedido ----------

    /**
     * Crea un pedido. Body esperado:
     * { "customerName": "...", "items": ["Pizza","Pasta"], "cuisine": "ITALIANA" }
     * El total se calcula con el catálogo de esa cocina + promo aplicada en el momento.
     */
    @PostMapping
    public Map<String, String> createOrder(@RequestBody Map<String, Object> body) {
        String customerName = Objects.toString(body.get("customerName"), "").trim();
        @SuppressWarnings("unchecked")
        List<String> items = (List<String>) body.get("items");
        String cuisine = normalizeCuisine(Objects.toString(body.get("cuisine"), ""));

        if (customerName.isEmpty()) {
            return Map.of("error", "customerName requerido");
        }
        if (items == null || items.isEmpty()) {
            return Map.of("error", "items requerido");
        }
        if (!"ITALIANA".equals(cuisine) && !"ASIATICA".equals(cuisine)) {
            return Map.of("error", "cuisine inválido (ITALIANA | ASIATICA)");
        }

        // Catálogo de la cocina con promo aplicada
        Map<String, Integer> catalog = new LinkedHashMap<>(getBaseCatalog(cuisine));
        applyPromotion(catalog, cuisine);

        // Calcular total con catálogo ya promocionado
        double total = items.stream()
                .mapToInt(i -> catalog.getOrDefault(i, 0))
                .sum();

        Pedido pedido = new Pedido(customerName, items, total);
        pedido.setCuisine(cuisine); // asegúrate que Pedido tenga este campo
        pedidos.put(pedido.getId(), pedido);

        // Enviar a la cola de pedidos
        producer.enviarPedido(pedido);

        return Map.of("orderId", pedido.getId());
    }

    // ---------- Estado del pedido ----------

    @GetMapping("/{id}/status")
    public Map<String, String> getStatus(@PathVariable("id") String id) {
        Pedido p = pedidos.get(id);
        if (p == null) return Map.of("status", "NOT_FOUND");
        return Map.of("status", p.getStatus());
    }

    // ---------- (Opcional) Promo activa para una cocina ----------

    /**
     * Devuelve la promoción activa (si existe) para la cocina dada.
     * Útil si tu front quiere mostrar un "badge" de promo activa.
     */
    @GetMapping("/promotions/active")
    public Object getActivePromotion(@RequestParam("cuisine") String cuisine) {
        return promos.getActive(normalizeCuisine(cuisine)).orElse(null);
    }

    // ---------- Helpers ----------

    private static String normalizeCuisine(String cuisine) {
        return cuisine == null ? "" : cuisine.trim().toUpperCase(Locale.ROOT);
    }

    private static Map<String, Integer> getBaseCatalog(String cuisine) {
        return "ASIATICA".equals(cuisine) ? CAT_ASIATICA : CAT_ITALIANA;
    }

    /**
     * Aplica la promoción activa (si hay) sobre el mapa de precios.
     * - DESCUENTO: percent 0..1 → reduce todos los precios (redondeo al entero más cercano).
     * - 2X1: aplica 50% al producto indicado (precio efectivo por unidad cuando el cliente lleva 2).
     */
    private void applyPromotion(Map<String, Integer> prices, String cuisine) {
        Optional<Promotion> opt = promos.getActive(cuisine);
        if (opt.isEmpty()) return;

        Promotion p = opt.get();
        if (!p.isActive()) return;

        switch (p.getType()) {
            case "DESCUENTO" -> {
                double percent = clamp(p.getPercent(), 0.0, 1.0);
                prices.replaceAll((k, v) -> (int) Math.round(v * (1.0 - percent)));
            }
            case "2X1" -> {
                String prod = p.getProduct();
                if (prod != null && prices.containsKey(prod)) {
                    int newPrice = (int) Math.round(prices.get(prod) * 0.5);
                    prices.put(prod, Math.max(newPrice, 0));
                }
            }
            default -> {
                // tipos desconocidos: no aplicar
            }
        }
    }

    private static double clamp(double v, double min, double max) {
        return Math.max(min, Math.min(max, v));
    }
}
