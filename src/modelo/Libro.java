package modelo;

// Clase Libro - hereda de Documento
// Representa un libro fisico de la biblioteca con sus datos especificos
public class Libro extends Documento {
    // Datos propios de un libro
    private String isbn;        // Codigo unico del libro (ej: 978-84-376-0494-7)
    private String editorial;   // Casa editorial que lo publico
    private String edicion;     // Que edicion es (ej: "2a edicion")
    private int numPaginas;     // Cuantas paginas tiene
    private String idioma;      // En que idioma esta escrito
    private String materia;     // Tema principal (ej: Matematicas, Historia)

    public Libro() {}

    // Muestra un resumen con los datos del libro
    @Override
    public String verDetalles() {
        return "Libro - ISBN: " + isbn + ", Editorial: " + editorial + 
               ", Edición: " + edicion + ", Págs: " + numPaginas + 
               ", Idioma: " + idioma + ", Materia: " + materia +
               " (" + anioPublicacion + ")";
    }

    // Getters y Setters
    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }
    public String getEditorial() { return editorial; }
    public void setEditorial(String editorial) { this.editorial = editorial; }
    public String getEdicion() { return edicion; }
    public void setEdicion(String edicion) { this.edicion = edicion; }
    public int getNumPaginas() { return numPaginas; }
    public void setNumPaginas(int numPaginas) { this.numPaginas = numPaginas; }
    public String getIdioma() { return idioma; }
    public void setIdioma(String idioma) { this.idioma = idioma; }
    public String getMateria() { return materia; }
    public void setMateria(String materia) { this.materia = materia; }
}
