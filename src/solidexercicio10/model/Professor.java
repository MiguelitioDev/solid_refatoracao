package solidexercicio10.model;

public class Professor extends Passageiro {
    public Professor(String n, int x, int y) {
        super(n, "Professor", x, y);
    }

    public int getPontuacao() {
        return 15;
    }

    public String getSimbolo() {
        return "P";
    }
}
