package modelo;

public class Libro extends Documento {
    private String isbn;
    private String editorial;

    public Libro() {}

    @Override
    public String verDetalles() {
        return "Libro - ISBN: " + isbn + ", Editorial: " + editorial + " (" + anioPublicacion + ")";
    }

    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }
    public String getEditorial() { return editorial; }
    public void setEditorial(String editorial) { this.editorial = editorial; }
}
