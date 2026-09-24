package solidexercicio10.model;

public enum Dificuldade {
    FACIL,
    MEDIO,
    DIFICIL;

    public static Dificuldade deString(String valor) {
        if (valor == null) {
            return MEDIO;
        }
        return switch (valor.trim().toLowerCase()) {
            case "facil" -> FACIL;
            case "dificil" -> DIFICIL;
            default -> MEDIO;
        };
    }
}
