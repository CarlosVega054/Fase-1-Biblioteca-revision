package modelo;

// Clase Tesis - hereda de Documento
// Representa una tesis o trabajo de grado guardado en la biblioteca
public class Tesis extends Documento {
    // Datos propios de una tesis
    private String carrera;         // De que carrera es la tesis
    private String universidad;     // En que universidad se presento
    private String gradoAcademico;  // Que grado se obtuvo (Tecnico, Licenciatura, Maestria, etc.)
    private String asesor;          // Quien fue el tutor o asesor
    private String fechaDefensa;    // Cuando se defendio la tesis (formato YYYY-MM-DD)
    private int numPaginas;         // Cuantas paginas tiene

    // Muestra un resumen con los datos de la tesis
    @Override
    public String verDetalles() {
        return "Tesis - " + gradoAcademico + " en " + carrera + 
               ", " + universidad + ", Asesor: " + asesor +
               ", Defensa: " + fechaDefensa + ", Págs: " + numPaginas;
    }

    // Getters y Setters
    public String getCarrera() { return carrera; }
    public void setCarrera(String carrera) { this.carrera = carrera; }
    public String getUniversidad() { return universidad; }
    public void setUniversidad(String universidad) { this.universidad = universidad; }
    public String getGradoAcademico() { return gradoAcademico; }
    public void setGradoAcademico(String gradoAcademico) { this.gradoAcademico = gradoAcademico; }
    public String getAsesor() { return asesor; }
    public void setAsesor(String asesor) { this.asesor = asesor; }
    public String getFechaDefensa() { return fechaDefensa; }
    public void setFechaDefensa(String fechaDefensa) { this.fechaDefensa = fechaDefensa; }
    public int getNumPaginas() { return numPaginas; }
    public void setNumPaginas(int numPaginas) { this.numPaginas = numPaginas; }
}
