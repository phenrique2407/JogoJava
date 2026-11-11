package com.exemplo.cobra;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.utils.Array;

/** O jogador: uma cobra em grade que encolhe com a fome. */
public class Snake {
    private final Array<GridPoint2> body = new Array<>();
    private Direction dir = Direction.RIGHT;
    private Direction nextDir = Direction.RIGHT;
    private int pendingGrowth;

    public Snake(int startX, int startY, int length) {
        for (int i = 0; i < length; i++) body.add(new GridPoint2(startX - i, startY));
    }

    public void setDirection(Direction d) {
        if (d != dir.opposite()) nextDir = d;
    }

    /** Calcula a próxima posição da cabeça (sem mover). */
    public GridPoint2 nextHead(boolean wrap) {
        dir = nextDir;
        GridPoint2 h = body.first();
        int x = h.x + dir.dx, y = h.y + dir.dy;
        if (wrap) {
            x = (x + SnakeGame.COLS) % SnakeGame.COLS;
            y = (y + SnakeGame.ROWS) % SnakeGame.ROWS;
        }
        return new GridPoint2(x, y);
    }

    public void advance(GridPoint2 newHead) {
        body.insert(0, newHead);
        if (pendingGrowth > 0) pendingGrowth--;
        else body.removeIndex(body.size - 1);
    }

    public boolean hitsSelf(GridPoint2 p) {
        // a ponta do rabo sai do lugar neste passo, a menos que esteja crescendo
        int limit = pendingGrowth > 0 ? body.size : body.size - 1;
        for (int i = 0; i < limit; i++) if (body.get(i).equals(p)) return true;
        return false;
    }

    public boolean occupies(int x, int y) {
        for (GridPoint2 s : body) if (s.x == x && s.y == y) return true;
        return false;
    }

    public void grow(int n) { pendingGrowth += n; }

    /** Perde um segmento por fome. */
    public void shrink() { if (body.size > 0) body.removeIndex(body.size - 1); }

    public int length() { return body.size; }
    public Array<GridPoint2> getBody() { return body; }
    public GridPoint2 head() { return body.first(); }
}
