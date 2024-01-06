package modelo;

public class Auto {
    private Integer id;
    private String vin;
    private String placa;
    private String color;
    private Integer precio;
    private String marca;
    private boolean disponible;

    public Auto() {
    }

    public Auto(Integer id, String vin, String placa, String color, Integer precio, String marca, boolean disponible) {
        this.id = id;
        this.vin = vin;
        this.placa = placa;
        this.color = color;
        this.precio = precio;
        this.marca = marca;
        this.disponible = disponible;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    
    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }
    
    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Integer getPrecio() {
        return precio;
    }

    public void setPrecio(Integer precio) {
        this.precio = precio;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public boolean isDisponible() {
        return disponible;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }

    @Override
    public String toString() {
        return placa + " | " + marca;
    }
    
}

