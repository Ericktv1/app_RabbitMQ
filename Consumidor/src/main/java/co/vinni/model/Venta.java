package co.vinni.model;

import java.io.Serializable;
import java.time.Instant;

public class Venta implements Serializable {
    private String id;
    private String orderId;
    private String customerName;
    private double total;
    private boolean pagado;   // true=pagado, false=no pagado
    private String repartidor; // opcional: quién cobró
    private Instant ts;

    public Venta() {}

    public Venta(String id, String orderId, String customerName, double total, boolean pagado, String repartidor, Instant ts) {
        this.id = id;
        this.orderId = orderId;
        this.customerName = customerName;
        this.total = total;
        this.pagado = pagado;
        this.repartidor = repartidor;
        this.ts = ts;
    }

    // getters/setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }
    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }
    public boolean isPagado() { return pagado; }
    public void setPagado(boolean pagado) { this.pagado = pagado; }
    public String getRepartidor() { return repartidor; }
    public void setRepartidor(String repartidor) { this.repartidor = repartidor; }
    public Instant getTs() { return ts; }
    public void setTs(Instant ts) { this.ts = ts; }
}
