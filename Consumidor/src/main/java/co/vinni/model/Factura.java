package co.vinni.model;

import java.io.Serializable;

public class Factura implements Serializable {
    private String id;
    private String orderId;
    private String customerName;
    private double subtotal;
    private double tax;
    private double total;

    public Factura() {}

    public Factura(String id, String orderId, String customerName, double subtotal, double tax, double total) {
        this.id = id;
        this.orderId = orderId;
        this.customerName = customerName;
        this.subtotal = subtotal;
        this.tax = tax;
        this.total = total;
    }

    // Getters y Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public double getSubtotal() { return subtotal; }
    public void setSubtotal(double subtotal) { this.subtotal = subtotal; }

    public double getTax() { return tax; }
    public void setTax(double tax) { this.tax = tax; }

    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }

    @Override
    public String toString() {
        return "Factura{" +
                "id='" + id + '\'' +
                ", orderId='" + orderId + '\'' +
                ", customerName='" + customerName + '\'' +
                ", subtotal=" + subtotal +
                ", tax=" + tax +
                ", total=" + total +
                '}';
    }
}