package solidexercicio10.repository;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import solidexercicio10.model.Dificuldade;

public class RankingService implements RankingRepository {
    private final Path arquivo;

    public RankingService(String nomeArquivo) {
        this.arquivo = Paths.get(nomeArquivo);
    }

    @Override
    public void salvar(String nome, int pontos) {
        salvar(nome, pontos, Dificuldade.MEDIO, 0, 0);
    }

    @Override
    public void salvar(String nome, int pontos, Dificuldade dificuldade, int passageiros, long tempo) {
        List<String> linhas = new ArrayList<>();
        if (Files.exists(arquivo)) {
            try {
                linhas = Files.readAllLines(arquivo, StandardCharsets.UTF_8);
            } catch (IOException ignored) {
            }
        }

        String data = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
        String registro = String.format("%s|%d|%s|%d|%s|%d", nome, pontos, dificuldade, passageiros, data, tempo);
        linhas.add(registro);

        try {
            Path parent = arquivo.getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }
            Files.write(arquivo, linhas, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new IllegalStateException("Falha ao salvar ranking no arquivo: " + arquivo, e);
        }
    }

    @Override
    public List<RankingEntry> listar() {
        List<RankingEntry> ranking = new ArrayList<>();
        if (!Files.exists(arquivo)) {
            return ranking;
        }

        try {
            for (String linha : Files.readAllLines(arquivo, StandardCharsets.UTF_8)) {
                String[] partes = linha.split("\\|", -1);
                if (partes.length < 6) {
                    continue;
                }
                try {
                    String nome = partes[0];
                    int pontos = Integer.parseInt(partes[1]);
                    Dificuldade dif = Dificuldade.deString(partes[2]);
                    int passageiros = Integer.parseInt(partes[3]);
                    String data = partes[4];
                    long tempo = Long.parseLong(partes[5]);

                    ranking.add(new RankingEntry(nome, pontos, dif, passageiros, data, tempo));
                } catch (NumberFormatException ignored) {
                }
            }
        } catch (IOException ignored) {
            return ranking;
        }

        ranking.sort(Comparator.comparingInt((RankingEntry e) -> e.score).reversed());
        return ranking;
    }

    @Override
    public void limpar() {
        try {
            Files.deleteIfExists(arquivo);
        } catch (IOException e) {
            throw new IllegalStateException("Falha ao limpar ranking: " + arquivo, e);
        }
    }
}
