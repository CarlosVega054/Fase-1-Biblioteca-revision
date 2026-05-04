package vista;

import dao.DocumentoDAO;
import modelo.Documento;
import modelo.Libro;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class GestionDocumentosFrame extends JFrame {
    private JTextField txtCodigo, txtTitulo, txtAutor, txtUbicacion, txtTotal;
    private JTextField txtIsbn, txtEditorial, txtAnioPub, txtClasificacion;
    private JComboBox<String> cbTipo, cbEstado;
    private JButton btnRegistrarLibro, btnListar;
    private JTextArea txtResultados;
    private DocumentoDAO dao;

    public GestionDocumentosFrame() {
        dao = new DocumentoDAO();
        setTitle("Gestión de Catálogo - Ingresar Ejemplares");
        setSize(550, 600);
        setLocationRelativeTo(null);
        setLayout(null);

        JLabel lblTitulo = new JLabel("Ingreso de Nuevos Ejemplares (Libros)");
        lblTitulo.setBounds(150, 10, 250, 20);
        add(lblTitulo);

        // Campos Generales de Documento
        add(new JLabel("Código:")).setBounds(20, 50, 80, 25);
        txtCodigo = new JTextField();
        txtCodigo.setBounds(100, 50, 150, 25);
        add(txtCodigo);

        add(new JLabel("Título:")).setBounds(270, 50, 80, 25);
        txtTitulo = new JTextField();
        txtTitulo.setBounds(330, 50, 180, 25);
        add(txtTitulo);

        add(new JLabel("Autor:")).setBounds(20, 90, 80, 25);
        txtAutor = new JTextField();
        txtAutor.setBounds(100, 90, 150, 25);
        add(txtAutor);

        add(new JLabel("Ubicación:")).setBounds(270, 90, 80, 25);
        txtUbicacion = new JTextField();
        txtUbicacion.setBounds(330, 90, 180, 25);
        add(txtUbicacion);

        add(new JLabel("Cantidad:")).setBounds(20, 130, 80, 25);
        txtTotal = new JTextField("1");
        txtTotal.setBounds(100, 130, 50, 25);
        add(txtTotal);

        add(new JLabel("Año Pub:")).setBounds(160, 130, 60, 25);
        txtAnioPub = new JTextField();
        txtAnioPub.setBounds(220, 130, 60, 25);
        add(txtAnioPub);

        add(new JLabel("Clasif (Dewey):")).setBounds(290, 130, 100, 25);
        txtClasificacion = new JTextField();
        txtClasificacion.setBounds(390, 130, 120, 25);
        add(txtClasificacion);

        add(new JLabel("Tipo Mat:")).setBounds(20, 170, 80, 25);
        String[] tipos = {"LIBRO", "REVISTA", "CD", "TESIS"};
        cbTipo = new JComboBox<>(tipos);
        cbTipo.setBounds(100, 170, 100, 25);
        add(cbTipo);

        add(new JLabel("Estado:")).setBounds(210, 170, 60, 25);
        String[] estados = {"DISPONIBLE", "PRESTADO", "EN REPARACION", "RESERVADO"};
        cbEstado = new JComboBox<>(estados);
        cbEstado.setBounds(270, 170, 130, 25);
        add(cbEstado);

        // Campos Específicos de Libro
        JLabel lblSub = new JLabel("Datos específicos (Si es Libro):");
        lblSub.setBounds(20, 210, 250, 20);
        add(lblSub);

        add(new JLabel("ISBN:")).setBounds(20, 240, 80, 25);
        txtIsbn = new JTextField();
        txtIsbn.setBounds(100, 240, 150, 25);
        add(txtIsbn);

        add(new JLabel("Editorial:")).setBounds(270, 240, 80, 25);
        txtEditorial = new JTextField();
        txtEditorial.setBounds(330, 240, 180, 25);
        add(txtEditorial);

        // Botones
        btnRegistrarLibro = new JButton("Guardar Ejemplar");
        btnRegistrarLibro.setBounds(20, 280, 150, 30);
        add(btnRegistrarLibro);

        btnListar = new JButton("Consultar Catálogo");
        btnListar.setBounds(180, 280, 180, 30);
        add(btnListar);

        txtResultados = new JTextArea();
        JScrollPane scroll = new JScrollPane(txtResultados);
        scroll.setBounds(20, 330, 490, 200);
        add(scroll);

        // Eventos
        btnRegistrarLibro.addActionListener(e -> {
            try {
                Libro lib = new Libro();
                lib.setCodigo(txtCodigo.getText());
                lib.setTitulo(txtTitulo.getText());
                lib.setAutor(txtAutor.getText());
                lib.setUbicacion(txtUbicacion.getText());
                int cantidad = Integer.parseInt(txtTotal.getText());
                lib.setTotal(cantidad);
                lib.setDisponibles(cantidad); // Inician todos disponibles
                lib.setAnioPublicacion(Integer.parseInt(txtAnioPub.getText()));
                lib.setClasificacion(txtClasificacion.getText());
                lib.setEstadoFisico(cbEstado.getSelectedItem().toString());
                
                lib.setIsbn(txtIsbn.getText());
                lib.setEditorial(txtEditorial.getText());

                if (dao.registrarLibro(lib)) {
                    JOptionPane.showMessageDialog(this, "Libro Registrado Correctamente");
                } else {
                    JOptionPane.showMessageDialog(this, "Error al registrar (Revise si el código ya existe)");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Por favor revise los campos numéricos.");
            }
        });

        btnListar.addActionListener(e -> {
            List<Documento> lista = dao.listarLibros();
            txtResultados.setText("");
            for (Documento d : lista) {
                txtResultados.append("ID: " + d.getId() + " | " + d.getCodigo() + " | " + d.getTitulo() + 
                        " | Disp: " + d.getDisponibles() + "/" + d.getTotal() + " | Estado: " + d.getEstadoFisico() + "\n");
                txtResultados.append("    -> " + d.verDetalles() + " | Dewey: " + d.getClasificacion() + " | Ubicación: " + d.getUbicacion() + "\n\n");
            }
        });
    }
}
