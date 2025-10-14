package co.vinni.model;

import java.io.Serializable;

public class Promotion implements Serializable {
    private String id;
    private String cuisine; // ITALIANA o ASIATICA
    private String type;    // "DESCUENTO" o "2X1"
    private double percent; // para DESCUENTO
    private String product; // para 2X1
    private boolean active;

    public Promotion() {}

    public Promotion(String id, String cuisine, String type, double percent, String product, boolean active) {
        this.id = id;
        this.cuisine = cuisine;
        this.type = type;
        this.percent = percent;
        this.product = product;
        this.active = active;
    }

    // Getters y Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getCuisine() { return cuisine; }
    public void setCuisine(String cuisine) { this.cuisine = cuisine; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public double getPercent() { return percent; }
    public void setPercent(double percent) { this.percent = percent; }

    public String getProduct() { return product; }
    public void setProduct(String product) { this.product = product; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    @Override
    public String toString() {
        return "Promotion{" +
                "id='" + id + '\'' +
                ", cuisine='" + cuisine + '\'' +
                ", type='" + type + '\'' +
                ", percent=" + percent +
                ", product='" + product + '\'' +
                ", active=" + active +
                '}';
    }
}
