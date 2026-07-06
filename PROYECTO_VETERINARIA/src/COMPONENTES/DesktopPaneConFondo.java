package COMPONENTES;

import javax.swing.JDesktopPane;
import javax.swing.ImageIcon;
import java.awt.Graphics;
import java.awt.Image;

public class DesktopPaneConFondo extends JDesktopPane {

    private Image imagenFondo;

    public DesktopPaneConFondo() {
        ImageIcon icon = new ImageIcon(getClass().getResource("/IMAGENES/fondo.jpg"));
        imagenFondo = icon.getImage();
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (imagenFondo != null) {
            g.drawImage(imagenFondo, 0, 0, getWidth(), getHeight(), this);
        }
    }
}