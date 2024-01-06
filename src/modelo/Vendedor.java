package modelo;

public class Vendedor {
    private Integer id;
    private String dni;
    private String ruc;
    private String nombre;
    private String apellido;
    private String telefono;

    public Vendedor() {
    }

    public Vendedor(Integer id, String dni, String ruc, String nombre, String apellido, String telefono) {
        this.id = id;
        this.dni = dni;
        this.ruc = ruc;
        this.nombre = nombre;
        this.apellido = apellido;
        this.telefono = telefono;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getRuc() {
        return ruc;
    }

    public void setRuc(String ruc) {
        this.ruc = ruc;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    @Override
    public String toString() {
        return apellido + " " + nombre;
    }
}
