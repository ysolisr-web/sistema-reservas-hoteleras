package vista;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import modelo.Habitacion;
import modelo.Huesped;
import modelo.Reserva;
import negocio.GestorHotel;

public class VentanaPrincipal extends JFrame {

    private final GestorHotel gestor;

    // Modelos de tablas
    private DefaultTableModel modeloHuespedes;
    private DefaultTableModel modeloHabitaciones;
    private DefaultTableModel modeloReservas;
    private DefaultTableModel modeloCola;

    // Campos Huéspedes
    private JTextField txtDocHuesped, txtNombreHuesped, txtTelHuesped;

    // Campos Habitaciones
    private JTextField txtNumHab, txtTarifaHab;
    private JComboBox<String> cmbTipoHab;

    // Campos Reservas
    private JTextField txtCodReserva, txtDocResHuesped, txtNumResHab, txtFechaReserva;
    private JComboBox<String> cmbPrioridad;

    public VentanaPrincipal() {
        this.gestor = new GestorHotel();
        configurarVentana();
        inicializarComponentes();
        actualizarTodasLasTablas();
    }

    private void configurarVentana() {
        setTitle("Sistema de Reservas Hoteleras");
        setSize(920, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
    }

    private void inicializarComponentes() {
        JTabbedPane pestanas = new JTabbedPane();
        pestanas.addTab("Reservas y Cola (ArrayList / PriorityQueue)", crearPanelReservas());
        pestanas.addTab("Habitaciones (HashSet / TreeSet)", crearPanelHabitaciones());
        pestanas.addTab("Huéspedes (HashMap)", crearPanelHuespedes());
        add(pestanas, BorderLayout.CENTER);
    }

    // ==================== PESTAÑA 1: RESERVAS Y COLA ====================
    private JPanel crearPanelReservas() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Formulario superior
        JPanel form = new JPanel(new GridLayout(2, 6, 8, 8));
        form.setBorder(BorderFactory.createTitledBorder("Nueva Reserva"));

        txtCodReserva = new JTextField();
        txtDocResHuesped = new JTextField();
        txtNumResHab = new JTextField();
        txtFechaReserva = new JTextField("2026-11-20");
        cmbPrioridad = new JComboBox<>(new String[]{"NORMAL", "ALTA", "VIP"});
        JButton btnCrearReserva = new JButton("Registrar Reserva");

        form.add(new JLabel("Código:"));
        form.add(txtCodReserva);
        form.add(new JLabel("Doc. Huésped:"));
        form.add(txtDocResHuesped);
        form.add(new JLabel("N° Habitación:"));
        form.add(txtNumResHab);

        form.add(new JLabel("Fecha (YYYY-MM-DD):"));
        form.add(txtFechaReserva);
        form.add(new JLabel("Prioridad:"));
        form.add(cmbPrioridad);
        form.add(new JLabel(""));
        form.add(btnCrearReserva);

        panel.add(form, BorderLayout.NORTH);

        // Tablas centrales (Historial arriba, Cola de Prioridad abajo)
        JPanel centro = new JPanel(new GridLayout(2, 1, 10, 10));

        modeloReservas = new DefaultTableModel(
                new String[]{"Código", "Fecha", "Prioridad", "Estado", "Huésped", "Habitación"}, 0);
        JTable tablaReservas = new JTable(modeloReservas);
        JScrollPane scrollHistorial = new JScrollPane(tablaReservas);
        scrollHistorial.setBorder(BorderFactory.createTitledBorder("Historial General (ArrayList)"));

        modeloCola = new DefaultTableModel(
                new String[]{"Orden", "Código", "Fecha", "Prioridad", "Huésped", "Habitación"}, 0);
        JTable tablaCola = new JTable(modeloCola);
        JScrollPane scrollCola = new JScrollPane(tablaCola);
        scrollCola.setBorder(BorderFactory.createTitledBorder("Cola de Check-In Pendiente (PriorityQueue por Fecha)"));

        centro.add(scrollHistorial);
        centro.add(scrollCola);
        panel.add(centro, BorderLayout.CENTER);

        // Botones inferiores
        JPanel acciones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnProcesar = new JButton("Atender Siguiente en Cola (Poll)");
        JButton btnCancelar = new JButton("Cancelar Reserva Seleccionada");
        acciones.add(btnProcesar);
        acciones.add(btnCancelar);
        panel.add(acciones, BorderLayout.SOUTH);

        // Eventos
        btnCrearReserva.addActionListener(e -> registrarReservaUI());
        btnProcesar.addActionListener(e -> procesarSiguienteUI());
        btnCancelar.addActionListener(e -> cancelarReservaUI(tablaReservas));

        return panel;
    }

    // ==================== PESTAÑA 2: HABITACIONES ====================
    private JPanel crearPanelHabitaciones() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel form = new JPanel(new GridLayout(1, 7, 8, 8));
        form.setBorder(BorderFactory.createTitledBorder("Registrar Habitación"));

        txtNumHab = new JTextField();
        cmbTipoHab = new JComboBox<>(new String[]{"Sencilla", "Doble", "Suite"});
        txtTarifaHab = new JTextField();
        JButton btnAgregarHab = new JButton("Agregar");
        JButton btnVerDisponibles = new JButton("Solo Disponibles (TreeSet)");
        JButton btnVerTodas = new JButton("Ver Todas (HashSet)");

        form.add(new JLabel("Número:"));
        form.add(txtNumHab);
        form.add(new JLabel("Tipo:"));
        form.add(cmbTipoHab);
        form.add(new JLabel("Tarifa:"));
        form.add(txtTarifaHab);
        form.add(btnAgregarHab);

        panel.add(form, BorderLayout.NORTH);

        modeloHabitaciones = new DefaultTableModel(
                new String[]{"Número", "Tipo", "Tarifa / Noche", "Estado"}, 0);
        JTable tablaHabitaciones = new JTable(modeloHabitaciones);
        panel.add(new JScrollPane(tablaHabitaciones), BorderLayout.CENTER);

        JPanel filtros = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        filtros.add(btnVerDisponibles);
        filtros.add(btnVerTodas);
        panel.add(filtros, BorderLayout.SOUTH);

        btnAgregarHab.addActionListener(e -> registrarHabitacionUI());
        btnVerDisponibles.addActionListener(e -> cargarTablaHabitaciones(true));
        btnVerTodas.addActionListener(e -> cargarTablaHabitaciones(false));

        return panel;
    }

    // ==================== PESTAÑA 3: HUÉSPEDES ====================
    private JPanel crearPanelHuespedes() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel form = new JPanel(new GridLayout(1, 7, 8, 8));
        form.setBorder(BorderFactory.createTitledBorder("Registrar Huésped"));

        txtDocHuesped = new JTextField();
        txtNombreHuesped = new JTextField();
        txtTelHuesped = new JTextField();
        JButton btnRegistrarHuesped = new JButton("Guardar Huésped");

        form.add(new JLabel("Documento:"));
        form.add(txtDocHuesped);
        form.add(new JLabel("Nombre:"));
        form.add(txtNombreHuesped);
        form.add(new JLabel("Teléfono:"));
        form.add(txtTelHuesped);
        form.add(btnRegistrarHuesped);

        panel.add(form, BorderLayout.NORTH);

        modeloHuespedes = new DefaultTableModel(
                new String[]{"Documento", "Nombre", "Teléfono"}, 0);
        JTable tablaHuespedes = new JTable(modeloHuespedes);
        panel.add(new JScrollPane(tablaHuespedes), BorderLayout.CENTER);

        JPanel barraBuscar = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnBuscarHuesped = new JButton("Buscar por Documento (HashMap)");
        barraBuscar.add(btnBuscarHuesped);
        panel.add(barraBuscar, BorderLayout.SOUTH);

        btnRegistrarHuesped.addActionListener(e -> registrarHuespedUI());
        btnBuscarHuesped.addActionListener(e -> buscarHuespedUI());

        return panel;
    }

    // ==================== ACCIONES Y ACTUALIZACIÓN ====================
    private void registrarReservaUI() {
        String codigo = txtCodReserva.getText().trim();
        String doc = txtDocResHuesped.getText().trim();
        String numStr = txtNumResHab.getText().trim();
        String fecha = txtFechaReserva.getText().trim();
        String prioridad = (String) cmbPrioridad.getSelectedItem();

        if (codigo.isEmpty() || doc.isEmpty() || numStr.isEmpty() || fecha.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Completa todos los campos de la reserva.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int numHab = Integer.parseInt(numStr);
            String resultado = gestor.crearReserva(codigo, doc, numHab, fecha, prioridad);
            if ("OK".equals(resultado)) {
                txtCodReserva.setText("");
                txtNumResHab.setText("");
                actualizarTodasLasTablas();
                JOptionPane.showMessageDialog(this, "Reserva registrada exitosamente.");
            } else {
                JOptionPane.showMessageDialog(this, resultado, "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "El número de habitación debe ser numérico.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void procesarSiguienteUI() {
        Reserva atendida = gestor.procesarSiguienteReserva();
        if (atendida == null) {
            JOptionPane.showMessageDialog(this, "No hay reservas pendientes en la cola.");
        } else {
            actualizarTodasLasTablas();
            JOptionPane.showMessageDialog(this, "Reserva confirmada:\n" + atendida);
        }
    }

    private void cancelarReservaUI(JTable tablaReservas) {
        int fila = tablaReservas.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona una reserva en la tabla de historial.");
            return;
        }
        String codigo = modeloReservas.getValueAt(fila, 0).toString();
        if (gestor.cancelarReserva(codigo)) {
            actualizarTodasLasTablas();
            JOptionPane.showMessageDialog(this, "Reserva " + codigo + " cancelada y habitación liberada.");
        } else {
            JOptionPane.showMessageDialog(this, "La reserva ya estaba cancelada.");
        }
    }

    private void registrarHabitacionUI() {
        try {
            int numero = Integer.parseInt(txtNumHab.getText().trim());
            String tipo = (String) cmbTipoHab.getSelectedItem();
            double tarifa = Double.parseDouble(txtTarifaHab.getText().trim());

            if (gestor.registrarHabitacion(new Habitacion(numero, tipo, tarifa, true))) {
                txtNumHab.setText("");
                txtTarifaHab.setText("");
                actualizarTodasLasTablas();
                JOptionPane.showMessageDialog(this, "Habitación registrada.");
            } else {
                JOptionPane.showMessageDialog(this, "Ya existe una habitación con ese número.", "Duplicado", JOptionPane.WARNING_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Número y tarifa deben ser valores numéricos.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void registrarHuespedUI() {
        String doc = txtDocHuesped.getText().trim();
        String nombre = txtNombreHuesped.getText().trim();
        String tel = txtTelHuesped.getText().trim();

        if (doc.isEmpty() || nombre.isEmpty() || tel.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Completa todos los datos del huésped.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (gestor.registrarHuesped(new Huesped(doc, nombre, tel))) {
            txtDocHuesped.setText("");
            txtNombreHuesped.setText("");
            txtTelHuesped.setText("");
            actualizarTodasLasTablas();
            JOptionPane.showMessageDialog(this, "Huésped guardado.");
        } else {
            JOptionPane.showMessageDialog(this, "Ya existe un huésped con ese documento.", "Duplicado", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void buscarHuespedUI() {
        String doc = JOptionPane.showInputDialog(this, "Ingresa el documento a buscar:");
        if (doc != null && !doc.trim().isEmpty()) {
            Huesped h = gestor.buscarHuesped(doc.trim());
            if (h != null) {
                JOptionPane.showMessageDialog(this, "Huésped encontrado:\n" + h);
            } else {
                JOptionPane.showMessageDialog(this, "No se encontró ningún huésped con documento " + doc, "Sin resultados", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    private void actualizarTodasLasTablas() {
        // Huéspedes
        modeloHuespedes.setRowCount(0);
        for (Huesped h : gestor.listarHuespedes()) {
            modeloHuespedes.addRow(new Object[]{h.getDocumento(), h.getNombre(), h.getTelefono()});
        }

        // Habitaciones (por defecto disponibles ordenadas por precio en TreeSet)
        cargarTablaHabitaciones(true);

        // Historial de Reservas
        modeloReservas.setRowCount(0);
        for (Reserva r : gestor.listarHistorialReservas()) {
            modeloReservas.addRow(new Object[]{
                r.getCodigo(), r.getFecha(), r.getPrioridad(), r.getEstado(),
                r.getHuesped().getNombre(), "Hab #" + r.getHabitacion().getNumero()
            });
        }

        // Cola de Prioridad
        modeloCola.setRowCount(0);
        int turno = 1;
        for (Reserva r : gestor.listarColaPrioridad()) {
            modeloCola.addRow(new Object[]{
                turno++, r.getCodigo(), r.getFecha(), r.getPrioridad(),
                r.getHuesped().getNombre(), "Hab #" + r.getHabitacion().getNumero()
            });
        }
    }

    private void cargarTablaHabitaciones(boolean soloDisponibles) {
        modeloHabitaciones.setRowCount(0);
        for (Habitacion h : (soloDisponibles ? gestor.listarDisponiblesPorPrecio() : gestor.listarTodasHabitaciones())) {
            modeloHabitaciones.addRow(new Object[]{
                h.getNumero(), h.getTipo(), "$" + h.getTarifaPorNoche(),
                h.isDisponible() ? "Disponible" : "Ocupada"
            });
        }
    }
}
