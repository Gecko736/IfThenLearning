public class Main {
    public static Game newGame(Player p1, Player p2) {
        return new TicTacToe(p1, p2);
    }

    public static Player newPlayer() {
        return new Phenotype();
    }

    public static Player newPlayer(Player mother, Player father) {
        return new Phenotype((Phenotype) mother, (Phenotype) father);
    }
}
