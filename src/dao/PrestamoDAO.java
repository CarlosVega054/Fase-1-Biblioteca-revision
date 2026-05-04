package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import excepciones.ErrorValidacion;
import modelo.Prestamo;
import utilidades.ManejoErrores;
import java.util.Date;

public class PrestamoDAO {
    
    // Constantes de configuración de la biblioteca
    private static final int MAX_PRESTAMOS_ALUMNO = 3;
    private static final int MAX_PRESTAMOS_PROFESOR = 5;
    private static final double MORA_DIARIA = 0.50; // 50 centavos por día de retraso
    
    public void registrarPrestamo(Prestamo p, int idRolUsuario) throws ErrorValidacion {
        try {
            Connection con = Conexion.obtenerConexion();
            if (con != null) {
                // 1. Validar si el usuario tiene mora (préstamos vencidos sin devolver)
                String sqlMora = "SELECT COUNT(*) AS vencidos FROM prestamos WHERE id_usuario = ? AND estado = 'PRESTADO' AND fecha_devolucion < CURDATE()";
                PreparedStatement psMora = con.prepareStatement(sqlMora);
                psMora.setInt(1, p.getIdUsuario());
                ResultSet rsMora = psMora.executeQuery();
                if (rsMora.next() && rsMora.getInt("vencidos") > 0) {
                    throw new ErrorValidacion("El usuario tiene libros vencidos. Debe devolverlos y pagar mora antes de un nuevo préstamo.");
                }
                
                // 2. Validar límite de préstamos activos
                String sqlLimite = "SELECT COUNT(*) AS activos FROM prestamos WHERE id_usuario = ? AND estado = 'PRESTADO'";
                PreparedStatement psLimite = con.prepareStatement(sqlLimite);
                psLimite.setInt(1, p.getIdUsuario());
                ResultSet rsLimite = psLimite.executeQuery();
                if (rsLimite.next()) {
                    int activos = rsLimite.getInt("activos");
                    int maxPermitido = (idRolUsuario == 3) ? MAX_PRESTAMOS_ALUMNO : MAX_PRESTAMOS_PROFESOR;
                    if (activos >= maxPermitido) {
                        throw new ErrorValidacion("El usuario ha alcanzado el límite máximo de préstamos (" + maxPermitido + ").");
                    }
                }
                
                // 3. Verificar si hay libros disponibles
                String sqlCheck = "SELECT disponibles FROM documentos WHERE id_documento = ?";
                PreparedStatement psCheck = con.prepareStatement(sqlCheck);
                psCheck.setInt(1, p.getIdDocumento());
                ResultSet rs = psCheck.executeQuery();
                
                if (rs.next()) {
                    int disp = rs.getInt("disponibles");
                    if (disp <= 0) {
                        throw new ErrorValidacion("No hay copias disponibles de este documento.");
                    }
                } else {
                    throw new ErrorValidacion("El documento no existe.");
                }
                
                // Si todo está bien, registrar prestamo
                String sqlInsert = "INSERT INTO prestamos (id_usuario, id_documento, fecha_prestamo, fecha_devolucion, estado) VALUES (?, ?, ?, ?, 'PRESTADO')";
                PreparedStatement psInsert = con.prepareStatement(sqlInsert);
                psInsert.setInt(1, p.getIdUsuario());
                psInsert.setInt(2, p.getIdDocumento());
                psInsert.setDate(3, new java.sql.Date(p.getFechaPrestamo().getTime()));
                psInsert.setDate(4, new java.sql.Date(p.getFechaDevolucion().getTime()));
                psInsert.executeUpdate();
                
                // Restar 1 a disponibles
                String sqlUpdate = "UPDATE documentos SET disponibles = disponibles - 1 WHERE id_documento = ?";
                PreparedStatement psUpdate = con.prepareStatement(sqlUpdate);
                psUpdate.setInt(1, p.getIdDocumento());
                psUpdate.executeUpdate();
                
                con.close();
            }
        } catch (ErrorValidacion ev) {
            throw ev; // relanzar el error de validacion para mostrarlo en pantalla
        } catch (Exception e) {
            ManejoErrores.guardarError("Error al prestar: " + e.getMessage());
        }
    }

    public void registrarDevolucion(int idPrestamo, int idDocumento) {
        try {
            Connection con = Conexion.obtenerConexion();
            if (con != null) {
                // Calcular si hay mora
                String sqlMora = "SELECT fecha_devolucion, DATEDIFF(CURDATE(), fecha_devolucion) AS dias_retraso FROM prestamos WHERE id_prestamo = ?";
                PreparedStatement psMora = con.prepareStatement(sqlMora);
                psMora.setInt(1, idPrestamo);
                ResultSet rsMora = psMora.executeQuery();
                
                double moraCalculada = 0.0;
                if(rsMora.next()) {
                    int diasRetraso = rsMora.getInt("dias_retraso");
                    if(diasRetraso > 0) {
                        moraCalculada = diasRetraso * MORA_DIARIA;
                    }
                }

                // Actualizar estado del prestamo
                String sqlUpd = "UPDATE prestamos SET estado = 'DEVUELTO', mora = ? WHERE id_prestamo = ?";
                PreparedStatement psUpd = con.prepareStatement(sqlUpd);
                psUpd.setDouble(1, moraCalculada);
                psUpd.setInt(2, idPrestamo);
                psUpd.executeUpdate();

                // Devolver el libro al inventario (sumar 1)
                String sqlInv = "UPDATE documentos SET disponibles = disponibles + 1 WHERE id_documento = ?";
                PreparedStatement psInv = con.prepareStatement(sqlInv);
                psInv.setInt(1, idDocumento);
                psInv.executeUpdate();

                con.close();
            }
        } catch (Exception e) {
            ManejoErrores.guardarError("Error en devolución: " + e.getMessage());
        }
    }
}
