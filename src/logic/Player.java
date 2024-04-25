package logic;

import javax.swing.ImageIcon;

import gfx.Ressources;

public class Player {

    private final String name;
    private ImageIcon icon;

    public Player(String name, ImageIcon icon) {
        this.name = name;
        this.setIcon(icon);
    }

    public String getName() {
        return name;
    }

    public ImageIcon getIcon() {
        return icon;
    }

    public String getIconString() {
        if (Ressources.icon_x == icon) {
            return "X";
        }
        if (Ressources.icon_o == icon) {
            return "O";
        }
        if (Ressources.icon_tessa_blue == icon) {
            return "tessa-blue";
        }
        if (Ressources.icon_tessa_red == icon) {
            return "tessa-red";
        }
        return "";
    }

    public void setIcon(ImageIcon icon) {
        this.icon = icon;
    }

    @Override
    public String toString() {
        return "[" + name + "]";
    }
}
