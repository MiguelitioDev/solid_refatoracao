package solidexercicio10.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Missao {
    private final Nave nave;
    private final List<Passageiro> passageiros = new ArrayList<>();
    private final List<Asteroide> asteroides = new ArrayList<>();
    private final List<Inimigo> inimigos = new ArrayList<>();

    public Missao(Nave nave) {
        this.nave = nave;
    }

    public Nave getNave() {
        return nave;
    }

    public List<Passageiro> getPassageiros() {
        return passageiros;
    }

    public List<Asteroide> getAsteroides() {
        return asteroides;
    }

    public List<Inimigo> getInimigos() {
        return inimigos;
    }

    public void adicionarPassageiro(Passageiro p) {
        passageiros.add(p);
    }

    public void adicionarAsteroide(Asteroide a) {
        asteroides.add(a);
    }

    public void adicionarInimigo(Inimigo i) {
        inimigos.add(i);
    }

    public Passageiro passagemNaPosicao() {
        for (Passageiro p : passageiros) {
            if (mesmaPosicao(p, nave)) {
                return p;
            }
        }
        return null;
    }

    public boolean embarcarPassageiroNaPosicao() {
        Passageiro p = passagemNaPosicao();
        if (p == null || nave.getPassageiros().size() >= nave.getCapacidade()) {
            return false;
        }
        nave.embarcar(p);
        passageiros.remove(p);
        return true;
    }

    public void moverInimigos(Random r, int minX, int maxX, int minY, int maxY) {
        for (Inimigo i : inimigos) {
            int dx = r.nextInt(3) - 1;
            int dy = r.nextInt(3) - 1;
            if (i.getX() + dx >= minX && i.getX() + dx <= maxX && i.getY() + dy >= minY && i.getY() + dy <= maxY) {
                i.mover(dx, dy);
            }
        }
    }

    public boolean verificaColisao() {
        for (Asteroide a : asteroides) {
            if (mesmaPosicao(a, nave)) {
                return true;
            }
        }
        for (Inimigo i : inimigos) {
            if (mesmaPosicao(i, nave)) {
                return true;
            }
        }
        return false;
    }

    public boolean todosEmbarcados() {
        return passageiros.isEmpty();
    }

    private boolean mesmaPosicao(Posicionavel a, Posicionavel b) {
        return a.getX() == b.getX() && a.getY() == b.getY();
    }
}
