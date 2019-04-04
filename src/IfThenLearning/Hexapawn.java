package IfThenLearning;

import java.util.ArrayList;

public class Hexapawn extends Game {
    static {
        boardWidth = 3;
        boardHeight = 3;
        moveSize = 4;
        hasher = new Hasher() {
            @Override
            public int getStateHash(byte[][] state) {
                char[] binaryHash = new char[boardWidth * boardHeight * 2];
                int half = binaryHash.length / 2;
                int i = 0;
                for (int x = 0; x < boardWidth; x++) {
                     for (int y = 0; y < boardHeight; y++) {
                         binaryHash[i] = '0';
                         binaryHash[half + i] = '0';
                         if (state[x][y] > 0)
                             binaryHash[i] = '1';
                         else if (state[x][y] < 0)
                             binaryHash[half + i] = '1';
                         i++;
                     }
                }
                return Integer.parseInt(new String(binaryHash), 2);
            }

            @Override
            public int getMoveHash(byte[] move) {
                return move[0] | (move[1] << 8) | (move[2] << 16) | (move[3] << 24);
            }
        };
    }

    private static final IfThenLearning.State initialState = new IfThenLearning.State(new byte[][]{
            {1, 1, 1},
            {0, 0, 0},
            {-1, -1, -1}
    });

    @Override
    public IfThenLearning.State getInitialState() {
        return initialState;
    }

    public Hexapawn(Player p1, Player p2) {
        super(p1, p2);
    }

    @Override
    public void newState(Move move) {
        byte[][] newState = new byte[boardWidth][boardHeight];
        for (int x = 0; x < boardWidth; x++)
            for (int y = 0; y < boardHeight; y++)
                newState[x][y] = currentState.get(x, y);
        newState[move.get(2)][move.get(3)] = 1;
        newState[move.get(0)][move.get(1)] = 0;
        currentState = new IfThenLearning.State(newState);
    }

    @Override
    public void flipPerspective() {
        byte[][] newState = new byte[boardWidth][boardHeight];
        for (int x = 0; x < boardWidth; x++)
            for (int y = 0; y < boardHeight; y++)
                newState[x][y] = currentState.get(x, boardHeight - y);
        currentState = new IfThenLearning.State(newState);
    }

    @Override
    public Move[] getLegalMoves() {
        int count = 0;
        ArrayList<Move> moves = new ArrayList<>();
        for (int y = 0; y < boardHeight && count < 3; y++) {
            for (int x = 0; x < boardWidth && count < 3; x++) {
                if (currentState.get(x, y) == 1) {
                    count++;
                    if (x - 1 >= 0 && currentState.get(x - 1, y + 1) == -1)
                        moves.add(new Move((byte) x, (byte) y, (byte) (x - 1), (byte) (y + 1)));
                    if (x + 1 < boardWidth && currentState.get(x + 1, y + 1) == -1)
                        moves.add(new Move((byte) x, (byte) y, (byte) (x + 1) , (byte) (y + 1)));
                    if (currentState.get(x, y + 1) == 0)
                        moves.add(new Move((byte) x, (byte) y, (byte) x, (byte) (y + 1)));
                }
            }
        }
        return moves.toArray(new Move[0]);
    }

    @Override
    public void checkGameOver() {
        for (int x = 0; x < boardWidth; x++) {
            if (currentState.get(x, boardHeight - 1) == 1) {
                currentPlayerWon();
                return;
            }
        }
        boolean clear = true;
        for (int x = 0; x < boardWidth && clear; x++)
            for (int y = 0; y < boardHeight && clear; y++)
                if (currentState.get(x, y) == -1)
                    clear = false;
        if (clear)
            currentPlayerWon();
    }
}
