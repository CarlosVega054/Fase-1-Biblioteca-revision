package vista;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import utilidades.Sesion;

public class MainFrame extends JFrame {
    
    public MainFrame() {
        setTitle("Sistema de Biblioteca - Hola " + Sesion.usuarioLogueado.getNombre());
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        JMenuBar barra = new JMenuBar();
        
        JMenu menuArchivo = new JMenu("Archivo");
        JMenuItem itemSalir = new JMenuItem("Salir");
        itemSalir.addActionListener(e -> System.exit(0));
        menuArchivo.add(itemSalir);
        
        JMenu menuUsuarios = new JMenu("Usuarios");
        JMenuItem itemGestionUsuarios = new JMenuItem("Gestionar Usuarios");
        itemGestionUsuarios.addActionListener(e -> new GestionUsuariosFrame().setVisible(true));
        menuUsuarios.add(itemGestionUsuarios);

        JMenu menuLibros = new JMenu("Documentos");
        JMenuItem itemVerLibros = new JMenuItem("Ingreso y Consulta de Catálogo");
        itemVerLibros.addActionListener(e -> new GestionDocumentosFrame().setVisible(true));
        menuLibros.add(itemVerLibros);
        
        JMenu menuPrestamos = new JMenu("Préstamos");
        JMenuItem itemPrestar = new JMenuItem("Préstamos y Devoluciones");
        itemPrestar.addActionListener(e -> new PrestamosFrame().setVisible(true));
        menuPrestamos.add(itemPrestar);
        
        barra.add(menuArchivo);
        if (Sesion.usuarioLogueado.getIdRol() == 1) { // Solo el admin ve Gestion Usuarios
            barra.add(menuUsuarios);
        }
        barra.add(menuLibros);
        barra.add(menuPrestamos);
        
        // Si no es admin (rol 1), ocultar algunos menus de ejemplo
        if (Sesion.usuarioLogueado.getIdRol() != 1) {
            // Ejemplo: solo admin puede hacer prestamos o algo similar
            // Por simplicidad, todos ven el catalogo
        }
        
        setJMenuBar(barra);
        
        JLabel lblCentral = new JLabel("Bienvenido al sistema escolar de biblioteca", JLabel.CENTER);
        add(lblCentral);
    }
}
