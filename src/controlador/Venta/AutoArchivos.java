package controlador.Venta;

import controlador.DAO.DaoImplement;
import controlador.TDA.listas.DynamicList;
import modelo.Auto;

public class AutoArchivos extends DaoImplement<Auto> {

    private DynamicList<Auto> autos;
    private Auto auto;

    public AutoArchivos() {
        super(Auto.class);
    }

    public DynamicList<Auto> getAutos() {
        autos = all();
        return autos;
    }

    public void setAutos(DynamicList<Auto> autos) {
        this.autos = autos;
    }

    public Auto getAuto() {
        if (auto == null) {
            auto = new Auto();
        }
        return auto;
    }

    public void setAuto(Auto auto) {
        this.auto = auto;
    }

    public Boolean persist() {
        auto.setId(all().getLength());
        return persist(auto);
    }
}
