package IfThenLearning;

@FunctionalInterface
public interface Handler {
    Game newGame(Player p1, Player p2);
}
