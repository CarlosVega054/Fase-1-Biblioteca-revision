package controlador;

import dao.UsuarioDAO;
import modelo.Usuario;
import utilidades.Sesion;

public class LoginControlador {
    
    public boolean validarIngreso(String correo, String password) {
        UsuarioDAO dao = new UsuarioDAO();
        Usuario usu = dao.login(correo, password);
        
        if (usu != null) {
            Sesion.usuarioLogueado = usu;
            return true;
        } else {
            return false;
        }
    }
}
