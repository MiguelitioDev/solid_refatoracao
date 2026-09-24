package solidexercicio10.presentation;

import solidexercicio10.model.Missao;

public class MapaRenderer {

    public void desenhar(Missao m, int pontos, String piloto, int minX, int maxX, int minY, int maxY) {
        System.out.printf("%nMapa | Pontos: %d | Piloto: %s%n", pontos, piloto);
        for (int y = maxY; y >= minY; y--) {
            System.out.printf("%3d|", y);
            for (int x = minX; x <= maxX; x++) {
                System.out.printf(" %2c", simbolo(m, x, y));
            }
            System.out.println();
        }
        System.out.println("Legenda: @=Nave, L=Plataforma, P=Professor, E=Engenheiro, T=Astronauta, #=Asteroide, X=Inimigo, .=Vazio");
        System.out.println("Comandos: w/s/a/d (mover), c (embarcar), q (sair)");
    }

    private char simbolo(Missao m, int x, int y) {
        if (m.getNave().getX() == x && m.getNave().getY() == y) {
            return '@';
        }
        if (x == 0 && y == 0) {
            return 'L';
        }
        for (var p : m.getPassageiros()) {
            if (p.getX() == x && p.getY() == y) {
                return p.getSimbolo().charAt(0);
            }
        }
        for (var a : m.getAsteroides()) {
            if (a.getX() == x && a.getY() == y) {
                return '#';
            }
        }
        for (var i : m.getInimigos()) {
            if (i.getX() == x && i.getY() == y) {
                return 'X';
            }
        }
        return '.';
    }
}
