package solidexercicio10.model;

public class Inimigo extends EntidadeMapa implements Movel {
    public Inimigo(int x, int y) {
        super(x, y);
    }

    public void mover(int dx, int dy) {
        x += dx;
        y += dy;
    }

    public String getSimbolo() {
        return "X";
    }
}
