package controlador;

import controlador.TDA.listas.DynamicList;
import modelo.Venta;

public class VentaControl {

    private Venta venta = new Venta();
    private DynamicList<Venta> ventas;

    public VentaControl(Venta venta) {
        this.venta = venta;
    }

    public VentaControl() {
        this.ventas = new DynamicList<>();

    }

    public Boolean guardar() {

        try {
            getVenta().setId(getVentas().getLength());
            getVentas().add(getVenta());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Venta getVenta() {
        if (venta == null) {
            venta = new Venta();
        }
        return venta;
    }

    public void setVenta(Venta venta) {
        this.venta = venta;
    }

    public DynamicList<Venta> getVentas() {
        return ventas;
    }

    public void setVentas(DynamicList<Venta> ventas) {
        this.ventas = ventas;
    }
}
