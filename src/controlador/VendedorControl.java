package controlador;

import controlador.TDA.listas.DynamicList;
import modelo.Vendedor;

public class VendedorControl {

    private Vendedor vendedor = new Vendedor();
    private DynamicList<Vendedor> vendedores;

    public VendedorControl(Vendedor vendedor) {
        this.vendedor = vendedor;
    }

    public VendedorControl() {
        this.vendedores = new DynamicList<>();
    }

    public Boolean guardar() {
        try {
            getVendedor().setId(getVendedores().getLength());
            getVendedores().add(getVendedor());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Vendedor getVendedor() {
        if (vendedor == null) {
            vendedor = new Vendedor();
        }
        return vendedor;
    }

    public void setVendedor(Vendedor vendedor) {
        this.vendedor = vendedor;
    }

    public DynamicList<Vendedor> getVendedores() {
        return vendedores;
    }

    public void setVendedores(DynamicList<Vendedor> vendedores) {
        this.vendedores = vendedores;
    }

}
