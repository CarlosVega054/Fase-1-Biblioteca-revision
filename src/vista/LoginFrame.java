package vista;

import controlador.LoginControlador;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class LoginFrame extends JFrame {
    private JTextField txtCorreo;
    private JPasswordField txtPassword;
    private JButton btnIngresar;

    public LoginFrame() {
        setTitle("Login - Biblioteca");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null); // Layout absoluto simple (típico de estudiante)

        JLabel lblCorreo = new JLabel("Correo:");
        lblCorreo.setBounds(30, 30, 80, 25);
        add(lblCorreo);

        txtCorreo = new JTextField();
        txtCorreo.setBounds(100, 30, 150, 25);
        add(txtCorreo);

        JLabel lblPass = new JLabel("Clave:");
        lblPass.setBounds(30, 70, 80, 25);
        add(lblPass);

        txtPassword = new JPasswordField();
        txtPassword.setBounds(100, 70, 150, 25);
        add(txtPassword);

        btnIngresar = new JButton("Entrar");
        btnIngresar.setBounds(100, 110, 100, 30);
        add(btnIngresar);

        btnIngresar.addActionListener(e -> {
            String correo = txtCorreo.getText();
            String pass = new String(txtPassword.getPassword());
            
            LoginControlador ctrl = new LoginControlador();
            if (ctrl.validarIngreso(correo, pass)) {
                JOptionPane.showMessageDialog(null, "¡Bienvenido!");
                MainFrame main = new MainFrame();
                main.setVisible(true);
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(null, "Usuario o clave incorrectos", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
