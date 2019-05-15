package IfThenLearning;

/**
 * TicTacToe was originally going to be the only game this program ran. However
 * the algorithm for applying a move to the current game state had too many bugs
 * too close to the due date for this project. A year and a half ago, I made a
 * project similar to this one that played TicTacToe, and it worked just fine,
 * but the game's implementation here just didn't end up working out.

import java.util.ArrayList;
import java.util.Scanner;

public class TicTacToe extends Game {
    public static void setUp() {
        Game.name = "TicTacToe";
        Game.boardHeight = 3;
        Game.boardWidth = 3;
        Game.moveSize = 1;
        Game.hasher = new Hasher() {
            @Override
            public int getStateHash(byte[][] state) {
                char[] binaryHash = new char[Game.boardWidth * Game.boardHeight];
                int half = binaryHash.length / 2;
                for (int i = 0; i + half < binaryHash.length; i++) {
                    byte tile = state[i % Game.boardHeight][i / Game.boardWidth];
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
                return move[0];
            }
        };
        Game.view = new Viewer() {
            private String lineBreak = "---+---+---";

            @Override
            public void instructions() {
                System.out.println("TICTACTOE");
                System.out.println("each space has a number as shown below");
                System.out.println(" 0 | 1 | 2");
                System.out.println(lineBreak);
                System.out.println(" 3 | 4 | 5");
                System.out.println(lineBreak);
                System.out.println(" 6 | 7 | 8");
                System.out.println("When prompted, input the number that corresponds to the space you want to take.");
            }

            @SuppressWarnings("Duplicates")
            @Override
            public synchronized void board(IfThenLearning.State state) {
                System.out.print("[");
                for (int y = 0; y < boardHeight; y++) {
                    System.out.print("[");
                    for (int x = 0; x < boardWidth; x++) {
                        System.out.print(state.get(y, x));
                        if (x < boardWidth - 1)
                            System.out.print(",");
                    }
                    System.out.print("]");
                    if (y < boardHeight - 1)
                        System.out.print(",");
                }
                System.out.println("]");
                for (int y = 0; y < boardHeight; y++) {
                    println(state.get(y, 0),
                            state.get(y, 1),
                            state.get(y, 2),
                            y);
                    if (y < 2)
                        System.out.println(lineBreak);
                }
            }

            private Scanner s = new Scanner(System.in);

            @Override
            public int getUserMove(IfThenLearning.State state) {
                board(state);
                Move[] legalMoves = TicTacToe.getLegalMoves(state);
                int move;
                System.out.print("Legal moves:");
                for (int i = 0; i < legalMoves.length; i++) {
                    System.out.print(" " + (legalMoves[i].get(0)));
                    System.out.print(i + 1 < legalMoves.length ? "," : "");
                }
                System.out.print(">>");
                move = contains(legalMoves, s.nextInt());
                while (move == -1) {
                    System.out.print("Illegal move, try again >>");
                    move = contains(legalMoves, s.nextInt());
                }
                return move;
            }

            @SuppressWarnings("Duplicates")
            private void print(byte num, int index) {
                switch (num) {
                    case -1:
                        System.out.print(" o ");
                        break;
                    case 1:
                        System.out.print(" x ");
                        break;
                    default:
                        System.out.print("   ");
                }
            }

            private void println(byte a, byte b, byte c, int y) {
                print(a, y);
                System.out.print("|");
                print(b, y + 1);
                System.out.print("|");
                print(c, y + 2);
                System.out.println();
            }

            private int contains(Move[] moves, int move) {
                for (int i = 0; i < moves.length; i++) {
                    if (moves[i].get(0) == move)
                        return i;
                }
                return -1;
            }
        };
        initialState = new IfThenLearning.State(new byte[][]{
                {0, 0, 0},
                {0, 0, 0},
                {0, 0, 0}
        });
    }

    private static IfThenLearning.State initialState;

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
        int m = move.get(0);
        newState[m / 3][m % 3] = 1;
        currentState = new IfThenLearning.State(newState);
    }

    @Override
    public void flipPerspective() {
        byte[][] newState = new byte[boardWidth][boardHeight];
        for (int j = 0; j < boardHeight; j++)
            for (int i = 0; i < boardWidth; i++)
                newState[i][j] = (byte) (currentState.get(i, j) * -1);
        currentState = new IfThenLearning.State(newState);
    }

    @Override
    public Move[] getLegalMoves() {
        return getLegalMoves(currentState);
    }
    private static Move[] getLegalMoves(IfThenLearning.State state) {
        ArrayList<Move> legalMoves = new ArrayList<>();
        for (int y = 0; y < boardWidth; y++)
            for (int x = 0; x < boardHeight; x++)
                if (state.get(x, y) == 0)
                    legalMoves.add(new Move((byte) ((y * boardWidth) + x)));
        return legalMoves.toArray(new Move[0]);
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
            if (currentState.get(i, boardHeight - i - 1) != 1)
                diagonal = false;
        if (diagonal)
            currentPlayerWon();
    }
}
 */
