package solidexercicio10.model;

public class Astronauta extends Passageiro {
    public Astronauta(String n, int x, int y) {
        super(n, "Astronauta", x, y);
    }

    public int getPontuacao() {
        return 10;
    }

    public String getSimbolo() {
        return "T";
    }
}
