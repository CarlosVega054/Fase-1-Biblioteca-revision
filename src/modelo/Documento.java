package modelo;

public abstract class Documento {
    protected int id;
    protected String codigo;
    protected String titulo;
    protected String autor;
    protected int anioPublicacion;
    protected String clasificacion;
    protected String ubicacion;
    protected int disponibles;
    protected int total;
    protected String estadoFisico;

    public Documento() {}

    public abstract String verDetalles();

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getAutor() { return autor; }
    public void setAutor(String autor) { this.autor = autor; }
    public String getUbicacion() { return ubicacion; }
    public void setUbicacion(String ubicacion) { this.ubicacion = ubicacion; }
    public int getDisponibles() { return disponibles; }
    public void setDisponibles(int disponibles) { this.disponibles = disponibles; }
    public int getTotal() { return total; }
    public void setTotal(int total) { this.total = total; }
    
    public int getAnioPublicacion() { return anioPublicacion; }
    public void setAnioPublicacion(int anioPublicacion) { this.anioPublicacion = anioPublicacion; }
    public String getClasificacion() { return clasificacion; }
    public void setClasificacion(String clasificacion) { this.clasificacion = clasificacion; }
    public String getEstadoFisico() { return estadoFisico; }
    public void setEstadoFisico(String estadoFisico) { this.estadoFisico = estadoFisico; }
}
