package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import modelo.Documento;
import modelo.Libro;
import modelo.Revista;
import modelo.CD;
import modelo.Tesis;
import utilidades.ManejoErrores;

// Clase que se encarga de todas las operaciones de documentos contra la base de datos
// Aqui se hacen las consultas para insertar y listar libros, revistas, cds y tesis
public class DocumentoDAO {
    
    // Trae todos los documentos sin importar el tipo
    public List<Documento> listarTodos() {
        List<Documento> lista = new ArrayList<>();
        String sql = "SELECT * FROM documentos ORDER BY tipo, titulo";
        try {
            Connection con = Conexion.obtenerConexion();
            if (con != null) {
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    String tipo = rs.getString("tipo");
                    Documento doc = null;
                    int idDoc = rs.getInt("id_documento");
                    
                    // Segun el tipo, cargamos los datos especificos de cada documento
                    switch(tipo) {
                        case "LIBRO": doc = cargarDetallesLibro(con, idDoc, rs); break;
                        case "REVISTA": doc = cargarDetallesRevista(con, idDoc, rs); break;
                        case "CD": doc = cargarDetallesCD(con, idDoc, rs); break;
                        case "TESIS": doc = cargarDetallesTesis(con, idDoc, rs); break;
                    }
                    if (doc != null) lista.add(doc);
                }
                con.close();
            }
        } catch (Exception e) {
            ManejoErrores.guardarError("Error al listar documentos: " + e.getMessage());
        }
        return lista;
    }

    // Metodos para listar por cada tipo de documento
    public List<Documento> listarLibros() {
        return listarPorTipo("LIBRO");
    }

    public List<Documento> listarRevistas() {
        return listarPorTipo("REVISTA");
    }

    public List<Documento> listarCDs() {
        return listarPorTipo("CD");
    }

    public List<Documento> listarTesis() {
        return listarPorTipo("TESIS");
    }

    // Metodo generico que filtra documentos por tipo
    private List<Documento> listarPorTipo(String tipo) {
        List<Documento> lista = new ArrayList<>();
        String sqlBase = "SELECT d.* FROM documentos d WHERE d.tipo = ? ORDER BY d.titulo";
        try {
            Connection con = Conexion.obtenerConexion();
            if (con != null) {
                PreparedStatement ps = con.prepareStatement(sqlBase);
                ps.setString(1, tipo);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    int idDoc = rs.getInt("id_documento");
                    Documento doc = null;
                    switch(tipo) {
                        case "LIBRO": doc = cargarDetallesLibro(con, idDoc, rs); break;
                        case "REVISTA": doc = cargarDetallesRevista(con, idDoc, rs); break;
                        case "CD": doc = cargarDetallesCD(con, idDoc, rs); break;
                        case "TESIS": doc = cargarDetallesTesis(con, idDoc, rs); break;
                    }
                    if (doc != null) lista.add(doc);
                }
                con.close();
            }
        } catch (Exception e) {
            ManejoErrores.guardarError("Error al listar " + tipo + ": " + e.getMessage());
        }
        return lista;
    }

    // --- Metodos para cargar los datos especificos de cada tipo ---
    // Cada tipo tiene su propia tabla con campos extras

    // Carga los datos extras de un libro (isbn, editorial, edicion, etc.)
    private Libro cargarDetallesLibro(Connection con, int idDoc, ResultSet rsDoc) throws Exception {
        Libro lib = new Libro();
        llenarDocumentoBase(lib, rsDoc);
        String sql = "SELECT * FROM libros WHERE id_documento = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, idDoc);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            lib.setIsbn(rs.getString("isbn"));
            lib.setEditorial(rs.getString("editorial"));
            lib.setEdicion(rs.getString("edicion"));
            lib.setNumPaginas(rs.getInt("num_paginas"));
            lib.setIdioma(rs.getString("idioma"));
            lib.setMateria(rs.getString("materia"));
        }
        return lib;
    }

    // Carga los datos extras de una revista (issn, volumen, numero, etc.)
    private Revista cargarDetallesRevista(Connection con, int idDoc, ResultSet rsDoc) throws Exception {
        Revista rev = new Revista();
        llenarDocumentoBase(rev, rsDoc);
        String sql = "SELECT * FROM revistas WHERE id_documento = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, idDoc);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            rev.setIssn(rs.getString("issn"));
            rev.setEdicion(rs.getInt("edicion"));
            rev.setVolumen(rs.getInt("volumen"));
            rev.setNumero(rs.getInt("numero"));
            rev.setPeriodicidad(rs.getString("periodicidad"));
            rev.setEditorial(rs.getString("editorial"));
        }
        return rev;
    }

    // Carga los datos extras de un CD (genero, duracion, formato, etc.)
    private CD cargarDetallesCD(Connection con, int idDoc, ResultSet rsDoc) throws Exception {
        CD cd = new CD();
        llenarDocumentoBase(cd, rsDoc);
        String sql = "SELECT * FROM cds WHERE id_documento = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, idDoc);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            cd.setGenero(rs.getString("genero"));
            cd.setDuracion(rs.getInt("duracion"));
            cd.setFormato(rs.getString("formato"));
            cd.setContenido(rs.getString("contenido"));
            cd.setSistemaRequerido(rs.getString("sistema_requerido"));
        }
        return cd;
    }

    // Carga los datos extras de una tesis (carrera, universidad, asesor, etc.)
    private Tesis cargarDetallesTesis(Connection con, int idDoc, ResultSet rsDoc) throws Exception {
        Tesis tesis = new Tesis();
        llenarDocumentoBase(tesis, rsDoc);
        String sql = "SELECT * FROM tesis WHERE id_documento = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, idDoc);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            tesis.setCarrera(rs.getString("carrera"));
            tesis.setUniversidad(rs.getString("universidad"));
            tesis.setGradoAcademico(rs.getString("grado_academico"));
            tesis.setAsesor(rs.getString("asesor"));
            java.sql.Date fd = rs.getDate("fecha_defensa");
            tesis.setFechaDefensa(fd != null ? fd.toString() : "");
            tesis.setNumPaginas(rs.getInt("num_paginas"));
        }
        return tesis;
    }

    // Llena los campos que son iguales para todos los documentos
    // (titulo, autor, codigo, ubicacion, etc.)
    private void llenarDocumentoBase(Documento doc, ResultSet rs) throws Exception {
        doc.setId(rs.getInt("id_documento"));
        doc.setCodigo(rs.getString("codigo"));
        doc.setTitulo(rs.getString("titulo"));
        doc.setAutor(rs.getString("autor"));
        doc.setAnioPublicacion(rs.getInt("anio_publicacion"));
        doc.setClasificacion(rs.getString("clasificacion"));
        doc.setUbicacion(rs.getString("ubicacion"));
        doc.setDisponibles(rs.getInt("disponibles"));
        doc.setTotal(rs.getInt("total"));
        doc.setEstadoFisico(rs.getString("estado_fisico"));
    }

    // --- Metodo base para insertar en la tabla documentos ---
    // Todos los tipos pasan por aqui primero porque comparten la misma tabla base
    private int insertarDocumentoBase(Connection con, Documento doc, String tipo) throws Exception {
        String sqlDoc = "INSERT INTO documentos (codigo, titulo, autor, anio_publicacion, clasificacion, ubicacion, tipo, disponibles, total, estado_fisico) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement psDoc = con.prepareStatement(sqlDoc, Statement.RETURN_GENERATED_KEYS);
        psDoc.setString(1, doc.getCodigo());
        psDoc.setString(2, doc.getTitulo());
        psDoc.setString(3, doc.getAutor());
        psDoc.setInt(4, doc.getAnioPublicacion());
        psDoc.setString(5, doc.getClasificacion());
        psDoc.setString(6, doc.getUbicacion());
        psDoc.setString(7, tipo);
        psDoc.setInt(8, doc.getDisponibles());
        psDoc.setInt(9, doc.getTotal());
        psDoc.setString(10, doc.getEstadoFisico());
        psDoc.executeUpdate();

        // Obtenemos el ID que se genero para luego usarlo en la tabla especifica
        ResultSet rs = psDoc.getGeneratedKeys();
        int idGenerado = 0;
        if (rs.next()) {
            idGenerado = rs.getInt(1);
        }
        return idGenerado;
    }

    // Registra un libro nuevo en la base de datos
    // Primero inserta en 'documentos' y luego en 'libros' con los datos extras
    public boolean registrarLibro(Libro libro) {
        try {
            Connection con = Conexion.obtenerConexion();
            con.setAutoCommit(false); // Usamos transaccion por si algo falla

            int idGenerado = insertarDocumentoBase(con, libro, "LIBRO");

            // Insertamos los datos especificos del libro
            String sqlLibro = "INSERT INTO libros (id_documento, isbn, editorial, edicion, num_paginas, idioma, materia) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement psLib = con.prepareStatement(sqlLibro);
            psLib.setInt(1, idGenerado);
            psLib.setString(2, libro.getIsbn());
            psLib.setString(3, libro.getEditorial());
            psLib.setString(4, libro.getEdicion());
            psLib.setInt(5, libro.getNumPaginas());
            psLib.setString(6, libro.getIdioma());
            psLib.setString(7, libro.getMateria());
            psLib.executeUpdate();

            con.commit();
            con.close();
            return true;
        } catch (Exception e) {
            ManejoErrores.guardarError("Error al registrar libro: " + e.getMessage());
            return false;
        }
    }

    // Registra una revista nueva
    public boolean registrarRevista(Revista revista) {
        try {
            Connection con = Conexion.obtenerConexion();
            con.setAutoCommit(false);

            int idGenerado = insertarDocumentoBase(con, revista, "REVISTA");

            String sqlRev = "INSERT INTO revistas (id_documento, issn, edicion, volumen, numero, periodicidad, editorial) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement psRev = con.prepareStatement(sqlRev);
            psRev.setInt(1, idGenerado);
            psRev.setString(2, revista.getIssn());
            psRev.setInt(3, revista.getEdicion());
            psRev.setInt(4, revista.getVolumen());
            psRev.setInt(5, revista.getNumero());
            psRev.setString(6, revista.getPeriodicidad());
            psRev.setString(7, revista.getEditorial());
            psRev.executeUpdate();

            con.commit();
            con.close();
            return true;
        } catch (Exception e) {
            ManejoErrores.guardarError("Error al registrar revista: " + e.getMessage());
            return false;
        }
    }

    // Registra un CD nuevo
    public boolean registrarCD(CD cd) {
        try {
            Connection con = Conexion.obtenerConexion();
            con.setAutoCommit(false);

            int idGenerado = insertarDocumentoBase(con, cd, "CD");

            String sqlCD = "INSERT INTO cds (id_documento, genero, duracion, formato, contenido, sistema_requerido) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement psCD = con.prepareStatement(sqlCD);
            psCD.setInt(1, idGenerado);
            psCD.setString(2, cd.getGenero());
            psCD.setInt(3, cd.getDuracion());
            psCD.setString(4, cd.getFormato());
            psCD.setString(5, cd.getContenido());
            psCD.setString(6, cd.getSistemaRequerido());
            psCD.executeUpdate();

            con.commit();
            con.close();
            return true;
        } catch (Exception e) {
            ManejoErrores.guardarError("Error al registrar CD: " + e.getMessage());
            return false;
        }
    }

    // Registra una tesis nueva
    public boolean registrarTesis(Tesis tesis) {
        try {
            Connection con = Conexion.obtenerConexion();
            con.setAutoCommit(false);

            int idGenerado = insertarDocumentoBase(con, tesis, "TESIS");

            String sqlTesis = "INSERT INTO tesis (id_documento, carrera, universidad, grado_academico, asesor, fecha_defensa, num_paginas) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement psTesis = con.prepareStatement(sqlTesis);
            psTesis.setInt(1, idGenerado);
            psTesis.setString(2, tesis.getCarrera());
            psTesis.setString(3, tesis.getUniversidad());
            psTesis.setString(4, tesis.getGradoAcademico());
            psTesis.setString(5, tesis.getAsesor());
            // Si no tiene fecha de defensa, se guarda como null
            if (tesis.getFechaDefensa() != null && !tesis.getFechaDefensa().isEmpty()) {
                psTesis.setDate(6, java.sql.Date.valueOf(tesis.getFechaDefensa()));
            } else {
                psTesis.setNull(6, java.sql.Types.DATE);
            }
            psTesis.setInt(7, tesis.getNumPaginas());
            psTesis.executeUpdate();

            con.commit();
            con.close();
            return true;
        } catch (Exception e) {
            ManejoErrores.guardarError("Error al registrar tesis: " + e.getMessage());
            return false;
        }
    }
}
