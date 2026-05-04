package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import modelo.Usuario;
import utilidades.ManejoErrores;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {
    
    public Usuario login(String correo, String password) {
        Usuario usu = null;
        String sql = "SELECT * FROM usuarios WHERE correo = ? AND password = ?";
        
        try {
            Connection con = Conexion.obtenerConexion();
            if (con != null) {
                PreparedStatement ps = con.prepareStatement(sql);
                ps.setString(1, correo);
                ps.setString(2, password);
                
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    usu = new Usuario();
                    usu.setId(rs.getInt("id_usuario"));
                    usu.setNombre(rs.getString("nombre"));
                    usu.setApellidos(rs.getString("apellidos"));
                    usu.setCorreo(rs.getString("correo"));
                    usu.setIdRol(rs.getInt("id_rol"));
                }
                con.close();
            }
        } catch (Exception e) {
            ManejoErrores.guardarError("Error en login: " + e.getMessage());
        }
        
        return usu;
    }

    public boolean registrarUsuario(Usuario usu) {
        String sql = "INSERT INTO usuarios (nombre, apellidos, correo, password, id_rol) VALUES (?, ?, ?, ?, ?)";
        try {
            Connection con = Conexion.obtenerConexion();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, usu.getNombre());
            ps.setString(2, usu.getApellidos());
            ps.setString(3, usu.getCorreo());
            ps.setString(4, usu.getPassword());
            ps.setInt(5, usu.getIdRol());
            
            int filas = ps.executeUpdate();
            con.close();
            return filas > 0;
        } catch (Exception e) {
            ManejoErrores.guardarError("Error al registrar usuario: " + e.getMessage());
            return false;
        }
    }

    public boolean cambiarClave(String correo, String nuevaClave) {
        String sql = "UPDATE usuarios SET password = ? WHERE correo = ?";
        try {
            Connection con = Conexion.obtenerConexion();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, nuevaClave);
            ps.setString(2, correo);
            
            int filas = ps.executeUpdate();
            con.close();
            return filas > 0;
        } catch (Exception e) {
            ManejoErrores.guardarError("Error al cambiar clave: " + e.getMessage());
            return false;
        }
    }

    public List<Usuario> listarUsuarios() {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT * FROM usuarios";
        try {
            Connection con = Conexion.obtenerConexion();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Usuario u = new Usuario();
                u.setId(rs.getInt("id_usuario"));
                u.setNombre(rs.getString("nombre"));
                u.setApellidos(rs.getString("apellidos"));
                u.setCorreo(rs.getString("correo"));
                u.setIdRol(rs.getInt("id_rol"));
                lista.add(u);
            }
            con.close();
        } catch (Exception e) {
            ManejoErrores.guardarError("Error al listar usuarios: " + e.getMessage());
        }
        return lista;
    }
}
