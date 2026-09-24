package solidexercicio10.repository;

import java.util.List;
import solidexercicio10.model.Dificuldade;

public interface RankingRepository {
    void salvar(String nome, int pontos);
    void salvar(String nome, int pontos, Dificuldade dificuldade, int passageiros, long tempo);
    List<RankingEntry> listar();
    void limpar();
}
