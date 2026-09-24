package solidexercicio10.service;

import java.util.Random;
import java.util.Scanner;
import solidexercicio10.model.*;
import solidexercicio10.presentation.MapaRenderer;
import solidexercicio10.repository.RankingEntry;
import solidexercicio10.repository.RankingRepository;

public class JogoService {
    private final RankingRepository ranking;
    private final MapaRenderer renderer = new MapaRenderer();
    private final Random random = new Random();

    public JogoService(RankingRepository ranking) {
        this.ranking = ranking;
    }

    public void executarLoop(Scanner s) {
        boolean ativo = true;
        while (ativo) {
            System.out.println("\n--- MENU PRINCIPAL ---");
            System.out.println("1. Nova missao");
            System.out.println("2. Ranking Top Pontuacoes");
            System.out.println("3. Limpar ranking");
            System.out.println("4. Sair");
            System.out.println("----------------------");
            
            String op = ler(s, "Escolha uma opcao: ", "4");
            switch (op) {
                case "1" -> jogar(s);
                case "2" -> exibirRanking();
                case "3" -> {
                    ranking.limpar();
                    System.out.println("Historico de ranking limpo com sucesso.");
                }
                case "4" -> {
                    ativo = false;
                    System.out.println("\nObrigado por jogar a Missao Marte Unifor!");
                }
                default -> System.out.println("Opcao invalida. Tente novamente.");
            }
        }
    }

    private void jogar(Scanner s) {
        String nome = ler(s, "\nDigite o nome do piloto: ", "Piloto");
        Dificuldade dificuldade = Dificuldade.deString(ler(s, "Dificuldade (facil, medio, dificil): ", "medio"));
        
        int tamanho;
        try {
            tamanho = Integer.parseInt(ler(s, "Tamanho do raio do mapa (padrao 5): ", "5"));
        } catch (NumberFormatException e) {
            tamanho = 5;
        }
        tamanho = Math.max(1, tamanho);

        int min = -tamanho;
        int max = tamanho;

        int totalPassageiros = switch (dificuldade) {
            case FACIL -> 4;
            case DIFICIL -> 6;
            default -> 5;
        };

        Nave nave = new Nave("A-1", 0, 0, totalPassageiros);
        Missao missao = new Missao(nave);

        colocar(missao, totalPassageiros, min, max, true, nave);
        colocar(missao, dificuldade == Dificuldade.DIFICIL ? 3 : 2, min, max, false, nave);
        colocarPerigos(missao, dificuldade == Dificuldade.DIFICIL ? 3 : 2, min, max, nave);

        int pontos = switch (dificuldade) {
            case FACIL -> 30;
            case DIFICIL -> 15;
            default -> 20;
        };

        int movimentos = 0;
        long inicio = System.currentTimeMillis();
        ler(s, "\nPressione Enter para decolar!", "");

        while (true) {
            renderer.desenhar(missao, pontos, nome, min, max, min, max);
            System.out.printf("Vidas: %d | A bordo: %d/%d | Restantes no mapa: %d%n",
                    nave.getVidas(),
                    nave.getPassageiros().size(),
                    nave.getCapacidade(),
                    missao.getPassageiros().size());

            char c = ler(s, "Comando (w/s/a/d/c/q): ", "q").toLowerCase().charAt(0);
            if (c == 'q') {
                System.out.println("Missao abortada pelo piloto.");
                return;
            }

            if (c == 'c') {
                Passageiro p = missao.passagemNaPosicao();
                if (p != null && missao.embarcarPassageiroNaPosicao()) {
                    pontos += p.getPontuacao();
                    System.out.printf("Passageiro %s resgatado! (+%d pontos)%n", p.getNome(), p.getPontuacao());
                } else {
                    System.out.println("Nenhum passageiro nesta coordenada ou nave lotada!");
                }
            } else if ("wsad".indexOf(c) >= 0) {
                nave.moverComLimites(c, min, max, min, max);
                pontos--;
                movimentos++;
            }

            missao.moverInimigos(random, min, max, min, max);

            if (missao.verificaColisao()) {
                nave.perderVida();
                System.out.println("ALERTA: Colisao detectada! Perdeu 1 vida!");
            }

            if (pontos <= 0 || nave.getVidas() == 0) {
                System.out.println("\nFIM DE JOGO: A nave foi destruida ou os pontos acabaram!");
                return;
            }

            if (missao.todosEmbarcados() && nave.getX() == 0 && nave.getY() == 0) {
                long tempo = (System.currentTimeMillis() - inicio) / 1000;
                System.out.println("\n================================================================");
                System.out.println("                 MISSAO CONCLUIDA COM SUCESSO!                  ");
                System.out.println("================================================================");
                System.out.printf("Piloto: %s | Pontos: %d | Movimentos: %d | Tempo: %ds%n",
                        nome, pontos, movimentos, tempo);
                ranking.salvar(nome, pontos, dificuldade, nave.getPassageiros().size(), tempo);
                return;
            }
        }
    }

    private void colocar(Missao m, int quantidade, int min, int max, boolean passageiro, Nave n) {
        for (int i = 0; i < quantidade; i++) {
            int[] p = livre(m, min, max, n);
            if (passageiro) {
                if (i % 3 == 0) {
                    m.adicionarPassageiro(new Professor("Prof-" + (i + 1), p[0], p[1]));
                } else if (i % 3 == 1) {
                    m.adicionarPassageiro(new Engenheiro("Eng-" + (i + 1), p[0], p[1]));
                } else {
                    m.adicionarPassageiro(new Astronauta("Astro-" + (i + 1), p[0], p[1]));
                }
            } else {
                m.adicionarAsteroide(new Asteroide(p[0], p[1]));
            }
        }
    }

    private void colocarPerigos(Missao m, int quantidade, int min, int max, Nave n) {
        for (int i = 0; i < quantidade; i++) {
            int[] p = livre(m, min, max, n);
            m.adicionarInimigo(new Inimigo(p[0], p[1]));
        }
    }

    private int[] livre(Missao m, int min, int max, Nave n) {
        int x;
        int y;
        do {
            x = random.nextInt(max - min + 1) + min;
            y = random.nextInt(max - min + 1) + min;
        } while ((x == 0 && y == 0) || ocupado(m, x, y));
        return new int[] { x, y };
    }

    private boolean ocupado(Missao m, int x, int y) {
        for (Passageiro p : m.getPassageiros()) {
            if (p.getX() == x && p.getY() == y) return true;
        }
        for (Asteroide a : m.getAsteroides()) {
            if (a.getX() == x && a.getY() == y) return true;
        }
        for (Inimigo i : m.getInimigos()) {
            if (i.getX() == x && i.getY() == y) return true;
        }
        return false;
    }

    private void exibirRanking() {
        System.out.println("\n====== RANKING TOP PONTUACOES ======");
        var lista = ranking.listar();
        if (lista.isEmpty()) {
            System.out.println("Nenhum registro encontrado no ranking.");
            return;
        }
        int pos = 1;
        for (RankingEntry e : lista) {
            System.out.printf("%2d. %-15s | Pontos: %4d | Nivel: %-7s | Resgates: %d | Data: %s | Tempo: %ds%n",
                    pos++, e.name, e.score, e.dificuldade, e.passageirosColetados, e.dataHora, e.tempoJogo);
        }
    }

    private String ler(Scanner s, String mensagem, String padrao) {
        System.out.print(mensagem);
        if (!s.hasNextLine()) {
            return padrao;
        }
        String valor = s.nextLine();
        return valor.isBlank() ? padrao : valor;
    }
}
