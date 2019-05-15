package IfThenLearning;

public class Main {
    public static Handler gameMaker;

    public static Game newGame(Player p1, Player p2) {
        return gameMaker.newGame(p1, p2);
    }

    public static Player newPlayer() {
        return new Phenotype();
    }

    public static Player newPlayer(Player mother, Player father) {
        if (!(mother instanceof Phenotype) && !(father instanceof Phenotype))
            return new Phenotype((Phenotype) mother, (Phenotype) father);
        return newPlayer();
    }
}
