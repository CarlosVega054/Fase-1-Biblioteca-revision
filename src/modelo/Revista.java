package modelo;

public class Revista extends Documento {
    private String issn;
    private int edicion;

    @Override
    public String verDetalles() {
        return "Revista - ISSN: " + issn + ", Edicion: " + edicion;
    }

    public String getIssn() { return issn; }
    public void setIssn(String issn) { this.issn = issn; }
    public int getEdicion() { return edicion; }
    public void setEdicion(int edicion) { this.edicion = edicion; }
}
