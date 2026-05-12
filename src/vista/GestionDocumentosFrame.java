package vista;

import dao.DocumentoDAO;
import modelo.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

// Ventana para ingresar nuevos ejemplares a la biblioteca
// Se puede registrar: Libros, Revistas, CDs y Tesis
// Los campos cambian segun el tipo que se seleccione
public class GestionDocumentosFrame extends JFrame {

    // Campos generales - estos son iguales para cualquier tipo de documento
    private JTextField txtCodigo, txtTitulo, txtAutor, txtUbicacion, txtTotal;
    private JTextField txtAnioPub, txtClasificacion;
    private JComboBox<String> cbTipo, cbEstado;

    // Panel que cambia sus campos segun el tipo de documento seleccionado
    private JPanel panelEspecifico;
    private CardLayout cardLayout;

    // Campos que solo aparecen cuando se selecciona LIBRO
    private JTextField txtIsbn, txtEditorial, txtEdicion, txtNumPaginas, txtMateria;
    private JComboBox<String> cbIdioma;

    // Campos que solo aparecen cuando se selecciona REVISTA
    private JTextField txtIssn, txtEdicionRev, txtVolumen, txtNumero, txtEditorialRev;
    private JComboBox<String> cbPeriodicidad;

    // Campos que solo aparecen cuando se selecciona CD
    private JTextField txtGenero, txtDuracion, txtContenido, txtSistemaReq;
    private JComboBox<String> cbFormato;

    // Campos que solo aparecen cuando se selecciona TESIS
    private JTextField txtCarrera, txtUniversidad, txtAsesor, txtFechaDefensa, txtNumPaginasTesis;
    private JComboBox<String> cbGrado;

    // Botones y tabla donde se muestran los resultados
    private JButton btnRegistrar, btnListar;
    private JTable tablaResultados;
    private DefaultTableModel modeloTabla;
    private DocumentoDAO dao;

    public GestionDocumentosFrame() {
        dao = new DocumentoDAO();
        setTitle("Gestión de Catálogo - Ingresar Nuevos Ejemplares");
        setSize(850, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(5, 5));

        // Parte de arriba: campos que son iguales para todos
        JPanel panelGeneral = crearPanelGeneral();
        add(panelGeneral, BorderLayout.NORTH);

        // Parte del medio: campos que cambian segun el tipo
        JPanel panelCentral = new JPanel(new BorderLayout());
        panelCentral.setBorder(BorderFactory.createTitledBorder("Campos Específicos según Tipo de Documento"));
        
        // Usamos CardLayout para cambiar entre los paneles de cada tipo
        cardLayout = new CardLayout();
        panelEspecifico = new JPanel(cardLayout);
        panelEspecifico.add(crearPanelLibro(), "LIBRO");
        panelEspecifico.add(crearPanelRevista(), "REVISTA");
        panelEspecifico.add(crearPanelCD(), "CD");
        panelEspecifico.add(crearPanelTesis(), "TESIS");
        panelCentral.add(panelEspecifico, BorderLayout.CENTER);

        // Botones de accion
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        btnRegistrar = new JButton("Guardar Nuevo Ejemplar");
        btnRegistrar.setFont(new Font("SansSerif", Font.BOLD, 13));
        btnRegistrar.addActionListener(e -> registrarDocumento());
        panelBotones.add(btnRegistrar);

        btnListar = new JButton("Consultar Catálogo");
        btnListar.addActionListener(e -> listarDocumentos());
        panelBotones.add(btnListar);

        JButton btnLimpiar = new JButton("Limpiar Campos");
        btnLimpiar.addActionListener(e -> limpiarCampos());
        panelBotones.add(btnLimpiar);

        panelCentral.add(panelBotones, BorderLayout.SOUTH);
        add(panelCentral, BorderLayout.CENTER);

        // Parte de abajo: tabla donde se ven los documentos registrados
        modeloTabla = new DefaultTableModel(
            new String[]{"ID", "Código", "Tipo", "Título", "Autor", "Disp.", "Total", "Estado", "Detalles"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tablaResultados = new JTable(modeloTabla);
        tablaResultados.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        JScrollPane scrollTabla = new JScrollPane(tablaResultados);
        scrollTabla.setPreferredSize(new Dimension(800, 180));
        scrollTabla.setBorder(BorderFactory.createTitledBorder("Catálogo de Documentos"));
        add(scrollTabla, BorderLayout.SOUTH);
    }

    // Crea el panel con los campos generales que aplican a todos los documentos
    private JPanel crearPanelGeneral() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Datos Generales del Documento"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 6, 4, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Fila 1: Tipo de material, Codigo, Estado
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Tipo Material:"), gbc);
        gbc.gridx = 1;
        String[] tipos = {"LIBRO", "REVISTA", "CD", "TESIS"};
        cbTipo = new JComboBox<>(tipos);
        // Cuando se cambia el tipo, se muestra el panel con los campos correspondientes
        cbTipo.addActionListener(e -> {
            cardLayout.show(panelEspecifico, (String) cbTipo.getSelectedItem());
        });
        panel.add(cbTipo, gbc);

        gbc.gridx = 2;
        panel.add(new JLabel("Código:"), gbc);
        gbc.gridx = 3; gbc.weightx = 1.0;
        txtCodigo = new JTextField(12);
        panel.add(txtCodigo, gbc);

        gbc.gridx = 4; gbc.weightx = 0;
        panel.add(new JLabel("Estado:"), gbc);
        gbc.gridx = 5;
        String[] estados = {"DISPONIBLE", "PRESTADO", "EN REPARACION", "RESERVADO"};
        cbEstado = new JComboBox<>(estados);
        panel.add(cbEstado, gbc);

        // Fila 2: Titulo y Cantidad
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        panel.add(new JLabel("Título:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 3; gbc.weightx = 1.0;
        txtTitulo = new JTextField(30);
        panel.add(txtTitulo, gbc);

        gbc.gridx = 4; gbc.gridwidth = 1; gbc.weightx = 0;
        panel.add(new JLabel("Cantidad:"), gbc);
        gbc.gridx = 5;
        txtTotal = new JTextField("1", 5);
        panel.add(txtTotal, gbc);

        // Fila 3: Autor y Año
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Autor:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 3; gbc.weightx = 1.0;
        txtAutor = new JTextField(30);
        panel.add(txtAutor, gbc);

        gbc.gridx = 4; gbc.gridwidth = 1; gbc.weightx = 0;
        panel.add(new JLabel("Año Pub.:"), gbc);
        gbc.gridx = 5;
        txtAnioPub = new JTextField(5);
        panel.add(txtAnioPub, gbc);

        // Fila 4: Ubicacion y Clasificacion Dewey
        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("Ubicación:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 3; gbc.weightx = 1.0;
        txtUbicacion = new JTextField(30);
        panel.add(txtUbicacion, gbc);

        gbc.gridx = 4; gbc.gridwidth = 1; gbc.weightx = 0;
        panel.add(new JLabel("Clasif. Dewey:"), gbc);
        gbc.gridx = 5;
        txtClasificacion = new JTextField(8);
        panel.add(txtClasificacion, gbc);

        return panel;
    }

    // Panel con los campos especificos de un LIBRO
    // ISBN, Editorial, Edicion, Paginas, Idioma, Materia
    private JPanel crearPanelLibro() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 6, 4, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("ISBN:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtIsbn = new JTextField(15);
        panel.add(txtIsbn, gbc);

        gbc.gridx = 2; gbc.weightx = 0;
        panel.add(new JLabel("Editorial:"), gbc);
        gbc.gridx = 3; gbc.weightx = 1.0;
        txtEditorial = new JTextField(15);
        panel.add(txtEditorial, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        panel.add(new JLabel("Edición:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtEdicion = new JTextField("1a edición", 15);
        panel.add(txtEdicion, gbc);

        gbc.gridx = 2; gbc.weightx = 0;
        panel.add(new JLabel("Num. Páginas:"), gbc);
        gbc.gridx = 3; gbc.weightx = 1.0;
        txtNumPaginas = new JTextField("0", 8);
        panel.add(txtNumPaginas, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0;
        panel.add(new JLabel("Idioma:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        cbIdioma = new JComboBox<>(new String[]{"Español", "Inglés", "Francés", "Portugués", "Alemán", "Otro"});
        panel.add(cbIdioma, gbc);

        gbc.gridx = 2; gbc.weightx = 0;
        panel.add(new JLabel("Materia:"), gbc);
        gbc.gridx = 3; gbc.weightx = 1.0;
        txtMateria = new JTextField(15);
        panel.add(txtMateria, gbc);

        return panel;
    }

    // Panel con los campos especificos de una REVISTA
    // ISSN, Edicion, Volumen, Numero, Periodicidad, Editorial
    private JPanel crearPanelRevista() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 6, 4, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("ISSN:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtIssn = new JTextField(15);
        panel.add(txtIssn, gbc);

        gbc.gridx = 2; gbc.weightx = 0;
        panel.add(new JLabel("Editorial:"), gbc);
        gbc.gridx = 3; gbc.weightx = 1.0;
        txtEditorialRev = new JTextField(15);
        panel.add(txtEditorialRev, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        panel.add(new JLabel("Edición:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtEdicionRev = new JTextField("1", 8);
        panel.add(txtEdicionRev, gbc);

        gbc.gridx = 2; gbc.weightx = 0;
        panel.add(new JLabel("Volumen:"), gbc);
        gbc.gridx = 3; gbc.weightx = 1.0;
        txtVolumen = new JTextField("1", 8);
        panel.add(txtVolumen, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0;
        panel.add(new JLabel("Número:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtNumero = new JTextField("1", 8);
        panel.add(txtNumero, gbc);

        gbc.gridx = 2; gbc.weightx = 0;
        panel.add(new JLabel("Periodicidad:"), gbc);
        gbc.gridx = 3; gbc.weightx = 1.0;
        cbPeriodicidad = new JComboBox<>(new String[]{
            "Diaria", "Semanal", "Quincenal", "Mensual", "Bimestral", 
            "Trimestral", "Semestral", "Anual", "Irregular"
        });
        cbPeriodicidad.setSelectedItem("Mensual");
        panel.add(cbPeriodicidad, gbc);

        return panel;
    }

    // Panel con los campos especificos de un CD
    // Genero, Duracion, Formato, Contenido, Sistema requerido
    private JPanel crearPanelCD() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 6, 4, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Género:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtGenero = new JTextField(15);
        panel.add(txtGenero, gbc);

        gbc.gridx = 2; gbc.weightx = 0;
        panel.add(new JLabel("Duración (min):"), gbc);
        gbc.gridx = 3; gbc.weightx = 1.0;
        txtDuracion = new JTextField("0", 8);
        panel.add(txtDuracion, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        panel.add(new JLabel("Formato:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        cbFormato = new JComboBox<>(new String[]{"CD-ROM", "DVD", "Blu-ray", "USB", "Digital"});
        panel.add(cbFormato, gbc);

        gbc.gridx = 2; gbc.weightx = 0;
        panel.add(new JLabel("Contenido:"), gbc);
        gbc.gridx = 3; gbc.weightx = 1.0;
        txtContenido = new JTextField("Educativo", 15);
        panel.add(txtContenido, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0;
        panel.add(new JLabel("Sistema requerido:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 3; gbc.weightx = 1.0;
        txtSistemaReq = new JTextField("Lector de CD/DVD", 30);
        panel.add(txtSistemaReq, gbc);

        return panel;
    }

    // Panel con los campos especificos de una TESIS
    // Carrera, Universidad, Grado, Asesor, Fecha defensa, Paginas
    private JPanel crearPanelTesis() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 6, 4, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Carrera:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtCarrera = new JTextField(15);
        panel.add(txtCarrera, gbc);

        gbc.gridx = 2; gbc.weightx = 0;
        panel.add(new JLabel("Universidad:"), gbc);
        gbc.gridx = 3; gbc.weightx = 1.0;
        txtUniversidad = new JTextField(15);
        panel.add(txtUniversidad, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        panel.add(new JLabel("Grado Académico:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        cbGrado = new JComboBox<>(new String[]{"Técnico", "Licenciatura", "Ingeniería", "Maestría", "Doctorado"});
        cbGrado.setSelectedItem("Licenciatura");
        panel.add(cbGrado, gbc);

        gbc.gridx = 2; gbc.weightx = 0;
        panel.add(new JLabel("Asesor/Tutor:"), gbc);
        gbc.gridx = 3; gbc.weightx = 1.0;
        txtAsesor = new JTextField(15);
        panel.add(txtAsesor, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0;
        panel.add(new JLabel("Fecha Defensa:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtFechaDefensa = new JTextField("YYYY-MM-DD", 12);
        panel.add(txtFechaDefensa, gbc);

        gbc.gridx = 2; gbc.weightx = 0;
        panel.add(new JLabel("Num. Páginas:"), gbc);
        gbc.gridx = 3; gbc.weightx = 1.0;
        txtNumPaginasTesis = new JTextField("0", 8);
        panel.add(txtNumPaginasTesis, gbc);

        return panel;
    }

    // Cuando el usuario da clic en "Guardar Nuevo Ejemplar"
    // Se lee el tipo seleccionado y se crea el objeto correspondiente
    private void registrarDocumento() {
        try {
            String tipoSeleccionado = (String) cbTipo.getSelectedItem();
            boolean exito = false;

            switch (tipoSeleccionado) {
                case "LIBRO":
                    Libro lib = new Libro();
                    llenarCamposGenerales(lib);
                    lib.setIsbn(txtIsbn.getText().trim());
                    lib.setEditorial(txtEditorial.getText().trim());
                    lib.setEdicion(txtEdicion.getText().trim());
                    lib.setNumPaginas(Integer.parseInt(txtNumPaginas.getText().trim()));
                    lib.setIdioma((String) cbIdioma.getSelectedItem());
                    lib.setMateria(txtMateria.getText().trim());
                    exito = dao.registrarLibro(lib);
                    break;

                case "REVISTA":
                    Revista rev = new Revista();
                    llenarCamposGenerales(rev);
                    rev.setIssn(txtIssn.getText().trim());
                    rev.setEdicion(Integer.parseInt(txtEdicionRev.getText().trim()));
                    rev.setVolumen(Integer.parseInt(txtVolumen.getText().trim()));
                    rev.setNumero(Integer.parseInt(txtNumero.getText().trim()));
                    rev.setPeriodicidad((String) cbPeriodicidad.getSelectedItem());
                    rev.setEditorial(txtEditorialRev.getText().trim());
                    exito = dao.registrarRevista(rev);
                    break;

                case "CD":
                    CD cd = new CD();
                    llenarCamposGenerales(cd);
                    cd.setGenero(txtGenero.getText().trim());
                    cd.setDuracion(Integer.parseInt(txtDuracion.getText().trim()));
                    cd.setFormato((String) cbFormato.getSelectedItem());
                    cd.setContenido(txtContenido.getText().trim());
                    cd.setSistemaRequerido(txtSistemaReq.getText().trim());
                    exito = dao.registrarCD(cd);
                    break;

                case "TESIS":
                    Tesis tesis = new Tesis();
                    llenarCamposGenerales(tesis);
                    tesis.setCarrera(txtCarrera.getText().trim());
                    tesis.setUniversidad(txtUniversidad.getText().trim());
                    tesis.setGradoAcademico((String) cbGrado.getSelectedItem());
                    tesis.setAsesor(txtAsesor.getText().trim());
                    String fecha = txtFechaDefensa.getText().trim();
                    tesis.setFechaDefensa(fecha.equals("YYYY-MM-DD") ? "" : fecha);
                    tesis.setNumPaginas(Integer.parseInt(txtNumPaginasTesis.getText().trim()));
                    exito = dao.registrarTesis(tesis);
                    break;
            }

            if (exito) {
                JOptionPane.showMessageDialog(this, tipoSeleccionado + " registrado correctamente en el catálogo.");
                listarDocumentos(); // Refrescar la tabla
            } else {
                JOptionPane.showMessageDialog(this, "Error al registrar. Verifique que el código no exista ya.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Por favor revise los campos numéricos (cantidad, año, páginas, etc.).",
                "Datos Inválidos", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error inesperado: " + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Toma los datos generales del formulario y los pone en el objeto documento
    private void llenarCamposGenerales(Documento doc) {
        doc.setCodigo(txtCodigo.getText().trim());
        doc.setTitulo(txtTitulo.getText().trim());
        doc.setAutor(txtAutor.getText().trim());
        doc.setUbicacion(txtUbicacion.getText().trim());
        int cantidad = Integer.parseInt(txtTotal.getText().trim());
        doc.setTotal(cantidad);
        doc.setDisponibles(cantidad); // Al registrar, todos estan disponibles
        doc.setAnioPublicacion(Integer.parseInt(txtAnioPub.getText().trim()));
        doc.setClasificacion(txtClasificacion.getText().trim());
        doc.setEstadoFisico(cbEstado.getSelectedItem().toString());
    }

    // Carga los documentos en la tabla segun el tipo que este seleccionado
    private void listarDocumentos() {
        modeloTabla.setRowCount(0);
        String tipoFiltro = (String) cbTipo.getSelectedItem();
        List<Documento> lista;

        // Filtra segun el tipo seleccionado en el combo
        switch (tipoFiltro) {
            case "LIBRO": lista = dao.listarLibros(); break;
            case "REVISTA": lista = dao.listarRevistas(); break;
            case "CD": lista = dao.listarCDs(); break;
            case "TESIS": lista = dao.listarTesis(); break;
            default: lista = dao.listarTodos();
        }

        for (Documento d : lista) {
            modeloTabla.addRow(new Object[]{
                d.getId(),
                d.getCodigo(),
                cbTipo.getSelectedItem(),
                d.getTitulo(),
                d.getAutor(),
                d.getDisponibles(),
                d.getTotal(),
                d.getEstadoFisico(),
                d.verDetalles()
            });
        }
    }

    // Limpia todos los campos del formulario para registrar otro documento
    private void limpiarCampos() {
        txtCodigo.setText("");
        txtTitulo.setText("");
        txtAutor.setText("");
        txtUbicacion.setText("");
        txtTotal.setText("1");
        txtAnioPub.setText("");
        txtClasificacion.setText("");
        // Libro
        txtIsbn.setText(""); txtEditorial.setText(""); txtEdicion.setText("1a edición");
        txtNumPaginas.setText("0"); txtMateria.setText("");
        // Revista
        txtIssn.setText(""); txtEdicionRev.setText("1"); txtVolumen.setText("1");
        txtNumero.setText("1"); txtEditorialRev.setText("");
        // CD
        txtGenero.setText(""); txtDuracion.setText("0"); txtContenido.setText("Educativo");
        txtSistemaReq.setText("Lector de CD/DVD");
        // Tesis
        txtCarrera.setText(""); txtUniversidad.setText(""); txtAsesor.setText("");
        txtFechaDefensa.setText("YYYY-MM-DD"); txtNumPaginasTesis.setText("0");
    }
}
