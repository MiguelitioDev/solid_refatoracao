package solidexercicio10.repository;

import solidexercicio10.model.Dificuldade;

public class RankingEntry {
    public final String name;
    public final int score;
    public final Dificuldade dificuldade;
    public final int passageirosColetados;
    public final String dataHora;
    public final long tempoJogo;

    public RankingEntry(String name, int score, Dificuldade dificuldade, int passageiros, String data, long tempo) {
        this.name = name;
        this.score = score;
        this.dificuldade = dificuldade;
        this.passageirosColetados = passageiros;
        this.dataHora = data;
        this.tempoJogo = tempo;
    }
}
