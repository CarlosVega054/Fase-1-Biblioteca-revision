package modelo;

public class CD extends Documento {
    private String genero;
    private int duracion;

    @Override
    public String verDetalles() {
        return "CD - Genero: " + genero + ", Duracion: " + duracion + " mins";
    }

    public String getGenero() { return genero; }
    public void setGenero(String genero) { this.genero = genero; }
    public int getDuracion() { return duracion; }
    public void setDuracion(int duracion) { this.duracion = duracion; }
}
