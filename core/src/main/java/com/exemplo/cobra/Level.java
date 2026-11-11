package com.exemplo.cobra;

public enum Level {
    //        nome       passo  fome  obstáculos  multiplicador
    FACIL("Fácil",       0.14f, 3.0f, 0,  1),
    MEDIO("Médio",       0.11f, 2.5f, 6,  2),
    DIFICIL("Difícil",   0.08f, 2.0f, 12, 3);

    public final String nome;
    public final float stepTime;    // segundos entre movimentos
    public final float shrinkTime;  // segundos até perder um segmento
    public final int obstacles;
    public final int multiplier;

    Level(String nome, float stepTime, float shrinkTime, int obstacles, int multiplier) {
        this.nome = nome;
        this.stepTime = stepTime;
        this.shrinkTime = shrinkTime;
        this.obstacles = obstacles;
        this.multiplier = multiplier;
    }
}
