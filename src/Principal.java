import vista.LoginFrame;
import utilidades.ManejoErrores;

public class Principal {
    public static void main(String[] args) {
        try {
            LoginFrame login = new LoginFrame();
            login.setVisible(true);
        } catch (Exception e) {
            ManejoErrores.guardarError("Error fatal al iniciar la aplicación: " + e.getMessage());
        }
    }
}
