public class TicTacToe implements Game {
    private Game.Status status = Status.UNPLAYED;
    private final Player p1;
    private final Player p2;

    private static State initialState = new State(new byte[][]{{0, 0, 0}, {0, 0, 0}, {0, 0, 0}});

    public TicTacToe(Player p1, Player p2) {
        this.p1 = p1;
        this.p2 = p2;
    }

    private State currentState = initialState;
    private byte currentPlayer = 1;

    @Override
    public void run() {
        status = Status.IN_PROGRESS;

        Move[] legalMoves;
        int move;
        while (status == Status.IN_PROGRESS) {

        }
    }

    private void newState(Move move) {

    }

    private void flipPerspective() {
        byte[][] newState = new byte[Main.boardWidth][Main.boardHeight];
        for (int i = 0; i < Main.boardHeight; i++) {
            for (int j = 0; j < Main.boardWidth; j++)
                newState[i][j] = (byte) (currentState.get(j, i) * -1);
        }
        currentState = new State(newState);
    }

    private Move[] getLegalMoves() {
        int boardSize = Main.boardWidth * Main.boardHeight;
        int[] legalMoves = new int[boardSize];
        int numOfMoves = 0;
        for (int i = 0; i < boardSize; i++) {
            if (currentState.get(i % Main.boardWidth, i / Main.boardHeight) == 0) {
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
        for (int y = 0; y < Main.boardHeight; y++) {
            boolean column = true;
            for (int x = 0; x < Main.boardWidth && column; x++) {
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
        for (int x = 0; x < Main.boardWidth; x++) {
            boolean row = true;
            for (int y = 0; y < Main.boardHeight && row; y++) {
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
        for (int i = 0; i < Main.boardHeight && i < Main.boardWidth && diagonal; i++) {
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
        for (int i = 0; i < Main.boardHeight && diagonal; i++) {
            if (s.get(i, Main.boardHeight - i) != 1)
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

    @Override
    public Player getWinner() throws GameOverException {
        if (status == Status.P1WON)
            return p1;
        if (status == Status.P2WON)
            return p2;
        throw new GameOverException(status);
    }

    @Override
    public Player getLoser() throws GameOverException {
        if (status == Status.P1WON)
            return p1;
        if (status == Status.P2WON)
            return p2;
        throw new GameOverException(status);
    }
}
