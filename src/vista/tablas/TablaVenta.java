package vista.tablas;

import controlador.TDA.listas.DynamicList;
import controlador.TDA.listas.Exception.EmptyException;
import controlador.Venta.AutoArchivos;
import controlador.Venta.VendedorArchivos;
import javax.swing.event.EventListenerList;
import javax.swing.table.AbstractTableModel;
import modelo.Venta;

public class TablaVenta extends AbstractTableModel {

    private DynamicList<Venta> ventas;

    @Override
    public int getRowCount() {
        return ventas.getLength();
    }

    @Override
    public int getColumnCount() {
        return 4;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        try {
            Venta vt = ventas.getInfo(rowIndex);
            switch (columnIndex) {
                case 0:
                    return (vt != null) ? vt.getId() : " ";
                case 1:
                    return (vt != null) ? vt.getFecha() : " ";
                case 2:
                    return (vt != null) ? vt.getVendedor().toString(): "";
                case 3:
                    return (vt != null) ? vt.getAuto().toString(): "";
                default:
                    return null;
            }
        } catch (EmptyException ex) {
            return null;
        }
    }

    @Override
    public String getColumnName(int column) {
        switch (column) {
            case 0:
                return "NUM. VENTA";
            case 1:
                return "FECHA";
            case 2:
                return "VENDEDOR";
            case 3:
                return "AUTO";
            default:
                return null;
        }
    }

    public DynamicList<Venta> getVentas() {
        return ventas;
    }

    public void setVentas(DynamicList<Venta> ventas) {
        this.ventas = ventas;
    }

}
