package IfThenLearning;

public class TicTacToe extends Game {
    static {
        Game.boardHeight = 3;
        Game.boardWidth = 3;
        Game.moveSize = 2;
        Game.hasher = new Hasher() {
            @Override
            public int getStateHash(byte[][] state) {
                char[] binaryHash = new char[Game.boardWidth * Game.boardHeight];
                int half = binaryHash.length / 2;
                for (int i = 0; i < binaryHash.length; i++) {
                    byte tile = state[i / Game.boardHeight][i % Game.boardWidth];
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

            @Override
            public int getMoveHash(byte[] move) {
                return move[0] | move[1] << 8;
            }
        };
    }

    private static IfThenLearning.State initialState = new IfThenLearning.State(new byte[][]{
            {0, 0, 0},
            {0, 0, 0},
            {0, 0, 0}
    });

    @Override
    public IfThenLearning.State getInitialState() {
        return initialState;
    }

    public TicTacToe(Player p1, Player p2) {
        super(p1, p2);
    }

    @Override
    public void newState(Move move) {
        byte[][] newState = new byte[boardWidth][boardHeight];
        for (int x = 0; x < boardWidth; x++)
            for (int y = 0; y < boardHeight; y++)
                newState[x][y] = currentState.get(x, y);
        newState[move.get(0)][move.get(1)] = 1;
        currentState = new IfThenLearning.State(newState);
    }

    @Override
    public void flipPerspective() {
        byte[][] newState = new byte[boardWidth][boardHeight];
        for (int i = 0; i < boardWidth; i++)
            for (int j = 0; j < boardHeight; j++)
                newState[i][j] = (byte) (currentState.get(i, boardHeight - j) * -1);
        currentState = new IfThenLearning.State(newState);
    }

    @Override
    public Move[] getLegalMoves() {
        int boardSize = boardWidth * boardHeight;
        int[] legalMoves = new int[boardSize];
        int numOfMoves = 0;
        for (int i = 0; i < boardSize; i++)
            if (currentState.get(i % boardWidth, i / boardHeight) == 0)
                legalMoves[numOfMoves++] = i;
        Move[] output = new Move[numOfMoves];
        for (int i = 0; i < numOfMoves; i++)
            output[i] = new Move((byte) legalMoves[i]);
        return output;
    }

    @SuppressWarnings("Duplicates")
    @Override
    public void checkGameOver() {
        for (int y = 0; y < boardHeight; y++) {
            boolean column = true;
            for (int x = 0; x < boardWidth && column; x++)
                if (currentState.get(x, y) != 1)
                    column = false;
            if (column)
                currentPlayerWon();
        }
        for (int x = 0; x < boardWidth; x++) {
            boolean row = true;
            for (int y = 0; y < boardHeight && row; y++)
                if (currentState.get(x, y) != 1)
                    row = false;
            if (row)
                currentPlayerWon();
        }
        boolean diagonal = true;
        for (int i = 0; i < boardHeight && i < boardWidth && diagonal; i++)
            if (currentState.get(i, i) != 1)
                diagonal = false;
        if (diagonal)
            currentPlayerWon();
        diagonal = true;
        for (int i = 0; i < boardHeight && diagonal; i++)
            if (currentState.get(i, boardHeight - i) != 1)
                diagonal = false;
        if (diagonal)
            currentPlayerWon();
    }
}
