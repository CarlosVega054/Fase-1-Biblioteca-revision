package vista;

import dao.PrestamoDAO;
import excepciones.ErrorValidacion;
import modelo.Prestamo;
import utilidades.Sesion;

import javax.swing.*;
import java.util.Date;

public class PrestamosFrame extends JFrame {
    private JTextField txtIdUsuario, txtIdDocumento, txtDias;
    private JButton btnPrestar, btnDevolver;
    private PrestamoDAO dao;

    public PrestamosFrame() {
        dao = new PrestamoDAO();
        setTitle("Módulo de Préstamos y Devoluciones");
        setSize(400, 300);
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

        add(new JLabel("ID Documento:")).setBounds(30, 90, 100, 25);
        txtIdDocumento = new JTextField();
        txtIdDocumento.setBounds(140, 90, 150, 25);
        add(txtIdDocumento);

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

        btnPrestar.addActionListener(e -> {
            try {
                Prestamo p = new Prestamo();
                p.setIdUsuario(Integer.parseInt(txtIdUsuario.getText()));
                p.setIdDocumento(Integer.parseInt(txtIdDocumento.getText()));
                
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
