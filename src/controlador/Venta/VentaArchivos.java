package controlador.Venta;

import controlador.DAO.DaoImplement;
import controlador.TDA.listas.DynamicList;
import controlador.TDA.listas.Exception.EmptyException;
import modelo.Venta;

public class VentaArchivos extends DaoImplement<Venta> {

    private DynamicList<Venta> ventas;
    private Venta venta;
    Integer cont = 0;

    public VentaArchivos() {
        super(Venta.class);
    }

    public DynamicList<Venta> getVentas() {
        ventas = all();
        return ventas;
    }
    
    public DynamicList<Venta> getVentasAux() {
        return ventas;
    }

    public void setVentas(DynamicList<Venta> ventas) {
        this.ventas = ventas;
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

    public Boolean persist() {
        venta.setId(all().getLength());
        return persist(venta);
    }

    //Busqueda Lineal
    public Venta busquedaLineal(String criterio, String parametro) throws EmptyException {
        DynamicList<Venta> lista = all();
        for (int i = 0; i < lista.getLength(); i++) {
            cont++;
            Venta ventaAux = lista.getInfo(i);
            if (ventaAux.comparar(criterio, parametro) == 0) {
                return ventaAux;
            }
        }
        return null;
    }
    
    //Busqueda Binaria
    public Venta busquedaBinaria(String criterio, String parametro) throws EmptyException {
        int inicio = 0;
        DynamicList<Venta> lista = all();
        int fin = lista.getLength() - 1;

        while (inicio <= fin) {
            cont++;
            int medio = (inicio + fin) / 2;
            Venta ventaAux = lista.getInfo(medio);
            int comparacion = ventaAux.comparar(criterio, parametro);
            if (comparacion == 0) {
                return ventaAux;
            } else if (comparacion < 0) {
                inicio = medio + 1;
            } else {
                fin = medio - 1;
            }
        }
        return null;
    }

    
    public Venta buscar(Integer tipo, String criterio, String paramtro) throws EmptyException {
        switch (tipo) {
            case 0:
                return busquedaLineal(criterio, paramtro);
            case 1:
                return busquedaBinaria(criterio, paramtro);
            default:
                throw new AssertionError();
        }
    }
}
