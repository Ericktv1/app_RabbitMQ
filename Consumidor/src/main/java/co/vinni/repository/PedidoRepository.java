package co.vinni.repository;

import co.vinni.model.Pedido;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Repository
public class PedidoRepository {

    private final Map<String, Pedido> pedidos = new ConcurrentHashMap<>();
    private final List<Map<String, Object>> facturas = new ArrayList<>();

    public void addPedido(Pedido pedido) {
        pedidos.put(pedido.getId(), pedido);
    }


    public List<Pedido> getPendientes() {
        return pedidos.values().stream()
                .filter(p -> "PENDIENTE".equals(p.getStatus()))
                .collect(Collectors.toList());
    }
    // ...
    public List<Pedido> getPendientesByCuisine(String cuisine) {
        return pedidos.values().stream()
                .filter(p -> "PENDIENTE".equals(p.getStatus()))
                .filter(p -> p.getCuisine() != null && p.getCuisine().equalsIgnoreCase(cuisine))
                .collect(Collectors.toList());
    }

    public Pedido findById(String id) {
        return pedidos.get(id);
    }

    public void update(Pedido pedido) {
        if (pedido != null && pedido.getId() != null) {
            pedidos.put(pedido.getId(), pedido);
        }
    }

    public void addFactura(Map<String, Object> factura) {
        facturas.add(factura);
    }

    public List<Map<String, Object>> getFacturas() {
        return new ArrayList<>(facturas);
    }
    public List<Map<String, Object>> getFacturasByCuisine(String cuisine) {
        if (cuisine == null || cuisine.isBlank()) {
            return getFacturas();
        }
        return facturas.stream()
                .filter(f -> {
                    Object c = f.get("cuisine");
                    return c != null && cuisine.equalsIgnoreCase(c.toString());
                })
                .collect(Collectors.toList());
    }
    // añade dentro de PedidoRepository
    public List<Pedido> getAceptados() {
        return pedidos.values().stream()
                .filter(p -> Pedido.ESTADO_ACEPTADO.equals(p.getStatus()))
                .collect(Collectors.toList());
    }

    public List<Pedido> getAceptadosByCuisine(String cuisine) {
        return pedidos.values().stream()
                .filter(p -> Pedido.ESTADO_ACEPTADO.equals(p.getStatus()))
                .filter(p -> p.getCuisine() != null && p.getCuisine().equalsIgnoreCase(cuisine))
                .collect(Collectors.toList());
    }
    public void remove(String id) {
        pedidos.remove(id);
    }

}