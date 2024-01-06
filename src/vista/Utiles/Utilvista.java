package vista.Utiles;

import controlador.AutoControl;
import controlador.TDA.listas.Exception.EmptyException;
import controlador.VendedorControl;
import controlador.Venta.AutoArchivos;
import controlador.Venta.VendedorArchivos;
import javax.swing.JComboBox;
import modelo.Auto;
import modelo.Vendedor;

import java.time.LocalDate;

public class Utilvista {

    public static void cargarComboAutos(JComboBox cbx) throws EmptyException {
        AutoControl ac = new AutoControl();
        AutoArchivos vv = new AutoArchivos();
        ac.setAutos(vv.all());
        cbx.removeAllItems();
        for (Integer i = 0; i < ac.getAutos().getLength(); i++) {
            if(ac.getAutos().getInfo(i).isDisponible()){
                cbx.addItem(ac.getAutos().getInfo(i));
            }
        }
    }

    public static Auto obtenerAutoControl(JComboBox cbx) {
        return (Auto) cbx.getSelectedItem();
    }

    public static void cargarComboVendedores(JComboBox cbx) throws EmptyException {
        VendedorControl vc = new VendedorControl();
        VendedorArchivos vv = new VendedorArchivos();
        vc.setVendedores(vv.all());
        cbx.removeAllItems();
        for (Integer i = 0; i < vc.getVendedores().getLength(); i++) {
            cbx.addItem(vc.getVendedores().getInfo(i));
        }
    }

    public static Vendedor obtenerVendedorControl(JComboBox cbx) {
        return (Vendedor) cbx.getSelectedItem();
    }

    public static LocalDate convertirFecha(String text) {
        String[] fecha = text.split("/");
        return LocalDate.of(Integer.parseInt(fecha[2]), Integer.parseInt(fecha[1]), Integer.parseInt(fecha[0]));
    }
}
