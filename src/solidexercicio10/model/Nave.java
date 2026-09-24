package solidexercicio10.model;

import java.util.ArrayList;
import java.util.List;

public class Nave extends EntidadeMapa implements Movel {
    private final String nome;
    private final List<Passageiro> passageiros = new ArrayList<>();
    private final int capacidade;
    private int vidas = 3;

    public Nave(String nome, int x, int y, int capacidade) {
        super(x, y);
        this.nome = nome;
        this.capacidade = capacidade;
    }

    public String getNome() {
        return nome;
    }

    public int getVidas() {
        return vidas;
    }

    public int getCapacidade() {
        return capacidade;
    }

    public List<Passageiro> getPassageiros() {
        return passageiros;
    }

    public void embarcar(Passageiro p) {
        if (passageiros.size() < capacidade) {
            passageiros.add(p);
        }
    }

    public void perderVida() {
        vidas = Math.max(0, vidas - 1);
    }

    @Override
    public void mover(int dx, int dy) {
        this.x += dx;
        this.y += dy;
    }

    @Override
    public String getSimbolo() {
        return "@";
    }

    public void moverComLimites(char comando, int minX, int maxX, int minY, int maxY) {
        int dx = 0;
        int dy = 0;
        switch (comando) {
            case 'w' -> dy = 1;
            case 's' -> dy = -1;
            case 'a' -> dx = -1;
            case 'd' -> dx = 1;
            default -> { }
        }
        int novoX = x + dx;
        int novoY = y + dy;
        if (novoX >= minX && novoX <= maxX && novoY >= minY && novoY <= maxY) {
            this.x = novoX;
            this.y = novoY;
        }
    }
}
