package vista;

import dao.ConfiguracionDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

// Ventana de configuracion del sistema - solo el admin puede acceder
// Tiene dos pestañas: una para la mora y otra para los limites de prestamo
public class ConfiguracionFrame extends JFrame {

    private ConfiguracionDAO configDAO;

    // Componentes de la pestaña de mora
    private JTable tablaMora;
    private DefaultTableModel modeloMora;
    private JTextField txtAnio, txtMoraDiaria;

    // Componentes de la pestaña de prestamos
    private JTable tablaPrestamos;
    private DefaultTableModel modeloPrestamos;
    private JComboBox<String> cbRol;
    private JTextField txtMaxEjemplares, txtMaxDias;

    public ConfiguracionFrame() {
        configDAO = new ConfiguracionDAO();

        setTitle("Configuración del Sistema - Administrador");
        setSize(700, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Las dos pestañas de configuracion
        JTabbedPane pestanas = new JTabbedPane();
        pestanas.addTab("Mora por Año", crearPanelMora());
        pestanas.addTab("Límite de Préstamos", crearPanelPrestamos());
        add(pestanas);

        // Cargar los datos que ya estan en la BD
        cargarDatosMora();
        cargarDatosPrestamos();
    }

    // ==============================================
    //  PESTAÑA 1: CONFIGURAR MORA POR AÑO
    //  Aqui el admin define cuanto se cobra por dia
    //  de retraso en cada año. Ej: en 2026 = $1.00
    // ==============================================
    private JPanel crearPanelMora() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Texto que explica para que sirve esta pestaña
        JLabel lblDescripcion = new JLabel(
            "<html><b>Configuración de Mora Diaria por Año</b><br>" +
            "Establezca la tarifa diaria de mora que se cobra por cada día de retraso en la devolución de documentos.</html>"
        );
        lblDescripcion.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        panel.add(lblDescripcion, BorderLayout.NORTH);

        // Tabla que muestra las moras ya guardadas
        modeloMora = new DefaultTableModel(new String[]{"Año", "Mora Diaria ($)"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaMora = new JTable(modeloMora);
        tablaMora.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        // Cuando selecciono una fila, se cargan los datos en los campos de abajo
        tablaMora.getSelectionModel().addListSelectionListener(e -> {
            int fila = tablaMora.getSelectedRow();
            if (fila >= 0) {
                txtAnio.setText(modeloMora.getValueAt(fila, 0).toString());
                txtMoraDiaria.setText(modeloMora.getValueAt(fila, 1).toString());
            }
        });
        JScrollPane scrollMora = new JScrollPane(tablaMora);
        panel.add(scrollMora, BorderLayout.CENTER);

        // Formulario para agregar o modificar la mora
        JPanel panelEntrada = new JPanel(new GridBagLayout());
        panelEntrada.setBorder(BorderFactory.createTitledBorder("Agregar / Modificar Mora"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        panelEntrada.add(new JLabel("Año:"), gbc);

        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 1.0;
        txtAnio = new JTextField(10);
        panelEntrada.add(txtAnio, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        panelEntrada.add(new JLabel("Mora diaria ($):"), gbc);

        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 1.0;
        txtMoraDiaria = new JTextField(10);
        panelEntrada.add(txtMoraDiaria, gbc);

        // Botones para guardar, eliminar o limpiar
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));

        JButton btnGuardar = new JButton("Guardar Mora");
        btnGuardar.addActionListener(e -> guardarMora());
        panelBotones.add(btnGuardar);

        JButton btnEliminar = new JButton("Eliminar Mora");
        btnEliminar.addActionListener(e -> eliminarMora());
        panelBotones.add(btnEliminar);

        JButton btnLimpiar = new JButton("Limpiar");
        btnLimpiar.addActionListener(e -> {
            txtAnio.setText("");
            txtMoraDiaria.setText("");
            tablaMora.clearSelection();
        });
        panelBotones.add(btnLimpiar);

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        panelEntrada.add(panelBotones, gbc);

        panel.add(panelEntrada, BorderLayout.SOUTH);
        return panel;
    }

    // Carga las moras desde la BD y las pone en la tabla
    private void cargarDatosMora() {
        modeloMora.setRowCount(0);
        List<String[]> moras = configDAO.listarMoras();
        for (String[] fila : moras) {
            modeloMora.addRow(fila);
        }
    }

    // Valida los datos y guarda la mora en la BD
    private void guardarMora() {
        try {
            int anio = Integer.parseInt(txtAnio.getText().trim());
            double mora = Double.parseDouble(txtMoraDiaria.getText().trim());

            if (anio < 2000 || anio > 2100) {
                JOptionPane.showMessageDialog(this, "Ingrese un año válido (2000-2100).", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (mora < 0) {
                JOptionPane.showMessageDialog(this, "La mora no puede ser negativa.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (configDAO.guardarMora(anio, mora)) {
                JOptionPane.showMessageDialog(this, "Mora guardada correctamente para el año " + anio + ".");
                cargarDatosMora();
                txtAnio.setText("");
                txtMoraDiaria.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo guardar la mora.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Ingrese valores numéricos válidos.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Elimina la mora del año seleccionado en la tabla
    private void eliminarMora() {
        int fila = tablaMora.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione un año de la tabla para eliminar.");
            return;
        }
        int anio = Integer.parseInt(modeloMora.getValueAt(fila, 0).toString());
        int confirmacion = JOptionPane.showConfirmDialog(this,
            "¿Está seguro de eliminar la configuración de mora para el año " + anio + "?",
            "Confirmar eliminación", JOptionPane.YES_NO_OPTION);

        if (confirmacion == JOptionPane.YES_OPTION) {
            if (configDAO.eliminarMora(anio)) {
                JOptionPane.showMessageDialog(this, "Mora eliminada correctamente.");
                cargarDatosMora();
                txtAnio.setText("");
                txtMoraDiaria.setText("");
            }
        }
    }

    // ======================================================
    //  PESTAÑA 2: CONFIGURAR CUANTOS EJEMPLARES SE PRESTAN
    //  Aqui se define el maximo de documentos y dias por rol
    // ======================================================
    private JPanel crearPanelPrestamos() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel lblDescripcion = new JLabel(
            "<html><b>Configuración de Límites de Préstamos por Rol</b><br>" +
            "Establezca cuántos ejemplares puede prestar cada tipo de usuario y por cuántos días máximo.</html>"
        );
        lblDescripcion.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        panel.add(lblDescripcion, BorderLayout.NORTH);

        // Tabla que muestra la configuracion actual por cada rol
        modeloPrestamos = new DefaultTableModel(
            new String[]{"Rol", "ID Rol", "Máx. Ejemplares", "Máx. Días"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaPrestamos = new JTable(modeloPrestamos);
        tablaPrestamos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        // Al seleccionar un rol en la tabla, se cargan sus datos para editarlos
        tablaPrestamos.getSelectionModel().addListSelectionListener(e -> {
            int fila = tablaPrestamos.getSelectedRow();
            if (fila >= 0) {
                String nombreRol = modeloPrestamos.getValueAt(fila, 0).toString();
                cbRol.setSelectedItem(nombreRol);
                txtMaxEjemplares.setText(modeloPrestamos.getValueAt(fila, 2).toString());
                txtMaxDias.setText(modeloPrestamos.getValueAt(fila, 3).toString());
            }
        });
        JScrollPane scrollPrestamos = new JScrollPane(tablaPrestamos);
        panel.add(scrollPrestamos, BorderLayout.CENTER);

        // Formulario para modificar los limites
        JPanel panelEntrada = new JPanel(new GridBagLayout());
        panelEntrada.setBorder(BorderFactory.createTitledBorder("Modificar Límites de Préstamo"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        panelEntrada.add(new JLabel("Rol:"), gbc);

        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 1.0;
        cbRol = new JComboBox<>(new String[]{"ADMINISTRADOR", "PROFESOR", "ALUMNO"});
        panelEntrada.add(cbRol, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        panelEntrada.add(new JLabel("Máx. ejemplares:"), gbc);

        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 1.0;
        txtMaxEjemplares = new JTextField(10);
        panelEntrada.add(txtMaxEjemplares, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0;
        panelEntrada.add(new JLabel("Máx. días:"), gbc);

        gbc.gridx = 1; gbc.gridy = 2; gbc.weightx = 1.0;
        txtMaxDias = new JTextField(10);
        panelEntrada.add(txtMaxDias, gbc);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));

        JButton btnGuardar = new JButton("Guardar Configuración");
        btnGuardar.addActionListener(e -> guardarConfigPrestamo());
        panelBotones.add(btnGuardar);

        JButton btnLimpiar = new JButton("Limpiar");
        btnLimpiar.addActionListener(e -> {
            cbRol.setSelectedIndex(0);
            txtMaxEjemplares.setText("");
            txtMaxDias.setText("");
            tablaPrestamos.clearSelection();
        });
        panelBotones.add(btnLimpiar);

        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        panelEntrada.add(panelBotones, gbc);

        panel.add(panelEntrada, BorderLayout.SOUTH);
        return panel;
    }

    // Carga la configuracion de prestamos desde la BD
    private void cargarDatosPrestamos() {
        modeloPrestamos.setRowCount(0);
        List<String[]> configs = configDAO.listarConfigPrestamos();
        for (String[] fila : configs) {
            modeloPrestamos.addRow(fila);
        }
    }

    // Guarda los limites de prestamo para el rol seleccionado
    private void guardarConfigPrestamo() {
        try {
            String rolSeleccionado = (String) cbRol.getSelectedItem();
            int idRol;
            switch (rolSeleccionado) {
                case "ADMINISTRADOR": idRol = 1; break;
                case "PROFESOR": idRol = 2; break;
                case "ALUMNO": idRol = 3; break;
                default: idRol = 3;
            }
            int maxEjemplares = Integer.parseInt(txtMaxEjemplares.getText().trim());
            int maxDias = Integer.parseInt(txtMaxDias.getText().trim());

            if (maxEjemplares < 1) {
                JOptionPane.showMessageDialog(this, "El máximo de ejemplares debe ser al menos 1.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (maxDias < 1) {
                JOptionPane.showMessageDialog(this, "El máximo de días debe ser al menos 1.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (configDAO.actualizarConfigPrestamo(idRol, maxEjemplares, maxDias)) {
                JOptionPane.showMessageDialog(this, "Configuración actualizada para: " + rolSeleccionado);
                cargarDatosPrestamos();
            } else {
                JOptionPane.showMessageDialog(this, "Error al guardar configuración.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Ingrese valores numéricos válidos.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
