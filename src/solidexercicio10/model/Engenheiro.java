package solidexercicio10.model;

public class Engenheiro extends Passageiro {
    public Engenheiro(String n, int x, int y) {
        super(n, "Engenheiro", x, y);
    }

    public int getPontuacao() {
        return 20;
    }

    public String getSimbolo() {
        return "E";
    }
}
