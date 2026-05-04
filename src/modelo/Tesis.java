package modelo;

public class Tesis extends Documento {
    private String carrera;
    private String universidad;

    @Override
    public String verDetalles() {
        return "Tesis - " + carrera + " en " + universidad;
    }

    public String getCarrera() { return carrera; }
    public void setCarrera(String carrera) { this.carrera = carrera; }
    public String getUniversidad() { return universidad; }
    public void setUniversidad(String universidad) { this.universidad = universidad; }
}
