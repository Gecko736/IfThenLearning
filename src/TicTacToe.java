public class TicTacToe extends Game {
    static {
        Game.hasher = new TicTacToeHasher();
        Game.boardHeight = 3;
        Game.boardWidth = 3;
        Game.moveSize = 2;
    }
    private static State initialState = new State(new byte[][]{{0, 0, 0}, {0, 0, 0}, {0, 0, 0}});

    public TicTacToe(Player p1, Player p2) {
        super(p1, p2);
    }

    private static class TicTacToeHasher implements Hasher {
        private TicTacToeHasher() {}

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
    }

    private State currentState = initialState;
    private byte currentPlayer = 1;

    @Override
    public void run() {
        p1.initiate();
        p2.initiate();
        status = Status.IN_PROGRESS;

        Move[] legalMoves;
        Move move;
        while (status == Status.IN_PROGRESS) {
            flipPerspective();
            currentPlayer = 1;
            legalMoves = getLegalMoves();
            move = legalMoves[p1.move(currentState, legalMoves.length)];
            newState(move);
            checkGameOver();
            if (status == Status.IN_PROGRESS) {
                flipPerspective();
                currentPlayer = 2;
                legalMoves = getLegalMoves();
                move = legalMoves[p2.move(currentState, legalMoves.length)];
                newState(move);
                checkGameOver();
            }
        }
        if (status == Status.P1WON) {
            p1.won();
            p2.lost();
        } else if (status == Status.P2WON) {
            p1.lost();
            p2.won();
        } else if (status == Status.TIE) {
            p1.lost();
            p2.lost();
        }
    }

    private void newState(Move move) {
        byte[][] newState = new byte[boardHeight][boardWidth];
        for (int x = 0; x < boardWidth; x++) {
            for (int y = 0; y < boardHeight; y++) {
                newState[x][y] = currentState.get(x, y);
            }
        }
        newState[move.get(0)][move.get(1)] = 1;
        currentState = new State(newState);
    }

    private void flipPerspective() {
        byte[][] newState = new byte[boardWidth][boardHeight];
        for (int i = 0; i < boardHeight; i++) {
            for (int j = 0; j < boardWidth; j++)
                newState[i][j] = (byte) (currentState.get(j, i) * -1);
        }
        currentState = new State(newState);
    }

    private Move[] getLegalMoves() {
        int boardSize = boardWidth * boardHeight;
        int[] legalMoves = new int[boardSize];
        int numOfMoves = 0;
        for (int i = 0; i < boardSize; i++) {
            if (currentState.get(i % boardWidth, i / boardHeight) == 0) {
                legalMoves[numOfMoves++] = i;
            }
        }
        Move[] output = new Move[numOfMoves];
        for (int i = 0; i < numOfMoves; i++)
            output[i] = new Move(new byte[]{(byte) legalMoves[i]});
        return output;
    }

    private void checkGameOver() {
        State s = currentState;
        for (int y = 0; y < boardHeight; y++) {
            boolean column = true;
            for (int x = 0; x < boardWidth && column; x++) {
                if (s.get(x, y) != 1)
                    column = false;
            }
            if (column) {
                if (currentPlayer == 1)
                    status = Status.P1WON;
                else
                    status = Status.P2WON;
                return;
            }
        }
        for (int x = 0; x < boardWidth; x++) {
            boolean row = true;
            for (int y = 0; y < boardHeight && row; y++) {
                if (s.get(x, y) != 1)
                    row = false;
            }
            if (row) {
                if (currentPlayer == 1)
                    status = Status.P1WON;
                else
                    status = Status.P2WON;
                return;
            }
        }
        boolean diagonal = true;
        for (int i = 0; i < boardHeight && i < boardWidth && diagonal; i++) {
            if (s.get(i, i) != 1)
                diagonal = false;
        }
        if (diagonal) {
            if (currentPlayer == 1)
                status = Status.P1WON;
            else
                status = Status.P2WON;
            return;
        }
        diagonal = true;
        for (int i = 0; i < boardHeight && diagonal; i++) {
            if (s.get(i, boardHeight - i) != 1)
                diagonal = false;
        }
        if (diagonal) {
            if (currentPlayer == 1)
                status = Status.P1WON;
            else
                status = Status.P2WON;
            return;
        }
    }
}
