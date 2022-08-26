package org.example.model;

/**
 * @author Emiel Bloem
 * <p>
 * add data to a GameResults object
 */
public class GameResults implements Comparable<GameResults>{
    private final String name;
    private final String category;
    private final int score;

    public GameResults(String name, String category, int score) {
        this.name = name;
        this.category = category;
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public int getScore() {
        return score;
    }

    @Override
    public String toString() {
        return String.format("%s | score: %s | %s", this.name, this.score, this.category);
    }

    @Override
    public int compareTo(GameResults o) {
        return Integer.compare(o.score, this.score);
    }
}
