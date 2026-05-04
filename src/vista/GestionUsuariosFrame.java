package vista;

import dao.UsuarioDAO;
import modelo.Usuario;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class GestionUsuariosFrame extends JFrame {
    private JTextField txtNombre, txtApellidos, txtCorreo;
    private JPasswordField txtPassword;
    private JComboBox<String> cbRoles;
    private JButton btnRegistrar, btnCambiarClave, btnListar;
    private JTextArea txtResultados;
    private UsuarioDAO dao;

    public GestionUsuariosFrame() {
        dao = new UsuarioDAO();
        setTitle("Gestión de Usuarios - Encargados");
        setSize(500, 500);
        setLocationRelativeTo(null);
        setLayout(null);

        JLabel lblTitulo = new JLabel("Módulo de Encargados - Usuarios");
        lblTitulo.setBounds(150, 10, 250, 20);
        add(lblTitulo);

        // Campos
        add(new JLabel("Nombre:")).setBounds(20, 50, 80, 25);
        txtNombre = new JTextField();
        txtNombre.setBounds(100, 50, 150, 25);
        add(txtNombre);

        add(new JLabel("Apellidos:")).setBounds(260, 50, 80, 25);
        txtApellidos = new JTextField();
        txtApellidos.setBounds(330, 50, 150, 25);
        add(txtApellidos);

        add(new JLabel("Correo:")).setBounds(20, 90, 80, 25);
        txtCorreo = new JTextField();
        txtCorreo.setBounds(100, 90, 150, 25);
        add(txtCorreo);

        add(new JLabel("Password:")).setBounds(260, 90, 80, 25);
        txtPassword = new JPasswordField();
        txtPassword.setBounds(330, 90, 150, 25);
        add(txtPassword);

        add(new JLabel("Rol:")).setBounds(20, 130, 80, 25);
        String[] roles = {"1 - Administrador", "2 - Profesor", "3 - Alumno"};
        cbRoles = new JComboBox<>(roles);
        cbRoles.setBounds(100, 130, 150, 25);
        add(cbRoles);

        // Botones
        btnRegistrar = new JButton("Registrar");
        btnRegistrar.setBounds(20, 180, 100, 30);
        add(btnRegistrar);

        btnCambiarClave = new JButton("Restablecer Clave");
        btnCambiarClave.setBounds(130, 180, 150, 30);
        add(btnCambiarClave);

        btnListar = new JButton("Listar Todos");
        btnListar.setBounds(290, 180, 150, 30);
        add(btnListar);

        txtResultados = new JTextArea();
        JScrollPane scroll = new JScrollPane(txtResultados);
        scroll.setBounds(20, 230, 450, 200);
        add(scroll);

        // Eventos
        btnRegistrar.addActionListener(e -> {
            Usuario u = new Usuario();
            u.setNombre(txtNombre.getText());
            u.setApellidos(txtApellidos.getText());
            u.setCorreo(txtCorreo.getText());
            u.setPassword(new String(txtPassword.getPassword()));
            u.setIdRol(cbRoles.getSelectedIndex() + 1); // Indice 0 es Rol 1

            if (dao.registrarUsuario(u)) {
                JOptionPane.showMessageDialog(this, "Usuario Registrado");
            } else {
                JOptionPane.showMessageDialog(this, "Error al registrar");
            }
        });

        btnCambiarClave.addActionListener(e -> {
            String correo = JOptionPane.showInputDialog("Ingrese el correo del usuario:");
            String nuevaClave = JOptionPane.showInputDialog("Ingrese la NUEVA contraseña:");
            if (correo != null && nuevaClave != null) {
                if (dao.cambiarClave(correo, nuevaClave)) {
                    JOptionPane.showMessageDialog(this, "Contraseña Restablecida");
                } else {
                    JOptionPane.showMessageDialog(this, "Error o correo no encontrado");
                }
            }
        });

        btnListar.addActionListener(e -> {
            List<Usuario> lista = dao.listarUsuarios();
            txtResultados.setText("");
            for (Usuario u : lista) {
                txtResultados.append(u.getId() + " - " + u.getNombre() + " " + u.getApellidos() + " | Rol: " + u.getIdRol() + "\n");
            }
        });
    }
}
