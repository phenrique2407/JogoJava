package com.exemplo.cobra;

import com.badlogic.gdx.math.MathUtils;

/** Regra sorteada a cada partida. */
public enum Constraint {
    SEM_PAREDES("Sem paredes", "As bordas se conectam: saia por um lado e volte pelo outro."),
    CONTROLES_INVERTIDOS("Controles invertidos", "Cima é baixo, esquerda é direita. Boa sorte!"),
    NEBLINA("Neblina", "Você só enxerga ao redor da cabeça."),
    COMIDA_FUGITIVA("Comida fugitiva", "A comida muda de lugar a cada 4 segundos.");

    public final String nome;
    public final String descricao;

    Constraint(String nome, String descricao) {
        this.nome = nome;
        this.descricao = descricao;
    }

    public static Constraint sortear() {
        Constraint[] v = values();
        return v[MathUtils.random(v.length - 1)];
    }
}
