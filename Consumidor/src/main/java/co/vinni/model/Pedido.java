package co.vinni.model;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

public class Pedido implements Serializable {
    private static final long serialVersionUID = 1L;

    // Estados sugeridos (opcional)
    public static final String ESTADO_PENDIENTE = "PENDIENTE";
    public static final String ESTADO_ACEPTADO  = "ACEPTADO";
    public static final String ESTADO_DENEGADO  = "DENEGADO";

    private String id;
    private String customerName;
    private List<String> items;
    private double total;
    private String status;

    /** NUEVO: cocina a la que pertenece el pedido: "ITALIANA" o "ASIATICA" */
    private String cuisine;

    public Pedido() {
        this.id = UUID.randomUUID().toString();
        this.status = ESTADO_PENDIENTE;
        // Por defecto dejamos la cocina como ITALIANA para compatibilidad
        this.cuisine = "ITALIANA";
    }

    /** Constructor antiguo (compatibilidad). Usa cocina por defecto "ITALIANA". */
    public Pedido(String customerName, List<String> items, double total) {
        this();
        this.customerName = customerName;
        this.items = items;
        this.total = total;
    }

    /** Constructor recomendado con cocina explícita. */
    public Pedido(String customerName, List<String> items, double total, String cuisine) {
        this();
        this.customerName = customerName;
        this.items = items;
        this.total = total;
        this.cuisine = cuisine;
    }

    // Getters y Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public List<String> getItems() { return items; }
    public void setItems(List<String> items) { this.items = items; }

    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getCuisine() { return cuisine; }
    public void setCuisine(String cuisine) { this.cuisine = cuisine; }

    @Override
    public String toString() {
        return "Pedido{" +
                "id='" + id + '\'' +
                ", customerName='" + customerName + '\'' +
                ", items=" + items +
                ", total=" + total +
                ", status='" + status + '\'' +
                ", cuisine='" + cuisine + '\'' +
                '}';
    }
}
