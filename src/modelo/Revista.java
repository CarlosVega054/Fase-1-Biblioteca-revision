package modelo;

// Clase Revista - hereda de Documento
// Representa una revista o publicacion periodica de la biblioteca
public class Revista extends Documento {
    // Datos propios de una revista
    private String issn;         // Codigo unico de la revista (como el ISBN pero para revistas)
    private int edicion;         // Numero de edicion
    private int volumen;         // Volumen (agrupa los numeros de un periodo, normalmente un año)
    private int numero;          // Numero del ejemplar dentro del volumen
    private String periodicidad; // Cada cuanto se publica (Mensual, Trimestral, etc.)
    private String editorial;    // Editorial que la publica

    // Muestra un resumen con los datos de la revista
    @Override
    public String verDetalles() {
        return "Revista - ISSN: " + issn + ", Edición: " + edicion +
               ", Vol." + volumen + " No." + numero + 
               ", Periodicidad: " + periodicidad + 
               ", Editorial: " + editorial;
    }

    // Getters y Setters
    public String getIssn() { return issn; }
    public void setIssn(String issn) { this.issn = issn; }
    public int getEdicion() { return edicion; }
    public void setEdicion(int edicion) { this.edicion = edicion; }
    public int getVolumen() { return volumen; }
    public void setVolumen(int volumen) { this.volumen = volumen; }
    public int getNumero() { return numero; }
    public void setNumero(int numero) { this.numero = numero; }
    public String getPeriodicidad() { return periodicidad; }
    public void setPeriodicidad(String periodicidad) { this.periodicidad = periodicidad; }
    public String getEditorial() { return editorial; }
    public void setEditorial(String editorial) { this.editorial = editorial; }
}
