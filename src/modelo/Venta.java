package modelo;

import controlador.TDA.listas.Exception.EmptyException;
import java.time.LocalDate;

public class Venta {

    private Integer id;
    private LocalDate fecha;
    private Vendedor vendedor;
    private Auto auto;

    public Venta() {
    }

    public Venta(Integer id, LocalDate fecha, Vendedor vendedor, Auto auto) {
        this.id = id;
        this.fecha = fecha;
        this.vendedor = vendedor;
        this.auto = auto;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public Vendedor getVendedor() {
        return vendedor;
    }

    public void setVendedor(Vendedor vendedor) {
        this.vendedor = vendedor;
    }

    public Auto getAuto() {
        return auto;
    }

    public void setAuto(Auto auto) {
        this.auto = auto;
    }

    public int comparar(String criterio, String parametro) throws EmptyException {
        controlador.Venta.VendedorArchivos vendedor = new controlador.Venta.VendedorArchivos();
        controlador.Venta.AutoArchivos auto = new controlador.Venta.AutoArchivos();
        switch (criterio) {
            case "Num. Venta":
                return this.id.compareTo(Integer.valueOf(parametro));
            case "Vendedor (DNI)":
                return this.vendedor.getDni().compareTo(parametro);
            case "Auto (Placa)": {
                return this.auto.getPlaca().compareTo(parametro);
            }
            case "Auto (VIN)": {
                return this.auto.getVin().compareTo(parametro);
            }
            default:
                throw new EmptyException("No se ha seleccionado un criterio de ordenamiento");
        }
    }
}
