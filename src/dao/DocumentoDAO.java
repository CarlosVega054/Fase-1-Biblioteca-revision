package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import modelo.Documento;
import modelo.Libro;
import utilidades.ManejoErrores;

public class DocumentoDAO {
    
    public List<Documento> listarLibros() {
        List<Documento> lista = new ArrayList<>();
        String sql = "SELECT d.*, l.isbn, l.editorial FROM documentos d INNER JOIN libros l ON d.id_documento = l.id_documento";
        
        try {
            Connection con = Conexion.obtenerConexion();
            if (con != null) {
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery();
                
                while (rs.next()) {
                    Libro lib = new Libro();
                    lib.setId(rs.getInt("id_documento"));
                    lib.setCodigo(rs.getString("codigo"));
                    lib.setTitulo(rs.getString("titulo"));
                    lib.setAutor(rs.getString("autor"));
                    lib.setUbicacion(rs.getString("ubicacion"));
                    lib.setDisponibles(rs.getInt("disponibles"));
                    lib.setTotal(rs.getInt("total"));
                    lib.setIsbn(rs.getString("isbn"));
                    lib.setEditorial(rs.getString("editorial"));
                    lib.setAnioPublicacion(rs.getInt("anio_publicacion"));
                    lib.setClasificacion(rs.getString("clasificacion"));
                    lib.setEstadoFisico(rs.getString("estado_fisico"));
                    
                    lista.add(lib);
                }
                con.close();
            }
        } catch (Exception e) {
            ManejoErrores.guardarError("Error al listar libros: " + e.getMessage());
        }
        return lista;
    }

    public boolean registrarLibro(Libro libro) {
        String sqlDoc = "INSERT INTO documentos (codigo, titulo, autor, anio_publicacion, clasificacion, ubicacion, tipo, disponibles, total, estado_fisico) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        String sqlLibro = "INSERT INTO libros (id_documento, isbn, editorial) VALUES (?, ?, ?)";
        
        try {
            Connection con = Conexion.obtenerConexion();
            con.setAutoCommit(false); // Iniciar transaccion
            
            // Insertar en documentos
            PreparedStatement psDoc = con.prepareStatement(sqlDoc, Statement.RETURN_GENERATED_KEYS);
            psDoc.setString(1, libro.getCodigo());
            psDoc.setString(2, libro.getTitulo());
            psDoc.setString(3, libro.getAutor());
            psDoc.setInt(4, libro.getAnioPublicacion());
            psDoc.setString(5, libro.getClasificacion());
            psDoc.setString(6, libro.getUbicacion());
            psDoc.setString(7, "LIBRO");
            psDoc.setInt(8, libro.getDisponibles());
            psDoc.setInt(9, libro.getTotal());
            psDoc.setString(10, libro.getEstadoFisico());
            psDoc.executeUpdate();
            
            ResultSet rs = psDoc.getGeneratedKeys();
            int idGenerado = 0;
            if(rs.next()) {
                idGenerado = rs.getInt(1);
            }
            
            // Insertar en libros
            PreparedStatement psLib = con.prepareStatement(sqlLibro);
            psLib.setInt(1, idGenerado);
            psLib.setString(2, libro.getIsbn());
            psLib.setString(3, libro.getEditorial());
            psLib.executeUpdate();
            
            con.commit();
            con.close();
            return true;
        } catch (Exception e) {
            ManejoErrores.guardarError("Error al registrar libro: " + e.getMessage());
            return false;
        }
    }
}
