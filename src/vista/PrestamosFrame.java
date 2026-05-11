package vista;

import dao.PrestamoDAO;
import excepciones.ErrorValidacion;
import modelo.Prestamo;
import utilidades.Sesion;
import dao.DocumentoDAO;
import modelo.Documento;

import javax.swing.*;
import java.util.Date;

public class PrestamosFrame extends JFrame {
    private JTextField txtIdUsuario, txtDias;
    private JComboBox<String> cbDocumentos;
    private JButton btnPrestar, btnDevolver;
    private PrestamoDAO dao;

    public PrestamosFrame() {
        dao = new PrestamoDAO();
        setTitle("Módulo de Préstamos y Devoluciones");
        if (Sesion.usuarioLogueado.getIdRol() == 1) {
            setSize(950, 350);
        } else {
            setSize(400, 300);
        }
        setLocationRelativeTo(null);
        setLayout(null);

        JLabel lblTitulo = new JLabel("Préstamos de Documentos");
        lblTitulo.setBounds(120, 10, 200, 20);
        add(lblTitulo);

        add(new JLabel("ID Usuario:")).setBounds(30, 50, 100, 25);
        txtIdUsuario = new JTextField();
        txtIdUsuario.setBounds(140, 50, 150, 25);
        // Si el usuario no es admin, fijar su propio ID para que solo preste a su nombre
        if (Sesion.usuarioLogueado.getIdRol() != 1) {
            txtIdUsuario.setText(String.valueOf(Sesion.usuarioLogueado.getId()));
            txtIdUsuario.setEditable(false);
        }
        add(txtIdUsuario);

        add(new JLabel("Documento:")).setBounds(30, 90, 100, 25);
        cbDocumentos = new JComboBox<>();
        DocumentoDAO docDao = new DocumentoDAO();
        for (Documento d : docDao.listarLibros()) {
            cbDocumentos.addItem(d.getId() + " - " + d.getTitulo());
        }
        cbDocumentos.setBounds(140, 90, 200, 25);
        add(cbDocumentos);

        add(new JLabel("Días de préstamo:")).setBounds(30, 130, 120, 25);
        txtDias = new JTextField("7"); // 7 dias por defecto
        txtDias.setBounds(150, 130, 50, 25);
        add(txtDias);

        btnPrestar = new JButton("Realizar Préstamo");
        btnPrestar.setBounds(30, 180, 150, 30);
        add(btnPrestar);

        btnDevolver = new JButton("Registrar Devolución");
        btnDevolver.setBounds(190, 180, 160, 30);
        add(btnDevolver);

        if (Sesion.usuarioLogueado.getIdRol() == 1) {
            JLabel lblAdmin = new JLabel("Administración de Préstamos (Historial y Moras):");
            lblAdmin.setBounds(400, 10, 350, 20);
            add(lblAdmin);

            JTextArea txtPrestamos = new JTextArea();
            txtPrestamos.setEditable(false);
            JScrollPane scroll = new JScrollPane(txtPrestamos);
            scroll.setBounds(400, 40, 500, 200);
            add(scroll);

            JButton btnActualizar = new JButton("Actualizar Vista");
            btnActualizar.setBounds(400, 250, 150, 30);
            add(btnActualizar);

            btnActualizar.addActionListener(e -> {
                txtPrestamos.setText("");
                for(String s : dao.listarPrestamos()) {
                    txtPrestamos.append(s + "\n");
                }
            });

            // Carga inicial
            for(String s : dao.listarPrestamos()) {
                txtPrestamos.append(s + "\n");
            }
        }

        btnPrestar.addActionListener(e -> {
            try {
                Prestamo p = new Prestamo();
                p.setIdUsuario(Integer.parseInt(txtIdUsuario.getText()));
                
                String seleccionado = (String) cbDocumentos.getSelectedItem();
                int idDoc = Integer.parseInt(seleccionado.split(" - ")[0]);
                p.setIdDocumento(idDoc);
                
                Date hoy = new Date();
                p.setFechaPrestamo(hoy);
                
                int dias = Integer.parseInt(txtDias.getText());
                // Sumar dias a la fecha
                Date devolucion = new Date(hoy.getTime() + (dias * 24L * 60L * 60L * 1000L));
                p.setFechaDevolucion(devolucion);

                // Pasamos el ID del rol para la validacion del límite
                dao.registrarPrestamo(p, Sesion.usuarioLogueado.getIdRol());
                JOptionPane.showMessageDialog(this, "Préstamo exitoso. Fecha entrega: " + devolucion.toString());
            } catch (ErrorValidacion ev) {
                JOptionPane.showMessageDialog(this, ev.getMessage(), "Alerta de Validación", JOptionPane.WARNING_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Datos inválidos.");
            }
        });

        btnDevolver.addActionListener(e -> {
            if (Sesion.usuarioLogueado.getIdRol() != 1) {
                JOptionPane.showMessageDialog(this, "Solo los administradores pueden registrar devoluciones físicas.");
                return;
            }
            
            try {
                String inputPrestamo = JOptionPane.showInputDialog("ID del Préstamo a devolver:");
                String inputDoc = JOptionPane.showInputDialog("ID del Documento:");
                
                if(inputPrestamo != null && inputDoc != null) {
                    dao.registrarDevolucion(Integer.parseInt(inputPrestamo), Integer.parseInt(inputDoc));
                    JOptionPane.showMessageDialog(this, "Devolución registrada correctamente. Si hubo mora se aplicó.");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Datos inválidos.");
            }
        });
    }
}
