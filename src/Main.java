public class Main {
    public static final int boardWidth = 3;
    public static final int boardHeight = 3;

    public static Game newGame(Player p1, Player p2) {
        return new TicTacToe(p1, p2);
    }

    public static int getStateHashCode(byte[][] state) {
        char[] binaryHash = new char[boardWidth * boardHeight];
        int half = binaryHash.length / 2;
        for (int i = 0; i < binaryHash.length; i++) {
            byte tile = state[i / boardHeight][i % boardWidth];
            if (tile == 1) {
                binaryHash[i] = '1';
                binaryHash[i + half] = '0';
            } else if (tile == -1) {
                binaryHash[i] = '0';
                binaryHash[i + half] = '1';
            } else {
                binaryHash[i] = '0';
                binaryHash[i + half] = '0';
            }
        }
        return Integer.parseInt(new String(binaryHash), 2);
    }
}
