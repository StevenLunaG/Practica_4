package controlador.Venta;

import controlador.DAO.DaoImplement;
import controlador.TDA.listas.DynamicList;
import modelo.Vendedor;

public class VendedorArchivos extends DaoImplement<Vendedor> {

    private DynamicList<Vendedor> vendedores;
    private Vendedor vendedor;

    public VendedorArchivos() {
        super(Vendedor.class);
    }

    public DynamicList<Vendedor> getVendedores() {
        vendedores = all();
        return vendedores;
    }

    public void setVendedores(DynamicList<Vendedor> vendedores) {
        this.vendedores = vendedores;
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

    public Boolean persist() {
        vendedor.setId(all().getLength());
        return persist(vendedor);
    }
}
