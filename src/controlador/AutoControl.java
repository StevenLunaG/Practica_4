package controlador;

import controlador.TDA.listas.DynamicList;
import modelo.Auto;

public class AutoControl {

    private Auto auto = new Auto();
    private DynamicList<Auto> autos;

    public AutoControl(Auto auto) {
        this.auto = auto;
    }

    public AutoControl() {
        this.autos = new DynamicList<>();
    }

    public Boolean guardar() {
        try {
            getAuto().setId(getAutos().getLength());
            getAutos().add(getAuto());
            return true;
        } catch (Exception e) {
            return false;
        }
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

    public DynamicList<Auto> getAutos() {
        return autos;
    }

    public void setAutos(DynamicList<Auto> autos) {
        this.autos = autos;
    }

}
