package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import utilidades.ManejoErrores;

public class Conexion {
    private static final String URL = "jdbc:mysql://localhost:3306/bd_biblioteca";
    private static final String USUARIO = "root";
    private static final String CLAVE = "Colinas15";

    public static Connection obtenerConexion() {
        Connection con = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(URL, USUARIO, CLAVE);
        } catch (ClassNotFoundException e) {
            ManejoErrores.guardarError("Falta el driver de MySQL (el archivo .jar)");
        } catch (SQLException e) {
            ManejoErrores.guardarError("Error conectando a la BD: " + e.getMessage());
        }
        return con;
    }
}
