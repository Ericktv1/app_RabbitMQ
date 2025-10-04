package co.vinni.model;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

public class Pedido implements Serializable {
    private String id;
    private String customerName;
    private List<String> items;
    private double total;
    private String status;

    public Pedido() {
        this.id = UUID.randomUUID().toString();
        this.status = "PENDIENTE";
    }

    public Pedido(String customerName, List<String> items, double total) {
        this();
        this.customerName = customerName;
        this.items = items;
        this.total = total;
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

    @Override
    public String toString() {
        return "Pedido{" +
                "id='" + id + '\'' +
                ", customerName='" + customerName + '\'' +
                ", items=" + items +
                ", total=" + total +
                ", status='" + status + '\'' +
                '}';
    }
}