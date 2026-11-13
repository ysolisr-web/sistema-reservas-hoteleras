package sistemareservashoteleras;

import javax.swing.SwingUtilities;
import vista.VentanaPrincipal;

public class SistemaReservasHoteleras {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new VentanaPrincipal().setVisible(true);
        });
    }
}
