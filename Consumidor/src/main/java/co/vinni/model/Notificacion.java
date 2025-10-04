package co.vinni.model;

import java.io.Serializable;

public class Notificacion implements Serializable {
    private String orderId;
    private String customerName;
    private String mensaje;
    private String tipo; // "ACEPTADO" o "DENEGADO"

    public Notificacion() {}

    public Notificacion(String orderId, String customerName, String mensaje, String tipo) {
        this.orderId = orderId;
        this.customerName = customerName;
        this.mensaje = mensaje;
        this.tipo = tipo;
    }

    // Getters y Setters
    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    @Override
    public String toString() {
        return "Notificacion{" +
                "orderId='" + orderId + '\'' +
                ", customerName='" + customerName + '\'' +
                ", mensaje='" + mensaje + '\'' +
                ", tipo='" + tipo + '\'' +
                '}';
    }
}